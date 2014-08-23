package mk.ukim.finki.jmm.findJob.asyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mk.ukim.finki.jmm.findJob.adapter.MyAdsListAdapter;
import mk.ukim.finki.jmm.findJob.adapter.SubscribedUsersListAdapter;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.Subscription;
import mk.ukim.finki.jmm.findJob.model.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class AdSubscribedUsersTask extends  AsyncTask<String, Void, List<Subscription>> {

	private ProgressDialog loadingDialog;
	private Context context;
	private Ad ad;
	private ArrayList<Subscription> subscribedItems;
	private SubscribedUsersListAdapter subUsersListAdapter;
	
	
	public AdSubscribedUsersTask(Context context, Ad ad, ArrayList<Subscription> subscribedItems, SubscribedUsersListAdapter subUsersListAdapter) {
		this.context=context;
		this.ad = ad;
		this.subscribedItems=subscribedItems;
		this.subUsersListAdapter=subUsersListAdapter;
		
		
	}

	private void createDialog() {
		loadingDialog = new ProgressDialog(context);
		loadingDialog.setTitle("Processing");
		loadingDialog.setMessage("Please wait...");
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		createDialog();
		loadingDialog.show();
	}

	@Override
	protected List<Subscription> doInBackground(String... params) {
		List<Subscription> result = null;
		if (params.length == 1) {
			String url = params[0];
			result = loadData(url);
		}
		return result;
	}
	
	private List<Subscription> loadData(String url) {
		try {
			
			String content = getContentFromUrl(url);
			return parseJson(content);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getContentFromUrl(String url) throws Exception {
		InputStream is = getStreamFromUrl(url);
		String content;
		if (is != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			content = sb.toString();

			return content;
		}
		return null;

	}
	
	public InputStream getStreamFromUrl(String url) throws Exception {
		HttpResponse httpResponse = null;
		InputStream is = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		
		 try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		        nameValuePairs.add(new BasicNameValuePair("ad.id", ad.id+""));
		        
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		        // Execute HTTP Post Request
		         httpResponse = httpClient.execute(httppost);
		        HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				

		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    }
			
		
		 if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == 200) {
				return is;
			}
			return null;
		
		
	}
	
	public List<Subscription> parseJson(String content) throws JSONException {
		
		List<Subscription> subscriptions= new ArrayList<Subscription>();
		Log.i("JSON", content);
		
		JSONArray jsonItems = new JSONArray(content);

		for (int i = 0; i < jsonItems.length(); i++) {

			JSONObject jObj = (JSONObject) jsonItems.get(i);	
			Subscription sub=new Subscription();
			
			
			String subscribedUser=jObj.getString("subscribedUser");
			JSONObject jsubscribedUser= new JSONObject(subscribedUser);
			User subUser= new User();
			subUser.id = jsubscribedUser.getLong("id");
			subUser.name = jsubscribedUser.getString("name");
			subUser.password = jsubscribedUser.getString("password");
			subUser.username = jsubscribedUser.getString("username");
			subUser.phone = jsubscribedUser.getString("phone");
			subUser.role = jsubscribedUser.getInt("role");
			subUser.CVLink = jsubscribedUser.getString("CVLink");
			subUser.email = jsubscribedUser.getString("email");			
			sub.subscribedUser=subUser;
			
			String subscribedAd=jObj.getString("ad");
			JSONObject jsubscribedAd= new JSONObject(subscribedAd);
			Ad subAd = new Ad();
			subAd.id=jsubscribedAd.getLong("id");
			subAd.name=jsubscribedAd.getString("name");
			subAd.description=jsubscribedAd.getString("description");
			subAd.dateFrom=new Date(jsubscribedAd.getLong("dateFrom"));
			subAd.dateTo=new Date(jsubscribedAd.getLong("dateTo"));
			subAd.Location=jsubscribedAd.getString("Location");
			sub.ad=subAd;
			
			sub.shortDescription=jObj.getString("shortDescription");

			subscriptions.add(sub);
		}
		return subscriptions;
	}

	@Override
	protected void onPostExecute(List<Subscription> result) {
		
		//mAdapter.addAll(result);
		loadingDialog.dismiss();
		
		
		if(result!=null)
		{
			Log.i("RESULT",result.toString());
			subscribedItems.addAll(result);
			Log.i("RESULT",subscribedItems.size()+"");
			subUsersListAdapter.notifyDataSetChanged();
		}
		else{
			
			Log.i("RESULT","Result is null");
		}
		
		super.onPostExecute(result);
	}
}
