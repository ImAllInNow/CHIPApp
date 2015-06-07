package chiprogram.chipapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import chiprogram.chipapp.database.CHIPLoaderSQL;

/**
 * Created by Rob Tanniru on 10/6/2014.
 */
public class CHIPUser implements Parcelable {

    private String m_id;
    private String m_firstName;
    private String m_lastName;
    private String m_role;
    private String m_email;

    public CHIPUser(Parcel p) {
        readFromParcel(p);
    }

    public static final Parcelable.Creator<CHIPUser> CREATOR
            = new Parcelable.Creator<CHIPUser>() {
        public CHIPUser createFromParcel(Parcel in) {
            return new CHIPUser(in);
        }

        public CHIPUser[] newArray(int size) {
            return new CHIPUser[size];
        }
    };

    public CHIPUser(JSONObject jsonObject) {
        try {
            JSONObject CHIP = jsonObject.getJSONObject("CHIP");
            m_id = "" + CHIP.getInt("ID");
            m_firstName = CHIP.getString("FirstName");
            m_lastName = CHIP.getString("LastName");
            m_role = CHIP.getInt("RoleID") == 2 ? "Admin" : "Innovator";
            m_email = CHIP.getString("Email");
        } catch (JSONException e) {
            m_id = null;
        } catch (NullPointerException e) {
            m_id = null;
        }
    }

    public CHIPUser(String _id, String _firstName, String _lastName,
                    String _role, String _email) {
        m_id = _id;
        m_firstName = _firstName;
        m_lastName = _lastName;
        m_role = _role;
        m_email = _email;
    }

    public String get_id() {
        return m_id;
    }

    public String get_firstName() {
        return m_firstName;
    }

    public String get_lastName() {
        return m_lastName;
    }

    public String get_role() {
        return m_role;
    }

    public String get_email() {
        return m_email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(m_id);
        out.writeString(m_firstName);
        out.writeString(m_lastName);
        out.writeString(m_role);
        out.writeString(m_email);
    }

    private void readFromParcel(Parcel in) {
        m_id = in.readString();
        m_firstName = in.readString();
        m_lastName = in.readString();
        m_role = in.readString();
        m_email = in.readString();
    }
}
