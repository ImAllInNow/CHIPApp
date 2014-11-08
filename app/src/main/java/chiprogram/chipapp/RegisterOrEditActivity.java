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
import android.widget.Toast;

import java.util.ArrayList;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Consts;

public class RegisterOrEditActivity extends Activity implements
        ConfirmationDialog.ConfirmationDialogListener,
        AdapterView.OnItemSelectedListener {

    public enum ProfileEditType {
        REGISTER,
        EDIT,
    }

    private static final String CONFIRM_CANCEL_REGISTRATION_TAG = "fragment_confirm_cancel_registration";

    private CHIPUser m_user;
    private ProfileEditType m_type;
    private String[] m_mentorEmailList;

    private FragmentManager m_fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        m_fragmentManager = getFragmentManager();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null && extras.containsKey(ProfileActivity.ARGUMENT_USER)) {
            m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);
            m_type = ProfileEditType.EDIT;
        } else {
            m_user = null;
            m_type = ProfileEditType.REGISTER;
        }

        // set mentors spinner
        m_mentorEmailList = CHIPLoaderSQL.getInstance().getMentorEmailList();
        String[] mentorInfoList = CHIPLoaderSQL.getInstance().getMentorInfoList(m_mentorEmailList);
        String[] mentorPlusNone = new String[mentorInfoList.length + 1];
        mentorPlusNone[0] = getString(R.string.common_none);
        for (int i = 0; i < mentorInfoList.length; ++i) {
            mentorPlusNone[i+1] = mentorInfoList[i];
        }
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
        Spinner location = (Spinner) findViewById(R.id.reg_spinnerLocation);
        Spinner role = (Spinner) findViewById(R.id.reg_spinnerRole);
        LinearLayout specLL = (LinearLayout) findViewById(R.id.reg_specializationBlock);
        EditText specialization = (EditText) findViewById(R.id.reg_editTextSpecialization);
        Spinner mentor = (Spinner) findViewById(R.id.reg_spinnerMentor);
        EditText shortBioView = (EditText) findViewById(R.id.reg_editTextBio);
        EditText emailView = (EditText) findViewById(R.id.reg_editTextEmail);
        EditText reenterEmailView = (EditText) findViewById(R.id.reg_editTextReenterEmail);
        LinearLayout reenterEmailLL = (LinearLayout) findViewById(R.id.reg_layoutReenterEmail);
        LinearLayout emailLL = (LinearLayout) findViewById(R.id.reg_layoutEmail);
        LinearLayout passwordLL = (LinearLayout) findViewById(R.id.reg_layoutPassword);
        EditText passwordView = (EditText) findViewById(R.id.reg_editTextPassword);

        String[] locsArray = CHIPLoaderSQL.getInstance().getLocationList();
        String[] rolesArray = CHIPLoaderSQL.getInstance().getRoleList();

        firstNameView.setText(m_user.get_firstName());
        lastNameView.setText(m_user.get_lastName());
        addressView.setText(m_user.get_address());
        for (int i = 0; i < locsArray.length; ++i) {
            if (locsArray[i].equals(m_user.get_location())) {
                location.setSelection(i);
            }
        }
        for (int i = 0; i < rolesArray.length; ++i) {
            if (rolesArray[i].equals(m_user.get_role())) {
                role.setSelection(i);
            }
        }
        if (m_user.get_role() == getString(R.string.common_mentor)) {
            specLL.setVisibility(LinearLayout.VISIBLE);
            specialization.setText(m_user.get_specialization());
        } else {
            specLL.setVisibility(LinearLayout.GONE);
            specialization.setText("");
        }
        if (m_user.get_mentorEmail() != null) {
            for (int i = 0; i < m_mentorEmailList.length; ++i) {
                if (m_mentorEmailList[i].equals(m_user.get_mentorEmail())) {
                    mentor.setSelection(i + 1);
                }
            }
        } else {
            mentor.setSelection(0);
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
        Spinner location = (Spinner) findViewById(R.id.reg_spinnerLocation);
        Spinner role = (Spinner) findViewById(R.id.reg_spinnerRole);
        LinearLayout specLL = (LinearLayout) findViewById(R.id.reg_specializationBlock);
        EditText specialization = (EditText) findViewById(R.id.reg_editTextSpecialization);
        Spinner mentor = (Spinner) findViewById(R.id.reg_spinnerMentor);
        EditText shortBioView = (EditText) findViewById(R.id.reg_editTextBio);
        EditText emailView = (EditText) findViewById(R.id.reg_editTextEmail);
        EditText reenterEmailView = (EditText) findViewById(R.id.reg_editTextReenterEmail);
        EditText passwordView = (EditText) findViewById(R.id.reg_editTextPassword);

        int chosenMentor = mentor.getSelectedItemPosition();
        String mentorEmail;
        if (chosenMentor == 0) {
            mentorEmail = "";
        } else {
            mentorEmail = m_mentorEmailList[chosenMentor - 1];
        }

        boolean valid = true;

        String specializationString;
        if (specLL.getVisibility() == LinearLayout.GONE) {
            specializationString = "";
        } else {
            specializationString = specialization.getText().toString();
        }

        // create user (with default id)
        CHIPUser newUser = new CHIPUser(firstNameView.getText().toString(),
                lastNameView.getText().toString(),
                addressView.getText().toString(),
                location.getSelectedItem().toString(),
                role.getSelectedItem().toString(),
                specializationString,
                mentorEmail,
                shortBioView.getText().toString(),
                emailView.getText().toString());

        // verify all required fields are filled in and valid
        int errorInput = newUser.validateUser(this);
        if (errorInput > 0) {
            // the errorInput'th field is the last error

            // TODO: add error

            valid = false;
        }

        // verify email matches the second email input for registering
        if (m_type == ProfileEditType.REGISTER &&
            reenterEmailView.getText().toString().equals(emailView.getText().toString()) == false) {
            // emails don't match

            // TODO: add error

            valid = false;
        }

        // TODO: make sure password is valid

        if (valid) {
            if (m_type == ProfileEditType.EDIT) {
                // TODO: edit user in database

                Toast.makeText(this, getString(R.string.edit_success), Toast.LENGTH_SHORT).show();

                CommonFunctions.navigateToProfile(this, newUser);
                finish();
            } else {
                boolean unique = true;
                // TODO: check if user is already in the database

                if (unique) {
                    // TODO: submit user to database
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();

                    CommonFunctions.navigateToHome(this, newUser);
                    finish();
                }
            }
        }
    }
}
