package mk.ukim.finki.jmm.findJob.asyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mk.ukim.finki.jmm.findJob.adapter.MyAdsListAdapter;
import mk.ukim.finki.jmm.findJob.adapter.NewAdsListAdapter;
import mk.ukim.finki.jmm.findJob.adapter.SubscribedAdsByUserListAdapter;
import mk.ukim.finki.jmm.findJob.model.Ad;
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
import android.widget.BaseAdapter;

public class SearchAdsTask extends AsyncTask<String, Void, List<Ad>> {

	public static final int MY_ADS = 0;
	public static final int NEW_ADS = 1;
	public static final int SUBSCIBED_ADS = 2;
	
	private ProgressDialog loadingDialog;
	private Context context;
	private String keyword;
	private String city;
	private User user;
	private ArrayList<Ad> adItems;
	private BaseAdapter adsListAdapter;
	
	
	
	public SearchAdsTask(Context context,String keyword, String city,User user, ArrayList<Ad> adItems, BaseAdapter adsListAdapter, int type) {
		this.context=context;
		this.keyword=keyword;
		this.city=city;
		this.adItems=adItems;
		this.user=user;
		
		if(type==MY_ADS)
			this.adsListAdapter=(MyAdsListAdapter)adsListAdapter;
		else if(type==NEW_ADS)
			this.adsListAdapter=(NewAdsListAdapter)adsListAdapter;
		else if(type==SUBSCIBED_ADS)
			this.adsListAdapter=(SubscribedAdsByUserListAdapter)adsListAdapter;
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
	protected List<Ad> doInBackground(String... params) {
		List<Ad> result = null;
		if (params.length == 1) {
			String url = params[0];
			result = loadData(url);
		}
		return result;
	}
	
	private List<Ad> loadData(String url) {
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
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		        nameValuePairs.add(new BasicNameValuePair("keyword", keyword));
		        nameValuePairs.add(new BasicNameValuePair("city", city)); 
		        nameValuePairs.add(new BasicNameValuePair("user.id", user.id+"")); 
		        
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
	
	public List<Ad> parseJson(String content) throws JSONException {
		
		List<Ad> ads= new ArrayList<Ad>();
		Log.i("JSON", content);
		
		JSONArray jsonItems = new JSONArray(content);

		for (int i = 0; i < jsonItems.length(); i++) {

			JSONObject jObj = (JSONObject) jsonItems.get(i);
			Ad ad = new Ad();
			ad.id=jObj.getLong("id");
			ad.name=jObj.getString("name");
			ad.description=jObj.getString("description");
			ad.dateFrom=new Date(jObj.getLong("dateFrom"));
			ad.dateTo=new Date(jObj.getLong("dateTo"));
			ad.Location=jObj.getString("Location");
			
			String publisher=jObj.getString("publisher");
			JSONObject jPublisher= new JSONObject(publisher);
			User pub= new User();
			pub.id = jPublisher.getLong("id");
			pub.name = jPublisher.getString("name");
			pub.password = jPublisher.getString("password");
			pub.username = jPublisher.getString("username");
			pub.phone = jPublisher.getString("phone");
			pub.role = jPublisher.getInt("role");
			pub.CVLink = jPublisher.getString("CVLink");
			pub.email = jPublisher.getString("email");
			
			ad.publisher=pub;

			ads.add(ad);
		}
		return ads;
	}

	@Override
	protected void onPostExecute(List<Ad> result) {
		
		//mAdapter.addAll(result);
		loadingDialog.dismiss();
		
		
		if(result!=null)
		{
			Log.i("RESULT",result.toString());
			adItems.addAll(result);
			Log.i("RESULT",adItems.size()+"");
			adsListAdapter.notifyDataSetChanged();
		}
		else{
			
			Log.i("RESULT","Result is null");
		}
		
		super.onPostExecute(result);
	}
	
}
