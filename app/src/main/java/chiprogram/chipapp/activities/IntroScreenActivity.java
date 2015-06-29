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

import java.io.ByteArrayOutputStream;

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

    public static byte[] convertMixedStringToByteArray(String data)
    {
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        int hexState = 0;
        String hexString = "";
        try
        {
            for (int i = 0; i < data.length(); i++)
            {
                char charInStr = data.charAt(i);
                String charAsStr = new String(new char[] { charInStr });
                bytesOut.write(charAsStr.getBytes("UTF8"));

                switch (charInStr)
                {
                    case '\\':
                        hexState = hexState == 0 ? 1 : 0;
                        hexString = "";
                        break;
                    case 'X':
                    case 'x':
                        hexState = hexState == 1 ? 2 : 0;
                        break;
                    default:
                        hexState = hexState >= 2 ? ++hexState : 0;
                        hexString += charInStr;
                        break;
                }

                if (hexState == 4)
                {
                    byte[] tempBytes = bytesOut.toByteArray();
                    bytesOut.reset();
                    bytesOut.write(tempBytes, 0, tempBytes.length - 4);
                    try
                    {
                        byte hexVal = (byte) Integer.parseInt(hexString, 16);
                        bytesOut.write(hexVal);
                    }
                    catch (NumberFormatException exp)
                    {
                    }
                    hexState = 0;
                    hexString = "";
                }
            }
        }

        catch (Exception exp)
        {
        }

        return bytesOut.toByteArray();
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
