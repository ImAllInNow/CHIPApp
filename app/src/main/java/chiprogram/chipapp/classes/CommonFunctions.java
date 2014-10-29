package chiprogram.chipapp.classes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import chiprogram.chipapp.AboutCHIPActivity;
import chiprogram.chipapp.DiscussionActivity;
import chiprogram.chipapp.IntroScreenActivity;
import chiprogram.chipapp.LoginActivity;
import chiprogram.chipapp.ModuleListActivity;
import chiprogram.chipapp.ProfileActivity;
import chiprogram.chipapp.SettingsActivity;

/**
 * Created by Rob Tanniru on 10/6/2014.
 */
public class CommonFunctions {

    public static boolean quitting_app = false;

    public static boolean ValidateEmail(String email) {
        // TODO: make this better

        return (email.indexOf('@') != -1);
    }

    public static boolean ValidatePassword(String password) {
        // TODO: make this better

        return (password.length() >= 4);
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

    public static void navigateToTraining(Activity activity, CHIPUser user) {
        Intent intent = new Intent(activity, ModuleListActivity.class);

        // add in user to bundle
        Bundle extras = new Bundle();
        extras.putParcelable(ProfileActivity.ARGUMENT_USER, user);

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

    private static void resetAutoLoginPreference(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(Consts.PREF_REM_PASSWORD, "");

        editor.apply();
    }
}
