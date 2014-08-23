package mk.ukim.finki.jmm.findJob;

import mk.ukim.finki.jmm.findJob.model.User;

import com.google.gson.Gson;

import info.androidhive.slidingmenu.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		Gson gson = new Gson();
	    String json =preferences.getString("USER", "");
	    
	    if(json.compareTo("")==0){
	    	
	    	SignInFragment fragment = new SignInFragment();
			DisplayFragment(fragment);
	    	
	    }else{
	    	
	    	 User user = gson.fromJson(json, User.class);
	    	 	
	    	 if(user.role==1){
					this.startActivity(new Intent(this, EmployeerActivity.class));
				}
				else{
					this.startActivity(new Intent(this, EmployeeActivity.class));
				}
	    	
	    }
	    
	   
		
	    
		
		
		
	}
	
	
	public void DisplayFragment(Fragment fragment){
		
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.main_container, fragment).commit();
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}
}
