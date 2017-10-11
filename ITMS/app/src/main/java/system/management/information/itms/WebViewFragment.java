package system.management.information.itms;


import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;



/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {


    ProgressDialog progressDialog;
    public WebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        String url = " http://soctech.sut.ac.th/it/webitsut2015/index.php";
        WebView webView = (WebView) rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        progressDialog.dismiss();
        // Inflate the layout for this fragment
        return rootView;
    }

}
