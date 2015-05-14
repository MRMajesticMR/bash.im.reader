package ru.majestic.bashimreader.activities;

import org.json.JSONException;
import org.json.JSONObject;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.ads.AdsDisableManager;
import ru.majestic.bashimreader.datebase.QuotesDatebaseManager;
import ru.majestic.bashimreader.managers.QuotesManager;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.utils.ApplicationUtils;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class SettingsActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private ScrollView 	rootLayout;
	private RelativeLayout 	mainHeaderLyt;
	private TextView 		mainHeaderTxt;
	private TextView 		h2TxtView, h2TxtCache, h2TxtAbout;
	private TextView 		fontSizeTxt, nightModeTxt, clearNewQuotesTxt, clearLikedQuotesTxt, versionTxt, authorTxt, disableAdsTxt;
	private TextView 		versionValueTxt, authorValueTxt;

	private Button 			backBtn;
	private Button 			clearNewQuotesBtn, clearLikedQuotesBtn;
	private Spinner 		fontSizeSpinner;
	private Button 			toggleNightModeBtn;
	private Button			disableAdsBtn;

	private QuotesManager 			quotesManager;
	private ApplicationSettings 	applicationSettings;
	private AdsDisableManager		adsDisableManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		quotesManager 			= new QuotesManager(savedInstanceState, this);
		applicationSettings 	= new ApplicationSettings(this);
		adsDisableManager		= new AdsDisableManager(this);
		adsDisableManager.init();
				
		initGUI();
		versionValueTxt.setText(ApplicationUtils.getApplicationVersionName(this));
	}	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		adsDisableManager.deinit();
	}

	private void initGUI() {
		setContentView(R.layout.activity_settings);

		rootLayout 				= (ScrollView) findViewById(R.id.settings_root_layout);
		mainHeaderLyt 			= (RelativeLayout) findViewById(R.id.settings_lyt_main_header);
		mainHeaderTxt 			= (TextView) findViewById(R.id.settings_lyt_main_header_text);
		h2TxtView 				= (TextView) findViewById(R.id.settings_txt_h2_view);
		h2TxtCache 				= (TextView) findViewById(R.id.settings_txt_h2_cache);
		h2TxtAbout 				= (TextView) findViewById(R.id.settings_txt_h2_about);
		fontSizeTxt 			= (TextView) findViewById(R.id.settings_txt_font_size);
		nightModeTxt 			= (TextView) findViewById(R.id.settings_txt_night_mode);
		clearNewQuotesTxt 		= (TextView) findViewById(R.id.settings_txt_clear_new_quotes);
		clearLikedQuotesTxt 	= (TextView) findViewById(R.id.settings_txt_clear_liked_quotes);
		versionTxt 				= (TextView) findViewById(R.id.settings_txt_version);
		authorTxt 				= (TextView) findViewById(R.id.settings_txt_author);
		versionValueTxt 		= (TextView) findViewById(R.id.settings_txt_version_value);
		authorValueTxt 			= (TextView) findViewById(R.id.settings_txt_author_value);
		disableAdsTxt			= (TextView) findViewById(R.id.settings_txt_disable_ads);

		backBtn 				= (Button) findViewById(R.id.settings_btn_back);
		clearNewQuotesBtn 		= (Button) findViewById(R.id.settings_btn_cache_clear_new_quotes);
		clearLikedQuotesBtn 	= (Button) findViewById(R.id.settings_btn_cache_clear_liked_quotes);
		fontSizeSpinner 		= (Spinner) findViewById(R.id.settings_spinner_font_size);
		toggleNightModeBtn 		= (Button) findViewById(R.id.settings_btn_toggle_night_mode);
		disableAdsBtn			= (Button) findViewById(R.id.settings_btn_disable_ads);

		backBtn.setOnClickListener(this);
		clearNewQuotesBtn.setOnClickListener(this);
		clearLikedQuotesBtn.setOnClickListener(this);
		fontSizeSpinner.setSelection(applicationSettings.getQuotesTextArrayIndex());
		fontSizeSpinner.setOnItemSelectedListener(this);
		toggleNightModeBtn.setOnClickListener(this);
		disableAdsBtn.setOnClickListener(this);

		refreshNightModeBtnText();
		initNightMode();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
		}
		return true;
	}

	private void exit() {
		finish();
		overridePendingTransition(R.anim.animation_activity_exit_out, R.anim.animation_activity_exit_in);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_btn_back:
			exit();
			break;
			
		case R.id.settings_btn_cache_clear_new_quotes:
			quotesManager.getQuotesDatebaseManager().deleteCache(QuotesDatebaseManager.Type.NEW);
			Toast.makeText(this, "Новые цитаты удалены из кэша", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.settings_btn_cache_clear_liked_quotes:
			quotesManager.getQuotesDatebaseManager().deleteCache(QuotesDatebaseManager.Type.LIKED);
			Toast.makeText(this, "Понравившиеся цитаты удалены из кэша", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.settings_btn_toggle_night_mode:
			applicationSettings.setNightMode(!applicationSettings.isNightModeEnabled());
			refreshNightModeBtnText();
			initNightMode();
			break;
			
		case R.id.settings_btn_disable_ads:
			PendingIntent disableAdsPengingIntent = adsDisableManager.getPendingIntentForDisableAds();
			try {
				startIntentSenderForResult(disableAdsPengingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
			} catch (SendIntentException e) {
				Log.e("ADS_DISABLER", e.toString());
			}
			break;
		}
	}

	private void initNightMode() {
		if (applicationSettings.isNightModeEnabled()) {
			mainHeaderTxt.setTextColor(getResources().getColor(R.color.night_mode_h1_text));
			h2TxtView.setTextColor(getResources().getColor(R.color.night_mode_h2_text));
			h2TxtCache.setTextColor(getResources().getColor(R.color.night_mode_h2_text));
			h2TxtAbout.setTextColor(getResources().getColor(R.color.night_mode_h2_text));
			fontSizeTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			nightModeTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			clearNewQuotesTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			clearLikedQuotesTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			versionTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			authorTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			versionValueTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			authorValueTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
			disableAdsTxt.setTextColor(getResources().getColor(R.color.night_mode_text));

			rootLayout.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
			mainHeaderLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_h1_background));
			mainHeaderTxt.setBackgroundColor(getResources().getColor(R.color.night_mode_h1_background));
			h2TxtView.setBackgroundColor(getResources().getColor(R.color.night_mode_h2_background));
			h2TxtCache.setBackgroundColor(getResources().getColor(R.color.night_mode_h2_background));
			h2TxtAbout.setBackgroundColor(getResources().getColor(R.color.night_mode_h2_background));
		} else {
			backBtn.setTextColor(getResources().getColor(R.color.light_mode_text));
			clearNewQuotesBtn.setTextColor(getResources().getColor(R.color.light_mode_text));
			clearLikedQuotesBtn.setTextColor(getResources().getColor(R.color.light_mode_text));
			toggleNightModeBtn.setTextColor(getResources().getColor(R.color.light_mode_text));
			mainHeaderTxt.setTextColor(getResources().getColor(R.color.light_mode_h1_text));
			h2TxtView.setTextColor(getResources().getColor(R.color.light_mode_h2_text));
			h2TxtCache.setTextColor(getResources().getColor(R.color.light_mode_h2_text));
			h2TxtAbout.setTextColor(getResources().getColor(R.color.light_mode_h2_text));
			fontSizeTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			nightModeTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			clearNewQuotesTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			clearLikedQuotesTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			versionTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			authorTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			versionValueTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			authorValueTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
			disableAdsTxt.setTextColor(getResources().getColor(R.color.light_mode_text));

			rootLayout.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
			mainHeaderLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_h1_background));
			mainHeaderTxt.setBackgroundColor(getResources().getColor(R.color.light_mode_h1_background));
			h2TxtView.setBackgroundColor(getResources().getColor(R.color.light_mode_h2_background));
			h2TxtCache.setBackgroundColor(getResources().getColor(R.color.light_mode_h2_background));
			h2TxtAbout.setBackgroundColor(getResources().getColor(R.color.light_mode_h2_background));
		}

	}

	private void refreshNightModeBtnText() {
		if (applicationSettings.isNightModeEnabled())
			toggleNightModeBtn.setText("Выключить");
		else
			toggleNightModeBtn.setText("Включить");
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		FlurryAgent.onStartSession(this);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		FlurryAgent.onEndSession(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		applicationSettings.setQuotesTextSizeArrayIndex(arg2);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1001) {
			int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
			String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
			String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

			if (resultCode == RESULT_OK) {
				try {
					JSONObject jo = new JSONObject(purchaseData);
					String sku = jo.getString("productId");
					Log.i("ADS_DISABLER", "You bought: " + sku);
				} catch (JSONException e) {
					Log.e("ADS_DISABLER", "Error: " + e.toString());
				}
			}
		}
	}

}
