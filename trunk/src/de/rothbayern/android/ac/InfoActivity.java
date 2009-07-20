/*
 * Copyright (C) 2009 Analog Compass
 * 
 * This file is part of Analog Compass 
 * 
 * Analog Compass is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package de.rothbayern.android.ac;

import java.io.*;

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
        
        InputStream rawResource = getResources().openRawResource(R.raw.info);
        String content = streamToString(rawResource);
        try {rawResource.close();} catch (IOException e) {}

        String mimeType = "text/html";
        String encoding = "UTF-8";

        //webview.loadUrl("file:///android_asset/info.html");
        webview.loadData(content, mimeType, encoding);
    }
    
    public static String streamToString(InputStream in) {
        String l;
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder s = new StringBuilder();
        try {
            while ((l = r.readLine()) != null) {
                s.append(l + "\n");
            }
        } catch (IOException e) {} 
        return s.toString();
    }
}
 


