package chiprogram.chipapp.activities;

import android.app.Activity;

import chiprogram.chipapp.classes.CommonFunctions;

/**
 * Created by robtanniru on 5/31/15.
 */
public class BaseActivity extends Activity {
    @Override
    public void onResume() {
        if (CommonFunctions.quitting_app) {
            finish();
        }
        super.onResume();
    }
}
