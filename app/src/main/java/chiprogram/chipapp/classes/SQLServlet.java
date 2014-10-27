package chiprogram.chipapp.classes;

import android.os.AsyncTask;

import java.io.IOException;
import java.sql.*;

/**
 * Created by Robert Tanniru on 10/11/2014.
 */
public class SQLServlet extends AsyncTask<Void, Void, Void> {

    private String m_sqlString;
    private String m_dbName;

    private SQLListener m_listener;

    private ResultSet m_results;

    public SQLServlet(String _sql, String _dbName, SQLListener listener) {
        super();
        m_sqlString = _sql;
        m_dbName = _dbName;

        m_listener = listener;
        m_results = null;
    }

    public interface SQLListener {
        /**
         * Callback for when an item has been selected.
         */
        public void onResultSetReturned(ResultSet rs);
    }

    /*
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url;
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            url = "jdbc:google:mysql://brave-night-729:chip-data/" + m_dbName + "?user=root&password=CH!(ch19";

            if (SystemProperty.environment.value() ==
                    SystemProperty.Environment.Value.Production) {
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
                url = "jdbc:google:mysql://brave-night-729:dbchip/" + m_tableName + "?user=root";
            } else {
                // Local MySQL instance to use during development.
                Class.forName("com.mysql.jdbc.Driver");
                url = "jdbc:mysql://173.194.83.91:3306/dbchip?user=root";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            Connection conn = DriverManager.getConnection(url);
            try {
                String statement = m_sqlString;
                PreparedStatement stmt = conn.prepareStatement(statement);
                stmt.executeUpdate();
            } finally {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String url = null;
        m_rs = null;
        url = "jdbc:google:mysql://brave-night-729:chip-data/dbchip?user=root&password=CH!(ch19";

        try {
            Connection conn = DriverManager.getConnection(url);
            m_rs = conn.createStatement().executeQuery(m_sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    */

    @Override
    protected Void doInBackground(Void... voids) {
        String url;
        ResultSet rs;
        // TODO: change this to the correct ip address and instance name
        url = "jdbc:jtds:sqlserver://tombrusca.db.4217918.hostedresource.com/" + m_dbName;
        //url = "jdbc:jtds:sqlserver://tombrusca.db.4217918.hostedresource.com/" + m_dbName + ";encrypt=false;instance=tombrusca";
        //url = "jdbc:google:mysql://brave-night-729:chip-data/" + m_dbName;
        //url = "jdbc:mysql://173.194.83.91:3306/" + m_dbName;

        try {
            String className = "net.sourceforge.jtds.jdbc.Driver";
            //String className = "com.mysql.jdbc.GoogleDriver";
            //String className = "com.google.cloud.sql.jdbc.Driver";
            //String className = "com.mysql.jdbc.Driver";
            Class.forName(className);

            String user = "tombrusca";
            String password = "TacoB3!!";

            Connection conn = DriverManager.getConnection(url, user, password);
            //Connection conn = DriverManager.getConnection(url);
            m_results = conn.createStatement().executeQuery(m_sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    protected void onPostExecute(Void voids) {
        m_listener.onResultSetReturned(m_results);
    }
}
