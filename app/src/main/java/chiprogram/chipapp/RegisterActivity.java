package chiprogram.chipapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;

public class RegisterActivity extends Activity implements
        ConfirmationDialog.ConfirmationDialogListener {

    private static final String CONFIRM_CANCEL_REGISTRATION_TAG = "fragment_confirm_cancel_registration";

    private FragmentManager m_fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        m_fragmentManager = getFragmentManager();

        // set mentors spinner
        Spinner mentorSpinner = (Spinner) findViewById(R.id.reg_spinnerMentor);
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CHIPLoaderSQL.getMentorList());
        mentorSpinner.setAdapter(spinnerAdapter);

        // set location spinner
        Spinner locationSpinner = (Spinner) findViewById(R.id.reg_spinnerLocation);
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CHIPLoaderSQL.getLocationList());
        locationSpinner.setAdapter(spinnerAdapter);

        // set role spinner
        Spinner roleSpinner = (Spinner) findViewById(R.id.reg_spinnerRole);
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, CHIPLoaderSQL.getRoleList());
        roleSpinner.setAdapter(spinnerAdapter);
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
        args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.confirm_cancel_registration));
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

    public void cancelClicked(View view) {
        onBackPressed();
    }

    public void submitClicked(View view) {
        boolean valid = true;

        EditText firstNameView = (EditText) findViewById(R.id.reg_editTextFirstName);
        EditText lastNameView = (EditText) findViewById(R.id.reg_editTextLastName);
        EditText addressView = (EditText) findViewById(R.id.reg_editTextAddress);
        Spinner location = (Spinner) findViewById(R.id.reg_spinnerLocation);
        Spinner role = (Spinner) findViewById(R.id.reg_spinnerRole);
        Spinner mentor = (Spinner) findViewById(R.id.reg_spinnerMentor);
        EditText shortBioView = (EditText) findViewById(R.id.reg_editTextBio);
        EditText emailView = (EditText) findViewById(R.id.reg_editTextEmail);
        EditText reenterEmailView = (EditText) findViewById(R.id.reg_editTextReenterEmail);
        EditText passwordView = (EditText) findViewById(R.id.reg_editTextPassword);

        // create user (with default id)
        CHIPUser user = new CHIPUser(firstNameView.getText().toString(),
                lastNameView.getText().toString(),
                addressView.getText().toString(),
                location.getSelectedItem().toString(),
                role.getSelectedItem().toString(),
                mentor.getSelectedItem().toString(),
                shortBioView.getText().toString(),
                emailView.getText().toString());

        // verify all required fields are filled in and valid
        int errorInput = user.validateUser();
        if (errorInput > 0) {
            // the errorInput'th field is the last error

            // TODO: add error

            valid = false;
        }

        // verify email matches the second email input
        if (reenterEmailView.getText().toString().equals(emailView.getText().toString()) == false) {
            // emails don't match

            // TODO: add error

            valid = false;
        }

        // TODO: make sure password is valid

        if (valid) {
            boolean unique = true;
            // TODO: check if user is already in the database

            if (unique) {
                // TODO: submit user to database

                Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();

                // navigate to profile activity
                Intent intent = new Intent(this, ProfileActivity.class);

                // add in user to bundle
                Bundle extras = new Bundle();
                extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

                intent.putExtras(extras);

                startActivity(intent);
                finish();
            }
        }
    }
}
