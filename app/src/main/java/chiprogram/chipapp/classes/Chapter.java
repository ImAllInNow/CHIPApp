package chiprogram.chipapp.classes;

import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Created by Rob Tanniru on 10/7/2014.
 */
public class Chapter {

    private String m_id;
    private String m_name;
    private ArrayList<Session> m_sessions;

    private String m_chapterVideoURL;
    private String m_chapterPdfURL;

    public Chapter(String _id, String _name) {
        m_id = _id;
        m_name = _name;
        m_chapterVideoURL = null;
        m_chapterPdfURL = null;

        m_sessions = new ArrayList<Session>();
    }

    public Chapter(String _id, String _name, String _videoURL, String _pdfURL) {
        m_id = _id;
        m_name = _name;
        m_chapterVideoURL = _videoURL;
        m_chapterPdfURL = _pdfURL;

        m_sessions = new ArrayList<Session>();
    }

    public String getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public String getVideoURL() {
        return m_chapterVideoURL;
    }

    public String getPdfURL() {
        return m_chapterPdfURL;
    }

    public int numSessions() {
        return m_sessions.size();
    }

    public Session getSessions(int index) {
        if (index >= 0 && index < m_sessions.size()) {
            return m_sessions.get(index);
        } else {
            return null;
        }
    }

    public boolean addSession(Session s) {
        return m_sessions.add(s);
    }

    @Override
    public String toString() {
        return m_name;
    }
}
