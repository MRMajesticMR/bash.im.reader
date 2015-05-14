package ru.majestic.bashimreader.activities;

import com.flurry.android.FlurryAgent;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.dialogs.ExitApplicationAlertDialog;
import ru.majestic.bashimreader.flurry.utils.FlurryLogEventsDictionary;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.utils.EndAnimationListener;
import ru.majestic.bashimreader.utils.SocialUtils;
import ru.wapstart.plus1.sdk.Plus1BannerAsker;
import ru.wapstart.plus1.sdk.Plus1BannerView;
import ru.wapstart.plus1.sdk.Plus1Request;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainMenuActivity extends Activity implements OnClickListener {

	private Button 		citationsBtn;
	private Button 		comicsBtn;
	private Button 		settingsBtn;
	private Button 		exitBtn;
	
	private ViewGroup 	socialMenuView;
	private Button 		socialVKBtn;
	private Button 		socialTwitterBtn;
	private Button 		socialGoogleBtn;
	private Button 		social4pdaBtn;

	private ApplicationSettings applicationSettings;
	
	private ExitApplicationAlertDialog exitApplicationAlertDialog;
	
	private Plus1BannerAsker 	bannerAsker;
	private Plus1BannerView 	bannerView;

	private EndAnimationListener fadeInAnimationListener = new EndAnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			if (socialMenuView.getVisibility() == View.GONE) {
				socialMenuView.setVisibility(View.VISIBLE);
				socialMenuView.startAnimation(AnimationUtils.loadAnimation(MainMenuActivity.this, R.anim.social_menu_in));
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		applicationSettings = new ApplicationSettings(this);
		initGUI();
		initDialogs();
		startAnimation();
		initAds();
	}

	@Override
	public void onResume() {
		super.onResume();
		initNightMode();
	}

	private final void initGUI() {
		setContentView(R.layout.activity_main_menu);

		citationsBtn 			= (Button) findViewById(R.id.main_menu_btn_citations);
		comicsBtn 				= (Button) findViewById(R.id.main_menu_btn_comics);
		settingsBtn 			= (Button) findViewById(R.id.main_menu_btn_settings);
		exitBtn 				= (Button) findViewById(R.id.main_menu_btn_exit);

		socialMenuView 			= (ViewGroup) findViewById(R.id.main_menu_social_menu);
		socialVKBtn 			= (Button) findViewById(R.id.main_menu_btn_link_vk);
		socialTwitterBtn 		= (Button) findViewById(R.id.main_menu_btn_link_twitter);
		socialGoogleBtn 		= (Button) findViewById(R.id.main_menu_btn_link_google);
		social4pdaBtn 			= (Button) findViewById(R.id.main_menu_btn_link_4pda);

		citationsBtn.setOnClickListener(this);
		comicsBtn.setOnClickListener(this);
		settingsBtn.setOnClickListener(this);
		exitBtn.setOnClickListener(this);

		socialVKBtn.setOnClickListener(this);
		socialTwitterBtn.setOnClickListener(this);
		socialGoogleBtn.setOnClickListener(this);
		social4pdaBtn.setOnClickListener(this);
	}

	private final void initNightMode() {
		final FrameLayout mainLyt = (FrameLayout) findViewById(R.id.main_menu_lyt_main);
		if (applicationSettings.isNightModeEnabled()) {
			mainLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
			socialMenuView.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_social_view_background));
		} else {
			mainLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
			socialMenuView.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_social_view_background));
		}
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
	
	private void initDialogs() {
		exitApplicationAlertDialog = new ExitApplicationAlertDialog(this);
	}

	private final void startAnimation() {
		final Animation fadeInMenuAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_main_menu);
		fadeInMenuAnimation.setAnimationListener(fadeInAnimationListener);

		citationsBtn.startAnimation(fadeInMenuAnimation);
		comicsBtn.startAnimation(fadeInMenuAnimation);
		settingsBtn.startAnimation(fadeInMenuAnimation);
		exitBtn.startAnimation(fadeInMenuAnimation);
	}
	
	private void initAds() {
		bannerView = (Plus1BannerView) findViewById(R.id.main_menu_ad_view);	
		
		bannerAsker = new Plus1BannerAsker(new Plus1Request().setApplicationId(12736), bannerView).setCallbackUrl("wsp1bart://ru.majestic.bashimreader").setRefreshDelay(10);
		bannerAsker.refreshBanner();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_menu_btn_citations:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_QUOTE_BUTTON_PRESSED);
			startApplicationActivity(QuotesViewActivity.class);
			break;
			
		case R.id.main_menu_btn_comics:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_COMICS_BUTTON_PRESSED);
			startApplicationActivity(ComicsViewActivity.class);
			break;
			
		case R.id.main_menu_btn_settings:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_SETTINGS_BUTTON_PRESSED);
			startApplicationActivity(SettingsActivity.class);
			break;
			
		case R.id.main_menu_btn_exit:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_EXIT_BUTTON_PRESSED);
			exit();
			break;
			
		// Кнопки для доступа к социальным сетям.
		case R.id.main_menu_btn_link_vk:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_SOCIAL_VK_BTN_PRESSED);
			SocialUtils.openLink(this, SocialUtils.VK);			
			break;
			
		case R.id.main_menu_btn_link_twitter:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_SOCIAL_TWITTER_BTN_PRESSED);
			SocialUtils.openLink(this, SocialUtils.TWITTER);
			break;
			
		case R.id.main_menu_btn_link_google:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_SOCIAL_GP_BTN_PRESSED);
			SocialUtils.openLink(this, SocialUtils.GOOGLE);
			break;
			
		case R.id.main_menu_btn_link_4pda:
			FlurryAgent.logEvent(FlurryLogEventsDictionary.MAIN_MENU_SOCIAL_4PDA_BTN_PRESSED);
			SocialUtils.openLink(this, SocialUtils.PDA);
			break;
		}
	}

	private final void startApplicationActivity(final Class cls) {
		final Intent intent = new Intent(this, cls);
		startActivity(intent);
		overridePendingTransition(R.anim.animation_activity_in, R.anim.animation_activity_out);
	}

	private final void exit() {
		if (!applicationSettings.isUserAlreadyVoteOnMarket()) {
			exitApplicationAlertDialog.show();
		} else {
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			exit();
		}
		return true;
	}
}
