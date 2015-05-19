package ru.majestic.bashimreader.ads;

import ru.majestic.bashimreader.billing.GoogleBillingManager;
import android.app.Activity;
import android.os.Bundle;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.StartAppAd.AdMode;

public class StartAppAdManager implements AdEventListener {

	private static final String PUBLISHER_ID 	= "104729922";
	private static final String APPLICATION_ID	= "204649827";
	
	private StartAppAd 	startAppAd;
	private boolean		showFullScreenAdWhenReady;
	
	private Activity 	activity;
	private Bundle		savedInstanceState;
	
	public StartAppAdManager(Activity activity, Bundle savedInstanceState) {
		this.activity 			= activity;
		this.savedInstanceState	= savedInstanceState;
		
		StartAppSDK.init(activity, PUBLISHER_ID, APPLICATION_ID, false);
		
		showFullScreenAdWhenReady = false;
		
		startAppAd = new StartAppAd(activity);
		startAppAd.loadAd(AdMode.OFFERWALL, this);
	}
	
	public void showSplashAd() {
		if(!GoogleBillingManager.getInstance().isAdsDisabled()) {
			StartAppAd.showSplash(activity, savedInstanceState);
		}
	}
	
	public void showFullScreenAd() {
		if(!GoogleBillingManager.getInstance().isAdsDisabled()) {
			if(startAppAd.isReady()) {
				showAndLoadFullScreenAd();
			} else {
				showFullScreenAdWhenReady = true;
			}
		}
	}

	@Override
	public void onFailedToReceiveAd(Ad ad) {
		startAppAd.loadAd(AdMode.OFFERWALL, this);
	}

	@Override
	public void onReceiveAd(Ad ad) {
		if(showFullScreenAdWhenReady) {
			showAndLoadFullScreenAd();
			showFullScreenAdWhenReady = false;
		}
	}
	
	private void showAndLoadFullScreenAd() {
		startAppAd.showAd();
		startAppAd.loadAd(AdMode.OFFERWALL, this);
	}
	
}
