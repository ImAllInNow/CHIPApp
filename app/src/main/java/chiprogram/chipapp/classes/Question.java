package chiprogram.chipapp.classes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rob Tanniru on 10/7/2014.
 */
public class Question {

    public enum QuestionType {
        SINGLE_ANSWER,
        MULTIPLE_ANSWERS,
        DISCUSSION
    }

    private String m_id;
    private String m_questionString;
    private ArrayList<String> m_possibleAnswers;
    private ArrayList<Boolean> m_AnswersCorrectness;

    private QuestionType m_questionType;

    public Question(String _id, String _questionString, QuestionType _type) {
        m_id = _id;
        m_questionString = _questionString;
        m_questionType = _type;

        m_possibleAnswers = new ArrayList<String>();
        m_AnswersCorrectness = new ArrayList<Boolean>();
    }

    public String getId() {
        return m_id;
    }

    public QuestionType getType() {
        return m_questionType;
    }

    public int numAnswers() {
        return m_possibleAnswers.size();
    }

    public String getPossibleAnswer(int index) {
        if (index >= 0 && index < m_possibleAnswers.size()) {
            return (m_possibleAnswers.get(index));
        } else {
            return null;
        }
    }

    public boolean addPossibleAnswer(String answerString, boolean isCorrect) {
        if (m_possibleAnswers.add(answerString)) {
            if (m_AnswersCorrectness.add(isCorrect)) {
                return true;
            } else {
                m_possibleAnswers.remove(answerString);
            }
        }
        return false;
    }

    public boolean checkAnswer(Boolean[] answerChosen) {
        boolean defaultCorrectness = (m_questionType != QuestionType.SINGLE_ANSWER);

        for (int i = 0; i < m_AnswersCorrectness.size(); ++i) {
            if (m_questionType == QuestionType.SINGLE_ANSWER) {
                if (answerChosen[i] && m_AnswersCorrectness.get(i)) {
                    return true;
                }
            } else {
                if (answerChosen[i] && (m_AnswersCorrectness.get(i) == false)) {
                    return false;
                }
            }
        }

        return defaultCorrectness;
    }

    @Override
    public String toString() {
        return m_questionString;
    }
}
