package chiprogram.chipapp.webview;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by bhatia on 11/8/2014.
 */
public class DiscussionWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("http:") || url.startsWith("https:"))
        {
            try
            {
                URL urlObj = new URL(url);
                if (urlObj.getHost().endsWith("chiprogram.com") && urlObj.getPath().startsWith("/discussion")) {
                    view.loadUrl(url);
                }
                else
                {
                    view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                }

                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
