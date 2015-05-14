package ru.majestic.bashimreader.preference;

import ru.majestic.bashimreader.R;
import android.content.Context;

public class ApplicationSettings {

	private static final String BASH_IM_READER_SETTINGS_FILE_TITLE = "BASH_IM_READER_SETTINGS_FILE_TITLE";

	private static final String QUOTES_TEXT_SIZE 		= "QUOTES_TEXT_SIZE";
	private static final String NIGHT_MODE 				= "NIGHT_MODE";
	private static final String IS_USER_VOTE 			= "IS_USER_VOTE";
	private static final String IS_ADS_DISABLE_SHOWED	= "IS_ADS_DISABLE_SHOWED";	

	private static final int DEFAULT_QUOTES_TEXT_SIZE_INDEX = 3;

	private final Context mContext;

	public ApplicationSettings(Context pContext) {
		this.mContext = pContext;
	}

	public final int getQuotesTextSize() {
		final String[] sizes = mContext.getResources().getStringArray(R.array.font_sizes);		
		return Integer.valueOf(sizes[getQuotesTextArrayIndex()]);
	}
	
	public final int getQuotesTextArrayIndex() {
		return mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).getInt(QUOTES_TEXT_SIZE, DEFAULT_QUOTES_TEXT_SIZE_INDEX);
	}

	public final void setQuotesTextSizeArrayIndex(int arrayIndex) {
		mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).edit().putInt(QUOTES_TEXT_SIZE, arrayIndex).commit();
	}

	public final void setNightMode(boolean value) {
		mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).edit().putBoolean(NIGHT_MODE, value).commit();
	}

	public final boolean isNightModeEnabled() {
		return mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).getBoolean(NIGHT_MODE, false);
	}
	
	public final boolean isUserAlreadyVoteOnMarket() {
		return mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).getBoolean(IS_USER_VOTE, false);
	}
	
	public final void userVoted() {
		mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).edit().putBoolean(IS_USER_VOTE, true).commit();
	}
	
	public final boolean isDisableAdsDialogShowed() {
		return mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).getBoolean(IS_ADS_DISABLE_SHOWED, false);
	}
	
	public final void disableAdsDialogShowed() {
		mContext.getSharedPreferences(BASH_IM_READER_SETTINGS_FILE_TITLE, Context.MODE_PRIVATE).edit().putBoolean(IS_ADS_DISABLE_SHOWED, true).commit();
	}

}
