/*
 *  "Analog Compass" is an application for devices based on android os. 
 *  The application shows the orientation based on the intern magnetic sensor.   
 *  Copyright (C) 2009  Dieter Roth
 *
 *  This program is free software; you can redistribute it and/or modify it under the terms of the 
 *  GNU General Public License as published by the Free Software Foundation; either version 3 of 
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along with this program; 
 *  if not, see <http://www.gnu.org/licenses/>.
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
 


