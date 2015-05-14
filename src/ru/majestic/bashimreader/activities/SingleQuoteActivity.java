package ru.majestic.bashimreader.activities;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.loaders.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.utils.ServerAnswerParser;
import ru.majestic.bashimreader.utils.Voter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class SingleQuoteActivity extends Activity implements OnPageLoadListener, OnClickListener, AnimationListener {

   private String mUrl;
   private Quote mQuote;
   
   private ViewGroup mTopMenuLyt;
   
   private TextView mActivityTitleTxt;
   private TextView mDownloadQuoteTxt;
   private TextView mQuoteTextTxt, mQuoteDateTxt, mQuoteRatingTxt, mQuoteIdTxt;
   private Button mBackBtn, mReloadQuoteBtn;
   private Button mUpQuoteRating, mDownQuoteRating;
   private ViewGroup mQuoteLyt, mReloadQuoteLyt, mDownloadQuoteLyt, mActivityRootLyt;

   private Animation mDownloadQuoteInAnimation, mDownloadQuoteOutAnimation;
   
   private ApplicationSettings mApplicationSettings;
   
   private OnPageLoadListener mVoteLoadListener = new OnPageLoadListener() {
      
      @Override
      public void onPageLoadError() {
         Toast.makeText(SingleQuoteActivity.this, "Опаньки! Почему-то голос не был отправлен...", Toast.LENGTH_SHORT).show();
         mUpQuoteRating.setVisibility(View.VISIBLE);
         mDownQuoteRating.setVisibility(View.VISIBLE);
      }
      
      @Override
      public void onPageLoad(String content) {
         Toast.makeText(SingleQuoteActivity.this, "^_^ Спасибо за голос! ^_^", Toast.LENGTH_SHORT).show();
      }
   };
   
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      mApplicationSettings = new ApplicationSettings(this);
      initAnimation();
      initGUI();
      initNightMode();
      initFontSizes();
      mUrl = getIntent().getData().toString();
      loadPage(mUrl);      
   }   
   
   private void loadPage(final String pUrl) {
      mDownloadQuoteLyt.setVisibility(View.VISIBLE);      
      mDownloadQuoteLyt.startAnimation(mDownloadQuoteInAnimation);
      mQuoteLyt.setVisibility(View.GONE);
      mReloadQuoteLyt.setVisibility(View.GONE);
      PageLoader pageLoader = new PageLoader();
      pageLoader.setOnPageLoadListener(this);
      pageLoader.execute(pUrl);
   }
   
   private void initAnimation() {
      mDownloadQuoteInAnimation =  AnimationUtils.loadAnimation(this, R.anim.download_view_in);
      mDownloadQuoteOutAnimation = AnimationUtils.loadAnimation(this, R.anim.download_view_out);
      mDownloadQuoteOutAnimation.setAnimationListener(this);
   }
   
   private void initGUI() {
      setContentView(R.layout.activity_single_quote);
      
      mActivityRootLyt = (ViewGroup) findViewById(R.id.quotes_view_base_lyt);
      mTopMenuLyt = (ViewGroup) findViewById(R.id.quotes_view_lyt_top_menu);
      
      mQuoteLyt = (ViewGroup) findViewById(R.id.quote_view_lyt_main);
      mReloadQuoteLyt = (ViewGroup) findViewById(R.id.quotes_lyt_reload_quotes);
      
      mDownloadQuoteLyt = (ViewGroup) findViewById(R.id.quote_view_download_view);
      mDownloadQuoteTxt = (TextView) findViewById(R.id.quote_view_download_txt);
      
      mQuoteTextTxt = (TextView) findViewById(R.id.quote_text);
      mQuoteDateTxt = (TextView) findViewById(R.id.quote_date);
      mQuoteRatingTxt = (TextView) findViewById(R.id.quote_rating);
      mQuoteIdTxt = (TextView) findViewById(R.id.quote_id);
      
      mActivityTitleTxt = (TextView) findViewById(R.id.quotes_view_txt_title);
      
      mBackBtn = (Button) findViewById(R.id.quotes_view_btn_back);            
      mBackBtn.setOnClickListener(this);
      
      mReloadQuoteBtn = (Button) findViewById(R.id.quotes_view_btn_reload);
      mReloadQuoteBtn.setOnClickListener(this);
      
      mUpQuoteRating = (Button) findViewById(R.id.quote_btn_rating_up);
      mDownQuoteRating = (Button) findViewById(R.id.quote_btn_rating_down);
      mUpQuoteRating.setOnClickListener(this);
      mDownQuoteRating.setOnClickListener(this);
   }
   
   private void initNightMode() {
      if(mApplicationSettings.isNightModeEnabled()) {         
         mActivityTitleTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
         mDownloadQuoteTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
         mQuoteDateTxt.setTextColor(getResources().getColor(R.color.night_mode_quote_date_text));         
         mQuoteRatingTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
         mQuoteTextTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
         mUpQuoteRating.setTextColor(getResources().getColor(R.color.night_mode_text));
         mDownQuoteRating.setTextColor(getResources().getColor(R.color.night_mode_text));
                  
         mActivityRootLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         mReloadQuoteLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         mTopMenuLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quotes_title_background));
         mDownloadQuoteLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_download_view_background));
         mQuoteDateTxt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         mQuoteRatingTxt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         mQuoteTextTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_background));
         mUpQuoteRating.setBackgroundColor(getResources().getColor(R.color.night_mode_rating_btn_background));
         mDownQuoteRating.setBackgroundColor(getResources().getColor(R.color.night_mode_rating_btn_background));
      } else {
         mActivityTitleTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
         mDownloadQuoteTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
         mQuoteDateTxt.setTextColor(getResources().getColor(R.color.light_mode_quote_date_text));
         mQuoteRatingTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
         mQuoteTextTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
         mUpQuoteRating.setTextColor(getResources().getColor(R.color.light_mode_text));
         mDownQuoteRating.setTextColor(getResources().getColor(R.color.light_mode_text));
         
         mActivityRootLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         mReloadQuoteLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         mTopMenuLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quotes_title_background));
         mDownloadQuoteLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_download_view_background));
         mQuoteDateTxt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         mQuoteRatingTxt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         mQuoteTextTxt.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quote_background));
         mUpQuoteRating.setBackgroundColor(getResources().getColor(R.color.light_mode_rating_btn_background));
         mDownQuoteRating.setBackgroundColor(getResources().getColor(R.color.light_mode_rating_btn_background));
      }
   }
   
   private void initFontSizes() {
      int baseFontSize = mApplicationSettings.getQuotesTextSize();

      mQuoteTextTxt.setTextSize(baseFontSize);
      mQuoteIdTxt.setTextSize(baseFontSize - 2);
      mQuoteDateTxt.setTextSize(baseFontSize - 2);
      mQuoteRatingTxt.setTextSize(baseFontSize - 2);
      mUpQuoteRating.setTextSize(baseFontSize - 2);
      mDownQuoteRating.setTextSize(baseFontSize - 2);
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
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
         exit();
      }
      return true;
   }

   @Override
   public void onPageLoad(String content) {
      mQuote = ServerAnswerParser.parseSingleQuotePage(content);
      displayQuote(mQuote);            
      mDownloadQuoteLyt.startAnimation(mDownloadQuoteOutAnimation);
   }
   
   private void displayQuote(final Quote pQuote) {
      mQuoteTextTxt.setText(Html.fromHtml(pQuote.getContent()));
      mQuoteDateTxt.setText(pQuote.getDateString());
      mQuoteRatingTxt.setText(String.valueOf(pQuote.getRating()));
      mQuoteIdTxt.setText("#" + pQuote.getId());
      
      mActivityTitleTxt.setText("Цитата #" + pQuote.getId());
      
      mQuoteLyt.setVisibility(View.VISIBLE);
   }

   @Override
   public void onPageLoadError() {
      mReloadQuoteLyt.setVisibility(View.VISIBLE);
      mDownloadQuoteLyt.startAnimation(mDownloadQuoteOutAnimation);
   }      
   
   private void exit() {
      Intent intent = new Intent(this, QuotesViewActivity.class);
      startActivity(intent);
      finish();
      overridePendingTransition(R.anim.animation_activity_exit_out, R.anim.animation_activity_exit_in);
   }

   @Override
   public void onClick(View v) {
      switch(v.getId()) {
      case R.id.quotes_view_btn_back:
         exit();
         break;
      case R.id.quotes_view_btn_reload:
         loadPage(mUrl);
         break;
      case R.id.quote_btn_rating_up:
         voteQuote(Voter.VOTE_RULEZ);
         break;
      case R.id.quote_btn_rating_down:
         voteQuote(Voter.VOTE_SUX);
         break;
      }
   }
   
   private void voteQuote(String voteType) {
      mUpQuoteRating.setVisibility(View.GONE);
      mDownQuoteRating.setVisibility(View.GONE);
      Voter.vote(this, mVoteLoadListener, mQuote.getId(), voteType);
   }

   @Override
   public void onAnimationEnd(Animation animation) {
      mDownloadQuoteLyt.setVisibility(View.GONE);
   }

   @Override
   public void onAnimationRepeat(Animation animation) {
   }

   @Override
   public void onAnimationStart(Animation animation) {
   }
   
   

}
