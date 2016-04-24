package com.nengfei.web;

 
 
import com.nengfei.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);
		 Bundle bundle = this.getIntent().getExtras();  
	        if (bundle != null) {  
	          String  m_strCurURL = bundle.getString("url");  
	          WebView   m_webView = (WebView) findViewById(R.id.web);  
	          
	          
	          WebSettings    webSettings = m_webView.getSettings();
	          webSettings.setJavaScriptEnabled(true);
	          
	            m_webView.setWebViewClient(new WebViewClient() {  
	            });  
	            m_webView.loadUrl(m_strCurURL);  
	        } else {  
	        }  

	}

	public void toBack(View view){
		this.finish();
	}

	 
}
