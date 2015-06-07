package chiprogram.chipapp.database;

import android.util.Log;

import com.google.api.client.json.JsonFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import chiprogram.chipapp.classes.CommonFunctions;

/**
 * Created by Rob Tanniru on 1/24/2015.
 */
public class JSONServlet {

    public static String getResponseHttp(String URL) {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(URL);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e("getResponseHttp", "Failed to get response");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private static class LoginServlet extends Thread {
        @Override
        public void run() {
            m_listener.onLoginResult(getLoginResponse());
        }

        public LoginServlet(LoginServletListener _listener, String _email, String _password) {
            m_listener = _listener;
            m_email = _email;
            m_password = _password;
        }

        private LoginServletListener m_listener;
        private String m_email;
        private String m_password;

        public JSONObject getLoginResponse() {
            JSONObject jsonObject = null;
            try {
                String URL = CommonFunctions.DATABASE_URL_BASE + CommonFunctions.DATABASE_LOGIN_PAGE;
                URL += "?email=" + m_email;
                URL += "&password=" + m_password;

                jsonObject = new JSONObject(getResponseHttp(URL));
                Log.e("getLoginResponse", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
    public static interface LoginServletListener {
        public void onLoginResult(JSONObject jsonObject);
    }
    public static void runLoginServlet(LoginServletListener listener, String email, String password) {
        (new LoginServlet(listener, email, password)).start();
    }

    private static class AssessmentServlet extends Thread {
        @Override
        public void run() {
            m_listener.onAssessmentResult(getAssessmentResponse());
        }

        public AssessmentServlet(AssessmentServletListener _listener, String _trainingId) {
            m_listener = _listener;
            m_trainingId = _trainingId;
        }

        private AssessmentServletListener m_listener;
        private String m_trainingId;

        public JSONObject getAssessmentResponse() {
            JSONObject jsonObject = null;
            try {
                String URL = CommonFunctions.DATABASE_URL_BASE + CommonFunctions.DATABASE_GET_ASSESSMENTS_PAGE;
                URL += "?trainingId=" + m_trainingId;

                jsonObject = new JSONObject(getResponseHttp(URL));
                Log.e("getAssessmentResponse", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
    public static interface AssessmentServletListener {
        public void onAssessmentResult(JSONObject jsonObject);
    }
    public static void runAssessmentServlet(AssessmentServletListener listener, String trainingId) {
        (new AssessmentServlet(listener, trainingId)).start();
    }

    private static class GetAssessmentScoreServlet extends Thread {
        @Override
        public void run() {
            m_listener.onGetAssessmentScoreResult(getAssessmentScoreResponse());
        }

        public GetAssessmentScoreServlet(GetAssessmentScoreServletListener _listener, String _email, String _trainingId) {
            m_listener = _listener;
            m_email = _email;
            m_trainingId = _trainingId;
        }

        private GetAssessmentScoreServletListener m_listener;
        private String m_email;
        private String m_trainingId;

        public JSONObject getAssessmentScoreResponse() {
            JSONObject jsonObject = null;
            try {
                String URL = CommonFunctions.DATABASE_URL_BASE + CommonFunctions.DATABASE_GET_ASSESSMENT_SCORE_PAGE;
                URL += "?email=" + m_email;
                URL += "&trainingId=" + m_trainingId;

                jsonObject = new JSONObject(getResponseHttp(URL));
                Log.e("getAssessmentScoreResponse", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
    public static interface GetAssessmentScoreServletListener {
        public void onGetAssessmentScoreResult(JSONObject jsonObject);
    }
    public static void runGetAssessmentScoreServlet(GetAssessmentScoreServletListener listener, String email, String trainingId) {
        (new GetAssessmentScoreServlet(listener, email, trainingId)).start();
    }

    private static class SetAssessmentScoreServlet extends Thread {
        @Override
        public void run() {
            m_listener.onSetAssessmentScoreResult(getSetAssessmentScoreResponse());
        }

        public SetAssessmentScoreServlet(SetAssessmentScoreListener _listener, String _email, String _trainingId, String _total, String _correct, String _score) {
            m_listener = _listener;
            m_email = _email;
            m_trainingId = _trainingId;
            m_total = _total;
            m_correct = _correct;
            m_score = _score;
        }

        private SetAssessmentScoreListener m_listener;
        private String m_email;
        private String m_trainingId;
        private String m_total;
        private String m_correct;
        private String m_score;

        public JSONObject getSetAssessmentScoreResponse() {
            JSONObject jsonObject = null;
            try {
                String URL = CommonFunctions.DATABASE_URL_BASE + CommonFunctions.DATABASE_SET_ASSESSMENT_SCORE_PAGE;
                URL += "?email=" + m_email;
                URL += "&trainingId=" + m_trainingId;
                URL += "&total=" + m_total;
                URL += "&correct=" + m_correct;
                URL += "&score=" + m_score;

                jsonObject = new JSONObject(getResponseHttp(URL));
                Log.e("getSetAssessmentScoreResponse", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
    public static interface SetAssessmentScoreListener {
        public void onSetAssessmentScoreResult(JSONObject jsonObject);
    }
    public static void runSetAssessmentScoreServlet(SetAssessmentScoreListener listener, String email, String trainingId, String total, String correct, String score) {
        (new SetAssessmentScoreServlet(listener, email, trainingId, total, correct, score)).start();
    }
}
