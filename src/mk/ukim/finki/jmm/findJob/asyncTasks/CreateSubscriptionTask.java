package mk.ukim.finki.jmm.findJob.asyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import mk.ukim.finki.jmm.findJob.adapter.SubscribedUsersListAdapter;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.Subscription;
import mk.ukim.finki.jmm.findJob.model.User;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class CreateSubscriptionTask extends  AsyncTask<String, Void, Subscription> {

	private ProgressDialog loadingDialog;
	private Context context;
	private Subscription subscription;
	private TextView txtStatus;
	
	
	public CreateSubscriptionTask(Context context, Subscription subscription/*, TextView txtStatus*/) {
		this.context=context;
		this.subscription = subscription;
		//this.txtStatus = txtStatus;
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
	protected Subscription doInBackground(String... params) {
		Subscription result = null;
		if (params.length == 1) {
			String url = params[0];
			result = loadData(url);
		}
		return result;
	}
	
	private Subscription loadData(String url) {
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
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("description", subscription.shortDescription));
		        nameValuePairs.add(new BasicNameValuePair("ad_id", subscription.ad.id.toString()));
		        nameValuePairs.add(new BasicNameValuePair("user_id", subscription.subscribedUser.id.toString()));
		        
		        
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
	
	public Subscription parseJson(String content) throws JSONException, ParseException {
		Subscription subscription = new Subscription();
		
		if(content!=""){
	
		JSONObject jObj = new JSONObject(content);
		
		subscription.shortDescription = jObj.getString("shortDescription");
		
		
		
		
		JSONObject jObjAd = jObj.getJSONObject("ad");
		Ad ad = new Ad();
		ad.id=jObjAd.getLong("id");
		ad.name=jObjAd.getString("name");
		ad.description = jObjAd.getString("description");
		ad.Location = jObjAd.getString("Location");
		//SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
		ad.dateFrom = new Date(new Long(jObjAd.getString("dateFrom")));
		ad.dateTo = new Date(new Long(jObjAd.getString("dateTo")));
		JSONObject publisher = jObjAd.getJSONObject("publisher");
		User userr = new User();
		userr.id = publisher.getLong("id");
		userr.name = publisher.getString("name");
		userr.password = publisher.getString("password");
		userr.username = publisher.getString("username");
		userr.phone = publisher.getString("phone");
		userr.role = publisher.getInt("role");
		userr.CVLink = publisher.getString("CVLink");
		userr.email = publisher.getString("email");
		ad.publisher = userr;
		
	
		User user = new User();
		JSONObject jObjUser = jObj.getJSONObject("subscribedUser");
		user.id = jObjUser.getLong("id");
		user.name = jObjUser.getString("name");
		user.password = jObjUser.getString("password");
		user.username = jObjUser.getString("username");
		user.phone = jObjUser.getString("phone");
		user.role = jObjUser.getInt("role");
		user.CVLink = jObjUser.getString("CVLink");
		user.email = jObjUser.getString("email");
	    subscription.subscribedUser = user;
	    
	    
	    
	    
	    
		return subscription;
		
		}
		Log.i("NULL", "aaaaaaaaaaaaaa");
		return null;
	}

	@Override
	protected void onPostExecute(Subscription result) {
		
		//mAdapter.addAll(result);
		loadingDialog.dismiss();
		
		
		if(result!=null)
		{
			//txtStatus.setText("Sucessfull creation");
		}
		else{
			//txtStatus.setText("Error");
			
		}
		
		super.onPostExecute(result);
	}
}
