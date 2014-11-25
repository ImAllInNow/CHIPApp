package chiprogram.chipapp.classes;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private Map<String, Assessment> m_assessmentMap = new HashMap<String, Assessment>();
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
                currNavItem.addAssessment(getAssessment("6"));
            } else if (navItemId.equals("7")) { // Session 2-2
                currNavItem = new NavItem(navItemId, "2", "Session 2");

                currNavItem.addContent(getContent("8"));
                currNavItem.addAssessment(getAssessment("7"));
            } else if (navItemId.equals("8")) { // Session 2-3
                currNavItem = new NavItem(navItemId, "2", "Session 3");

                currNavItem.addContent(getContent("9"));
                currNavItem.addAssessment(getAssessment("8"));
            } else if (navItemId.equals("9")) { // Session 1-1-1
                currNavItem = new NavItem(navItemId, "3", "Principles of Research Ethics");

                currNavItem.addContent(getContent("3"));

                currNavItem.addAssessment(getAssessment("1"));
            } else if (navItemId.equals("10")) { // Session 1-1-2
                currNavItem = new NavItem(navItemId, "3", "Foundations of Research Ethics");

                currNavItem.addContent(getContent("4"));

                currNavItem.addAssessment(getAssessment("2"));
            } else if (navItemId.equals("11")) { // Session 1-1-3
                currNavItem = new NavItem(navItemId, "3", "Responsible Conduct of Ethical Research");

                currNavItem.addContent(getContent("5"));

                currNavItem.addAssessment(getAssessment("3"));
            } else if (navItemId.equals("12")) { // Session 1-1-4
                currNavItem = new NavItem(navItemId, "3", "Roles and Responsibilities");

                currNavItem.addContent(getContent("6"));

                currNavItem.addAssessment(getAssessment("4"));
            } else if (navItemId.equals("13")) { // Session 2-4
                currNavItem = new NavItem(navItemId, "2", "Session 4");

                currNavItem.addContent(getContent("10"));
                currNavItem.addAssessment(getAssessment("9"));
            } else if (navItemId.equals("14")) { // Session 1-2-1
                currNavItem = new NavItem(navItemId, "4", "Session 1");

                currNavItem.addContent(getContent("12"));
                currNavItem.addAssessment(getAssessment("10"));
            } else if (navItemId.equals("15")) { // Session 1-2-2
                currNavItem = new NavItem(navItemId, "4", "Session 2");

                currNavItem.addContent(getContent("13"));
                currNavItem.addAssessment(getAssessment("11"));
            } else if (navItemId.equals("16")) { // Session 1-2-3
                currNavItem = new NavItem(navItemId, "4", "Session 3");

                currNavItem.addContent(getContent("14"));
                currNavItem.addAssessment(getAssessment("12"));
            } else if (navItemId.equals("17")) { // Session 1-2-4
                currNavItem = new NavItem(navItemId, "4", "Session 4");

                currNavItem.addContent(getContent("15"));
                currNavItem.addAssessment(getAssessment("13"));
            } else if (navItemId.equals("18")) { // Assignment 1
                currNavItem = new NavItem(navItemId, null, "Assignment 1");

                currNavItem.addAssessment(getAssessment("5"));
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
        baseNavItems.add(getNavItem("18"));

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

    public Assessment getAssessment(String assessmentId) {
        if (m_assessmentMap.containsKey(assessmentId) == false || isNewUpdate) {
            // TODO: make call to database to get assessment
            // SQL: select * from Assessment where id=" + assessmentId + ";";

            Assessment assessment = null;
            if (assessmentId.equals("1")) { // Session 1-1-1 Assessment
                assessment = new Assessment(assessmentId, "Principles of Research Ethics Assessment", 75);
                Question q1 = new Question("1", "Which of the following statements define the human research principle of respect for persons?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("The capacity and rights of all individuals to make their own decisions", false);
                q1.addPossibleAnswer("The respect for the autonomy of all human beings", false);
                q1.addPossibleAnswer("The recognition of the dignity and freedom of all persons", false);
                q1.addPossibleAnswer("The need to provide special protection to vulnerable persons", false);
                q1.addPossibleAnswer("All of the above", true);
                assessment.addQuestion(q1);
                Question q2 = new Question("2", "Which of the following statements define the human research principle of beneficence?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("Secure the participant's physical, mental and social well-being", false);
                q2.addPossibleAnswer("Reduce the participant's risks to a minimum", false);
                q2.addPossibleAnswer("Protection of the participant is more important than the pursuit of new knowledge", false);
                q2.addPossibleAnswer("Protection of the participant is more important than personal or professional research interest", false);
                q2.addPossibleAnswer("All of the above", true);
                assessment.addQuestion(q2);
                Question q3 = new Question("3", "Which of the following statements define the human research principle of justice?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("The selection of participants must be done in an equitable manner", false);
                q3.addPossibleAnswer("Using research participants for the exclusive benefit of more privileged groups is not permitted", false);
                q3.addPossibleAnswer("Groups such as minors and pregnant women need special protection", false);
                q3.addPossibleAnswer("The poor and those with limited access to health care services need special protection", false);
                q3.addPossibleAnswer("All of the above", true);
                assessment.addQuestion(q3);
                Question q4 = new Question("4", "Which 2 of the following statements are essential elements of the definition of research?",
                        Question.QuestionType.MULTIPLE_ANSWERS);
                q4.addPossibleAnswer("A systematic investigation", true);
                q4.addPossibleAnswer("A protocol approved by a scientific review group", false);
                q4.addPossibleAnswer("A confirmation of recently obtained new knowledge", false);
                q4.addPossibleAnswer("Develops or contributes to generalizable knowledge", true);
                q4.addPossibleAnswer("Contributes to the advancement of science", false);
                assessment.addQuestion(q4);
            } else if (assessmentId.equals("2")) { // Session 1-1-2 Assessment
                assessment = new Assessment(assessmentId, "Foundations of Research Ethics Assessment", 80);
                Question q1 = new Question("5", "According to the Nuremberg Code, which of the following is true?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("Military doctors should never conduct medical research", false);
                q1.addPossibleAnswer("The voluntary consent of the human subject is absolutely essential", true);
                q1.addPossibleAnswer("Research must not be conducted in times of war", false);
                q1.addPossibleAnswer("Research should be regulated by an international agency", false);
                q1.addPossibleAnswer("All of the above", false);
                assessment.addQuestion(q1);
                Question q2 = new Question("6", "The Declaration of Helsinki was revised in 2000. This revision prohibited the use of placebos in which scenario?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("In psychiatric research where a washout period could prove harmful", false);
                q2.addPossibleAnswer("In less developed countries where participants cannot afford standard therapy", false);
                q2.addPossibleAnswer("In research with children", false);
                q2.addPossibleAnswer("In cases where proven prophylactic, diagnostic or therapeutic method exists", true);
                q2.addPossibleAnswer("All of the above", false);
                assessment.addQuestion(q2);
                Question q3 = new Question("7", "The Belmont Report, which sets forth the basic ethical principles that govern the conduct of research involving human subjects, was developed in response to which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("Nazi experiments on prisoners in concentration camps", false);
                q3.addPossibleAnswer("Placebo-controlled AZT studies in Africa", false);
                q3.addPossibleAnswer("Research conducted on pregnant women", false);
                q3.addPossibleAnswer("The Tuskegee syphilis study", true);
                q3.addPossibleAnswer("The Common Rule", false);
                assessment.addQuestion(q3);
                Question q4 = new Question("8", "The US Common Rule governs which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q4.addPossibleAnswer("Research funded by the U.S. government", false);
                q4.addPossibleAnswer("All research on new drugs", false);
                q4.addPossibleAnswer("All research conducted in the United States", false);
                q4.addPossibleAnswer("All of the above", false);
                q4.addPossibleAnswer("None of the above", true);
                assessment.addQuestion(q4);
                Question q5 = new Question("9", "Published in 1993, the CIOMS guidelines specifically address which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q5.addPossibleAnswer("Conflict of interest", false);
                q5.addPossibleAnswer("The accreditation of research centers", false);
                q5.addPossibleAnswer("International research", true);
                q5.addPossibleAnswer("The use of new designs in research", false);
                q5.addPossibleAnswer("Behavioral research", false);
                assessment.addQuestion(q5);
            } else if (assessmentId.equals("3")) { // Session 1-1-3 Assessment
                assessment = new Assessment(assessmentId, "Responsible Conduct of Ethical Research Assessment", 100);
                Question q1 = new Question("10", "Which 3 of the following statements are essential elements of informed consent?",
                        Question.QuestionType.MULTIPLE_ANSWERS);
                q1.addPossibleAnswer("The participant has received the necessary information", true);
                q1.addPossibleAnswer("The information has been given in the presence of a witness", false);
                q1.addPossibleAnswer("The participant has understood the information", true);
                q1.addPossibleAnswer("The participant arrived at a decision without undue influence", true);
                q1.addPossibleAnswer("The information has been presented in a written document", false);
                assessment.addQuestion(q1);
                Question q2 = new Question("11", "Which of the following statements are true?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("The foreseeable risks presented in the informed consent do not need to be reviewed & approved by the Ethics Committee", false);
                q2.addPossibleAnswer("Participants may not withdraw from a study without prior approval of the researcher", false);
                q2.addPossibleAnswer("Informed consent is mostly a legal document rather than an ethical issue", false);
                q2.addPossibleAnswer("Information in an informed consent must be presented in a way comprehensible to the potential participant", true);
                q2.addPossibleAnswer("Informed consent must be obtained by a third party with no interest in the research", false);
                q2.addPossibleAnswer("A researcher's cultural or intellectual status should not play a role in the potential participant's decision to enroll in a research study", false);
                assessment.addQuestion(q2);
            } else if (assessmentId.equals("4")) { // Session 1-1-4 Assessment
                assessment = new Assessment(assessmentId, "Roles and Responsibilities Assessment", 100);
                Question q = new Question("12", "To be effective, Ethical Committees require which of the following?",
                        Question.QuestionType.SINGLE_ANSWER);
                q.addPossibleAnswer("Members who are un-affiliated with the institution", false);
                q.addPossibleAnswer("Members who are qualified scientists", false);
                q.addPossibleAnswer("That the institution designates adequate resources", false);
                q.addPossibleAnswer("All of the above", true);
                q.addPossibleAnswer("None of the above", false);
                assessment.addQuestion(q);
            } else if (assessmentId.equals("5")) { // Assignment 1 Assessment
                assessment = new Assessment(assessmentId, "Tasks for Assignment 1", 100);
                Question q = new Question("13", "Have you met with your mentor to discuss your progress so far?.",
                        Question.QuestionType.SINGLE_ANSWER);
                q.addPossibleAnswer("Yes", true);
                q.addPossibleAnswer("No", false);
                assessment.addQuestion(q);
            } else if (assessmentId.equals("6")) { // Session 2-1 Assessment
                assessment = new Assessment(assessmentId, "Session 1 Assessment", 100);
                Question q1 = new Question("14", "mHealth applications can only be used on mobile phones.",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("True", false);
                q1.addPossibleAnswer("False", true);
                assessment.addQuestion(q1);
                Question q2 = new Question("15", "What is \"gamification?\"",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("Developing a separate game to help people learn the concepts.", false);
                q2.addPossibleAnswer("The application of typical elements of game playing to other areas of activity.", true);
                q2.addPossibleAnswer("Using prizes to motivate people to finish work.", false);
                q2.addPossibleAnswer("Manipulating people who use your system to get other people to use the service.", false);
                assessment.addQuestion(q2);
                Question q3 = new Question("16", "mHealth and eHealth are terms that can be used interchangeably.",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("True", false);
                q3.addPossibleAnswer("False", true);
                assessment.addQuestion(q3);
            } else if (assessmentId.equals("7")) { // Session 2-2 Assessment
                assessment = new Assessment(assessmentId, "Session 2 Assessment", 100);
                Question q1 = new Question("17", "mHealth applications can only be used on smart phones and devices.",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("True", false);
                q1.addPossibleAnswer("False", true);
                assessment.addQuestion(q1);
                Question q2 = new Question("18", "Data vulnerability is one of the biggest challenges to mHealth data collection apps.",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("True", true);
                q2.addPossibleAnswer("False", false);
                assessment.addQuestion(q2);
            } else if (assessmentId.equals("8")) { // Session 2-3 Assessment
                // TODO: add support for discussion questions
                assessment = new Assessment(assessmentId, "Session 3 Assessment", 100);
                Question q1 = new Question("19", "Please discuss two ways that mHealth applications and devices can monitor people’s health in their everyday lives. Please post your response to this question at the link below:\n" +
                        "\n" +
                        "http://chiprogram.com/discussion/viewtopic.php?f=5&t=4\n",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("Complete", true);
                q1.addPossibleAnswer("Incomplete", false);
                assessment.addQuestion(q1);
            } else if (assessmentId.equals("9")) { // Session 2-4 Assessment
                assessment = new Assessment(assessmentId, "Session 4 Assessment", 100);
                Question q1 = new Question("20", "mHealth applications can help to supplement traditional point-of-care methods.",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("True", true);
                q1.addPossibleAnswer("False", false);
                assessment.addQuestion(q1);
            } else if (assessmentId.equals("10")) { // Session 1-2-1 Assessment
                assessment = new Assessment(assessmentId, "Session 1 Assessment", 75);
                Question q1 = new Question("21", "Which of the following is a reason to conduct research?",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("To get rich and earn a lot of money", false);
                q1.addPossibleAnswer("To reduce the efficiency in our use of resources", false);
                q1.addPossibleAnswer("To find new ways to improve the quality of our lives", true);
                assessment.addQuestion(q1);
                Question q2 = new Question("22", "Research is characterized by which of the following:",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("A systematic process of collecting and logically analyzing information", true);
                q2.addPossibleAnswer("A random process of making occasional observations", false);
                q2.addPossibleAnswer("A process that does not involve use of the scientific method", false);
                assessment.addQuestion(q2);
                Question q3 = new Question("23", "The scientific method involves:",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("A disorganized approach to collection of scientific data", false);
                q3.addPossibleAnswer("Principles and processes regarded as necessary for scientific investigation", true);
                q3.addPossibleAnswer("A process or approach for generating unreliable knowledge", false);
                assessment.addQuestion(q3);
                Question q4 = new Question("24", "You form a research hypothesis when you:",
                        Question.QuestionType.SINGLE_ANSWER);
                q4.addPossibleAnswer("Make an educated guess at the answer to your research question", true);
                q4.addPossibleAnswer("Collect information from a large number of survey participants", false);
                q4.addPossibleAnswer("Review results of data collected during the process of your research", false);
                assessment.addQuestion(q4);
            } else if (assessmentId.equals("11")) { // Session 1-2-2 Assessment
                assessment = new Assessment(assessmentId, "Session 2 Assessment", 75);
                Question q1 = new Question("25", "A research hypothesis provides the basis for investigators to:",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("Describe their prediction of research outcomes based on their fortune teller", false);
                q1.addPossibleAnswer("Avoid expressing what they see as the relationship between two or more variables", false);
                q1.addPossibleAnswer("State their prediction regarding the relationship between two or more variables", true);
                assessment.addQuestion(q1);
                Question q2 = new Question("26", "Quantitative research involves:",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("Systematic collection, evaluation and analysis of information based on numerical measurements of outcomes", true);
                q2.addPossibleAnswer("Random collection, evaluation and analysis of information based on non-numerical measurement of outcomes", false);
                q2.addPossibleAnswer("Non-systematic collection, evaluation and analysis that provides no measurements or values for pre-specified outcomes or comparisons of interest", false);
                assessment.addQuestion(q2);
                Question q3 = new Question("27", "In conducting research, the target population is:",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("The smallest population from which a sample can be selected", false);
                q3.addPossibleAnswer("The population to be studied to which the investigator wants to generalize results", true);
                q3.addPossibleAnswer("The population that will be all enrolled in the study once recruitment begins", false);
                assessment.addQuestion(q3);
                Question q4 = new Question("28", "In taking a random sample of participants from a community:",
                        Question.QuestionType.SINGLE_ANSWER);
                q4.addPossibleAnswer("Each community pre-selects residents who they wish to participate in a study", false);
                q4.addPossibleAnswer("Each participant has an unknown probability of being selected for inclusion", false);
                q4.addPossibleAnswer("Each participant has a known probability of being selected for inclusion", true);
                assessment.addQuestion(q4);
            } else if (assessmentId.equals("12")) { // Session 1-2-3 Assessment
                assessment = new Assessment(assessmentId, "Session 3 Assessment", 100);
                Question q1 = new Question("29", "A community health needs assessment helps to describe:",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("The health status of a community ten years in the future", false);
                q1.addPossibleAnswer("The present health status as a baseline for comparison in the future", true);
                q1.addPossibleAnswer("The past health status of a community at a point 20 years in the past", false);
                assessment.addQuestion(q1);
                Question q2 = new Question("30", "Community health needs assessments tend to assess:",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("What services are currently being provided", false);
                q2.addPossibleAnswer("What do local people see as their health needs", false);
                q2.addPossibleAnswer("What the national and local priorities for health are", false);
                q2.addPossibleAnswer("All of the above", true);
                assessment.addQuestion(q2);
                Question q3 = new Question("31", "Which of the following isn’t involved in the holistic view of health found in a community needs assessment?",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("The physical environment", false);
                q3.addPossibleAnswer("The social environment", false);
                q3.addPossibleAnswer("The political spectrum environment", true);
                q3.addPossibleAnswer("The economic environment", false);
                assessment.addQuestion(q3);
            } else if (assessmentId.equals("13")) { // Session 1-2-4 Assessment
                assessment = new Assessment(assessmentId, "Session 4 Assessment", 100);
                Question q1 = new Question("32", "Interpretation of research data is:",
                        Question.QuestionType.SINGLE_ANSWER);
                q1.addPossibleAnswer("A process of examining data collection forms for errors in design of questionnaires", false);
                q1.addPossibleAnswer("A process of creating new data on the computer and looking for patterns in the data", false);
                q1.addPossibleAnswer("A process of examining research data and searching for relationships and patterns", true);
                assessment.addQuestion(q1);
                Question q2 = new Question("33", "How should you aim to present your results to someone not familiar with your data?",
                        Question.QuestionType.SINGLE_ANSWER);
                q2.addPossibleAnswer("Starting off with general information and progressing to more specific info", true);
                q2.addPossibleAnswer("Starting off with specific information and working backwards from there", false);
                q2.addPossibleAnswer("A highly specific and technical report of your data", false);
                assessment.addQuestion(q2);
                Question q3 = new Question("34", "Valid data are:",
                        Question.QuestionType.SINGLE_ANSWER);
                q3.addPossibleAnswer("Information collected from whoever is available to study", false);
                q3.addPossibleAnswer("Information collected in a manner that minimizes bias due to random error or lack of generalizability", true);
                q3.addPossibleAnswer("Information collected in a manner that maximizes speed of collection", false);
                assessment.addQuestion(q3);
            }
            m_assessmentMap.put(assessmentId, assessment);
        }
        return m_assessmentMap.get(assessmentId);
    }

    // returns -1 if the user has not attempted the assessment
    public int getAssessmentScore(String assessmentId, String userId) {
        if (m_scoresMap.containsKey(assessmentId + "-" + userId) == false || isNewUpdate) {
            // TODO: make call to database to get assessment score

            // SQL: "Select score from scores where email=" + userEmail + " and session_id=" + sessionId + ";";

            m_scoresMap.put(assessmentId + "-" + userId, -1);
        }
        return m_scoresMap.get(assessmentId + "-" + userId);
    }

    public boolean setAssessmentScore(String assessmentId, String userId, int newScore) {
        // TODO: make call to database to set new score

        // SQL: "Insert into scores where email=" + userEmail + " and session_id=" + sessionId + ";";
        m_scoresMap.put(assessmentId + "-" + userId, newScore);
        return true;
    }

    public CHIPUser checkUserLogin(String email, String password, Context context) {
        // TODO: make call to database to check user login

        // SQL: "Select password from passwords where email=" + userEmail + ";";

        // TODO: remove placeholder
        try {
            InputStream inputStream = context.openFileInput("users");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                if (stringBuilder.toString().isEmpty() == false) {
                    String[] userPassStrings = stringBuilder.toString().split(Consts.CHIPUSER_SEPARATOR, -1);
                    for (int i = 0; i < userPassStrings.length; ++i) {
                        String currUserPass = userPassStrings[i];
                        String[] userStrings = currUserPass.split(Consts.PASSWORD_SEPARATOR, -1);

                        if (email.equals(userStrings[0]) && password.equals(userStrings[1])) {
                            return getUserFromFile(email, context);
                        }
                    }
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        /*
        if (email.equals("bkt421@gmail.com") && password.equals("CH!(ch19")) {
            return new CHIPUser("1", "Robert", "Tanniru", "123 4th Street\r\nRochester, MI 48306",
                                "Detroit", "Program Team", "", "Mohan Tanniru",
                                "Creating a mobile app version of the CHIP website",
                                "bkt421@gmail.com");
        } else {
            return null;
        }
        */
        return null;
    }

    public boolean registerUser(CHIPUser user, String password, Context context) {
        try {
            ArrayList<CHIPUser> currentUsers = getUsersFromFile("users", context);

            if (currentUsers != null) {
                for (CHIPUser u : currentUsers) {
                    if (u.get_email().equals(user.get_email())) {
                        // not unique
                        return false;
                    }
                }
            }

            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput("users", Context.MODE_APPEND));

            outputStreamWriter.write(user.get_email() + Consts.PASSWORD_SEPARATOR + password +
                    Consts.PASSWORD_SEPARATOR + Consts.CHIPUSER_SEPARATOR);
            outputStreamWriter.close();

            writeUserToFile(user.get_email(), user, context);
        }
        catch (IOException e) {
            String message = "error writing to file: users";
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void writeUserToFile(String filename, CHIPUser user, Context context) {
        try {
            OutputStreamWriter outputStreamWriter =
                    new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_PRIVATE));

            outputStreamWriter.write(user.toFileString());
            outputStreamWriter.close();
        }
        catch (IOException e) {
            String message = "error writing to file: " + filename;
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<CHIPUser> getUsersFromFile(String filename, Context context) {
        ArrayList<CHIPUser> users = new ArrayList<CHIPUser>();
        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                if (stringBuilder.toString().isEmpty() == false) {
                    String[] userPassStrings = stringBuilder.toString().split(Consts.CHIPUSER_SEPARATOR, -1);
                    for (int i = 0; i < userPassStrings.length - 1; ++i) {
                        String currUserPass = userPassStrings[i];
                        String[] userStrings = currUserPass.split(Consts.PASSWORD_SEPARATOR, -1);
                        users.add(getUserFromFile(userStrings[0], context));
                    }
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return users;
    }

    private CHIPUser getUserFromFile(String filename, Context context) {
        try {
            InputStream inputStream = context.openFileInput(filename);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString + "\n");
                }

                inputStream.close();
                if (stringBuilder.toString().isEmpty() == false) {
                    return new CHIPUser(stringBuilder.toString());
                } else {
                    return null;
                }
            }
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    public CHIPUser getMentor(String mentorId, String userId) {
        // TODO: make call to database to get mentor and make sure that user has current mentor chosen

        // TODO: remove placeholder
        if (mentorId.equals("2") && userId.equals("1")) {
            return new CHIPUser("2" , "Mohan", "Tanniru", "123 4th Street\r\nRochester, MI 48306",
                    "Detroit", "Mentor", "Business IT", "",
                    "Helping with the business side of the CHIProgram",
                    "tanniru@oakland.edu");
        } else {
            return null;
        }
    }


    public String getMentorInfo(String mentorId) {
        // TODO: make call to database to get mentor overview info

        // TODO: remove placeholder
        if (mentorId.equals("2")) {
            return ("Mohan Tanniru - Business IT (Detroit)");
        } else {
            return null;
        }
    }

    public String[] getMentorIdList() {
        if (mentors == null || isNewUpdate) {
            // TODO: add in call to database for this
            /*
                    "Yubraj Acharya - Nutrition & Health Financing (Nepal)",
                    "Eli Bailey - Micro Financing and Financial Modeling (US and India)",
                    "Deepak Bajracharya - Management & Communications (Nepal)",
                    "Kofi Awusabo-Asare - Population Studies (Ghana)",
                    "Linda Kaljee - Anthropology (US)",
                    "Paul Kilgore - Vaccines (US)",
                    "Mentor Lucien - Microbiology & Immunology (Haiti)",
                    "Kate Otto - mHealth & data collection (US)",
                    "Genevieve Poitevien - Medicine (Haiti)",
                    "Mohan Tanniru - Business IT (US)",
                    "Placide Tapsoba - Community Health (Ghana)",
                    "Marcus Zervos - Infectious Diseases (US)";
             */

            // TODO: remove placeholder
            mentors = new String[]{"2"};
        }

        return mentors;
    }

    public String[] getMentorInfoList(String[] mentorIds) {
        String[] mentorInfoList = new String[mentorIds.length];
        for (int i = 0; i < mentorIds.length; ++i) {
            mentorInfoList[i] = getMentorInfo(mentorIds[i]);
        }
        return mentorInfoList;
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