package chiprogram.chipapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.webview.DiscussionWebViewClient;

public class DiscussionActivity extends Activity {

    private static final String CURRENT_URL = "chiprogram.chipapp.CURRENT_URL";

    private CHIPUser m_user;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        // Show the Up button in the action bar.
        setupActionBar();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);

        myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        myWebView.setWebViewClient(new DiscussionWebViewClient());
        myWebView.loadUrl("http://www.chiprogram.com/discussion/index.php");
    }

    @Override
    public void onResume() {
        if (CommonFunctions.quitting_app) {
            finish();
        }
        super.onResume();
    }

    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.discussion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent intent = new Intent(this, HomeActivity.class);

            // add in user to bundle
            Bundle extras = new Bundle();
            extras.putParcelable(ProfileActivity.ARGUMENT_USER, m_user);

            intent.putExtras(extras);

            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_settings) {
            CommonFunctions.navigateToSettings(this, m_user);
            return true;
        } else if (id == R.id.action_training) {
            CommonFunctions.navigateToTraining(this, m_user);
            return true;
        } else if (id == R.id.action_profile) {
            CommonFunctions.navigateToProfile(this, m_user);
            return true;
        } else if (id == R.id.action_logout) {
            CommonFunctions.handleLogout(this);
            return true;
        } else if (id == R.id.action_about_chip) {
            CommonFunctions.navigateToAboutCHIP(this);
            return true;
        } else if (id == R.id.action_email_mentor) {
            CommonFunctions.emailMentor(this, m_user);
        }
         return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack())
            myWebView.goBack();
        else
            super.onBackPressed();
    }
}
