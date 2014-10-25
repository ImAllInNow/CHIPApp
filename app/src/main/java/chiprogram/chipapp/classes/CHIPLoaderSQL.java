package chiprogram.chipapp.classes;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rob Tanniru on 10/7/2014.
 */
public class CHIPLoaderSQL {
    private static Map<String, Content> m_contentMap = new HashMap<String, Content>();
    private static Map<String, NavItem> m_navItemMap = new HashMap<String, NavItem>();

    private static Map<String, Module> m_moduleMap = new HashMap<String, Module>();
    private static Map<String, Chapter> m_chapterMap = new HashMap<String, Chapter>();
    private static Map<String, Session> m_sessionMap = new HashMap<String, Session>();
    private static Map<String, Integer> m_scoresMap = new HashMap<String, Integer>();

    private static String[] mentors;
    private static String[] roles;
    private static String[] locations;

    private static boolean isNewUpdate = false; // TODO: make this better and actually work

    public static NavItem getNavItem(String navItemId) {
        if (m_navItemMap.containsKey(navItemId) == false || isNewUpdate) {
            // TODO: make call to database to get navitem

            // TODO: remove placeholder
            NavItem currNavItem = null;
            if (navItemId.equals("1")) { // Module 1
                currNavItem = new NavItem(navItemId, "Module 1", "Chapters");
                currNavItem.addChild(getNavItem("3"));
                currNavItem.addChild(getNavItem("4"));
                currNavItem.addChild(getNavItem("5"));
            } else if (navItemId.equals("2")) { // Module 2
                currNavItem = new NavItem(navItemId, "Module 2", "Chapters");
                currNavItem.addChild(getNavItem("6"));
                currNavItem.addChild(getNavItem("7"));
                currNavItem.addChild(getNavItem("8"));
            } else if (navItemId.equals("3")) { // Chapter 1-1
                currNavItem = new NavItem(navItemId, "Chapter 1: Ethics", "Sessions");

                currNavItem.addContent(getContent("1"));
                currNavItem.addContent(getContent("2"));

                currNavItem.addChild(getNavItem("9"));
                currNavItem.addChild(getNavItem("10"));
                currNavItem.addChild(getNavItem("11"));
                currNavItem.addChild(getNavItem("12"));
            } else if (navItemId.equals("4")) { // Chapter 1-2
                currNavItem = new NavItem(navItemId, "Chapter 2", "Sessions");
            } else if (navItemId.equals("5")) { // Chapter 1-3
                currNavItem = new NavItem(navItemId, "Chapter 3", "Sessions");
            } else if (navItemId.equals("6")) { // Chapter 2-1
                currNavItem = new NavItem(navItemId, "Chapter 1", "Sessions");
            } else if (navItemId.equals("7")) { // Chapter 2-2
                currNavItem = new NavItem(navItemId, "Chapter 2", "Sessions");
            } else if (navItemId.equals("8")) { // Chapter 2-3
                currNavItem = new NavItem(navItemId, "Chapter 3", "Sessions");
            } else if (navItemId.equals("9")) { // Session 1-1-1
                currNavItem = new NavItem(navItemId, "Principles of Research Ethics");

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
                currNavItem = new NavItem(navItemId, "Foundations of Research Ethics");

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
                currNavItem = new NavItem(navItemId, "Responsible Conduct of Ethical Research");

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
                currNavItem = new NavItem(navItemId, "Roles and Responsibilities");

                currNavItem.addContent(getContent("6"));

                Question q = new Question("12", "To be effective, Ethical Committees require which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q.addPossibleAnswer("Members who are un-affiliated with the institution", false);
                q.addPossibleAnswer("Members who are qualified scientists", false);
                q.addPossibleAnswer("That the institution designates adequate resources", false);
                q.addPossibleAnswer("All of the above", true);
                q.addPossibleAnswer("None of the above", false);
                currNavItem.addQuestion(q);
            }
            m_navItemMap.put(navItemId, currNavItem);
        }
        return m_navItemMap.get(navItemId);
    }

    public static ArrayList<NavItem> getBaseNavItems() {
        ArrayList<NavItem> baseNavItems = new ArrayList<NavItem>();
        // TODO: make call to database to get modules

        // TODO: remove placeholder
        baseNavItems.add(getNavItem("1"));
        baseNavItems.add(getNavItem("2"));

        return baseNavItems;
    }

    public static Content getContent(String contentId) {
        if (m_contentMap.containsKey(contentId) == false || isNewUpdate) {
            // TODO: make call to database to get content

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
            }
            m_contentMap.put(contentId, currContent);
        }
        return m_contentMap.get(contentId);
    }


    public static Module getModule(String moduleId) {
        if (m_moduleMap.containsKey(moduleId) == false || isNewUpdate) {
            // TODO: make call to database to get modules

            SQLServlet sqlServ = new SQLServlet("Select * from modules where id=" + moduleId + ";", "dbchip");

            ResultSet rs = null;

            /*
            try {
                rs = sqlServ.doGet();
            } catch (IOException e) {
                e.printStackTrace();
            }
            */

            if (rs != null) {
                int i = 8;
            } else {
                // TODO: remove placeholder
                getModules();
            }
        }
        return m_moduleMap.get(moduleId);
    }

    public static ArrayList<Module> getModules() {
        ArrayList<Module> currModules = new ArrayList<Module>();

        // TODO: make call to database to get modules

        // SQL: "Select * from modules;";
        SQLServlet sqlServ = new SQLServlet("Select * from modules;", "dbchip");

        ResultSet rs = null;

        /*
        try {
            rs = sqlServ.doGet();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        if (rs != null) {
            int i = 8;
        } else {
            // TODO: remove placeholder
            Module mod1 = new Module("1", "Module 1");
            mod1.addChapter(new Chapter("1", "Chapter 1: Ethics"));
            mod1.addChapter(new Chapter("2", "Chapter 2"));
            mod1.addChapter(new Chapter("3", "Chapter 3"));
            m_moduleMap.put("1", mod1);
            currModules.add(mod1);

            Module mod2 = new Module("2", "Module 2");
            mod2.addChapter(new Chapter("4", "Chapter 1"));
            mod2.addChapter(new Chapter("5", "Chapter 2"));
            mod2.addChapter(new Chapter("6", "Chapter 3"));
            m_moduleMap.put("2", mod2);
            currModules.add(mod2);
        }
        return currModules;
    }

    public static Chapter getChapterDetails(String chapId) {
        if (m_chapterMap.containsKey(chapId) == false || isNewUpdate) {
            // TODO: make call to database to get chapter

            // SQL: "Select * from chapters where id=" + chapId + ";";
            // SQL: "Select * from sessions where chapter_id=" + chapId + ";";
            // for (int i = 0; i < numQuestions; ++i) {
                // currChapter.addSession(getSessionDetails(sessionId));
            // }

            // TODO: remove placeholder
            Chapter currChapter;
            if (chapId.equals("1")) {
                currChapter = new Chapter(chapId, "Chapter 1: Ethics",
                        "https://www.youtube.com/watch?v=Ir3VvYNzHeM",
                        "http://chiprogram.com/training_articles/ethics.pdf");

                currChapter.addSession(getSessionDetails("1"));
                currChapter.addSession(getSessionDetails("2"));
                currChapter.addSession(getSessionDetails("3"));
                currChapter.addSession(getSessionDetails("4"));
            } else {
                currChapter = new Chapter(chapId, "Chapter ID: " + chapId);
            }
            m_chapterMap.put(chapId, currChapter);
        }
        return m_chapterMap.get(chapId);
    }

    public static Session getSessionDetails(String sessionId) {
        if (m_sessionMap.containsKey(sessionId) == false || isNewUpdate) {
            // TODO: make call to database to get session

            // SQL: "Select * from sessions where id=" + sessionId + ";";
            // SQL: "Select * from questions where session_id=" + sessionId + ";";
            // for (int i = 0; i < numQuestions; ++i) {
                // SQL: "Select * from choices where question_id=" + questionId + ";";
            // }

            // TODO: remove placeholder
            if (sessionId.equals("1")) {
                Session sess = new Session("1", "Principles of Research Ethics", "http://chiprogram.com/training_ppt/session_1.pptx");
                Question q1 = new Question("1", "Which of the following statements define the human research principle of respect for persons?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("The capacity and rights of all individuals to make their own decisions", false);
                q1.addPossibleAnswer("The respect for the autonomy of all human beings", false);
                q1.addPossibleAnswer("The recognition of the dignity and freedom of all persons", false);
                q1.addPossibleAnswer("The need to provide special protection to vulnerable persons", false);
                q1.addPossibleAnswer("All of the above", true);
                sess.addQuestion(q1);
                Question q2 = new Question("2", "Which of the following statements define the human research principle of beneficence?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("Secure the participant's physical, mental and social well-being", false);
                q2.addPossibleAnswer("Reduce the participant's risks to a minimum", false);
                q2.addPossibleAnswer("Protection of the participant is more important than the pursuit of new knowledge", false);
                q2.addPossibleAnswer("Protection of the participant is more important than personal or professional research interest", false);
                q2.addPossibleAnswer("All of the above", true);
                sess.addQuestion(q2);
                Question q3 = new Question("3", "Which of the following statements define the human research principle of justice?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("The selection of participants must be done in an equitable manner", false);
                q3.addPossibleAnswer("Using research participants for the exclusive benefit of more privileged groups is not permitted", false);
                q3.addPossibleAnswer("Groups such as minors and pregnant women need special protection", false);
                q3.addPossibleAnswer("The poor and those with limited access to health care services need special protection", false);
                q3.addPossibleAnswer("All of the above", true);
                sess.addQuestion(q3);
                Question q4 = new Question("4", "Which 2 of the following statements are essential elements of the definition of research?",
                        Question.QuestionType.MULTIPLE_ANSWERS);
                q4.addPossibleAnswer("A systematic investigation", true);
                q4.addPossibleAnswer("A protocol approved by a scientific review group", false);
                q4.addPossibleAnswer("A confirmation of recently obtained new knowledge", false);
                q4.addPossibleAnswer("Develops or contributes to generalizable knowledge", true);
                q4.addPossibleAnswer("Contributes to the advancement of science", false);
                sess.addQuestion(q4);
                m_sessionMap.put(sessionId, sess);
            } else if (sessionId.equals("2")) {
                Session sess = new Session("2", "Foundations of Research Ethics", "http://chiprogram.com/training_ppt/session_2.pptx");
                Question q1 = new Question("5", "According to the Nuremberg Code, which of the following is true?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("Military doctors should never conduct medical research", false);
                q1.addPossibleAnswer("The voluntary consent of the human subject is absolutely essential", true);
                q1.addPossibleAnswer("Research must not be conducted in times of war", false);
                q1.addPossibleAnswer("Research should be regulated by an international agency", false);
                q1.addPossibleAnswer("All of the above", false);
                sess.addQuestion(q1);
                Question q2 = new Question("6", "The Declaration of Helsinki was revised in 2000. This revision prohibited the use of placebos in which scenario?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("In psychiatric research where a washout period could prove harmful", false);
                q2.addPossibleAnswer("In less developed countries where participants cannot afford standard therapy", false);
                q2.addPossibleAnswer("In research with children", false);
                q2.addPossibleAnswer("In cases where proven prophylactic, diagnostic or therapeutic method exists", true);
                q2.addPossibleAnswer("All of the above", false);
                sess.addQuestion(q2);
                Question q3 = new Question("7", "The Belmont Report, which sets forth the basic ethical principles that govern the conduct of research involving human subjects, was developed in response to which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("Nazi experiments on prisoners in concentration camps", false);
                q3.addPossibleAnswer("Placebo-controlled AZT studies in Africa", false);
                q3.addPossibleAnswer("Research conducted on pregnant women", false);
                q3.addPossibleAnswer("The Tuskegee syphilis study", true);
                q3.addPossibleAnswer("The Common Rule", false);
                sess.addQuestion(q3);
                Question q4 = new Question("8", "The US Common Rule governs which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q4.addPossibleAnswer("Research funded by the U.S. government", false);
                q4.addPossibleAnswer("All research on new drugs", false);
                q4.addPossibleAnswer("All research conducted in the United States", false);
                q4.addPossibleAnswer("All of the above", false);
                q4.addPossibleAnswer("None of the above", true);
                sess.addQuestion(q4);
                Question q5 = new Question("9", "Published in 1993, the CIOMS guidelines specifically address which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q5.addPossibleAnswer("Conflict of interest", false);
                q5.addPossibleAnswer("The accreditation of research centers", false);
                q5.addPossibleAnswer("International research", true);
                q5.addPossibleAnswer("The use of new designs in research", false);
                q5.addPossibleAnswer("Behavioral research", false);
                sess.addQuestion(q5);
                m_sessionMap.put(sessionId, sess);
            } else if (sessionId.equals("3")) {
                Session sess = new Session("3", "Responsible Conduct of Ethical Research", "http://chiprogram.com/training_ppt/session_3.pptx");
                Question q1 = new Question("10", "Which 3 of the following statements are essential elements of informed consent?",
                        Question.QuestionType.MULTIPLE_ANSWERS);
                q1.addPossibleAnswer("The participant has received the necessary information", true);
                q1.addPossibleAnswer("The information has been given in the presence of a witness", false);
                q1.addPossibleAnswer("The participant has understood the information", true);
                q1.addPossibleAnswer("The participant arrived at a decision without undue influence", true);
                q1.addPossibleAnswer("The information has been presented in a written document", false);
                sess.addQuestion(q1);
                Question q2 = new Question("11", "Which of the following statements are true?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("The foreseeable risks presented in the informed consent do not need to be reviewed & approved by the Ethics Committee", false);
                q2.addPossibleAnswer("Participants may not withdraw from a study without prior approval of the researcher", false);
                q2.addPossibleAnswer("Informed consent is mostly a legal document rather than an ethical issue", false);
                q2.addPossibleAnswer("Information in an informed consent must be presented in a way comprehensible to the potential participant", true);
                q2.addPossibleAnswer("Informed consent must be obtained by a third party with no interest in the research", false);
                q2.addPossibleAnswer("A researcher's cultural or intellectual status should not play a role in the potential participant's decision to enroll in a research study", false);
                sess.addQuestion(q2);
                m_sessionMap.put(sessionId, sess);
            } else if (sessionId.equals("4")) {
                Session sess = new Session("4", "Roles and Responsibilities", "http://chiprogram.com/training_ppt/session_4.pptx");
                Question q = new Question("12", "To be effective, Ethical Committees require which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q.addPossibleAnswer("Members who are un-affiliated with the institution", false);
                q.addPossibleAnswer("Members who are qualified scientists", false);
                q.addPossibleAnswer("That the institution designates adequate resources", false);
                q.addPossibleAnswer("All of the above", true);
                q.addPossibleAnswer("None of the above", false);
                sess.addQuestion(q);
                m_sessionMap.put(sessionId, sess);
            } else {
                return null;
            }
        }
        return m_sessionMap.get(sessionId);
    }

    // returns -1 if the user has not attempted the assessment
    public static int getAssessmentScore(String sessionId, String userEmail) {
        if (m_scoresMap.containsKey(sessionId + "-" + userEmail) == false || isNewUpdate) {
            // TODO: make call to database to get assessment score

            // SQL: "Select score from scores where email=" + userEmail + " and session_id=" + sessionId + ";";

            m_scoresMap.put(sessionId + "-" + userEmail, -1);
        }
        return m_scoresMap.get(sessionId + "-" + userEmail);
    }

    public static boolean setAssessmentScore(String sessionId, String userEmail, int newScore) {
        // TODO: make call to database to set new score

        // SQL: "Insert into from scores where email=" + userEmail + " and session_id=" + sessionId + ";";
        m_scoresMap.put(sessionId + "-" + userEmail, newScore);
        return true;
    }

    public static CHIPUser checkUserLogin(String email, String password) {
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

    public static String[] getMentorList() {
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

    public static String[] getLocationList() {
        if (locations == null || isNewUpdate) {
            // TODO: make call to database to get mentor list

            // SQL: "Select name from locations;";

            // TODO: remove placeholder
            locations = new String[]{"Detroit", "Haiti", "Nepal", "Ghana"};
        }
        return locations;
    }

    public static String[] getRoleList() {
        if (roles == null || isNewUpdate) {
            // TODO: make call to database to get mentor list

            // SQL: "Select name from roles;";

            // TODO: remove placeholder
            roles = new String[]{"Community Health Worker", "Mentor", "Innovator", "Program Team"};
        }
        return roles;
    }
}