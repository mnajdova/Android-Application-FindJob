package mk.ukim.finki.jmm.findJob;

import info.androidhive.slidingmenu.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;










import mk.ukim.finki.jmm.findJob.asyncTasks.RegistrationTask;
import mk.ukim.finki.jmm.findJob.model.User;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.dropbox.chooser.android.DbxChooser;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationFragment extends Fragment {
	
	public static int GET_CVLINK=10;
	private Button mbtnUploadCV;
	private Button mbtnRegister;
	private EditText minputFirstName;
	private EditText minputLastName;
	private EditText minputNumber;
	private EditText minputEmail;
	private EditText minputUsername;
	private EditText minputPassword;
	private RadioButton mradBtnEmployer;
	private RadioButton mradBtnEmployee;
	private TextView mtxtError;
	private String cvLink;
	
	static final int DBX_CHOOSER_REQUEST = 0;  // You can change this if needed
	private static String APP_KEY="28nezcgqvr6kr9g";
	private Button mChooserButton;
	private DbxChooser mChooser;
	
	public BroadcastReceiver downloadReceiver;
	
	public RegistrationFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_registration, container, false);
       
        mChooser = new DbxChooser(APP_KEY);
        
        
        minputFirstName = (EditText)rootView.findViewById(R.id.inputFirstName);
        minputLastName = (EditText) rootView.findViewById(R.id.inputLastName);
        minputEmail = (EditText) rootView.findViewById(R.id.inputEmail);
        minputNumber = (EditText) rootView.findViewById(R.id.inputPhoneNumber);
        minputUsername = (EditText) rootView.findViewById(R.id.inputUsername);
        minputPassword = (EditText) rootView.findViewById(R.id.inputPassword);
        mtxtError = (TextView) rootView.findViewById(R.id.txtError);
        mradBtnEmployer = (RadioButton) rootView.findViewById(R.id.radBtnEmployer);
        mradBtnEmployee = (RadioButton) rootView.findViewById(R.id.radBtnEmployee);
        mbtnUploadCV=(Button)rootView.findViewById(R.id.btnUploadCV);
        
        mbtnUploadCV.setVisibility(View.INVISIBLE);
       mradBtnEmployee.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(mradBtnEmployee.isChecked()==true){
				mbtnUploadCV.setVisibility(View.VISIBLE);
				
			}else{
				
				mbtnUploadCV.setVisibility(View.INVISIBLE);
			}
			
		}
	});
       
       mradBtnEmployer.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(mradBtnEmployer.isChecked()==true){
				
				mbtnUploadCV.setVisibility(View.INVISIBLE);
			}else{
			
				mbtnUploadCV.setVisibility(View.VISIBLE);
			}
			
			
		}
	});
       
        mbtnUploadCV.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mChooser.forResultType(DbxChooser.ResultType.PREVIEW_LINK).launch(RegistrationFragment.this, DBX_CHOOSER_REQUEST);
				
			}
		});
         
        mbtnRegister = (Button)rootView.findViewById(R.id.btnRegistration);
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (checkConnection()) {
					//createDialog();
					Log.i("Connection","success");
					User user = new User();
					user.name = minputFirstName.getText()+" "+minputLastName.getText();
					user.CVLink = cvLink;
					user.phone = minputNumber.getText().toString();
					user.email = minputEmail.getText().toString();
					user.password = minputPassword.getText().toString();
					if(!mradBtnEmployee.isChecked())
						user.role=1;
					else
						user.role=2;
					user.username = minputUsername.getText().toString();
					RegistrationTask task = new RegistrationTask(getActivity(), user, mtxtError);
					task.execute(getString(R.string.registrationUrl));
					
				}
				else{
					
					Log.i("Connection","failed");
				}
			}
		});
        
        return rootView;
        
       
    }
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == DBX_CHOOSER_REQUEST) {
	        if (resultCode == getActivity().RESULT_OK) {
	            DbxChooser.Result result = new DbxChooser.Result(data);
	            Log.i("PATHHHHH", "Link to selected file: " + result.getLink());
	            cvLink = result.getLink().toString();
	            mbtnUploadCV.setText("File: "+result.getName());
	            // Handle the result
	        } else {
	            // Failed or was cancelled by the user.
	        }
	    } else {
	        super.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	private boolean checkConnection() {

		ConnectivityManager connectivityMannager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityMannager.getActiveNetworkInfo();
		if (netInfo == null) {
			Toast.makeText(getActivity(), "no network connection", 5000).show();
			return false;
		} else {
			return true;
		}

	}
	 

	   
	   
}
