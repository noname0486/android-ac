package de.rothbayern.android.ac;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class InfoActivity extends Activity {
	
	WebView webview;
	
    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.info);
        
        webview = (WebView) findViewById(R.id.webview_info);
        webview.loadUrl("file:///android_asset/info.html");
    }
    
    

}
