package chiprogram.chipapp.classes;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import chiprogram.chipapp.R;

/**
 * Created by Rob Tanniru on 10/6/2014.
 */
public class CHIPUser implements Parcelable {

    private String m_firstName;
    private String m_lastName;
    private String m_address;
    private String m_location;
    private String m_role;
    private String m_specialization;
    private String m_mentorEmail;
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

    public CHIPUser(String _firstName, String _lastName, String _address, String _location,
                    String _role, String _specialization, String _mentorEmail,
                    String _bio, String _email) {

        // TODO: remove placeholders
        m_firstName = _firstName.isEmpty() ? "Rob" : _firstName;
        m_lastName = _lastName.isEmpty() ? "Tanniru" : _lastName;
        m_address = _address.isEmpty() ? "123 4th Street\r\nRochester, MI 48306" : _address;
        m_location = _location.isEmpty() ? "Detroit" : _location;
        m_role = _role.isEmpty() ? "Mentor" : _role;
        m_specialization = _specialization;
        m_mentorEmail = _mentorEmail.isEmpty() ? "tanniru@oakland.edu" : _mentorEmail;
        m_bio = _bio.isEmpty() ? "Test Bio" : _bio;
        m_email = _email.isEmpty() ? "bkt421@gmail.com" : _email;
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

    public String get_mentorEmail() {
        return m_mentorEmail;
    }

    public String get_bio() {
        return m_bio;
    }

    public String get_email() {
        return m_email;
    }

    // return value of 0 means error,
    // if error > 0, then that member variable is the last one with an error.
    public int validateUser(Activity activity) {
        int error = 0;

        if (m_firstName.isEmpty()) {
            error = 1;
        } else if (m_lastName.isEmpty()) {
            error = 2;
        } else if (m_address.isEmpty()){
            error = 3;
        } else if (m_location.isEmpty()){
            error = 4;
        } else if (m_role.isEmpty()){
            error = 5;
        } /*else if (m_mentorEmail.isEmpty()){
            error = 6; TODO: is validation of mentor email needed?
        } */
        else if (m_role.equals(activity.getString(R.string.common_mentor)) && m_specialization.isEmpty()) { // only mentors have specializations
            error = 6;
        } else if (m_bio.isEmpty()){
            error = 7;
        } else if (CommonFunctions.ValidateEmail(m_email) == false){
            error = 8;
        }

        // TODO: check for SQL injection characters?

        return error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(m_firstName);
        out.writeString(m_lastName);
        out.writeString(m_address);
        out.writeString(m_location);
        out.writeString(m_role);
        out.writeString(m_specialization);
        out.writeString(m_mentorEmail);
        out.writeString(m_bio);
        out.writeString(m_email);
    }

    private void readFromParcel(Parcel in) {
        m_firstName = in.readString();
        m_lastName = in.readString();
        m_address = in.readString();
        m_location = in.readString();
        m_role = in.readString();
        m_specialization = in.readString();
        m_mentorEmail = in.readString();
        m_bio = in.readString();
        m_email = in.readString();
    }
}
