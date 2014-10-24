package chiprogram.chipapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Module;


/**
 * An activity representing a list of Chapters. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ChapterTabsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes use of fragments. The list of items is a
 * {@link chiprogram.chipapp.ChapterListFragment}.
 * <p>
 * This activity also implements the required
 * {@link chiprogram.chipapp.ChapterListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ChapterListActivity extends Activity
        implements ChapterListFragment.Callbacks {

    public static final String ARGUMENT_MODULE_ID = "chiprogram.chipapp.ARGUMENT_MODULE_ID";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    //private boolean mTwoPane;

    private CHIPUser m_user;
    private String m_moduleIdChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        // Show the Up button in the action bar.
        setupActionBar();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);

        if (extras.containsKey(ARGUMENT_MODULE_ID)) {
            m_moduleIdChosen = extras.getString(ARGUMENT_MODULE_ID);
            Module m = CHIPLoaderSQL.getModule(m_moduleIdChosen);
            this.setTitle(getString(R.string.title_chapter_list) + " " +
                    getString(R.string.common_for) + " " + m.toString());
        } else {
            m_moduleIdChosen = null;
        }

        ChapterListFragment childFragment =
                (ChapterListFragment) getFragmentManager().findFragmentById(R.id.chapter_list);
        childFragment.setModuleId(m_moduleIdChosen);
        childFragment.setArrayAdapter();
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
     * Callback method from {@link chiprogram.chipapp.ChapterListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String chapterId) {
        //Intent chapterDetailIntent = new Intent(this, ChapterDetailActivity.class);
        Intent chapterTabsIntent = new Intent(this, ChapterTabsActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, m_user);
        extras.putString(ChapterTabsActivity.MODULE_ID, m_moduleIdChosen);
        extras.putString(ChapterTabsActivity.CHAPTER_ID, chapterId);
        chapterTabsIntent.putExtras(extras);

        startActivity(chapterTabsIntent);
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
            onBackPressed();
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
