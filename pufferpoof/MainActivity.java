package com.pufferpoof.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView gameWebView;
    private ImageView diveBreatheIcon;

    @SuppressLint({"SetJavaScriptEnabled", "SourceLockedOrientationActivity"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Force hardware orientation to Portrait at the system level
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        // Use a FrameLayout to allow overlapping views (WebView + Icon)
        FrameLayout rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        // Initialize WebView programmatically
        gameWebView = new WebView(this);
        gameWebView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        rootLayout.addView(gameWebView);

        // --- DiveBreathe Side Icon Implementation ---
        diveBreatheIcon = new ImageView(this);
        int iconSize = (int) (60 * getResources().getDisplayMetrics().density); // 60dp
        FrameLayout.LayoutParams iconParams = new FrameLayout.LayoutParams(iconSize, iconSize);
        
        // Positioned at the top right with margins
        iconParams.gravity = Gravity.TOP | Gravity.END;
        iconParams.topMargin = (int) (80 * getResources().getDisplayMetrics().density);
        iconParams.rightMargin = (int) (10 * getResources().getDisplayMetrics().density);
        
        diveBreatheIcon.setLayoutParams(iconParams);
        
        // Note: You must place an icon named 'dive_breathe_logo' in your res/drawable folder
        int resId = getResources().getIdentifier("dive_breathe_logo", "drawable", getPackageName());
        if (resId != 0) {
            diveBreatheIcon.setImageResource(resId);
        } else {
            // Fallback to a system icon if your logo is not yet added to res/drawable
            diveBreatheIcon.setImageResource(android.R.drawable.ic_menu_info_details);
        }
        
        diveBreatheIcon.setElevation(10f); // Ensure it floats above the WebView
        diveBreatheIcon.setOnClickListener(v -> {
            try {
                // Link to the DiveBreathe Play Store app
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.divebreathe.app"));
                intent.setPackage("com.android.vending");
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                // Fallback if Play Store app is not installed (open in browser)
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.divebreathe.app")));
            }
        });
        
        rootLayout.addView(diveBreatheIcon);
        setContentView(rootLayout);

        // Configure high-performance web settings
        WebSettings webSettings = gameWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        
        // Critical Viewport Scaling
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        
        // Fullscreen/Immersive Mode
        gameWebView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        gameWebView.setWebViewClient(new WebViewClient());
        gameWebView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onBackPressed() {
        if (gameWebView.canGoBack()) {
            gameWebView.goBack();
        } else {
            super.onBackPressed() ;
        }
    }
}