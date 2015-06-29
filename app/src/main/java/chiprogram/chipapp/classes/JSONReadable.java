package chiprogram.chipapp.classes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by robtanniru on 6/20/15.
 */
public interface JSONReadable {
    public abstract void readFromJSONObject(JSONObject jsonObject,
                                            Map<String, NavItem> navItemMap,
                                            Map<String, Content> contentMap)
            throws JSONException, NullPointerException;
}
