package chiprogram.chipapp.classes;

import java.util.ArrayList;

/**
 * Created by Turtle II on 10/25/2014.
 */
public class NavItem {

    private String m_id;
    private String m_name;
    private double m_completionPercent;
    private ArrayList<Content> m_contentArray;
    private ArrayList<NavItem> m_childArray;
    private ArrayList<Question> m_questionArray;

    public NavItem(String _id, String _name) {
        m_id = _id;
        m_name = _name;
        m_completionPercent = 0;
        m_contentArray = new ArrayList<Content>();
        m_childArray = new ArrayList<NavItem>();
        m_questionArray = new ArrayList<Question>();
    }

    public String getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public double getCompletionPercent() {
        return m_completionPercent;
    }

    public boolean hasContent() {
        return (m_contentArray.isEmpty() == false);
    }

    public boolean hasChildren() {
        return (m_childArray.isEmpty() == false);
    }

    public boolean hasQuestions() {
        return (m_questionArray.isEmpty() == false);
    }

    public int getNumContent() {
        return m_contentArray.size();
    }

    public ArrayList<Content> getContentArray() {
        return m_contentArray;
    }

    public Content getContent(int index) {
        if (index >= 0 && index < m_contentArray.size()) {
            return m_contentArray.get(index);
        } else {
            return null;
        }
    }

    public ArrayList<Content> getContentByType(Content.ContentType type) {
        ArrayList<Content> returnArray = new ArrayList<Content>();

        for (Content content : m_contentArray) {
            if (content.getType().equals(type)) {
                returnArray.add(content);
            }
        }
        return returnArray;
    }

    public boolean addContent(Content _content) {
        return m_contentArray.add(_content);
    }

    public int getNumChildren() {
        return m_childArray.size();
    }

    public ArrayList<NavItem> getChildArray() {
        return m_childArray;
    }

    public NavItem getChild(String childName) {
        for (NavItem child : m_childArray) {
            if (child.getName().equals(childName)) {
                return child;
            }
        }
        return null;
    }

    public NavItem getChild(int index) {
        if (index >= 0 && index < m_childArray.size()) {
            return m_childArray.get(index);
        } else {
            return null;
        }
    }

    public boolean addChild(NavItem _child) {
        return m_childArray.add(_child);
    }

    public boolean addQuestion(Question q) {
        return (m_questionArray.add(q));
    }

    public int getNumQuestions() {
        return m_questionArray.size();
    }

    public Question getQuestion(int index) {
        if (index >= 0 && index < m_questionArray.size()) {
            return m_questionArray.get(index);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return m_name;
    }
}
