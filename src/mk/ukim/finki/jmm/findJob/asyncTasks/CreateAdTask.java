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
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import mk.ukim.finki.jmm.findJob.EmployeerActivity;
import mk.ukim.finki.jmm.findJob.model.Ad;
import mk.ukim.finki.jmm.findJob.model.User;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class CreateAdTask extends AsyncTask<String, Void, Ad> {

	private ProgressDialog loadingDialog;
	private Context context;
	private Ad ad;
	private TextView txtStatus;
	private User publisher;
	
	
	public CreateAdTask(Context context, Ad ad, TextView txtStatus) {
		this.context=context;
		this.ad = ad;
		this.txtStatus = txtStatus;
		this.publisher=ad.publisher;
		
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
	protected Ad doInBackground(String... params) {
		Ad result = null;
		if (params.length == 1) {
			String url = params[0];
			result = loadData(url);
		}
		return result;
	}
	
	private Ad loadData(String url) {
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
		        nameValuePairs.add(new BasicNameValuePair("name", ad.name));
		        nameValuePairs.add(new BasicNameValuePair("description", ad.description));
		        nameValuePairs.add(new BasicNameValuePair("user_id", publisher.id+""));
		        nameValuePairs.add(new BasicNameValuePair("location", ad.Location));
		        
		        
		        SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
		        nameValuePairs.add(new BasicNameValuePair("dateFrom", formatter.format(ad.dateFrom)));
		        nameValuePairs.add(new BasicNameValuePair("dateTo", formatter.format(ad.dateTo)));
		        
		        
		        
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
	
	public Ad parseJson(String content) throws JSONException, ParseException {
		Ad ad = new Ad();
		
			
		if(content!=""){
	
		JSONObject jObj = new JSONObject(content);
		
		ad.id=jObj.getLong("id");
		ad.name=jObj.getString("name");
		ad.description = jObj.getString("description");
		ad.Location = jObj.getString("Location");
		
		//SimpleDateFormat formatter = new SimpleDateFormat("dd-mm-yyyy");
		ad.dateFrom = new Date(new Long(jObj.getString("dateFrom")));
		ad.dateTo = new Date(new Long(jObj.getString("dateTo")));
		
	    ad.publisher=publisher;	
	    
		return ad;
		
		}
		Log.i("NULL", "aaaaaaaaaaaaaa");
		return null;
	}

	@Override
	protected void onPostExecute(Ad result) {
		
		//mAdapter.addAll(result);
		loadingDialog.dismiss();
		
		
		if(result!=null)
		{
			txtStatus.setText("Sucessfull creation");
		}
		else{
			txtStatus.setText("Error");
			
		}
		
		super.onPostExecute(result);
	}
}
