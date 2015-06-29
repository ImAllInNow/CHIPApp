package chiprogram.chipapp.classes;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import chiprogram.chipapp.database.CHIPLoaderSQL;

/**
 * Created by Turtle II on 10/25/2014.
 */
public class NavItem implements JSONReadable {

    private String m_id;
    private String m_parentId;
    private String m_name;
    private String m_childrenName;
    private ArrayList<Content> m_contentArray;
    private ArrayList<NavItem> m_childArray;
    private ArrayList<Assessment> m_assessmentArray;

    public NavItem(String _id, String _parentId, String _name) {
        initializeMembers(_id, _parentId, _name, null);
    }

    public NavItem(String _id, String _parentId, String _name, String _childrenName) {
        initializeMembers(_id, _parentId, _name, _childrenName);
    }

    private void initializeMembers(String _id, String _parentId, String _name, String _childrenName) {
        m_id = _id;
        m_parentId = _parentId;
        m_name = _name;
        m_childrenName = _childrenName;
        m_contentArray = new ArrayList<Content>();
        m_childArray = new ArrayList<NavItem>();
        m_assessmentArray = new ArrayList<Assessment>();
    }

    public String getId() {
        return m_id;
    }
    public String getParentId() {
        return m_parentId;
    }

    public String getName() {
        return m_name;
    }

    public String getChildrenName() {
        return m_childrenName;
    }

    // returns -1 if there are no items in this NavItem that can be completed
    public double getCompletionPercent(Activity activity, String userEmail) {
        double runningSum = 0;
        int runningCount = 0;
        if (hasChildren()) {
            for (NavItem navItem : m_childArray) {
                double subCompletionPercent = navItem.getCompletionPercent(activity, userEmail);
                if (subCompletionPercent != -1) {
                    runningSum = runningSum + subCompletionPercent;
                    ++runningCount;
                }
            }
        }
        if (hasAssessments()) {
            for (Assessment assessment : m_assessmentArray) {
                ++runningCount;
                int currScore = CHIPLoaderSQL.getInstance().getAssessmentScore(activity, assessment.getId(), userEmail);
                if (currScore == -1) currScore = 0;
                double assessmentScore = (double) currScore / assessment.getNumQuestions();
                if (assessmentScore * 100 >= assessment.getPassingPercent()) {
                    runningSum = runningSum + 100;
                }
            }
        }

        if (runningCount == 0) {
            return -1;
        } else {
            return runningSum / runningCount;
        }
    }

    public boolean hasContent() {
        return (m_contentArray.isEmpty() == false);
    }

    public boolean hasChildren() {
        return (m_childArray.isEmpty() == false);
    }

    public boolean hasAssessments() {
        return (m_assessmentArray.isEmpty() == false);
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

    public boolean addAssessment(Assessment a) {
        return (m_assessmentArray.add(a));
    }

    public int getNumAssessments() {
        return (m_assessmentArray.size());
    }

    public Assessment getAssessment(int index) {
        if (index >= 0 && index < m_assessmentArray.size()) {
            return m_assessmentArray.get(index);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return m_name;
    }


    @Override
    public void readFromJSONObject(JSONObject jsonObject,
                                   Map<String, NavItem> navItemMap,
                                   Map<String, Content> contentMap)
            throws JSONException, NullPointerException {
        JSONArray content = jsonObject.has("content") ? jsonObject.getJSONArray("content") : null;

        if (content != null) {
            for (int i = 0; i < content.length(); ++i) {
                JSONObject c = content.getJSONObject(i);
                String c_id = "" + c.getInt("id");
                String c_name = c.getString("name");
                Content.ContentType c_type = Content.ContentType.values()[c.getInt("type")];
                String c_link = c.getString("link");
                Content contentAdd = new Content(c_id, c_name, c_type, c_link);
                contentMap.put(c_id, contentAdd);
                m_contentArray.add(contentAdd);
            }
        }

        JSONArray assessments = jsonObject.has("assessments") ? jsonObject.getJSONArray("assessments") : null;

        if (assessments != null) {
            for (int i = 0; i < assessments.length(); ++i) {
                JSONObject a = assessments.getJSONObject(i);
                String a_id = "" + a.getInt("id");
                m_assessmentArray.add(CHIPLoaderSQL.getInstance().getAssessment(a_id));
            }
        }

        JSONArray children = jsonObject.has("children") ? jsonObject.getJSONArray("children") : null;

        if (children != null) {
            for (int i = 0; i < children.length(); ++i) {
                JSONObject child = children.getJSONObject(i);
                String c_id = "" + child.getInt("id");
                String c_parentId = m_id;
                String c_name = child.getString("name");
                String c_childrenName = child.has("childrenName") ? child.getString("childrenName") : null;
                NavItem childAdd = new NavItem(c_id, c_parentId, c_name, c_childrenName);
                childAdd.readFromJSONObject(child, navItemMap, contentMap);
                navItemMap.put(c_id, childAdd);
                m_childArray.add(childAdd);
            }
        }
    }
}
