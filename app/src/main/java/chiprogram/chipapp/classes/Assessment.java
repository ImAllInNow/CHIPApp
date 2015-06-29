package chiprogram.chipapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rob Tanniru on 11/9/2014.
 */
public class Assessment {
    private String m_id;
    private String m_name;
    private int m_passingPercent;
    private ArrayList<Question> m_questionArray;

    public Assessment(String _id, String _name, int _passingPercent) {
        m_id = _id;
        m_name = _name;
        m_passingPercent = _passingPercent;
        m_questionArray = new ArrayList<Question>();
    }

    public String getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public int getPassingPercent() {
        return m_passingPercent;
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
}
