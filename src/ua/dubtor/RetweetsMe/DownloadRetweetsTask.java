package ua.dubtor.RetweetsMe;

import android.os.AsyncTask;
import android.widget.SimpleCursorAdapter;

public class DownloadRetweetsTask extends AsyncTask<Object, Void, Void> {
	
	SimpleCursorAdapter cursorAdapter;

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

	}

	public void setCursorAdapter(SimpleCursorAdapter cursorAdapter2) {
		this.cursorAdapter=	cursorAdapter2;
	}
}