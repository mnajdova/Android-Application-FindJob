package mk.ukim.finki.jmm.findJob;

import java.util.ArrayList;

import com.google.gson.Gson;

import mk.ukim.finki.jmm.findJob.adapter.MyAdsListAdapter;
import mk.ukim.finki.jmm.findJob.asyncTasks.AdsTask;
import mk.ukim.finki.jmm.findJob.asyncTasks.SignInTask;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.User;
import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MyAdsFragment extends Fragment {
	
	private ListView mlstMyAds;
	private ArrayList<Ad> adItems;
	public MyAdsListAdapter adsListAdapter;
	
	public MyAdsFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_my_ads, container, false);
        
        mlstMyAds=(ListView)rootView.findViewById(R.id.lstMyAds);
        adItems = new ArrayList<Ad>();
		adsListAdapter = new MyAdsListAdapter(getActivity(), adItems);
		mlstMyAds.setAdapter(adsListAdapter);
       
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Gson gson = new Gson();
	    String json =preferences.getString("USER", "");
	    User user = gson.fromJson(json, User.class);
       
        //MyAdsTask task = new MyAdsTask(getActivity(), user, adItems, adsListAdapter);
	    AdsTask task = new AdsTask(getActivity(), user, adItems, adsListAdapter, AdsTask.MY_ADS);
		task.execute(getString(R.string.getAdsFromPublisherUrl));
         
        return rootView;
    }
}
