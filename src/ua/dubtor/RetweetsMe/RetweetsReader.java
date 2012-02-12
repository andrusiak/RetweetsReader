package ua.dubtor.RetweetsMe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuthConsumer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

public class RetweetsReader /*extends AsyncTask<> */{
	
	 private List<Retweet> list = new ArrayList<Retweet>();
	 
	 private OAuthConsumer consumer;
	 private String username;
	 
	 public static final String REQUEST_URL = "https://api.twitter.com/1/statuses/retweeted_by_user.json?screen_name=";
	 public static final String VERIFY_CREDENTIAL= "http://api.twitter.com/1/account/verify_credentials.json";

	 public static final int TWEETS_COUNT=50;
	 public static int page = 1;
	 
	 boolean auth_status;


	 
     public RetweetsReader()
	 {
	 }
	 
     public void Execute(){
    	 try {
    		 
			connect(getURL());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
     }
	 public  void connect(String url) 
	    {
		 	try {
		 	// create an HTTP request to a protected resource
	        HttpGet request = new HttpGet(url);

	        // sign the request
	        consumer.sign(request);
	        
	        // send the request
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpResponse response = httpClient.execute(request);
	        
			Log.i("Authentication Response",response.toString());

            HttpEntity entity = response.getEntity();
            if (entity != null) {
            	// A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                instream.close();
                
                Log.i("JSON Downloaded",result);
                
                JSONArray retweetsArray = new JSONArray(result);
        
                //transfer JSON response into list of retweets
                buildList(retweetsArray);
              }
		 	} catch (Exception e) {
	            e.printStackTrace(); } 
	    }	 
	
	 public boolean isAuthenticated() {
		auth_status=false;
		try{
		 	// create an HTTP request to a protected resource
	        HttpGet request = new HttpGet(VERIFY_CREDENTIAL);

	        // sign the request
	        consumer.sign(request);
	        
	        // send the request
	        HttpClient httpClient = new DefaultHttpClient();
	        HttpResponse response = httpClient.execute(request);

			if(response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK) 
				auth_status=true;
		  	} catch (Exception e) {
		 		e.printStackTrace();
		 	}
		 	Log.i("Verification Status", "Done!");

		//CheckAuthenticationTask authTask =new CheckAuthenticationTask();
		//authTask.execute();
		return auth_status;
	}
	 
	 private class CheckAuthenticationTask extends AsyncTask<Void, Void,Void> {

			/*public CheckAuthenticationTask() {
				progressBar= (ProgressBar)findViewById(R.id.progressBar1);	
		    }

			private ProgressBar progressBar ;	
		   
		    protected void onPreExecute() {
		    	progressBar.setVisibility(View.VISIBLE);
		    	}
		  
			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
			
				if (progressBar.isShown()) { 
					progressBar.setVisibility(View.GONE);
				}
			}
*/
			@Override
			protected Void doInBackground(Void... params) {
				try{
				 	// create an HTTP request to a protected resource
			        HttpGet request = new HttpGet(VERIFY_CREDENTIAL);

			        // sign the request
			        consumer.sign(request);
			        
			        // send the request
			        HttpClient httpClient = new DefaultHttpClient();
			        HttpResponse response = httpClient.execute(request);

					if(response.getStatusLine().getStatusCode()==HttpURLConnection.HTTP_OK) 
						auth_status=true;
				  	} catch (Exception e) {
				 		e.printStackTrace();
				 	}
				 	Log.i("Verification Status", "Done!");

				return null;
			}
		}

	public void buildList(JSONArray retweetsArray){
		  try{
			  JSONObject retweetJSON;
			 for(int i=0;i<retweetsArray.length();i++)
	         {			
	         	retweetJSON = retweetsArray.getJSONObject(i);
	         	if(retweetJSON!=null)
	         	{
	         		Retweet mRetweet = new Retweet(retweetJSON);
	         		Log.i("Retweet #"+i, mRetweet.getText());
	         		list.add(mRetweet);
	         	}
	         }
		 }
		 catch(Exception ex){}
	 }
	 
	 
	 public static String convertStreamToString(InputStream is) {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;
	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	
	 public List<Retweet> getList()
	 {
		 return this.list;
	 }
	 
	 public String getURL() 
	 {
		 	StringBuilder url= new StringBuilder(REQUEST_URL);
		 	url.append(this.username+"&count="+TWEETS_COUNT+"&include_entities=false"+"&page="+page);
			return url.toString();
	 }

	 public void setOAuthConsumer(OAuthConsumer mConsumer){
		 this.consumer=mConsumer;
	 }
	 
	 public void setUsername(String mUsername){
		 this.username=mUsername;
	 }
	 
	 public String mergeAllToString(){
		 StringBuilder merge=new StringBuilder();
		 for(Retweet current:list)
		 {
			 merge.append(current.getAuthorName()+"\n"+current.getText()+" " + "\n\n");
			 
		 }
		 return merge.toString();
		 
	 }

	
}
