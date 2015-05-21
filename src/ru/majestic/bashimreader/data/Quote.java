package ru.majestic.bashimreader.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Quote {   
   
   public static final int QUOTE_RATING_NEW_VALUE             = -99999;
   public static final int QUOTE_RATING_UNKNOWN_VALUE         = -99998;
   
   private static final String JSON_FIELD_ID       = "id";
   private static final String JSON_FIELD_RATING   = "rating";
   private static final String JSON_FIELD_CONTENT  = "content";
   private static final String JSON_FIELD_DATE     = "date";
   
	private final int         id;
	private final int         rating;
	private final String      content;
	private final String      dateString;

	public Quote(int id, int rating, String content, String dateString) {
		this.id            = id;
		this.rating        = rating;
		this.content       = content;
		this.dateString    = dateString;
	}
	
	public Quote(JSONObject savedQuote) throws JSONException {
	   this(savedQuote.getInt(JSON_FIELD_ID), savedQuote.getInt(JSON_FIELD_RATING), savedQuote.getString(JSON_FIELD_CONTENT), savedQuote.getString(JSON_FIELD_DATE));
	}

	public int getId() {
		return id;
	}

	public int getRating() {
		return rating;
	}

	public String getContent() {
		return content;
	}

	public String getDateString() {
		return dateString;
	}

	public String toJSONString() {
		return "{\"" + JSON_FIELD_ID + "\": " + id + ", \"" + JSON_FIELD_RATING + "\": " + rating + ", \"" + JSON_FIELD_CONTENT + "\": \"" + content + "\", \"" + JSON_FIELD_DATE + "\": \"" + dateString + "\"}";
	}

}
