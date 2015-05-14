package ru.majestic.bashimreader.application;

import ru.majestic.bashimreader.billing.GoogleBillingManager;
import ru.majestic.bashimreader.utils.ApplicationUtils;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;

import com.flurry.android.FlurryAgent;

public class BashApplication extends Application {
	
	private static final String FLURRY_KEY = "ZRDKWT9FJFGB8W6DNFPV";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		FlurryAgent.setLogEnabled(false);
		
		String googleAccount 	= getGoogleAccount();
		String programmVersion 	= ApplicationUtils.getApplicationVersionName(this);
		
		if(googleAccount != null)
			FlurryAgent.setUserId(googleAccount);
		else
			FlurryAgent.setUserId("unknown");
		
		if(programmVersion != null) 
			FlurryAgent.setVersionName(programmVersion);
		else
			FlurryAgent.setVersionName("unknown");
		
		FlurryAgent.init(this, FLURRY_KEY);
		
		GoogleBillingManager.getInstance().init(this);
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		
		GoogleBillingManager.getInstance().deinit();
	}

	
	private String getGoogleAccount() {
		final AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
		final Account[] list = manager.getAccounts();

		for(Account account: list)
		{
		    if(account.type.equalsIgnoreCase("com.google"))
		    {
		        return account.name;
		    }
		}
		
		return null;
	}
}
