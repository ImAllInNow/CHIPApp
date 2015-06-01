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

    public static interface LoginServletListener {
        public void onLoginResult(JSONObject jsonObject);
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

        public String getLoginResponseHttp() {
            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            String URL = CommonFunctions.DATABASE_URL;
            URL += "?email=" + m_email;
            URL += "&password=" + m_password;
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
                    Log.e("getLoginResponse", "Failed to get response");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }

        public JSONObject getLoginResponse() {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(getLoginResponseHttp());
                Log.e("getLoginResponse", jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }

    public static void runLoginServlet(LoginServletListener listener, String email, String password) {
        (new LoginServlet(listener, email, password)).start();
    }
}
