package chiprogram.chipapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import chiprogram.chipapp.R;

/**
 * Created by Rob Tanniru on 10/6/2014.
 */
public class CHIPUser implements Parcelable {

    private String m_id;
    private String m_firstName;
    private String m_lastName;
    private String m_address;
    private String m_location;
    private String m_role;
    private String m_specialization;
    private String m_mentorId;
    private String m_bio;
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

    public CHIPUser(String _id, String _firstName, String _lastName, String _address, String _location,
                    String _role, String _specialization, String _mentorId,
                    String _bio, String _email) {

        // TODO: remove placeholders
        m_id = _id;
        m_firstName = _firstName;
        m_lastName = _lastName;
        m_address = _address;
        m_location = _location;
        m_role = _role;
        m_specialization = _specialization;
        m_mentorId = _mentorId;
        m_bio = _bio;
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

    public String get_address() {
        return m_address;
    }

    public String get_location() {
        return m_location;
    }

    public String get_role() {
        return m_role;
    }

    public String get_specialization() {
        return m_specialization;
    }

    public String get_mentorId() {
        return m_mentorId;
    }

    public CHIPUser get_mentor() {
        return CHIPLoaderSQL.getInstance().getMentor(m_mentorId, m_id);
    }

    public String get_bio() {
        return m_bio;
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
        out.writeString(m_address);
        out.writeString(m_location);
        out.writeString(m_role);
        out.writeString(m_specialization);
        out.writeString(m_mentorId);
        out.writeString(m_bio);
        out.writeString(m_email);
    }

    private void readFromParcel(Parcel in) {
        m_id = in.readString();
        m_firstName = in.readString();
        m_lastName = in.readString();
        m_address = in.readString();
        m_location = in.readString();
        m_role = in.readString();
        m_specialization = in.readString();
        m_mentorId = in.readString();
        m_bio = in.readString();
        m_email = in.readString();
    }
}
