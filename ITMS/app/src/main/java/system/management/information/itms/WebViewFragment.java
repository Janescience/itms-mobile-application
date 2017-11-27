package system.management.information.itms;


import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {


    ProgressDialog progressDialog;
    Typeface Fonts;
    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        Fonts = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Kanit-Light.ttf");

        String url = " http://soctech.sut.ac.th/it/webitsut2015/index.php";
        WebView webView = (WebView) rootView.findViewById(R.id.webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.canGoBack();
        webView.loadUrl(url);
        webView.goBack();

        progressDialog = new ProgressDialog(getContext());

        String title = getContext().getString(R.string.loading);
        String message = getContext().getString(R.string.wait);

        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return true;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
