package chiprogram.chipapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableRow;

import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.Locale;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Content;
import chiprogram.chipapp.classes.NavItem;

public class NavItemTabsActivity extends Activity implements ActionBar.TabListener,
        ChapterVideoFragmentListener.OnFragmentInteractionListener,
        ChapterSessionsFragment.OnFragmentInteractionListener,
        NavItemListFragment.Callbacks,
        ContentListFragment.Callbacks,
        View.OnClickListener {

    public enum TabType {
        CONTENT,
        CHILDREN,
        QUESTIONS
    }

    public static final String PARENT_ID = "chiprogram.chipapp.PARENT_ID";
    public static final String CURRENT_ID = "chiprogram.chipapp.CURRENT_ID";

    public final static int REQUEST_ASSESSMENT = 1;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v13.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private CHIPUser m_user;
    private NavItem m_navItem;
    String m_parentId;
    String m_currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_tabs);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);

        m_parentId = extras.getString(PARENT_ID);
        m_currentId = extras.getString(CURRENT_ID);

        m_navItem = CHIPLoaderSQL.getNavItem(m_currentId);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        // TODO: make sure this is the right way to handle a null nav item
        if (m_navItem != null) {
            this.setTitle(m_navItem.toString());

            // Create the adapter that will return a fragment for each of the three
            // primary sections of the activity.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
            mSectionsPagerAdapter.hasContent = m_navItem.hasContent();
            mSectionsPagerAdapter.hasChildren = m_navItem.hasChildren();
            mSectionsPagerAdapter.hasQuestions = m_navItem.hasQuestions();

            // Set up the ViewPager with the sections adapter.
            mViewPager = (ViewPager) findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            // When swiping between different sections, select the corresponding
            // tab. We can also use ActionBar.Tab#select() to do this if we have
            // a reference to the Tab.
            mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }
            });

            // For each of the sections in the app, add a tab to the action bar.
            for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
                actionBar.addTab(
                        actionBar.newTab()
                                .setText(mSectionsPagerAdapter.getPageTitle(i))
                                .setTabListener(this));
            }
        }
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
        getMenuInflater().inflate(R.menu.training, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onItemSelected(boolean isNavItem, int index) {
        if (isNavItem) {
            NavItem chosenNavItem = m_navItem.getChild(index);

            Intent intent = new Intent(this, NavItemTabsActivity.class);

            // add in user to bundle
            Bundle extras = new Bundle();
            extras.putParcelable(ProfileActivity.ARGUMENT_USER, m_user);
            extras.putString(NavItemTabsActivity.CURRENT_ID, chosenNavItem.getId());
            extras.putString(NavItemTabsActivity.PARENT_ID, m_currentId);

            intent.putExtras(extras);

            startActivity(intent);
        } else {
            // branch based on type of content
            Content chosenContent = m_navItem.getContent(index);

            switch (chosenContent.getType()) {
                case YOUTUBE_VIDEO:
                    // TODO: start YouTubePlayerActivity

                    break;
                case PDF_LINK:
                    // TODO: handle pdfs

                    break;
                case PPT_LINK:
                    // TODO: handle ppts

                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getClass() == new TableRow(this).getClass()) {
            Intent intent = new Intent(this, AssessmentActivity.class);

            // add in user to bundle
            Bundle extras = new Bundle();
            extras.putParcelable(ProfileActivity.ARGUMENT_USER, m_user);
            extras.putString(AssessmentActivity.SESSION_ID, (String) view.getTag());

            intent.putExtras(extras);

            startActivityForResult(intent, REQUEST_ASSESSMENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Bundle extras = data.getExtras();

            if (requestCode == REQUEST_ASSESSMENT) {
                // score is held in resultCode (-1 == cancel)
                if (resultCode == -1) {

                } else {
                    // TODO: update score in database

                    // TODO: update assessments fragment
                    this.recreate();
                }
            }
        }
    }

    /**
     * A {@link android.support.v13.app.FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public boolean hasContent;
        public boolean hasChildren;
        public boolean hasQuestions;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            hasContent = false;
            hasChildren = false;
            hasQuestions = false;
        }

        private TabType getTabType(int position) {
            switch(position) {
                case 0:
                    if (hasContent) {
                        return TabType.CONTENT;
                    } else if (hasChildren) {
                        return TabType.CHILDREN;
                    } else if (hasQuestions) {
                        return TabType.QUESTIONS;
                    } else {
                        return null;
                    }
                case 1:
                    if (hasContent) {
                        if (hasChildren) {
                            return TabType.CHILDREN;
                        } else if (hasQuestions) {
                            return TabType.QUESTIONS;
                        } else {
                            return null;
                        }
                    } else if (hasChildren) {
                        if (hasQuestions) {
                            return TabType.QUESTIONS;
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                case 2:
                    if (hasContent && hasChildren && hasQuestions) {
                        return TabType.QUESTIONS;
                    } else {
                        return null;
                    }
            }
            return null;
        }

        @Override
        public Fragment getItem(int position) {
            switch(getTabType(position)) {
                case CONTENT:
                    ContentListFragment clFrag = new ContentListFragment();
                    clFrag.setNavItemId(m_currentId);
                    //clFrag.setArrayAdapter();
                    return clFrag;
                case CHILDREN:
                    NavItemListFragment nilFrag = new NavItemListFragment();
                    nilFrag.setNavItemId(m_currentId);
                    //nilFrag.setArrayAdapter();
                    return nilFrag;
                case QUESTIONS:
                    // TODO: fix how the assessment fragment works
                    return ChapterAssessmentsFragment.newInstance(m_user, m_currentId);
            }
            return null;
        }

        @Override
        public int getCount() {
            int count = 0;
            if (hasContent) {
                ++count;
            }
            if (hasChildren) {
                ++count;
            }
            if (hasQuestions) {
                ++count;
            }
            return count;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch(getTabType(position)) {
                case CONTENT:
                    return getString(R.string.title_section1).toUpperCase(l);
                case CHILDREN:
                    if (m_navItem.getChildrenName() == null) {
                        return getString(R.string.title_section2).toUpperCase(l);
                    } else if (m_navItem.getChildrenName().equals("Modules")) {
                        return getString(R.string.title_section2_modules).toUpperCase(l);
                    } else if (m_navItem.getChildrenName().equals("Chapters")) {
                        return getString(R.string.title_section2_chapters).toUpperCase(l);
                    } else if (m_navItem.getChildrenName().equals("Sessions")) {
                        return getString(R.string.title_section2_sessions).toUpperCase(l);
                    } else {
                        return m_navItem.getChildrenName().toUpperCase(l);
                    }
                case QUESTIONS:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }
    }
}
