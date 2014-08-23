package mk.ukim.finki.jmm.findJob;

import mk.ukim.finki.jmm.findJob.asyncTasks.RegistrationTask;
import mk.ukim.finki.jmm.findJob.asyncTasks.SignInTask;
import mk.ukim.finki.jmm.findJob.model.User;
import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignInFragment extends Fragment {
	
	private EditText minputUsername;
	private EditText minputPassword;
	private TextView mtxtError;
	private Button mbtnSingIn;
	private Button mbtnRegistration;
	
	public SignInFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_sign_in, container, false);
         
        minputUsername = (EditText) rootView.findViewById(R.id.inputUsernameS);
        minputPassword = (EditText) rootView.findViewById(R.id.inputPasswordS);
        mtxtError = (TextView) rootView.findViewById(R.id.txtErrorS);
        
        mbtnSingIn = (Button)rootView.findViewById(R.id.btnSignIn);
        mbtnSingIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (checkConnection()) {
					//createDialog();
					Log.i("Connection","success");
					User user = new User();
					user.password = minputPassword.getText().toString();
					user.username = minputUsername.getText().toString();
					SignInTask task = new SignInTask(getActivity(), user, mtxtError);
					task.execute(getString(R.string.signInUrl));
				}
				else{
					
					Log.i("Connection","failed");
				}
			}
		});
        
        mbtnRegistration = (Button) rootView.findViewById(R.id.btnRegistration);
        mbtnRegistration.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity myActivity = (MainActivity) getActivity();
				myActivity.DisplayFragment(new RegistrationFragment());
			}
		});
        
        return rootView;
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
