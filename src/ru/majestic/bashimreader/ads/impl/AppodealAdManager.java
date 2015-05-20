package ru.majestic.bashimreader.ads.impl;

import android.app.Activity;

import com.appodeal.ads.Appodeal;

import ru.majestic.bashimreader.ads.IAdManager;
import ru.majestic.bashimreader.billing.GoogleBillingManager;

public class AppodealAdManager implements IAdManager {

	private static final String APP_KEY = "015f2702a7f55e95e9cb5a5f228666df27191b62700fdd6b";
	
	private Activity activity;
	
	public AppodealAdManager(Activity activity) {
		this.activity = activity;
	}
	
	@Override
	public void init() {
		Appodeal.initialize(activity, APP_KEY);		
	}

	@Override
	public void showBanner() {
		if(!GoogleBillingManager.getInstance().isAdsDisabled())
			Appodeal.show(activity, Appodeal.BANNER_BOTTOM);		
	}

}
