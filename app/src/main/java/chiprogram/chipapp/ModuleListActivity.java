package chiprogram.chipapp;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;


/**
 * An activity representing a list of Modules. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NavItemTabsActivity} representing the chosen Module.
 * <p>
 * The activity makes use of fragments. The list of items is a
 * {@link ModuleListFragment}.
 * <p>
 * This activity also implements the required
 * {@link ModuleListFragment.Callbacks} interface
 * to listen for module selections.
 */
public class ModuleListActivity extends Activity
        implements ModuleListFragment.Callbacks {

    private CHIPUser m_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_list);

        // Show the Up button in the action bar.
        setupActionBar();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);
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
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
    }

    /**
     * Callback method from {@link ModuleListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String navItemId) {
        // start the nav item tabs activity with the chosen module
        Intent navItemTabsActivity = new Intent(this, NavItemTabsActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, m_user);
        extras.putString(NavItemTabsActivity.CURRENT_ID, navItemId);
        navItemTabsActivity.putExtras(extras);

        startActivity(navItemTabsActivity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.training, menu);
        return super.onCreateOptionsMenu(menu);
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

            NavUtils.navigateUpTo(this, intent);
            finish();
            return true;
        } else if (id == R.id.action_settings) {
            CommonFunctions.navigateToSettings(this, m_user);
            return true;
        } else if (id == R.id.action_profile) {
            CommonFunctions.navigateToProfile(this, m_user);
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
}
