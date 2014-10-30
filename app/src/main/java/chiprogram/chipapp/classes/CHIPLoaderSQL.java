package chiprogram.chipapp.classes;

import android.widget.Toast;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import chiprogram.chipapp.R;

/**
 * Created by Rob Tanniru on 10/7/2014.
 */
public class CHIPLoaderSQL implements SQLServlet.SQLListener {
    private Map<String, Content> m_contentMap = new HashMap<String, Content>();
    private Map<String, NavItem> m_navItemMap = new HashMap<String, NavItem>();
    private Map<String, Integer> m_scoresMap = new HashMap<String, Integer>();
    private Map<String, String> m_recentViewedItem = new HashMap<String, String>();

    private String[] mentors;
    private String[] roles;
    private String[] locations;

    private boolean isNewUpdate = false; // TODO: make this better and actually work

    private static CHIPLoaderSQL instance = null;

    private CHIPLoaderSQL() {

    }

    public static CHIPLoaderSQL getInstance() {
        if (instance == null) {
            instance = new CHIPLoaderSQL();
        }
        return instance;
    }

    @Override
    public void onResultSetReturned(ResultSet rs) {
        if (rs != null) {
            System.err.print("found!");
        } else {
            System.err.print("not found!");
        }
    }

    public String getMostRecentNavItem(String email) {
        if (m_recentViewedItem.containsKey(email) == false || isNewUpdate) {
            // TODO: make call to database to get more recent navitem

            // TODO: remove placeholder
            return "9";
        }
        return m_recentViewedItem.get(email);
    }

    public void setMostRecentNavItem(String email, String navItemId) {
        m_recentViewedItem.put(email, navItemId);
    }

    public NavItem getNavItem(String navItemId) {
        if (m_navItemMap.containsKey(navItemId) == false || isNewUpdate) {
            // TODO: make call to database to get navitem
            //new SQLServlet("select * from tbl_test", "tombrusca", this).execute();
            // SQL: select * from nav_item where id=" + navItemId + ";";
            // select id from nav_item where parentId=" + navItemId + ";";
            // for each id
            //   currNavItem.addChild(getNavItem(id));
            // select id from content where parentId=" + navItemId + ";";
            // for each id
            //   currNavItem.addContent(getContent(id));
            // select * from question where parentId=" + navItemId + ";";
            // for each question
            //   select * from choices where parentId=" + question.id + ";";
            //   currNavItem.addQuestion(new Question(/*data*/));

            // TODO: remove placeholder
            NavItem currNavItem = null;
            if (navItemId.equals("1")) { // Module 1
                currNavItem = new NavItem(navItemId, null, "Research Module", "Chapters");
                currNavItem.addChild(getNavItem("3"));
                currNavItem.addChild(getNavItem("4"));
                currNavItem.addChild(getNavItem("5"));
            } else if (navItemId.equals("2")) { // Module 2
                currNavItem = new NavItem(navItemId, null, "mHealth Module", "Sessions");
                currNavItem.addChild(getNavItem("6"));
                currNavItem.addChild(getNavItem("7"));
                currNavItem.addChild(getNavItem("8"));
                currNavItem.addChild(getNavItem("13"));
            } else if (navItemId.equals("3")) { // Chapter 1-1
                currNavItem = new NavItem(navItemId, "1", "Chapter 1: Ethics", "Sessions");

                currNavItem.addContent(getContent("1"));
                currNavItem.addContent(getContent("2"));

                currNavItem.addChild(getNavItem("9"));
                currNavItem.addChild(getNavItem("10"));
                currNavItem.addChild(getNavItem("11"));
                currNavItem.addChild(getNavItem("12"));
            } else if (navItemId.equals("4")) { // Chapter 1-2
                currNavItem = new NavItem(navItemId, "1", "Chapter 2: Intro to Research", "Sessions");
                currNavItem.addChild(getNavItem("14"));
                currNavItem.addChild(getNavItem("15"));
                currNavItem.addChild(getNavItem("16"));
                currNavItem.addChild(getNavItem("17"));
            } else if (navItemId.equals("5")) { // Chapter 1-3
                currNavItem = new NavItem(navItemId, "1", "Chapter 3: Unknown", "Sessions");
            } else if (navItemId.equals("6")) { // Session 2-1
                currNavItem = new NavItem(navItemId, "2", "Session 1");
                currNavItem.addContent(getContent("7"));
                currNavItem.addContent(getContent("11"));
            } else if (navItemId.equals("7")) { // Session 2-2
                currNavItem = new NavItem(navItemId, "2", "Session 2");
                currNavItem.addContent(getContent("8"));
            } else if (navItemId.equals("8")) { // Session 2-3
                currNavItem = new NavItem(navItemId, "2", "Session 3");
                currNavItem.addContent(getContent("9"));
            } else if (navItemId.equals("9")) { // Session 1-1-1
                currNavItem = new NavItem(navItemId, "3", "Principles of Research Ethics");

                currNavItem.addContent(getContent("3"));

                Question q1 = new Question("1", "Which of the following statements define the human research principle of respect for persons?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("The capacity and rights of all individuals to make their own decisions", false);
                q1.addPossibleAnswer("The respect for the autonomy of all human beings", false);
                q1.addPossibleAnswer("The recognition of the dignity and freedom of all persons", false);
                q1.addPossibleAnswer("The need to provide special protection to vulnerable persons", false);
                q1.addPossibleAnswer("All of the above", true);
                currNavItem.addQuestion(q1);
                Question q2 = new Question("2", "Which of the following statements define the human research principle of beneficence?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("Secure the participant's physical, mental and social well-being", false);
                q2.addPossibleAnswer("Reduce the participant's risks to a minimum", false);
                q2.addPossibleAnswer("Protection of the participant is more important than the pursuit of new knowledge", false);
                q2.addPossibleAnswer("Protection of the participant is more important than personal or professional research interest", false);
                q2.addPossibleAnswer("All of the above", true);
                currNavItem.addQuestion(q2);
                Question q3 = new Question("3", "Which of the following statements define the human research principle of justice?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("The selection of participants must be done in an equitable manner", false);
                q3.addPossibleAnswer("Using research participants for the exclusive benefit of more privileged groups is not permitted", false);
                q3.addPossibleAnswer("Groups such as minors and pregnant women need special protection", false);
                q3.addPossibleAnswer("The poor and those with limited access to health care services need special protection", false);
                q3.addPossibleAnswer("All of the above", true);
                currNavItem.addQuestion(q3);
                Question q4 = new Question("4", "Which 2 of the following statements are essential elements of the definition of research?",
                        Question.QuestionType.MULTIPLE_ANSWERS);
                q4.addPossibleAnswer("A systematic investigation", true);
                q4.addPossibleAnswer("A protocol approved by a scientific review group", false);
                q4.addPossibleAnswer("A confirmation of recently obtained new knowledge", false);
                q4.addPossibleAnswer("Develops or contributes to generalizable knowledge", true);
                q4.addPossibleAnswer("Contributes to the advancement of science", false);
                currNavItem.addQuestion(q4);
            } else if (navItemId.equals("10")) { // Session 1-1-2
                currNavItem = new NavItem(navItemId, "3", "Foundations of Research Ethics");

                currNavItem.addContent(getContent("4"));

                Question q1 = new Question("5", "According to the Nuremberg Code, which of the following is true?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("Military doctors should never conduct medical research", false);
                q1.addPossibleAnswer("The voluntary consent of the human subject is absolutely essential", true);
                q1.addPossibleAnswer("Research must not be conducted in times of war", false);
                q1.addPossibleAnswer("Research should be regulated by an international agency", false);
                q1.addPossibleAnswer("All of the above", false);
                currNavItem.addQuestion(q1);
                Question q2 = new Question("6", "The Declaration of Helsinki was revised in 2000. This revision prohibited the use of placebos in which scenario?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("In psychiatric research where a washout period could prove harmful", false);
                q2.addPossibleAnswer("In less developed countries where participants cannot afford standard therapy", false);
                q2.addPossibleAnswer("In research with children", false);
                q2.addPossibleAnswer("In cases where proven prophylactic, diagnostic or therapeutic method exists", true);
                q2.addPossibleAnswer("All of the above", false);
                currNavItem.addQuestion(q2);
                Question q3 = new Question("7", "The Belmont Report, which sets forth the basic ethical principles that govern the conduct of research involving human subjects, was developed in response to which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("Nazi experiments on prisoners in concentration camps", false);
                q3.addPossibleAnswer("Placebo-controlled AZT studies in Africa", false);
                q3.addPossibleAnswer("Research conducted on pregnant women", false);
                q3.addPossibleAnswer("The Tuskegee syphilis study", true);
                q3.addPossibleAnswer("The Common Rule", false);
                currNavItem.addQuestion(q3);
                Question q4 = new Question("8", "The US Common Rule governs which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q4.addPossibleAnswer("Research funded by the U.S. government", false);
                q4.addPossibleAnswer("All research on new drugs", false);
                q4.addPossibleAnswer("All research conducted in the United States", false);
                q4.addPossibleAnswer("All of the above", false);
                q4.addPossibleAnswer("None of the above", true);
                currNavItem.addQuestion(q4);
                Question q5 = new Question("9", "Published in 1993, the CIOMS guidelines specifically address which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q5.addPossibleAnswer("Conflict of interest", false);
                q5.addPossibleAnswer("The accreditation of research centers", false);
                q5.addPossibleAnswer("International research", true);
                q5.addPossibleAnswer("The use of new designs in research", false);
                q5.addPossibleAnswer("Behavioral research", false);
                currNavItem.addQuestion(q5);
            } else if (navItemId.equals("11")) { // Session 1-1-3
                currNavItem = new NavItem(navItemId, "3", "Responsible Conduct of Ethical Research");

                currNavItem.addContent(getContent("5"));

                Question q1 = new Question("10", "Which 3 of the following statements are essential elements of informed consent?",
                        Question.QuestionType.MULTIPLE_ANSWERS);
                q1.addPossibleAnswer("The participant has received the necessary information", true);
                q1.addPossibleAnswer("The information has been given in the presence of a witness", false);
                q1.addPossibleAnswer("The participant has understood the information", true);
                q1.addPossibleAnswer("The participant arrived at a decision without undue influence", true);
                q1.addPossibleAnswer("The information has been presented in a written document", false);
                currNavItem.addQuestion(q1);
                Question q2 = new Question("11", "Which of the following statements are true?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("The foreseeable risks presented in the informed consent do not need to be reviewed & approved by the Ethics Committee", false);
                q2.addPossibleAnswer("Participants may not withdraw from a study without prior approval of the researcher", false);
                q2.addPossibleAnswer("Informed consent is mostly a legal document rather than an ethical issue", false);
                q2.addPossibleAnswer("Information in an informed consent must be presented in a way comprehensible to the potential participant", true);
                q2.addPossibleAnswer("Informed consent must be obtained by a third party with no interest in the research", false);
                q2.addPossibleAnswer("A researcher's cultural or intellectual status should not play a role in the potential participant's decision to enroll in a research study", false);
                currNavItem.addQuestion(q2);
            } else if (navItemId.equals("12")) { // Session 1-1-4
                currNavItem = new NavItem(navItemId, "3", "Roles and Responsibilities");

                currNavItem.addContent(getContent("6"));

                Question q = new Question("12", "To be effective, Ethical Committees require which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q.addPossibleAnswer("Members who are un-affiliated with the institution", false);
                q.addPossibleAnswer("Members who are qualified scientists", false);
                q.addPossibleAnswer("That the institution designates adequate resources", false);
                q.addPossibleAnswer("All of the above", true);
                q.addPossibleAnswer("None of the above", false);
                currNavItem.addQuestion(q);
            } else if (navItemId.equals("13")) { // Session 2-4
                currNavItem = new NavItem(navItemId, "2", "Session 4");
                currNavItem.addContent(getContent("10"));
            } else if (navItemId.equals("14")) { // Session 1-2-1
                currNavItem = new NavItem(navItemId, "4", "Session 1");
                currNavItem.addContent(getContent("12"));
            } else if (navItemId.equals("15")) { // Session 1-2-2
                currNavItem = new NavItem(navItemId, "4", "Session 2");
                currNavItem.addContent(getContent("13"));
            } else if (navItemId.equals("16")) { // Session 1-2-3
                currNavItem = new NavItem(navItemId, "4", "Session 3");
                currNavItem.addContent(getContent("14"));
            } else if (navItemId.equals("17")) { // Session 1-2-4
                currNavItem = new NavItem(navItemId, "4", "Session 4");
                currNavItem.addContent(getContent("15"));
            }
            m_navItemMap.put(navItemId, currNavItem);

        }
        return m_navItemMap.get(navItemId);
    }

    public ArrayList<NavItem> getBaseNavItems() {
        ArrayList<NavItem> baseNavItems = new ArrayList<NavItem>();
        // TODO: make call to database to get modules
        // SQL: select * from nav_item where parentId=null;";

        // TODO: remove placeholder
        baseNavItems.add(getNavItem("1"));
        baseNavItems.add(getNavItem("2"));

        return baseNavItems;
    }

    public Content getContent(String contentId) {
        if (m_contentMap.containsKey(contentId) == false || isNewUpdate) {
            // TODO: make call to database to get content
            // SQL: select * from content where id=" + contentId + ";";

            // TODO: remove placeholder
            Content currContent = null;
            if (contentId.equals("1")) { // Chapter 1-1 YouTube video
                currContent = new Content(contentId, "Ethics YouTube Video",
                        Content.ContentType.YOUTUBE_VIDEO,
                        "https://www.youtube.com/watch?v=Ir3VvYNzHeM");
            } else if (contentId.equals("2")) { // Chapter 1-1 PDF file
                currContent = new Content(contentId, "Ethics PDF Link",
                        Content.ContentType.PDF_LINK,
                        "http://chiprogram.com/training_articles/ethics.pdf");
            } else if (contentId.equals("3")) { // Session 1-1-1 PPT file
                currContent = new Content(contentId, "Session 1 PPT",
                        Content.ContentType.PPT_LINK,
                        "http://chiprogram.com/training_ppt/session_1.pptx");
            } else if (contentId.equals("4")) { // Session 1-1-2 PPT file
                currContent = new Content(contentId, "Session 2 PPT",
                        Content.ContentType.PPT_LINK,
                        "http://chiprogram.com/training_ppt/session_2.pptx");
            } else if (contentId.equals("5")) { // Session 1-1-3 PPT file
                currContent = new Content(contentId, "Session 3 PPT",
                        Content.ContentType.PPT_LINK,
                        "http://chiprogram.com/training_ppt/session_3.pptx");
            } else if (contentId.equals("6")) { // Session 1-1-4 PPT file
                currContent = new Content(contentId, "Session 4 PPT",
                        Content.ContentType.PPT_LINK,
                        "http://chiprogram.com/training_ppt/session_4.pptx");
            } else if (contentId.equals("7")) { // Session 2-1 YouTube Video
                currContent = new Content(contentId, "Session 1 YouTube Video",
                        Content.ContentType.YOUTUBE_VIDEO,
                        "https://www.youtube.com/watch?v=k3Xr7sPi7iU");
            } else if (contentId.equals("8")) { // Session 2-2 YouTube Video
                currContent = new Content(contentId, "Session 2 YouTube Video",
                        Content.ContentType.YOUTUBE_VIDEO,
                        "https://www.youtube.com/watch?v=qkm_7XUDqIY");
            } else if (contentId.equals("9")) { // Session 2-3 YouTube Video
                currContent = new Content(contentId, "Session 3 YouTube Video",
                        Content.ContentType.YOUTUBE_VIDEO,
                        "https://www.youtube.com/watch?v=MRYVI42x41Q");
            } else if (contentId.equals("10")) { // Session 2-4 YouTube Video
                currContent = new Content(contentId, "Session 4 YouTube Video",
                        Content.ContentType.YOUTUBE_VIDEO,
                        "https://www.youtube.com/watch?v=XFcbx81CxeM");
            } else if (contentId.equals("11")) { // Session 2-1 YouTube Video 2
                currContent = new Content(contentId, "Session 1 YouTube Video 2",
                        Content.ContentType.YOUTUBE_VIDEO,
                        "https://www.youtube.com/watch?v=Xh52sRK13i8");
            } else if (contentId.equals("12")) { // Session 1-2-1 PDF
                currContent = new Content(contentId, "Session 1 PDF: (Pages 4-6)",
                        Content.ContentType.PDF_LINK,
                        "http://www.rds-yh.nihr.ac.uk/wp-content/uploads/2013/05/5_Introduction-to-qualitative-research-2009.pdf");
            } else if (contentId.equals("13")) { // Session 1-2-2 PDF
                currContent = new Content(contentId, "Session 2 PDF: (Pages 9-16)",
                        Content.ContentType.PDF_LINK,
                        "http://www.rds-yh.nihr.ac.uk/wp-content/uploads/2013/05/5_Introduction-to-qualitative-research-2009.pdf");
            } else if (contentId.equals("14")) { // Session 1-2-3 PDF
                currContent = new Content(contentId, "Session 3 PDF: (Pages 24-31)",
                        Content.ContentType.PDF_LINK,
                        "http://www.rds-yh.nihr.ac.uk/wp-content/uploads/2013/05/5_Introduction-to-qualitative-research-2009.pdf");
            } else if (contentId.equals("15")) { // Session 1-2-4 PDF
                currContent = new Content(contentId, "Session 4 PDF: (Pages 31-34)",
                        Content.ContentType.PDF_LINK,
                        "http://www.rds-yh.nihr.ac.uk/wp-content/uploads/2013/05/5_Introduction-to-qualitative-research-2009.pdf");
            }
            m_contentMap.put(contentId, currContent);
        }
        return m_contentMap.get(contentId);
    }

    // returns -1 if the user has not attempted the assessment
    public int getAssessmentScore(String sessionId, String userEmail) {
        if (m_scoresMap.containsKey(sessionId + "-" + userEmail) == false || isNewUpdate) {
            // TODO: make call to database to get assessment score

            // SQL: "Select score from scores where email=" + userEmail + " and session_id=" + sessionId + ";";

            m_scoresMap.put(sessionId + "-" + userEmail, -1);
        }
        return m_scoresMap.get(sessionId + "-" + userEmail);
    }

    public boolean setAssessmentScore(String sessionId, String userEmail, int newScore) {
        // TODO: make call to database to set new score

        // SQL: "Insert into scores where email=" + userEmail + " and session_id=" + sessionId + ";";
        m_scoresMap.put(sessionId + "-" + userEmail, newScore);
        return true;
    }

    public CHIPUser checkUserLogin(String email, String password) {
        // TODO: make call to database to check user login

        // SQL: "Select password from passwords where email=" + userEmail + ";";

        // TODO: remove placeholder
        if (email.equals("bkt421@gmail.com") && password.equals("CH!(ch19")) {
            return new CHIPUser("Robert", "Tanniru", "123 4th Street\r\nRochester, MI 48306",
                                "Detroit", "Program Team", "Mohan Tanniru",
                                "Creating a mobile app version of the CHIP website",
                                "bkt421@gmail.com");
        } else {
            return null;
        }
    }

    public String[] getMentorList() {
        if (mentors == null || isNewUpdate) {
            // TODO: make call to database to get mentor list

            // SQL: "Select * from users where role=\"Mentor\";";

            // TODO: remove placeholder
            mentors = new String[]{"None",
                    "Yubraj Acharya—Nutrition & Health Financing (Nepal)",
                    "Eli Bailey – Micro Financing and Financial Modeling (US and India)",
                    "Deepak Bajracharya—Management & Communications (Nepal)",
                    "Kofi Awusabo-Asare—Population Studies (Ghana)",
                    "Linda Kaljee—Anthropology (US)",
                    "Paul Kilgore—Vaccines (US)",
                    "Mentor Lucien—Microbiology & Immunology (Haiti)",
                    "Kate Otto—mHealth & data collection (US)",
                    "Genevieve Poitevien—Medicine (Haiti)",
                    "Mohan Tanniru—Business IT (US)",
                    "Placide Tapsoba—Community Health (Ghana)",
                    "Marcus Zervos—Infectious Diseases (US)"};
        }
        return mentors;
    }

    public String[] getLocationList() {
        if (locations == null || isNewUpdate) {
            // TODO: make call to database to get mentor list

            // SQL: "Select name from locations;";

            // TODO: remove placeholder
            locations = new String[]{"Detroit", "Haiti", "Nepal", "Ghana"};
        }
        return locations;
    }

    public String[] getRoleList() {
        if (roles == null || isNewUpdate) {
            // TODO: make call to database to get mentor list

            // SQL: "Select name from roles;";

            // TODO: remove placeholder
            roles = new String[]{"Community Health Worker", "Mentor", "Innovator", "Program Team"};
        }
        return roles;
    }
}