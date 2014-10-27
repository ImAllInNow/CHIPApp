package chiprogram.chipapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.CHIPUser;
import chiprogram.chipapp.classes.NavItem;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link AssessmentFragmentTab#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class AssessmentFragmentTab extends Fragment {

    private CHIPUser m_user;

    private NavItem m_navItem;

    private View.OnClickListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param navItemId Name of chosen Chapter.
     * @return A new instance of fragment ChapterVideoFragment.
     */
    public static AssessmentFragmentTab newInstance(CHIPUser user, String navItemId) {
        AssessmentFragmentTab fragment = new AssessmentFragmentTab();
        Bundle args = new Bundle();
        args.putParcelable(ProfileActivity.ARGUMENT_USER, user);
        args.putString(NavItemTabsActivity.CURRENT_ID, navItemId);
        fragment.setArguments(args);
        return fragment;
    }
    public AssessmentFragmentTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_user = getArguments().getParcelable(ProfileActivity.ARGUMENT_USER);
            String navItemId = getArguments().getString(NavItemTabsActivity.CURRENT_ID);

            m_navItem = CHIPLoaderSQL.getInstance().getNavItem(navItemId);
        } else {
            m_navItem = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assessment_tab, container, false);

        TableLayout baseLayout = (TableLayout) view.findViewById(R.id.assess_table);
        if (m_navItem != null) {
            int userScore = CHIPLoaderSQL.getInstance().getAssessmentScore(m_navItem.getId(), m_user.get_email());

            TableRow row = makeTableRow(view, m_navItem.toString(),
                    userScore != -1, userScore, m_navItem.getNumQuestions());
            row.setOnClickListener(mListener);

            baseLayout.addView(row);
        } else {
            // TODO: handle error somehow
        }

        return view;
    }

    private TableRow makeTableRow(View view, String sessionName, boolean complete, int correct, int total) {

        TableRow headerRow = (TableRow) view.findViewById(R.id.assess_table_header_row);
        TextView headerCol1 = (TextView) view.findViewById(R.id.assess_table_title1);
        TextView headerCol2 = (TextView) view.findViewById(R.id.assess_table_title2);

        TableRow row = new TableRow(view.getContext());
        row.setLayoutParams(headerRow.getLayoutParams());

        TextView firstColumn = new TextView(view.getContext());
        firstColumn.setLayoutParams(headerCol1.getLayoutParams());
        firstColumn.setText(sessionName);
        firstColumn.setTextSize(20);

        TextView secondColumn = new TextView(view.getContext());
        secondColumn.setLayoutParams(headerCol2.getLayoutParams());
        if (complete) {
            secondColumn.setText(getString(R.string.training_complete) + "\n" + correct + "/" + total);
            //row.setBackgroundColor(R.color.); // TODO: add color
        } else {
            secondColumn.setText(getString(R.string.training_incomplete));
            //row.setBackgroundColor(R.color.); // TODO: add color
        }
        secondColumn.setTextSize(18);
        row.addView(firstColumn);
        row.addView(secondColumn);

        return row;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (View.OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement View.OnClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
