package ua.dubtor.RetweetsMe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SQLiteAdapter {
	
	private static final int	DB_VERSION = 1;
	private static final String DB_NAME = "retweets_store";
	
	public static final String 	TABLE_RETWEETS = "retweets";
	
	public static final String KEY_ID = "_id";
	public static final String KEY_AUTHOR = "author_name";
	public static final String KEY_TEXT = "text";
	public static final String KEY_CREATED_AT = "created_at";
	public static final String KEY_RETWEET_ID = "rerweet_id";//id of retweet given by twitter.com
	
	
	 private final String[] allColumns = {KEY_ID,KEY_AUTHOR, KEY_TEXT, KEY_CREATED_AT, KEY_RETWEET_ID};
	 
	private static final String CREATE_TABLE = "create table " + TABLE_RETWEETS + " ("+ KEY_ID+" integer primary key autoincrement, "
		+ KEY_AUTHOR + " TEXT, "+ KEY_TEXT + " TEXT, "+ KEY_CREATED_AT + " TEXT, " + KEY_RETWEET_ID + " TEXT)";
	
	 private SQLiteHelper sqLiteHelper;
	 private SQLiteDatabase sqLiteDatabase;
	
	 private Context context;
	 
	 public SQLiteAdapter(Context c){
	  context = c;
	 }
	 
	 public SQLiteAdapter openToRead() throws android.database.SQLException {
	  sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
	  sqLiteDatabase = sqLiteHelper.getReadableDatabase();
	  return this; 
	 }
	 
	 public SQLiteAdapter openToWrite(){
	  try{
		  sqLiteHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		  sqLiteDatabase = sqLiteHelper.getWritableDatabase();
	  }catch(SQLException e){
		  sqLiteDatabase.execSQL(CREATE_TABLE);
	  }
	  return this; 
	 }
	 
	 public void close(){
	  sqLiteHelper.close();
	 }
	 
	 public long insert(Retweet retweet){
		 ContentValues values = new ContentValues();
		 values.put(KEY_AUTHOR, retweet.getAuthorName());
		 values.put(KEY_TEXT, retweet.getText());
		 values.put(KEY_CREATED_AT, retweet.getCreatedAt());
		 values.put(KEY_RETWEET_ID, retweet.getId());
		 return sqLiteDatabase.insert(TABLE_RETWEETS, null, values);
	 }
	 
	 public int deleteAll(){
	  return sqLiteDatabase.delete(TABLE_RETWEETS, null, null);
	 }
	 public void clear(){
		 sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_RETWEETS);
	 }
	 public Cursor queueAll(){
		 Cursor cursor = sqLiteDatabase.query(TABLE_RETWEETS, allColumns,
				 							  null, null, null, null, null);
		 return cursor;
	 }
	 
	 public class SQLiteHelper extends SQLiteOpenHelper {
	
	  public SQLiteHelper(Context context, String name,
	    CursorFactory factory, int version) {
	   super(context, name, factory, version);
	  }
	
	  @Override
	  public void onCreate(SQLiteDatabase db) {
	   // TODO Auto-generated method stub
	   db.execSQL(CREATE_TABLE);
	  }
	
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	   // TODO Auto-generated method stub
	  }
	 } 
}