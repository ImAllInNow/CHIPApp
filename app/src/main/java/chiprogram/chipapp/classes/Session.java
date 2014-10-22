package chiprogram.chipapp.classes;

import java.util.ArrayList;

/**
 * Created by Rob Tanniru on 10/7/2014.
 */
public class Session {

    private String m_id;
    private String m_name;
    private String m_presentationLink;

    private ArrayList<Question> m_questions;

    public Session(String _id, String _name, String _presentationLink) {
        m_id = _id;
        m_name = _name;
        m_presentationLink = _presentationLink;

        m_questions = new ArrayList<Question>();
    }

    public String getId() {
        return m_id;
    }

    public String getPresentationLink() {
        return m_presentationLink;
    }

    public boolean addQuestion(Question q) {
        return (m_questions.add(q));
    }

    public int getNumQuestions() {
        return m_questions.size();
    }

    public Question getQuestion(int index) {
        if (index >= 0 && index < m_questions.size()) {
            return m_questions.get(index);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return m_name;
    }


}
