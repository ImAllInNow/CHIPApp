package chiprogram.chipapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import chiprogram.chipapp.dialogs.ConfirmationDialog;
import chiprogram.chipapp.R;
import chiprogram.chipapp.classes.Assessment;
import chiprogram.chipapp.database.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.CommonFunctions;
import chiprogram.chipapp.classes.Question;


public class AssessmentActivity extends BaseActivity implements
        ConfirmationDialog.ConfirmationDialogListener {

    private static final String CONFIRM_CANCEL_ASSESSMENT_TAG = "fragment_confirm_cancel_assessment";
    private static final int QUESTIONS_LOC_IN_LAYOUT = 1;

    private static final String CURRENT_RESPONSES = "chiprogram.chipapp.CURRENT_RESPONSES";

    public static final String ASSESSMENT_ID = "chiprogram.chipapp.ASSESSMENT_ID";

    private CHIPUser m_user;
    private String m_assessmentId;
    private Assessment m_assessment;

    private FragmentManager m_fragmentManager;

    private ArrayList<Boolean[]> m_currentResponses;
    private boolean m_allQuestionsAnswered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        m_fragmentManager = getFragmentManager();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        m_user = extras.getParcelable(ProfileActivity.ARGUMENT_USER);

        m_assessmentId = extras.getString(ASSESSMENT_ID);
        m_assessment = CHIPLoaderSQL.getInstance().getAssessment(m_assessmentId);

        m_currentResponses = new ArrayList<Boolean[]>();
        m_allQuestionsAnswered = false;
        if (savedInstanceState == null) {
            for (int i = 0; i < m_assessment.getNumQuestions(); ++i) {
                Question currQuestion = m_assessment.getQuestion(i);

                m_currentResponses.add(new Boolean[currQuestion.numAnswers()]);
                for (int j = 0; j < currQuestion.numAnswers(); ++j) {
                    m_currentResponses.get(i)[j] = false;
                }
            }
        } else {
            for (int i = 0; i < m_assessment.getNumQuestions(); ++i) {
                boolean[] bundleResponses = savedInstanceState.getBooleanArray(CURRENT_RESPONSES + i);
                Boolean[] currResponses = new Boolean[bundleResponses.length];

                for(int j = 0; j < currResponses.length; ++j) {
                    currResponses[j] = bundleResponses[j];
                }
                m_currentResponses.add(currResponses);
            }
        }
        setUpQuestionsViews();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        getCurrentAnswers();

        for(int i = 0; i < m_currentResponses.size(); ++i) {
            Boolean[] currResponses = m_currentResponses.get(i);
            boolean[] bundleResponses = new boolean[currResponses.length];
            for(int j = 0; j < currResponses.length; ++j) {
                bundleResponses[j] = currResponses[j];
            }
            outState.putBooleanArray(CURRENT_RESPONSES + i, bundleResponses);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.assessment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // verify that user wants to cancel registration
        FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();

        ConfirmationDialog confirmDeleteCompletedTasksDialog =
                new ConfirmationDialog();

        Bundle args = new Bundle();
        args.putString(ConfirmationDialog.ARG_MESSAGE_TEXT, getString(R.string.confirm_cancel_assessment));
        args.putString(ConfirmationDialog.ARG_TAG, CONFIRM_CANCEL_ASSESSMENT_TAG);

        confirmDeleteCompletedTasksDialog.setArguments(args);

        fragmentTransaction.add(confirmDeleteCompletedTasksDialog, CONFIRM_CANCEL_ASSESSMENT_TAG);
        fragmentTransaction.commit();
    }

    public void onFinishConfirmationDialog(String tag) {
        if (tag.equals(CONFIRM_CANCEL_ASSESSMENT_TAG)) {
            Intent intent = new Intent();
            setResult(-1, intent);
            finish();
        }
    }

    private void setUpQuestionsViews() {

        TextView assessmentTitle = (TextView) findViewById(R.id.questions_assessment_title);
        assessmentTitle.setText(m_assessment.getName());

        LinearLayout baseLayout = (LinearLayout) findViewById(R.id.questions_layout);

        for (int i = 0; i < m_assessment.getNumQuestions(); ++i) {
            Question currQuestion = m_assessment.getQuestion(i);
            Boolean[] currResponses = m_currentResponses.get(i);

            LinearLayout questionLayout = new LinearLayout(this);
            questionLayout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(baseLayout.getLayoutParams());
            lp.setMargins(20, 0, 0, 20);
            questionLayout.setLayoutParams(lp);

            TextView questionText = new TextView(this);
            questionText.setText((i + 1) + ". " + currQuestion.toString());
            questionText.setTextSize(18);
            questionText.setTextIsSelectable(true);
            questionLayout.addView(questionText);

            if (currQuestion.getType() == Question.QuestionType.MULTIPLE_ANSWERS) {
                LinearLayout answerGroup = new LinearLayout(this);
                answerGroup.setOrientation(LinearLayout.VERTICAL);

                for (int j = 0; j < currQuestion.numAnswers(); ++j) {
                    CheckBox answerBox = new CheckBox(this);
                    answerBox.setText(((char)('a'+j)) + ". " + currQuestion.getPossibleAnswer(j));
                    answerBox.setChecked(currResponses[j]);
                    answerGroup.addView(answerBox);
                }

                lp = new LinearLayout.LayoutParams(baseLayout.getLayoutParams());
                lp.setMargins(0, 0, 0, 20);
                answerGroup.setLayoutParams(lp);
                questionLayout.addView(answerGroup);
            } else {
                RadioGroup answerGroup = new RadioGroup(this);
                answerGroup.setOrientation(RadioGroup.VERTICAL);

                for (int j = 0; j < currQuestion.numAnswers(); ++j) {
                    RadioButton answerButton = new RadioButton(this);
                    answerButton.setText(((char)('a'+j)) + ". " + currQuestion.getPossibleAnswer(j));
                    answerButton.setChecked(currResponses[j]);
                    answerGroup.addView(answerButton);
                }

                lp = new RadioGroup.LayoutParams(baseLayout.getLayoutParams());
                lp.setMargins(0, 0, 0, 20);
                answerGroup.setLayoutParams(lp);
                questionLayout.addView(answerGroup);
            }

            baseLayout.addView(questionLayout, i+QUESTIONS_LOC_IN_LAYOUT);
        }
    }

    private void getCurrentAnswers() {
        LinearLayout baseLayout = (LinearLayout) findViewById(R.id.questions_layout);

        m_allQuestionsAnswered = true;
        for (int i = 0; i < m_assessment.getNumQuestions(); ++i) {
            Question currQuestion = m_assessment.getQuestion(i);
            Boolean[] currResponses = m_currentResponses.get(i);

            LinearLayout questionLayout = (LinearLayout) baseLayout.getChildAt(i+QUESTIONS_LOC_IN_LAYOUT);

            boolean answerChosen = false;
            if (currQuestion.getType() == Question.QuestionType.MULTIPLE_ANSWERS) {
                LinearLayout answerGroup = (LinearLayout) questionLayout.getChildAt(1);

                for (int j = 0; j < currQuestion.numAnswers(); ++j) {
                    CheckBox chkBox = (CheckBox) answerGroup.getChildAt(j);
                    currResponses[j] = chkBox.isChecked();
                    if (currResponses[j]) {
                        answerChosen = true;
                    }
                }
            } else {
                RadioGroup answerGroup = (RadioGroup) questionLayout.getChildAt(1);

                for (int j = 0; j < currQuestion.numAnswers(); ++j) {
                    RadioButton rdoBtn = (RadioButton) answerGroup.getChildAt(j);
                    currResponses[j] = rdoBtn.isChecked();
                    if (currResponses[j]) {
                        answerChosen = true;
                    }
                }
            }

            if (answerChosen == false) {
                m_allQuestionsAnswered = false;
            }
        }
    }

    public void submitClicked(View view) {
        int numCorrect = 0;
        boolean[] answersCorrect = new boolean[m_assessment.getNumQuestions()];

        getCurrentAnswers();

        if (m_allQuestionsAnswered) {
            for (int i = 0; i < m_assessment.getNumQuestions(); ++i) {
                answersCorrect[i] = m_assessment.getQuestion(i).checkAnswer(m_currentResponses.get(i));

                if (answersCorrect[i]) {
                    numCorrect++;
                }
            }

            String numCorrectString;
            String questionWord = getString(R.string.response_questions);
            if (numCorrect == m_assessment.getNumQuestions()) {
                numCorrectString = getString(R.string.response_all);
            } else if (numCorrect == 0) {
                numCorrectString = getString(R.string.response_no);
            } else {
                numCorrectString = "" + numCorrect;
                if (numCorrect == 1) {
                    questionWord = getString(R.string.response_question);
                }
            }
            Toast.makeText(this, getString(R.string.response_You_got) + " " +
                    numCorrectString + " " + questionWord + " " +
                    getString(R.string.response_correct) + "!", Toast.LENGTH_LONG).show();

            CHIPLoaderSQL.getInstance().setAssessmentScore(this, m_assessmentId, m_user.get_id(), numCorrect);

            Intent intent = new Intent();

            // TODO: add extras to intent

            setResult(numCorrect, intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.submit_when_incomplete), Toast.LENGTH_LONG).show();
        }
    }
}
