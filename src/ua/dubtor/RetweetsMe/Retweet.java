package ua.dubtor.RetweetsMe;

import org.json.JSONException;
import org.json.JSONObject;

public class Retweet {
	private String authorName;
	private String text;
	private String createdAt;
	private String id;
	
	//define default constructor
	public Retweet(){}
	
	//construct tweet from JSONObject 
	public Retweet(JSONObject json) throws JSONException
	{
		//parse name of people who posts this tweet
		authorName=json.getJSONObject("retweeted_status").getJSONObject("user").getString("name");
		//parse tweet text
		text=json.getJSONObject("retweeted_status").getString("text");
    	//get date:time of retweeting
    	createdAt=String.copyValueOf(json.getString("created_at").toCharArray(), 0, 20);
    	//get id
    	id=json.getString("id");
    	
	}
	
	//define properties get-set
	public String getAuthorName(){
		return authorName;
	}
	public void setAuthorName(String mAuthorName){
		this.authorName=mAuthorName;
	}
	
	public String getText(){
		return text;
	}
	public void setText(String mText){
		this.text=mText;
	}

	public String getCreatedAt(){
		return createdAt;
	}
	public void setCreatedAt(String mCreatedAt){
		this.createdAt=mCreatedAt;
		}

	public String getId(){
		return id;
	}
	public void setId(String mId){
		this.id=mId;
	}

}
