package com.example.doanchuyennganh.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.doanchuyennganh.R;

public class SeeMoreFragment extends Fragment {

    View view;
    WebView webView;
    TextView txt_url;
    ImageButton btn_back;
    ProgressBar progress;
    String url ="http://stu.edu.vn/vi/cat/21/thong-bao.html?per-page=21&pIndex=2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_see_more, container, false);
        webView= view.findViewById(R.id.webview);
        txt_url= view.findViewById(R.id.txt_url);
        btn_back=view.findViewById(R.id.btn_back);
        progress= view.findViewById(R.id.progress);

        txt_url.setText(url);
        webView.setWebViewClient(new webClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack();
            }
        });

        return view;
    }

    public class MyWebChromeClient extends WebChromeClient {
        public void onProgressChanged(WebView view, int newProgress) {
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(newProgress);
        }
    }

    public class webClient extends WebViewClient {
        public boolean  shouldOverrideUrlLoading(WebView view, String url) {
            txt_url.setText(url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.setVisibility(View.GONE);
        }
    }
}