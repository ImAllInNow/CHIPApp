/*
 * Created by: Robert Tanniru
 * 2014
 */

package chiprogram.chipapp.classes;

import org.json.JSONObject;

public abstract interface SavableObject {
	public abstract void readFromFileString(String fs);
	public abstract String toFileString();

    public abstract void readFromJSONObject(JSONObject jsonObject);
}
