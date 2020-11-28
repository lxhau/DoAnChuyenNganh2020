package com.example.doanchuyennganh.Fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doanchuyennganh.Models.Items;
import com.example.doanchuyennganh.R;
import com.example.doanchuyennganh.Views.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BrowserFragment extends Fragment {

    View view;
    WebView webView;
    String url="http://stu.edu.vn/vi/cat/21/thong-bao.html";
    ImageButton btn_back,btn_openotherbrowser;
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
        btn_openotherbrowser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.dialog_miscell);
                bottomSheetDialog.setCanceledOnTouchOutside(true);

                LinearLayout lyt_Share = bottomSheetDialog.findViewById(R.id.Share);
                LinearLayout lyt_Open = bottomSheetDialog.findViewById(R.id.Open);
                LinearLayout lyt_Copy = bottomSheetDialog.findViewById(R.id.Copy);

                lyt_Open.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(myIntent);
                            Toast.makeText(getActivity(), "Đang mở bằng trình duyệt bên ngoài.",  Toast.LENGTH_LONG).show();

                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(getActivity(), "Không tìm thấy trình duyệt có sẵn trong thiết bị.",  Toast.LENGTH_LONG).show();
                            Log.e("TAG", e.getMessage().toString());
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                lyt_Copy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("",url);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getActivity(), "Đã lưu vào bộ nhớ tạm.",  Toast.LENGTH_LONG).show();
                        bottomSheetDialog.dismiss();
                    }
                });
                lyt_Share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                        shareIntent.setType("text/plain");
                        startActivity(shareIntent);
                        bottomSheetDialog.dismiss();
                    }
                });


                bottomSheetDialog.show();
            }
        });
    }

    private void addControls() {
        url= MainActivity.itemsclick.getLinkURL();

        webView= view.findViewById(R.id.webview);
        btn_back= view.findViewById(R.id.btn_back);
        txt_url= view.findViewById(R.id.txt_url);
        btn_openotherbrowser=view.findViewById(R.id.btn_openotherbrowser);
        progress= view.findViewById(R.id.progress);

        txt_url.setText(url);
        webView.setWebViewClient(new webClient());
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.itemsclick = null;
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