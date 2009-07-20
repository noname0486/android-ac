package de.rothbayern.android.ac.pref;


import android.R;
import android.os.Bundle;
import android.preference.*;

public class PreferencesActivity extends PreferenceActivity {
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          addPreferencesFromResource(R.layout.preference_category);
          
          
         
     }
     
     
    
      
     
}