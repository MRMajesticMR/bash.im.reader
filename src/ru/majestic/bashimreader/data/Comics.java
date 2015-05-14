package ru.majestic.bashimreader.data;

import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import ru.majestic.bashimreader.file.ComicsFileManager;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

public class Comics {

	private final String mTitle;
	private final String mAuthorName;
	private final String mFormat;
	private final int mQuoteNumber;
	private final String mDate;
	private Bitmap mImage;

	public Comics(final String pTitle, final String pAuthorName, final int pQuoteNumber, final String pFormat, final String pDate) {
		this.mTitle = pTitle;
		this.mAuthorName = pAuthorName;
		this.mQuoteNumber = pQuoteNumber;
		this.mFormat = pFormat;
		this.mDate = pDate;
	}	

	public Comics(final JSONObject pObject) throws JSONException {
		this.mTitle = pObject.getString("title");
		this.mAuthorName = pObject.getString("author_name");
		this.mQuoteNumber = pObject.getInt("quote_number");
		this.mFormat = pObject.getString("format");
		this.mDate = pObject.getString("date");
		try {
			this.mImage = ComicsFileManager.getPicture(this);
		} catch (FileNotFoundException e) {
		}
	}

	public void setImage(Bitmap pImage) {
		this.mImage = pImage;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getAuthorName() {
		return mAuthorName;
	}

	public int getQuoteNumber() {
		return mQuoteNumber;
	}

	public String getFormat() {
		return mFormat;
	}

	public Bitmap getImage() {
		return mImage;
	}

	public String getDate() {
		return mDate;
	}

	public final String toJSONObject() {
		return "{\"title\": \"" + mTitle + "\", \"author_name\": \"" + mAuthorName + "\", \"quote_number\": " + mQuoteNumber
				+ ", \"format\": \"" + mFormat + "\", \"date\": \"" + mDate + "\"}";
	}

	public Bitmap getStripImage() {
		final Bitmap stripBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

		final Paint p = new Paint();
		p.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		final Canvas c = new Canvas(stripBitmap);
		c.drawBitmap(mImage, -(mImage.getWidth() / 2 + 25), -(mImage.getHeight() / 2 + 25), null);

		return stripBitmap;
	}
	
	@Override
	public String toString() {
		return "Comics [mTitle=" + mTitle + ", mAuthorName=" + mAuthorName + ", mFormat=" + mFormat + ", mQuoteNumber=" + mQuoteNumber
				+ ", mDate=" + mDate + "]";
	}

}
