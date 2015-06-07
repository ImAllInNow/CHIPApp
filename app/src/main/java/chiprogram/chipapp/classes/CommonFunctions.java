package chiprogram.chipapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import chiprogram.chipapp.activities.AboutCHIPActivity;
import chiprogram.chipapp.activities.DiscussionActivity;
import chiprogram.chipapp.activities.HomeActivity;
import chiprogram.chipapp.activities.IntroScreenActivity;
import chiprogram.chipapp.activities.LoginActivity;
import chiprogram.chipapp.activities.ModuleListActivity;
import chiprogram.chipapp.activities.NavItemTabsActivity;
import chiprogram.chipapp.activities.ProfileActivity;
import chiprogram.chipapp.R;
import chiprogram.chipapp.activities.SettingsActivity;

/**
 * Created by Rob Tanniru on 10/6/2014.
 */
public class CommonFunctions {

    public static final String DATABASE_URL_BASE = "http://www.tombrusca.com/chip/json/";
    public static final String DATABASE_LOGIN_PAGE = "auth_user.aspx";
    public static final String DATABASE_GET_ASSESSMENTS_PAGE = "get_assessment.aspx";
    public static final String DATABASE_GET_ASSESSMENT_SCORE_PAGE = "get_assessment_score.aspx";
    public static final String DATABASE_SET_ASSESSMENT_SCORE_PAGE = "set_assessment_score.aspx";

    public static boolean quitting_app = false;

    public static boolean ValidateEmail(String email) {
        return (email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,4})+$"));
    }

    public static boolean ValidatePassword(String password) {
        return (password.matches("^\\w{4,12}$"));
    }

    // TODO: extract time bracket parameters
    public static String getYouTubeVideoID(String url) {
        int startIndex = 0;
        int endIndex = url.length();
        if (url.indexOf("www.youtube.com") != -1) {
            String searchString = "v=";
            startIndex = url.indexOf(searchString) + searchString.length();
            endIndex = (url.indexOf("&") == -1) ? endIndex : url.indexOf("&");

        } else if (url.indexOf("youtu.be") != -1) {
            String searchString = "youtu.be/";
            startIndex = url.indexOf(searchString) + searchString.length();
            endIndex = (url.indexOf("?") == -1) ? endIndex : url.indexOf("?");
        } else {
            // TODO: throw error
        }

        return url.substring(startIndex, endIndex);
    }

    public static String convertUrlToGoogleDocsURL(String url) {
        return "https://docs.google.com/viewer?url=" + convertFromUrlToUrlParameter(url);
    }

    public static String convertFromUrlToUrlParameter(String url) {
        try {
            String urlParam = URLEncoder.encode(url, "UTF-8");
            return urlParam;
        } catch (UnsupportedEncodingException e) {
            // TODO: handle exception
        }
        return url;
    }

    public static void navigateToAboutCHIP(Activity activity) {
        Intent intent = new Intent(activity, AboutCHIPActivity.class);
        activity.startActivity(intent);
    }

    public static void navigateToHome(Activity activity, CHIPUser user) {
        Intent intent = new Intent(activity, HomeActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    public static void navigateToTraining(Activity activity, CHIPUser user) {
        Intent intent = new Intent(activity, ModuleListActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    public static void navigateToNavItem(Activity activity, CHIPUser user, String navItemId) {
        Intent intent = new Intent(activity, NavItemTabsActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);
        extras.putString(NavItemTabsActivity.CURRENT_ID, navItemId);

        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    public static void navigateToProfile(Activity activity, CHIPUser user) {
        Intent intent = new Intent(activity, ProfileActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    public static void navigateToDiscussion(Activity activity, CHIPUser user) {
        Intent intent = new Intent(activity, DiscussionActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    public static void navigateToSettings(Activity activity, CHIPUser user) {
        Intent intent = new Intent(activity, SettingsActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

        intent.putExtras(extras);

        activity.startActivity(intent);
    }

    public static void handleLogout(Activity activity) {
        resetAutoLoginPreference(activity);

        Intent intent = new Intent(activity, IntroScreenActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void emailMentor(Activity activity, CHIPUser user) {
        CHIPUser mentor = user.get_mentor();
        if (mentor != null) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mentor.get_email(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "CHIP mentor email");
            activity.startActivity(Intent.createChooser(emailIntent, "Send email to mentor..."));
        } else {
            Toast.makeText(activity, activity.getString(R.string.no_mentor), Toast.LENGTH_SHORT).show();
        }
    }

    private static void resetAutoLoginPreference(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Consts.PREF_REM_PASSWORD, "");

        editor.apply();
    }
}
