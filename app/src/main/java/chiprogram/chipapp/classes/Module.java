package chiprogram.chipapp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Rob Tanniru on 10/7/2014.
 */
public class Module implements Parcelable {

    private String m_id;
    private String m_name;
    private ArrayList<Chapter> m_chapters;

    public Module(Parcel p) {
        readFromParcel(p);
    }

    public static final Parcelable.Creator<Module> CREATOR
            = new Parcelable.Creator<Module>() {
        public Module createFromParcel(Parcel in) {
            return new Module(in);
        }

        public Module[] newArray(int size) {
            return new Module[size];
        }
    };

    public Module(String _id, String _name) {
        m_id = _id;
        m_name = _name;
        m_chapters = new ArrayList<Chapter>();
    }

    public String getId() {
        return m_id;
    }

    public ArrayList<Chapter> getChapters() {
        return m_chapters;
    }

    public Chapter getChapter(String chapterName) {
        for (Chapter m_chapter : m_chapters) {
            if (m_chapter.getName().equals(chapterName)) {
                return m_chapter;
            }
        }
        return null;
    }

    public boolean addChapter(Chapter _chapter) {
        return m_chapters.add(_chapter);
    }

    @Override
    public String toString() {
        return m_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    // Note: only parcels up the names of the chapters, not the entire details of the chapter.
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(m_id);
        out.writeString(m_name);
        out.writeInt(m_chapters.size());
        for (Chapter m_chapter : m_chapters) {
            out.writeString(m_chapter.getId());
            out.writeString(m_chapter.getName());
        }
    }

    private void readFromParcel(Parcel in) {
        m_id = in.readString();
        m_name = in.readString();
        int numChapters = in.readInt();
        m_chapters = new ArrayList<Chapter>();
        for (int i = 0; i < numChapters; ++i) {
            String id = in.readString();
            m_chapters.add(new Chapter(id, in.readString()));
        }
    }
}
