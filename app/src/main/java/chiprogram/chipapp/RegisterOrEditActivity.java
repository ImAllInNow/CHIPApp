package chiprogram.chipapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;

public class RegisterOrEditActivity extends Activity implements
        ConfirmationDialog.ConfirmationDialogListener,
        AdapterView.OnItemSelectedListener {

    public enum ProfileEditType {
        REGISTER,
        EDIT,
    }

    public enum RegistrationError {
        NONE,
        FIRST_NAME_EMPTY,
        ADDRESS_EMPTY,
        MENTOR_SPECIALIZATION_EMPTY,
        SHORT_BIO_EMPTY,
        EMAIL_INVALID,
        PASSWORD_INVALID,
        REENTER_EMAIL_MISMATCH
    }

    private static final String CONFIRM_CANCEL_REGISTRATION_TAG = "fragment_confirm_cancel_registration";

    private CHIPUser m_user;
    private ProfileEditType m_type;
    private String[] m_mentorIdList;

    private FragmentManager m_fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_edit);

        m_fragmentManager = getFragmentManager();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        TextView titleView = (TextView) findViewById(R.id.reg_instructions);
        if (extras != null && extras.containsKey(ProfileActivity.ARGUMENT_USER)) {
            m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);
            m_type = ProfileEditType.EDIT;

            titleView.setVisibility(TextView.GONE);
            setTitle(getString(R.string.title_activity_edit));
        } else {
            m_user = null;
            m_type = ProfileEditType.REGISTER;

            titleView.setVisibility(TextView.VISIBLE);
            setTitle(getString(R.string.title_activity_register));
        }

        // set mentors spinner
        m_mentorIdList = CHIPLoaderSQL.getInstance().getMentorIdList();
        String[] mentorInfoList = CHIPLoaderSQL.getInstance().getMentorInfoList(m_mentorIdList);
        String[] mentorPlusNone = new String[mentorInfoList.length + 1];
        mentorPlusNone[0] = getString(R.string.common_none);
        System.arraycopy(mentorInfoList, 0, mentorPlusNone, 1, mentorInfoList.length);

        Spinner mentorSpinner = (Spinner) findViewById(R.id.reg_spinnerMentor);
        ArrayAdapter spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                                                               mentorPlusNone);
        mentorSpinner.setAdapter(spinnerAdapter);

        // set location spinner
        Spinner locationSpinner = (Spinner) findViewById(R.id.reg_spinnerLocation);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                                                  CHIPLoaderSQL.getInstance().getLocationList());
        locationSpinner.setAdapter(spinnerAdapter);

        // set role spinner
        Spinner roleSpinner = (Spinner) findViewById(R.id.reg_spinnerRole);
        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                                                  CHIPLoaderSQL.getInstance().getRoleList());
        roleSpinner.setAdapter(spinnerAdapter);
        roleSpinner.setOnItemSelectedListener(this);

        if (m_type == ProfileEditType.EDIT) {
            prefillData();
        }
    }

    private void prefillData() {
        EditText firstNameView = (EditText) findViewById(R.id.reg_editTextFirstName);
        EditText lastNameView = (EditText) findViewById(R.id.reg_editTextLastName);
        EditText addressView = (EditText) findViewById(R.id.reg_editTextAddress);
        Spinner locationSpinner = (Spinner) findViewById(R.id.reg_spinnerLocation);
        Spinner roleSpinner = (Spinner) findViewById(R.id.reg_spinnerRole);
        LinearLayout specLL = (LinearLayout) findViewById(R.id.reg_specializationBlock);
        EditText specializationView = (EditText) findViewById(R.id.reg_editTextSpecialization);
        Spinner mentorSpinner = (Spinner) findViewById(R.id.reg_spinnerMentor);
        EditText shortBioView = (EditText) findViewById(R.id.reg_editTextBio);
        EditText emailView = (EditText) findViewById(R.id.reg_editTextEmail);
        LinearLayout reenterEmailLL = (LinearLayout) findViewById(R.id.reg_layoutReenterEmail);
        LinearLayout emailLL = (LinearLayout) findViewById(R.id.reg_layoutEmail);
        LinearLayout passwordLL = (LinearLayout) findViewById(R.id.reg_layoutPassword);

        String[] locsArray = CHIPLoaderSQL.getInstance().getLocationList();
        String[] rolesArray = CHIPLoaderSQL.getInstance().getRoleList();

        firstNameView.setText(m_user.get_firstName());
        lastNameView.setText(m_user.get_lastName());
        addressView.setText(m_user.get_address());
        for (int i = 0; i < locsArray.length; ++i) {
            if (locsArray[i].equals(m_user.get_location())) {
                locationSpinner.setSelection(i);
            }
        }
        for (int i = 0; i < rolesArray.length; ++i) {
            if (rolesArray[i].equals(m_user.get_role())) {
                roleSpinner.setSelection(i);
            }
        }
        if (m_user.get_role().equals(getString(R.string.common_mentor))) {
            specLL.setVisibility(LinearLayout.VISIBLE);
            specializationView.setText(m_user.get_specialization());
        } else {
            specLL.setVisibility(LinearLayout.GONE);
            specializationView.setText("");
        }
        if (m_user.get_mentorId() != null && m_user.get_mentorId().isEmpty() == false) {
            for (int i = 0; i < m_mentorIdList.length; ++i) {
                if (m_mentorIdList[i].equals(m_user.get_mentorId())) {
                    mentorSpinner.setSelection(i + 1);
                }
            }
        } else {
            mentorSpinner.setSelection(0);
        }
        shortBioView.setText(m_user.get_bio());

        emailView.setText(m_user.get_email());
        reenterEmailLL.setVisibility(LinearLayout.GONE);
        emailLL.setVisibility(LinearLayout.GONE);
        passwordLL.setVisibility(LinearLayout.GONE);
    }

    @Override
    public void onResume() {
        if (CommonFunctions.quitting_app) {
            finish();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            return true;
        } else if (id == R.id.action_submit) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // verify that user wants to cancel registration
        FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();

        ConfirmationDialog confirmDeleteCompletedTasksDialog =
                new ConfirmationDialog();

        Bundle args = new Bundle();
        if (m_type == ProfileEditType.REGISTER) {
            args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.confirm_cancel_registration));
        } else {
            args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.confirm_cancel_edit));
        }
        args.putString(ConfirmationDialog.ARG_TAG, CONFIRM_CANCEL_REGISTRATION_TAG);

        confirmDeleteCompletedTasksDialog.setArguments(args);

        fragmentTransaction.add(confirmDeleteCompletedTasksDialog, CONFIRM_CANCEL_REGISTRATION_TAG);
        fragmentTransaction.commit();
    }

    public void onFinishConfirmationDialog(String tag) {
        if (tag.equals(CONFIRM_CANCEL_REGISTRATION_TAG)) {
            if (m_type == ProfileEditType.EDIT) {
                setResult(ProfileActivity.EDIT_FAILED);
            }
            finish();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (view != null && view.getClass() == CheckedTextView.class) {
            CheckedTextView editText = (CheckedTextView) view;
            LinearLayout specLL = (LinearLayout) findViewById(R.id.reg_specializationBlock);
            if (editText.getText().toString().equals(getString(R.string.common_mentor))) {
                specLL.setVisibility(LinearLayout.VISIBLE);
            } else {
                specLL.setVisibility(LinearLayout.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        LinearLayout specLL = (LinearLayout) findViewById(R.id.reg_specializationBlock);
        specLL.setVisibility(LinearLayout.GONE);
    }

    public void cancelClicked(View view) {
        onBackPressed();
    }

    public void submitClicked(View view) {
        EditText firstNameView = (EditText) findViewById(R.id.reg_editTextFirstName);
        EditText lastNameView = (EditText) findViewById(R.id.reg_editTextLastName);
        EditText addressView = (EditText) findViewById(R.id.reg_editTextAddress);
        Spinner locationSpinner = (Spinner) findViewById(R.id.reg_spinnerLocation);
        Spinner roleSpinner = (Spinner) findViewById(R.id.reg_spinnerRole);
        LinearLayout specLL = (LinearLayout) findViewById(R.id.reg_specializationBlock);
        EditText specializationView = (EditText) findViewById(R.id.reg_editTextSpecialization);
        Spinner mentor = (Spinner) findViewById(R.id.reg_spinnerMentor);
        EditText shortBioView = (EditText) findViewById(R.id.reg_editTextBio);
        EditText emailView = (EditText) findViewById(R.id.reg_editTextEmail);
        EditText reenterEmailView = (EditText) findViewById(R.id.reg_editTextReenterEmail);
        EditText passwordView = (EditText) findViewById(R.id.reg_editTextPassword);

        int chosenMentor = mentor.getSelectedItemPosition();
        String mentorId;
        if (chosenMentor == 0) {
            mentorId = "";
        } else {
            mentorId = m_mentorIdList[chosenMentor - 1];
        }

        String specializationString;
        if (specLL.getVisibility() == LinearLayout.GONE) {
            specializationString = "";
        } else {
            specializationString = specializationView.getText().toString();
        }

        // TODO: Remove placeholders in this part
        String firstName = firstNameView.getText().toString();//.isEmpty() ? "Rob" : firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString().isEmpty() ? "Tanniru" : lastNameView.getText().toString();
        String address = addressView.getText().toString().isEmpty() ? "123 4th Street\r\nRochester, MI 48306" : addressView.getText().toString();
        String location = locationSpinner.getSelectedItem().toString().isEmpty() ? "Detroit" : locationSpinner.getSelectedItem().toString();
        String role = roleSpinner.getSelectedItem().toString().isEmpty() ? "Mentor" : roleSpinner.getSelectedItem().toString();
        String shortBio = shortBioView.getText().toString().isEmpty() ? "Test Bio" : shortBioView.getText().toString();
        String email = emailView.getText().toString().isEmpty() ? "bkt421@gmail.com" : emailView.getText().toString();
        String reenterEmail = reenterEmailView.getText().toString().isEmpty() ? "bkt421@gmail.com" : reenterEmailView.getText().toString();
        String password = passwordView.getText().toString().isEmpty() ? "CH!(ch19" : passwordView.getText().toString();

        // verify all required fields are filled in and valid
        RegistrationError errorInput = validateUser(firstName, address, role,
                specializationString, mentorId, shortBio, email, reenterEmail, password);

        if (errorInput != RegistrationError.NONE) {
            handleRegistrationError(errorInput);
        } else {
            // create user with default id

            if (m_type == ProfileEditType.EDIT) {
                CHIPUser editedUser = new CHIPUser(m_user.get_id(), firstName, lastName, address, location, role,
                        specializationString, mentorId, shortBio, email);

                // TODO: edit user in database
                CHIPLoaderSQL.getInstance().writeUserToFile(editedUser.get_email(), editedUser, this);

                Toast.makeText(this, getString(R.string.edit_success), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                Bundle extras = new Bundle();
                extras.putParcelable(ProfileActivity.ARGUMENT_USER, editedUser);
                intent.putExtras(extras);
                setResult(ProfileActivity.EDIT_SUCCESSFUL, intent);
                finish();
            } else {
                // uses default id of 1
                CHIPUser newUser = new CHIPUser("1", firstName, lastName, address, location, role,
                        specializationString, mentorId, shortBio, email);

                // TODO: check if user email is already in the database
                boolean unique = CHIPLoaderSQL.getInstance().registerUser(newUser, password, this);

                if (unique) {
                    // TODO: submit user to database and update id

                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();

                    CommonFunctions.navigateToHome(this, newUser);
                    finish();
                } else {
                    // TODO: handle non-unique user
                    Toast.makeText(this, getString(R.string.register_non_unique), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private RegistrationError validateUser(String firstName, String address,
                                           String role, String specialization,
                                           String mentorId, String shortBio, String email,
                                           String reenterEmail, String password) {
        RegistrationError error = RegistrationError.NONE;

        if (firstName.isEmpty()) {
            error = RegistrationError.FIRST_NAME_EMPTY;
        } else if (address.isEmpty()){
            error = RegistrationError.ADDRESS_EMPTY;
        } /*else if (mentorId == null || mentorId.isEmpty()){
            error = ??; TODO: is validation of mentor email needed?
        } */
        else if (role.equals(getString(R.string.common_mentor)) && specialization.isEmpty()) { // only mentors have specializations
            error = RegistrationError.MENTOR_SPECIALIZATION_EMPTY;
        } else if (shortBio.isEmpty()){
            error = RegistrationError.SHORT_BIO_EMPTY;
        } else if (CommonFunctions.ValidateEmail(email) == false){
            error = RegistrationError.EMAIL_INVALID;
        } else if (m_type == ProfileEditType.REGISTER &&
                   email.equals(reenterEmail) == false) {
            error = RegistrationError.REENTER_EMAIL_MISMATCH;
        } else if (m_type == ProfileEditType.REGISTER &&
                   CommonFunctions.ValidatePassword(password) == false) {
            error = RegistrationError.PASSWORD_INVALID;
        }

        // TODO: check for SQL injection characters?

        return error;
    }

    private void handleRegistrationError(RegistrationError errorCode) {
        EditText firstNameView = (EditText) findViewById(R.id.reg_editTextFirstName);
        EditText addressView = (EditText) findViewById(R.id.reg_editTextAddress);
        EditText specializationView = (EditText) findViewById(R.id.reg_editTextSpecialization);
        EditText shortBioView = (EditText) findViewById(R.id.reg_editTextBio);
        EditText emailView = (EditText) findViewById(R.id.reg_editTextEmail);
        EditText reenterEmailView = (EditText) findViewById(R.id.reg_editTextReenterEmail);
        EditText passwordView = (EditText) findViewById(R.id.reg_editTextPassword);

        // reset all errors
        firstNameView.setError(null);
        addressView.setError(null);
        specializationView.setError(null);
        shortBioView.setError(null);
        emailView.setError(null);
        reenterEmailView.setError(null);
        passwordView.setError(null);

        switch (errorCode) {
            case NONE:
                break;
            case FIRST_NAME_EMPTY:
                firstNameView.setError(getString(R.string.register_error_first_name));
                firstNameView.requestFocus();
                break;
            case ADDRESS_EMPTY:
                addressView.setError(getString(R.string.register_error_address));
                addressView.requestFocus();
                break;
            case MENTOR_SPECIALIZATION_EMPTY:
                specializationView.setError(getString(R.string.register_error_specialization));
                specializationView.requestFocus();
                break;
            case SHORT_BIO_EMPTY:
                shortBioView.setError(getString(R.string.register_error_short_bio));
                shortBioView.requestFocus();
                break;
            case EMAIL_INVALID:
                emailView.setError(getString(R.string.register_error_email));
                emailView.requestFocus();
                break;
            case PASSWORD_INVALID:
                passwordView.setError(getString(R.string.register_error_password));
                passwordView.requestFocus();
                break;
            case REENTER_EMAIL_MISMATCH:
                reenterEmailView.setError(getString(R.string.register_error_reenter_email));
                reenterEmailView.requestFocus();
                break;
        }
    }
}
