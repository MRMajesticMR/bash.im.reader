package ru.majestic.bashimreader.billing;

import java.util.ArrayList;

import com.android.vending.billing.IInAppBillingService;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class GoogleBillingManager implements ServiceConnection{

	private static GoogleBillingManager instance;
	
	private static final String BIILING_SERVICE_INTENT 			= "com.android.vending.billing.InAppBillingService.BIND";
	private static final String BIILING_SERVICE_INTENT_PACKAGE 	= "com.android.vending";
	
	private static final String DISABLE_ADS_ID = "disable_ads";
	
	private Context context;
	private IInAppBillingService billingService;
	
	private GoogleBillingManager() {
		
	}
	
	public synchronized static GoogleBillingManager getInstance() {
		if(instance == null)
			instance = new GoogleBillingManager();
		return instance;
	}
	
	public void init(Context context) {
		this.context = context;
		
		final Intent serviceIntent = new Intent(BIILING_SERVICE_INTENT);
		serviceIntent.setPackage(BIILING_SERVICE_INTENT_PACKAGE);
		context.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
	}
	
	public void deinit() {
		if(billingService != null)
			context.unbindService(this);
	}
	
	public boolean isReady() {
		return billingService != null;
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
	
	public PendingIntent getPendingIntentForDisableAds() {
		try {
			Bundle buyIntentBundle = billingService.getBuyIntent(3, context.getPackageName(), DISABLE_ADS_ID, "inapp", String.valueOf(System.currentTimeMillis()));
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

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		billingService = IInAppBillingService.Stub.asInterface(service);
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		billingService = null;
		
	}
	
}
