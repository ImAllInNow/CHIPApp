package chiprogram.chipapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import chiprogram.chipapp.R;
import chiprogram.chipapp.database.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Consts;
import chiprogram.chipapp.database.JSONServlet;

/**
 * A login screen that offers login via email/password.

 */
public class LoginActivity extends Activity implements JSONServlet.LoginServletListener {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    //private UserLoginTask m_AuthTask = null;

    // UI references.
    private AutoCompleteTextView m_EmailView;
    private EditText m_PasswordView;
    private View m_ProgressView;
    private View m_LoginFormView;

    private CheckBox m_rememberEmail;
    private CheckBox m_autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        m_EmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //populateAutoComplete();

        m_PasswordView = (EditText) findViewById(R.id.password);
        m_PasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        m_LoginFormView = findViewById(R.id.login_form);
        m_ProgressView = findViewById(R.id.login_progress);

        m_rememberEmail = (CheckBox) findViewById(R.id.chbx_remEmail);
        m_autoLogin = (CheckBox) findViewById(R.id.chbx_keepLoggedIn);

        checkForRemEmailAndPassword();
    }

    private void checkForRemEmailAndPassword() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        if (prefs.getString(Consts.PREF_REM_EMAIL, "").isEmpty() == false) {
            m_EmailView.setText(prefs.getString(Consts.PREF_REM_EMAIL, ""));
            m_rememberEmail.setChecked(true);
        }

        if (prefs.getString(Consts.PREF_REM_PASSWORD, "").isEmpty() == false) {
            m_PasswordView.setText(prefs.getString(Consts.PREF_REM_PASSWORD, ""));
            m_autoLogin.setChecked(true);
            m_rememberEmail.setEnabled(false);
            attemptLogin();
        }
    }

    public void autoLoginClicked(View view) {
        if (m_autoLogin.isChecked()) {
            m_rememberEmail.setChecked(true);
            m_rememberEmail.setEnabled(false);
        } else {
            m_rememberEmail.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        if (CommonFunctions.quitting_app) {
            finish();
        }
        super.onResume();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        //if (m_AuthTask != null) {
        //    return;
        //}

        // Reset errors.
        m_EmailView.setError(null);
        m_PasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = m_EmailView.getText().toString();
        String password = m_PasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !CommonFunctions.ValidatePassword(password)) {
            m_PasswordView.setError(getString(R.string.error_invalid_password));
            focusView = m_PasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            m_EmailView.setError(getString(R.string.error_field_required));
            focusView = m_EmailView;
            cancel = true;
        } else if (CommonFunctions.ValidateEmail(email) == false) {
            m_EmailView.setError(getString(R.string.error_invalid_email));
            focusView = m_EmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            JSONServlet.runLoginServlet(this, email, password);

            //m_AuthTask = new UserLoginTask(this, email, password);
            //m_AuthTask.execute((Void) null);
        }
    }

    @Override
    public void onLoginResult(JSONObject jsonObject) {
        //showProgress(false);

        CHIPUser user = new CHIPUser(jsonObject);

        if (user.get_id() != null) {
            setLoginPreferences();

            // navigate to profile activity
            Intent intent = new Intent(this, HomeActivity.class);

            // add in user to bundle
            Bundle extras = new Bundle();
            extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

            intent.putExtras(extras);

            startActivity(intent);
            finish();
        } else {
            //m_PasswordView.setError(getString(R.string.error_incorrect_password));
            //m_PasswordView.requestFocus();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            m_LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            m_LoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    m_LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            m_ProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            m_ProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            m_LoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final Activity m_Activity;
        private final String m_Email;
        private final String m_Password;

        private CHIPUser m_loginUser;

        UserLoginTask(Activity baseActivity, String email, String password) {
            m_loginUser = null;

            m_Activity = baseActivity;
            m_Email = email;
            m_Password = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            m_loginUser = CHIPLoaderSQL.getInstance().checkUserLogin(m_Email, m_Password, m_Activity);

            return (m_loginUser != null);
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            //m_AuthTask = null;
            showProgress(false);

            if (success) {
                setLoginPreferences();

                // navigate to profile activity
                Intent intent = new Intent(m_Activity, HomeActivity.class);

                // add in user to bundle
                Bundle extras = new Bundle();
                extras.putParcelable(ProfileActivity.ARGUMENT_USER, m_loginUser);

                intent.putExtras(extras);

                startActivity(intent);
                finish();
            } else {
                m_PasswordView.setError(getString(R.string.error_incorrect_password));
                m_PasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            //m_AuthTask = null;
            showProgress(false);
        }
    }

    private void setLoginPreferences() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (m_rememberEmail.isChecked()) {
            editor.putString(Consts.PREF_REM_EMAIL, m_EmailView.getText().toString());
        } else {
            editor.putString(Consts.PREF_REM_EMAIL, "");
        }

        if (m_autoLogin.isChecked()) {
            editor.putString(Consts.PREF_REM_PASSWORD, m_PasswordView.getText().toString());
        } else {
            editor.putString(Consts.PREF_REM_PASSWORD, "");
        }

        editor.apply();
    }
}



