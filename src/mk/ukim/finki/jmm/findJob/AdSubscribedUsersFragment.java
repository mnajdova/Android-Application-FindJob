package mk.ukim.finki.jmm.findJob;

import java.util.ArrayList;

import mk.ukim.finki.jmm.findJob.adapter.MyAdsListAdapter;
import mk.ukim.finki.jmm.findJob.adapter.SubscribedUsersListAdapter;
import mk.ukim.finki.jmm.findJob.asyncTasks.AdSubscribedUsersTask;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.Subscription;
import info.androidhive.slidingmenu.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class AdSubscribedUsersFragment extends Fragment {
	
	private ListView mlstSubscribedUsers;
	private ArrayList<Subscription> mSubscribedItems;
	private SubscribedUsersListAdapter subUsersListAdapter;
	private TextView mtxtSubAdName;
	
	public AdSubscribedUsersFragment(){}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_ad_subscribed_users, container, false);
       
        Ad ad =new Ad();
        ad=(Ad)getArguments().getSerializable(EmployeerActivity.SELECTED_AD);
        
        mtxtSubAdName=(TextView)rootView.findViewById(R.id.txtSubAdName);
        mlstSubscribedUsers=(ListView)rootView.findViewById(R.id.lstSubscribedUsers);
        mSubscribedItems = new ArrayList<Subscription>();
        subUsersListAdapter = new SubscribedUsersListAdapter(getActivity(), mSubscribedItems);
        mlstSubscribedUsers.setAdapter(subUsersListAdapter);
        
        mtxtSubAdName.setText(ad.name);
        AdSubscribedUsersTask task= new AdSubscribedUsersTask(getActivity(), ad, mSubscribedItems, subUsersListAdapter);
        task.execute(getString(R.string.getAdSubscibersUrl));
        
        return rootView;
    }
}
