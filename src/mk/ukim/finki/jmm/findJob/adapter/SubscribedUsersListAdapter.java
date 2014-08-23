package mk.ukim.finki.jmm.findJob.adapter;

import info.androidhive.slidingmenu.R;

import java.util.ArrayList;

import mk.ukim.finki.jmm.findJob.EmployeerActivity;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.Subscription;
import mk.ukim.finki.jmm.findJob.model.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SubscribedUsersListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Subscription> subscribedItems;
	
	public SubscribedUsersListAdapter(Context context, ArrayList<Subscription> subscribedItems){
		this.context = context;
		this.subscribedItems = subscribedItems;
	}

	@Override
	public int getCount() {
		return subscribedItems.size();
	}

	@Override
	public Object getItem(int position) {		
		return subscribedItems.get(position);
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
            convertView = mInflater.inflate(R.layout.subscribed_users_list_item, null);
        }
         
        TextView mtxtUserFullName=(TextView)convertView.findViewById(R.id.txtUserFullName);
        TextView mtxtUserSelfDescription=(TextView)convertView.findViewById(R.id.txtUserSelfDescription);        
        Button mbtnViewCV=(Button)convertView.findViewById(R.id.btnViewUserCV);
        Button mbtnCallUser=(Button)convertView.findViewById(R.id.btnCallUser);
        
        Subscription subscription= subscribedItems.get(position);
        mtxtUserFullName.setText(subscription.subscribedUser.name);
        mtxtUserSelfDescription.setText(subscription.shortDescription);
       
        mbtnViewCV.setOnClickListener(new OnButtonViewCVClickListener(position));
        mbtnCallUser.setOnClickListener(new OnButtonCallUserClickListener(subscription.subscribedUser.phone) );
              
        return convertView;
	}
	
	private class OnButtonViewCVClickListener  implements OnClickListener{           
        private int mPosition;
         
        OnButtonViewCVClickListener(int position){
             mPosition = position;
        }
         
        @Override
        public void onClick(View v) {
        	User user= new User();
			user=subscribedItems.get(mPosition).subscribedUser;
			
			Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.view_user_cv_dialog);
            WebView wb = (WebView) dialog.findViewById(R.id.viewUserCV);
            wb.getSettings().setJavaScriptEnabled(true);
            
            String urlTokens[]=user.CVLink.split("www.dropbox.com");
            wb.loadUrl("http://docs.google.com/viewer?url=https://dl.dropboxusercontent.com"+urlTokens[1]);
            wb.setWebViewClient(new WebClient());
            dialog.setCancelable(true);
            dialog.setTitle(user.name+"'s CV");
            dialog.show();
			
	
			
        }               
    }
	
	
	private class OnButtonCallUserClickListener implements OnClickListener{

		private String phone=null;
		
		public OnButtonCallUserClickListener(String phoneNumber) {
			// TODO Auto-generated constructor stub
			this.phone=phoneNumber;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 String uri = "tel:" + phone;
			 Intent intent = new Intent(Intent.ACTION_CALL);
			 intent.setData(Uri.parse(uri));
			 context.startActivity(intent);
			// Toast.makeText(context, phone, 5000);
			
		}
		
		
	}
	
	

}

 class WebClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
}