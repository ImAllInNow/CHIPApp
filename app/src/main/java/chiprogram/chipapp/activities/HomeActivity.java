package chiprogram.chipapp.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import chiprogram.chipapp.dialogs.ConfirmationDialog;
import chiprogram.chipapp.R;
import chiprogram.chipapp.database.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Consts;
import chiprogram.chipapp.classes.NavItem;


public class HomeActivity extends Activity implements
        ConfirmationDialog.ConfirmationDialogListener,
        View.OnClickListener {

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

        updateRecentlyViewedItem();
        updateProgressReport();
    }

    private void updateRecentlyViewedItem() {
        LinearLayout recentItemLayout = (LinearLayout) findViewById(R.id.most_recently_viewed_item);

        for (int i = recentItemLayout.getChildCount() - 1; i > 0; --i) {
            recentItemLayout.removeViewAt(recentItemLayout.getChildCount() - 1);
        }

        String recentlyViewedId = CHIPLoaderSQL.getInstance().getMostRecentNavItem(m_user.get_id());
        if (recentlyViewedId == null) {
            recentItemLayout.setVisibility(LinearLayout.GONE);
        } else {
            recentItemLayout.setVisibility(LinearLayout.VISIBLE);
            NavItem recentlyViewedItem = CHIPLoaderSQL.getInstance().getNavItem(recentlyViewedId);
            ArrayList<NavItem> navItemTree = new ArrayList<NavItem>();

            NavItem currentItem = recentlyViewedItem;
            navItemTree.add(currentItem);
            while(currentItem.getParentId() != null) {
                currentItem = CHIPLoaderSQL.getInstance().getNavItem(currentItem.getParentId());
                navItemTree.add(currentItem);
            }

            for (int i = navItemTree.size() - 1; i >= 0; --i) {
                TextView newTextView = new TextView(this);
                currentItem = navItemTree.get(i);
                newTextView.setText(currentItem.toString());
                newTextView.setTextSize(Consts.MEDIUM_TEXT_SIZE);

                TableRow fillerRow = new TableRow(this);
                fillerRow.addView(newTextView);
                recentItemLayout.addView(fillerRow);

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) fillerRow.getLayoutParams();
                lp.setMargins(20 * (navItemTree.size() - i), 5, 5, 5);
                fillerRow.setLayoutParams(lp);
                fillerRow.setTag(currentItem.getId());
                fillerRow.setOnClickListener(this);
            }
        }
    }

    private void updateProgressReport() {
        LinearLayout progressReportLayout = (LinearLayout) findViewById(R.id.progress_report);

        for (int i = progressReportLayout.getChildCount() - 1; i > 0; --i) {
            progressReportLayout.removeViewAt(progressReportLayout.getChildCount() - 1);
        }

        ArrayList<NavItem> topLevelItems = CHIPLoaderSQL.getInstance().getBaseNavItems();

        if (topLevelItems.isEmpty()) {
            progressReportLayout.setVisibility(LinearLayout.GONE);
        } else {
            for (int i = 0; i < topLevelItems.size(); ++i) {
                TextView newTextView = new TextView(this);
                NavItem currentItem = topLevelItems.get(i);

                double completionPercentage = currentItem.getCompletionPercent(m_user.get_id());
                if (completionPercentage == -1) {
                    completionPercentage = 100;
                }
                newTextView.setText(currentItem.toString() + " - " + completionPercentage + "%");
                newTextView.setTextSize(Consts.MEDIUM_TEXT_SIZE);

                TableRow fillerRow = new TableRow(this);
                fillerRow.addView(newTextView);
                progressReportLayout.addView(fillerRow);

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) fillerRow.getLayoutParams();
                lp.setMargins(20, 5, 5, 5);
                fillerRow.setLayoutParams(lp);
                fillerRow.setTag(currentItem.getId());
                fillerRow.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getClass() == TableRow.class &&
            view.getTag() != null && view.getTag().getClass() == String.class) {
            CommonFunctions.navigateToNavItem(this, m_user, (String) view.getTag());
        }
    }

    @Override
    public void onResume() {
        if (CommonFunctions.quitting_app) {
            finish();
        } else {
            updateRecentlyViewedItem();
            updateProgressReport();
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
        } else if (id == R.id.action_email_mentor) {
            CommonFunctions.emailMentor(this, m_user);
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
