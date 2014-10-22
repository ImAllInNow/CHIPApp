package chiprogram.chipapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.Chapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChapterVideoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChapterVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ChapterVideoFragment extends Fragment {

    private Chapter m_chapter;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param chapterId of chosen Chapter.
     * @return A new instance of fragment ChapterVideoFragment.
     */
    public static ChapterVideoFragment newInstance(String chapterId) {
        ChapterVideoFragment fragment = new ChapterVideoFragment();
        Bundle args = new Bundle();
        args.putString(ChapterTabsActivity.CHAPTER_ID, chapterId);
        fragment.setArguments(args);
        return fragment;
    }
    public ChapterVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String chapterId = getArguments().getString(ChapterTabsActivity.CHAPTER_ID);

            m_chapter = CHIPLoaderSQL.getChapterDetails(chapterId);
        } else {
            m_chapter = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chapter_video, container, false);

        TextView videoURL = (TextView) view.findViewById(R.id.chap_video_title);

        if (m_chapter != null) {
            // TODO: change this
            videoURL.setText("Video: " + m_chapter.getVideoURL());
        } else {
            // TODO: change this
            videoURL.setText("Oops!  Error loading Chapter!");
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
