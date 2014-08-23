package mk.ukim.finki.jmm.findJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mk.ukim.finki.jmm.findJob.asyncTasks.CreateAdTask;
import mk.ukim.finki.jmm.findJob.asyncTasks.RegistrationTask;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.User;

import com.google.gson.Gson;

import info.androidhive.slidingmenu.R;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateNewAdFragment extends Fragment {
	
	
	 private TextView mOutputDateFrom;
	 private TextView mOutputDateTo;
	 private EditText mchangeDateFrom;
	 private EditText mchangeDateTo;
	 private Button mbtnCreateAd;
	 private EditText minputAdName;
	 private EditText minputLocation;
	 private EditText minputAdDescription;
	 private TextView mtxtErrorA;
	 
	 private int yearFrom;
	 private int monthFrom;
	 private int dayFrom;
	 private int yearTo;
	 private int monthTo;
	 private int dayTo;
	 
	 private Button mchangeDate;
	 
	
	public CreateNewAdFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_create_new_ad, container, false);
         
       
        
        mchangeDateFrom=(EditText)rootView.findViewById(R.id.inputDateFrom);
        mchangeDateTo=(EditText)rootView.findViewById(R.id.inputDateTo);
        mbtnCreateAd=(Button)rootView.findViewById(R.id.btnCreateAd);
       minputAdDescription = (EditText)rootView.findViewById(R.id.inputAdDescription);
       minputAdName = (EditText)rootView.findViewById(R.id.inputAdName);
       minputLocation = (EditText)rootView.findViewById(R.id.inputLocation);
        mtxtErrorA = (TextView) rootView.findViewById(R.id.txtErrorA);
        
        
        mbtnCreateAd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (checkConnection()) {
					//createDialog();
					Log.i("Connection","success");
					SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
					Gson gson = new Gson();
				    String json =preferences.getString("USER", "");
				    User user = gson.fromJson(json, User.class);
				    
				    Ad ad = new Ad();
				    SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
				    
				    Date dateFrom=null;
				    Date dateTo=null;
					try {
						dateFrom = formatter.parse(mchangeDateFrom.getText().toString());
						dateTo = formatter.parse(mchangeDateTo.getText().toString());
						ad.dateFrom = dateFrom;
					    ad.dateTo = dateTo;
					    
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    
				    ad.Location = minputLocation.getText().toString();
				    ad.name = minputAdName.getText().toString();
				    ad.description = minputAdDescription.getText().toString();
				    ad.publisher = user;
				    
				    
					CreateAdTask task = new CreateAdTask(getActivity(),ad,mtxtErrorA);
					task.execute(getString(R.string.createNewAdUrl));
					
				}
				else{
					
					Log.i("Connection","failed");
				}
				
				
				
				
			}
		});
        
        // Get current date by calender         
        final Calendar c = Calendar.getInstance();
        yearFrom=yearTo = c.get(Calendar.YEAR);
        monthFrom=monthTo = c.get(Calendar.MONTH);
        dayFrom=dayTo = c.get(Calendar.DAY_OF_MONTH);
 
        // Show current date        
        mchangeDateFrom.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(dayFrom).append("-").append(monthFrom + 1).append("-")
                .append(yearFrom).append(" "));
        
        mchangeDateTo.setText(new StringBuilder()
        // Month is 0 based, just add 1
        .append(dayTo).append("-").append(monthTo + 1).append("-")
        .append(yearTo).append(" "));

  
        // Button listener to show date picker dialog       
        mchangeDateFrom.setOnClickListener(new View.OnClickListener() {
		
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog 
            
            	DatePickerDialog dialog= new DatePickerDialog(getActivity(), pickerListenerDateFrom, yearFrom, monthFrom,dayFrom);
                dialog.show();
                
            }
 
        });
        
        mchangeDateTo.setOnClickListener(new View.OnClickListener() {
			
            @Override
            public void onClick(View v) {
                 
                // On button click show datepicker dialog 
            	DatePickerDialog dialog= new DatePickerDialog(getActivity(), pickerListenerDateTo, yearTo, monthTo,dayTo);
                dialog.show();
 
            }
 
        });
        
        
        return rootView;
    }
	

	 private DatePickerDialog.OnDateSetListener pickerListenerDateFrom = new DatePickerDialog.OnDateSetListener() {
		 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear,
	                int selectedMonth, int selectedDay) {
	             
	            yearFrom  = selectedYear;
	            monthFrom = selectedMonth;
	            dayFrom   = selectedDay;
	 
	            // Show selected date 
	            mchangeDateFrom.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append(dayFrom).append("-").append(monthFrom + 1).append("-")
                .append(yearFrom).append(" "));
	     
	           }

			
	};
	
	 private DatePickerDialog.OnDateSetListener pickerListenerDateTo = new DatePickerDialog.OnDateSetListener() {
		 
	        // when dialog box is closed, below method will be called.
	        @Override
	        public void onDateSet(DatePicker view, int selectedYear,
	                int selectedMonth, int selectedDay) {
	             
	            yearTo  = selectedYear;
	            monthTo = selectedMonth;
	            dayTo   = selectedDay;
	 
	            // Show selected date 
	            mchangeDateTo.setText(new StringBuilder()
	            // Month is 0 based, just add 1
	            .append(dayTo).append("-").append(monthTo + 1).append("-")
	            .append(yearTo).append(" "));
	     
	          
	        }

			
	};
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
