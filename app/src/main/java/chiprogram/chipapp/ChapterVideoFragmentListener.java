package chiprogram.chipapp;

import android.net.Uri;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import chiprogram.chipapp.classes.CHIPLoaderSQL;
import chiprogram.chipapp.classes.Chapter;
import chiprogram.chipapp.classes.CommonFunctions;

public class ChapterVideoFragmentListener implements YouTubePlayer.OnInitializedListener {

    private Chapter m_chapter;

    /**
     * @param chapterId of chosen Chapter.
     */
    public ChapterVideoFragmentListener(String chapterId) {
        m_chapter = CHIPLoaderSQL.getChapterDetails(chapterId);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(CommonFunctions.getYouTubeVideoID(m_chapter.getVideoURL()));
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
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
