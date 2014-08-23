package mk.ukim.finki.jmm.findJob;

import java.util.ArrayList;

import com.google.gson.Gson;

import info.androidhive.slidingmenu.R;
import mk.ukim.finki.jmm.findJob.adapter.NewAdsListAdapter;
import mk.ukim.finki.jmm.findJob.asyncTasks.AdsTask;
import mk.ukim.finki.jmm.findJob.asyncTasks.SearchAdsTask;
import mk.ukim.finki.jmm.findJob.asyncTasks.SignInTask;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.User;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchAdsFragment extends Fragment {
	
	private EditText minputKeyword;
	private EditText minputCity;
	private Button mbtnSearchAds;
	
	private ListView mlstSearchAds;
	private ArrayList<Ad> adItems;
	private User user;
	public NewAdsListAdapter adsListAdapter;
	
	
	
	public SearchAdsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_search_ads, container, false);
         
        minputKeyword = (EditText) rootView.findViewById(R.id.inputKeyword);
        minputCity = (EditText) rootView.findViewById(R.id.inputCity);
        
        mlstSearchAds=(ListView)rootView.findViewById(R.id.lstSearchAds);
        adItems = new ArrayList<Ad>();
		adsListAdapter = new NewAdsListAdapter(getActivity(), adItems);
		mlstSearchAds.setAdapter(adsListAdapter);
       
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Gson gson = new Gson();
	    String json =preferences.getString("USER", "");
	    user = gson.fromJson(json, User.class);
       
        mbtnSearchAds = (Button)rootView.findViewById(R.id.btnSearch);
        mbtnSearchAds.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (checkConnection()) {
					//createDialog();
					Log.i("Connection","success");
					String keyword;
					String city;
					if(minputKeyword.getText()==null)
						keyword="";
					else
						keyword=minputKeyword.getText().toString();
					if(minputCity.getText()==null)
						city="";
					else
						city=minputCity.getText().toString();
					
					adItems.clear();
					SearchAdsTask task = new SearchAdsTask(getActivity(),keyword,city ,user, adItems, adsListAdapter, AdsTask.NEW_ADS);
					task.execute(getString(R.string.getSearchedAdsUrl));
				}
				else{
					
					Log.i("Connection","failed");
				}
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
