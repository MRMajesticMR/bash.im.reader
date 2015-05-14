package ru.majestic.bashimreader.ads;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

public class AdsDisableManager implements ServiceConnection {

	private static final int BILLING_API_VERSION = 3;
	
	private static final String DISABLE_ADS_ID = "disable_ads";

	private IInAppBillingService 	billingService;
	private Context					context;
	
	public AdsDisableManager(Context context) {
		this.context = context;
	}
	
	public void init() {
		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
	}
	
	public void deinit() {
		if(billingService != null)
			context.unbindService(this);
	}

	public PendingIntent getPendingIntentForDisableAds() {
		try {
			Bundle buyIntentBundle = billingService.getBuyIntent(3, context.getPackageName(), DISABLE_ADS_ID, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
			int response = buyIntentBundle.getInt("RESPONSE_CODE");
			if (response == 0) {
				return buyIntentBundle.getParcelable("BUY_INTENT");				
			} else {
				Log.e("ADS_DISABLER", "Error: response not 0. Response: " + response);
				return null;
			}			
		} catch (RemoteException e) {
			Log.e("ADS_DISABLER", "Error: " + e.toString());
			return null;
		}
	}

	public boolean isAdsDisabled() {
		try {
			Bundle ownedItems = billingService.getPurchases(3, context.getPackageName(), "inapp", null);
			int response = ownedItems.getInt("RESPONSE_CODE");
			if (response == 0) {
			   ArrayList<String> ownedSkus = ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");			   
			   return ownedSkus.contains(DISABLE_ADS_ID);			   			   
			} else {
				Log.e("ADS_DISABLER", "Error: response not 0. Response: " + response);
				return false;
			}
		} catch (RemoteException e) {
			Log.e("ADS_DISABLER", "Error: " + e.toString());
			return false;
		}
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		billingService = IInAppBillingService.Stub.asInterface(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		billingService = null;
	}

}
