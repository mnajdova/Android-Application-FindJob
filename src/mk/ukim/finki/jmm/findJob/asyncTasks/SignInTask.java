package mk.ukim.finki.jmm.findJob.asyncTasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mk.ukim.finki.jmm.findJob.EmployeeActivity;
import mk.ukim.finki.jmm.findJob.EmployeerActivity;
import mk.ukim.finki.jmm.findJob.model.User;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class SignInTask extends AsyncTask<String, Void, User> {

	private ProgressDialog loadingDialog;
	private Context context;
	private User userr;
	private TextView txtStatus;
	
	public SignInTask(Context context, User user, TextView txtStatus) {
		this.context=context;
		this.userr = user;
		this.txtStatus = txtStatus;
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
	protected User doInBackground(String... params) {
		User result = null;
		if (params.length == 1) {
			String url = params[0];
			result = loadData(url);
		}
		return result;
	}
	
	private User loadData(String url) {
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
		        nameValuePairs.add(new BasicNameValuePair("user.username", userr.username));
		        nameValuePairs.add(new BasicNameValuePair("user.password", userr.password));
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
	
	public User parseJson(String content) throws JSONException {
		User user = new User();
		if(content!=""){
	
		JSONObject jObj = new JSONObject(content);
		
    	user.id = jObj.getLong("id");
		user.name = jObj.getString("name");
		user.password = jObj.getString("password");
		user.username = jObj.getString("username");
		user.phone = jObj.getString("phone");
		user.role = jObj.getInt("role");
		user.CVLink = jObj.getString("CVLink");
		user.email = jObj.getString("email");
		
		return user;
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(User result) {
		
		//mAdapter.addAll(result);
		loadingDialog.dismiss();
		
		
		if(result!=null)
		{
			Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		     Gson gson = new Gson();
		     String json = gson.toJson(result);
		     editor.putString("USER", json);
		     editor.commit();
			
			if(result.role==1){
				context.startActivity(new Intent(context, EmployeerActivity.class));
			}
			else{
				context.startActivity(new Intent(context, EmployeeActivity.class));
			}
		}
		else{
			txtStatus.setText("Invalid username or password");
			
		}
		
		super.onPostExecute(result);
	}
}
