package ua.dubtor.RetweetsMe;


import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class RetweetsListActivity extends ListActivity {
	
	private SQLiteAdapter mySQLiteAdapter;
	 
	SimpleCursorAdapter cursorAdapter;
	Cursor cursor;
	
	private SharedPreferences prefs;
	/*private final Handler mTwitterHandler = new Handler();
	private TextView loginStatus;
	
    final Runnable mUpdateTwitterNotification = new Runnable() {
        public void run() {
        	Toast.makeText(getBaseContext(), "Tweet sent !", Toast.LENGTH_LONG).show();}    };*/
    
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.retweets_list);
        

        mySQLiteAdapter = new SQLiteAdapter(this);
        mySQLiteAdapter.openToWrite();

        cursor = mySQLiteAdapter.queueAll();
        String[] from = new String[] { SQLiteAdapter.KEY_AUTHOR,
        							   SQLiteAdapter.KEY_TEXT,
        							   SQLiteAdapter.KEY_CREATED_AT};
        int[] to = new int[]{ R.id.retweet_author_name, 
        					  R.id.retweet_text,
        					  R.id.retweet_created_at };
        cursorAdapter =
         new SimpleCursorAdapter(this, R.layout.retweet_item, cursor, from, to);
        setListAdapter(cursorAdapter);
	    this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
	
	    Button getRetweets=(Button)findViewById(R.id.btn_get_retweets);
	   
	    getRetweets.setOnClickListener(new View.OnClickListener() {
    	@Override
    	public void onClick(View v) {
        		downloadRetweetsList();	 
        		}
    });
	}
   
	private class DownloadRetweetsTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... arg0) {
			RetweetsReader retweetsReader=(RetweetsReader) arg0[0];
			SQLiteAdapter mySQLiteAdapter = (SQLiteAdapter) arg0[1];
			
			retweetsReader.Execute();
			mySQLiteAdapter.deleteAll();
			for(Retweet retweet:retweetsReader.getList()){
				mySQLiteAdapter.insert(retweet);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			updateList();
		}

	}

	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    }
	
	 	 
	public void downloadRetweetsList() {
		//define work thread which will be connect to API
		
		EditText usernameField =(EditText)findViewById(R.id.username_field);
		String username=usernameField.getText().toString();
		if(username.equals(""))
		{
			Log.e("Input Status", "username is empty!");
    		usernameField.setBackgroundColor(Color.RED);
    		Toast.makeText(getBaseContext(), "Please enter username correctly! ", Toast.LENGTH_LONG).show();		
       	}
		else{
    		Log.i("Input Status", "All rights!");
			usernameField.setBackgroundColor(Color.WHITE);

			RetweetsReader retweetsReader= new RetweetsReader();
			//retweetsReader.setContext(getApplicationContext());
			retweetsReader.setOAuthConsumer(createOAuthConsumer());
			retweetsReader.setUsername(username);
			if(retweetsReader.isAuthenticated())
			{	
				DownloadRetweetsTask drTask= new DownloadRetweetsTask();
				drTask.execute(retweetsReader ,mySQLiteAdapter);
				/*retweetsReader.Execute();
				mySQLiteAdapter.deleteAll();
				for(Retweet retweet:retweetsReader.getList()){
					mySQLiteAdapter.insert(retweet);
				}*/
								
				//updateList();
			
			} else	{
				Intent i = new Intent(getApplicationContext(), PrepareRequestTokenActivity.class);
				startActivity(i);
			}
    	}
	}

    /*
	private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}*/
	
	public OAuthConsumer createOAuthConsumer()
	{
		//creating consumer for signing Http OAuth requests
		OAuthConsumer mConsumer;
		try{
		mConsumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY,Constants.CONSUMER_SECRET);
	    //read access token and secret from prefs   
        String oauth_token = prefs.getString(OAuth.OAUTH_TOKEN, "");
 		String oauth_token_secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
 		//Insert access token into consumer 
 		mConsumer.setTokenWithSecret(oauth_token, oauth_token_secret);
		}
		catch(Exception err)
		{
			mConsumer=null;
		}
		return mConsumer;
	}
	
	@Override
	 protected void onDestroy() {
	  // TODO Auto-generated method stub
	  super.onDestroy();
	  mySQLiteAdapter.close();
	 }
	
	@Override
	 protected void onResume() {
	  // TODO Auto-generated method stub
	  super.onResume();
	  updateList();
	  
	 }

	 public void updateList(){
	  cursor.requery();
	   }
	 
}

