package chiprogram.chipapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Consts;


public class HomeActivity extends Activity implements
        ConfirmationDialog.ConfirmationDialogListener {

    public static final String ARGUMENT_USER = "chiprogram.chipapp.ARGUMENT_USER";

    private static final String CONFIRM_LOGOUT_TAG = "fragment_confirm_logout";

    private CHIPUser m_user;

    private FragmentManager m_fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Show the Up button in the action bar.
        setupActionBar();

        m_fragmentManager = getFragmentManager();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        m_user = extras.getParcelable(ARGUMENT_USER);

        // Update profile for current user
        this.setTitle(getString(R.string.title_activity_home) + " " + m_user.get_firstName());
    }

    @Override
    public void onResume() {
        if (CommonFunctions.quitting_app) {
            finish();
        }
        super.onResume();
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            CommonFunctions.navigateToSettings(this, m_user);
            return true;
        } else if (id == R.id.action_profile) {
            CommonFunctions.navigateToProfile(this, m_user);
            return true;
        } else if (id == R.id.action_training) {
            CommonFunctions.navigateToTraining(this, m_user);
            return true;
        } else if (id == R.id.action_discussion) {
            CommonFunctions.navigateToDiscussion(this, m_user);
            return true;
        } else if (id == R.id.action_logout) {
            CommonFunctions.handleLogout(this);
            return true;
        } else if (id == R.id.action_about_chip) {
            CommonFunctions.navigateToAboutCHIP(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // verify that user wants to log out
        FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();

        ConfirmationDialog confirmLogoutDialog =
                new ConfirmationDialog();

        Bundle args = new Bundle();

        SharedPreferences prefs = getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        if (prefs.getString(Consts.PREF_REM_PASSWORD, "").isEmpty()) {
            args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.confirm_logout));
        } else {
            args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.common_confirm_exit_app));
        }
        args.putString(ConfirmationDialog.ARG_TAG, CONFIRM_LOGOUT_TAG);

        confirmLogoutDialog.setArguments(args);

        fragmentTransaction.add(confirmLogoutDialog, CONFIRM_LOGOUT_TAG);
        fragmentTransaction.commit();
    }

    public void onFinishConfirmationDialog(String tag) {
        if (tag.equals(CONFIRM_LOGOUT_TAG)) {
            SharedPreferences prefs = getSharedPreferences(LoginActivity.class.getSimpleName(),
                    Context.MODE_PRIVATE);
            if (prefs.getString(Consts.PREF_REM_PASSWORD, "").isEmpty()) {
                Intent intent = new Intent(this, IntroScreenActivity.class);
                startActivity(intent);
            } else {
                CommonFunctions.quitting_app = true;
            }

            finish();
        }
    }
}
