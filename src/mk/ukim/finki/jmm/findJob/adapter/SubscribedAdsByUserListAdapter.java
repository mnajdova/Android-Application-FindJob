package mk.ukim.finki.jmm.findJob.adapter;

import info.androidhive.slidingmenu.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import mk.ukim.finki.jmm.findJob.asyncTasks.CreateSubscriptionTask;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.Subscription;
import mk.ukim.finki.jmm.findJob.model.User;

import com.google.gson.Gson;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SubscribedAdsByUserListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Ad> adsItems;
	private Dialog subscriptionDialog;
	
	public SubscribedAdsByUserListAdapter(Context context, ArrayList<Ad> adsItems){
		this.context = context;
		this.adsItems = adsItems;
	}

	@Override
	public int getCount() {
		return adsItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return adsItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.new_ads_list_item, null);
        }
         
        
        TextView mtxtAdName = (TextView) convertView.findViewById(R.id.txtAdNameN);
        TextView mtxtAdDescription = (TextView) convertView.findViewById(R.id.txtAdDescriptionN);
        Button mbtnAdDetails=(Button)convertView.findViewById(R.id.btnViewAdDetailsN);
        Button mbtnSubscribe=(Button)convertView.findViewById(R.id.btnSubscribe);
        
        mtxtAdName.setText(adsItems.get(position).name);
        mtxtAdDescription.setText(adsItems.get(position).description);
        
        mbtnAdDetails.setOnClickListener(new OnButtonDetailsClickListener(position));
        mbtnSubscribe.setVisibility(View.INVISIBLE);
        
       
        
              
        return convertView;
	}
	
	private class OnButtonDetailsClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnButtonDetailsClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View v) {
        	Ad ad = new Ad();
			ad = adsItems.get(mPosition);
			
			Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.ad_details_dialog);
			dialog.setTitle(ad.name);
			TextView mtxtNameA = (TextView) dialog.findViewById(R.id.txtNameA);
			TextView mtxtLocationA = (TextView) dialog.findViewById(R.id.txtLocationA);
			TextView mtxtDateFromA = (TextView) dialog.findViewById(R.id.txtDateFromA);
			TextView mtxtDateToA = (TextView) dialog.findViewById(R.id.txtDateToA);
			TextView mtxtDescription = (TextView) dialog.findViewById(R.id.txtDescription);
			mtxtNameA.setText(ad.name);
			mtxtLocationA.setText(ad.Location);
			SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
			
			mtxtDateFromA.setText(format.format(ad.dateFrom));
			mtxtDateToA.setText(format.format(ad.dateTo));
			mtxtDescription.setText(ad.description);
			dialog.show();
        }               
    }
	
	
	
}
