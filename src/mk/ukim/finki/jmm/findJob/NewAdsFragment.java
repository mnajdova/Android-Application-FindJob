package mk.ukim.finki.jmm.findJob;

import java.util.ArrayList;

import mk.ukim.finki.jmm.findJob.adapter.MyAdsListAdapter;
import mk.ukim.finki.jmm.findJob.adapter.NewAdsListAdapter;
import mk.ukim.finki.jmm.findJob.asyncTasks.AdsTask;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.User;

import com.google.gson.Gson;

import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewAdsFragment extends Fragment {

	private ListView mlstMyAds;
	private ArrayList<Ad> adItems;
	public NewAdsListAdapter adsListAdapter;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState){
		
		View rootView = inflater.inflate(R.layout.fragment_new_ads, container, false);
		
		mlstMyAds=(ListView)rootView.findViewById(R.id.lstNewAds);
        adItems = new ArrayList<Ad>();
		adsListAdapter = new NewAdsListAdapter(getActivity(), adItems);
		mlstMyAds.setAdapter(adsListAdapter);
       
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Gson gson = new Gson();
	    String json =preferences.getString("USER", "");
	    User user = gson.fromJson(json, User.class);
       
        AdsTask task = new AdsTask(getActivity(), user, adItems, adsListAdapter, AdsTask.NEW_ADS);
		task.execute(getString(R.string.getNewAdsUrl));
		
		return rootView;
		
	}
}
