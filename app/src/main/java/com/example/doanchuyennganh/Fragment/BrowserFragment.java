package com.example.doanchuyennganh.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Views.MainActivity;

public class BrowserFragment extends Fragment {

    View view;
    WebView webView;
    String url="http://stu.edu.vn/vi/cat/21/thong-bao.html";
    ImageButton btn_back;
    TextView txt_url;
    ProgressBar progress;

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_browser, container, false);
        addControls();
        addEvents();
        return view;
    }

    private void addEvents() {
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });
    }

    private void addControls() {
        url= MainActivity.itemsclick.getLinkURL();

        webView= view.findViewById(R.id.webview);
        btn_back= view.findViewById(R.id.btn_back);
        txt_url= view.findViewById(R.id.txt_url);
        progress= view.findViewById(R.id.progress);

        txt_url.setText(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progress.setVisibility(View.GONE);
            }
        });
        webView.loadUrl(url);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.itemsclick = null;
    }
}