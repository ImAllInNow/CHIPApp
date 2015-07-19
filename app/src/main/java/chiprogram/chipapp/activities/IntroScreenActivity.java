package chiprogram.chipapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import chiprogram.chipapp.dialogs.ConfirmationDialog;
import chiprogram.chipapp.R;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Consts;


public class IntroScreenActivity extends BaseActivity implements
        ConfirmationDialog.ConfirmationDialogListener {

    private FragmentManager m_fragmentManager;

    private static final String CONFIRM_EXIT_APP_TAG = "fragment_confirm_exit_app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        CommonFunctions.quitting_app = false;

        m_fragmentManager = getFragmentManager();

        checkForAutoLogin();
    }

    private void checkForAutoLogin() {
        SharedPreferences prefs = getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);

        if (prefs.getString(Consts.PREF_REM_PASSWORD, "").isEmpty() == false) {
            loginClicked(null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intro_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_login) {
            loginClicked(null);
            return true;
        } else if (id == R.id.action_about_chip) {
            CommonFunctions.navigateToAboutCHIP(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // verify that user wants to exit the app
        FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();

        ConfirmationDialog confirmExitAppDialog =
                new ConfirmationDialog();

        Bundle args = new Bundle();
        args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.common_confirm_exit_app));
        args.putString(ConfirmationDialog.ARG_TAG, CONFIRM_EXIT_APP_TAG);

        confirmExitAppDialog.setArguments(args);

        fragmentTransaction.add(confirmExitAppDialog, CONFIRM_EXIT_APP_TAG);
        fragmentTransaction.commit();
    }

    public void onFinishConfirmationDialog(String tag) {
        if (tag.equals(CONFIRM_EXIT_APP_TAG)) {
            // exit app
            CommonFunctions.quitting_app = true;
            finish();
        }
    }

    public void loginClicked(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
