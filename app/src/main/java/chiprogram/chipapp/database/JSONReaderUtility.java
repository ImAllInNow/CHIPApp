package chiprogram.chipapp.database;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by bhatia on 6/20/2015.
 */
public class JSONReaderUtility {
    private static JSONReaderUtility instance;

    private JSONReaderUtility(){}
    public static JSONReaderUtility getInstance()
    {
        if (instance==null)
            instance = new JSONReaderUtility();
        return instance;
    }

    public JSONObject readJSONAsset(Context currContext, String assetFileNameAndRelativePath)
    {
        JSONObject jsonObject = null;
        try {
            InputStream is = currContext.getAssets().open(assetFileNameAndRelativePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonObject = new JSONObject(new String(buffer, "UTF-8"));
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException ex2) {
            ex2.printStackTrace();
        }
        return jsonObject;
    }
}
