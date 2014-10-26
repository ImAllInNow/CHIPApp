package chiprogram.chipapp.classes;

/**
 * Created by Turtle II on 10/25/2014.
 */
public class Content {

    public enum ContentType {
        YOUTUBE_VIDEO,
        PDF_LINK,
        PPT_LINK,
    }

    private String m_id;
    private String m_name;
    private ContentType m_type;
    private String m_link;

    public Content(String _id, String _name, ContentType _type, String _link) {
        m_id = _id;
        m_name = _name;
        m_type = _type;
        m_link = _link;
    }

    public String getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public ContentType getType() {
        return m_type;
    }

    public String getLink() {
        return m_link;
    }

    @Override
    public String toString() {
        return m_name;
    }
}
