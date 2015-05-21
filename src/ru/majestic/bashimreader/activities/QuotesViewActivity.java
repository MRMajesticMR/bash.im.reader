package ru.majestic.bashimreader.activities;

import java.util.List;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.adapters.QuotesAdapter;
import ru.majestic.bashimreader.ads.IAdManager;
import ru.majestic.bashimreader.ads.impl.AppodealAdManager;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.flurry.utils.FlurryLogEventsDictionary;
import ru.majestic.bashimreader.managers.QuotesManager;
import ru.majestic.bashimreader.managers.listeners.CitationListener;
import ru.majestic.bashimreader.menu.QuotesMenu;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.quotes.sections.IQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.NewQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.listeners.OnNewQuotesReadyListener;
import ru.majestic.bashimreader.quotes.view.IQuoteListViewAdapter;
import ru.majestic.bashimreader.quotes.view.impl.QuoteListViewAdapter;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;

public class QuotesViewActivity extends Activity implements OnClickListener,
                                                            OnNewQuotesReadyListener,
                                                            OnNeedLoadMoreQuotesListener {   

   private TextView        downloadTxt;
   private ViewGroup       topMenuLyt, baseLyt;
   
   private Button          backBtn, reloadQuotesBtn, menuBtn, refreshBtn;
   private Button          quickMenuAbyssBestBtn, quickMenuNewQuotesBtn, quickMenuRandomQuotesBtn;
   private ViewGroup       reloadQuotesLyt;
   private QuotesMenu      quotesMenu;
   private TextView        listTitle;
   private ViewGroup       downloadQuotesView; 

   private ApplicationSettings applicationSettings;
   
   private IAdManager               adManager;
   private IQuotesSectionManager    quotesSectionManager;
   private IQuoteListViewAdapter    quoteListView;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      
      applicationSettings = new ApplicationSettings(this);
      
      initGUI(savedInstanceState);                              
      
      quoteListView = new QuoteListViewAdapter(this);       
      quoteListView.setOnNeedLoadMoreQuotesListener(this);
      
//    adManager = new AppodealAdManager(this);
//    adManager.init();
//    adManager.showBanner();
      
      quotesSectionManager = new NewQuotesSectionManager();
      quotesSectionManager.setOnNewQuotesReadyListener(this);
      quotesSectionManager.restoreState(savedInstanceState);
      if(quotesSectionManager.isNoQuotes())
         quotesSectionManager.loadNextPage();
            
   }
   

   private final void initGUI(final Bundle savedInstanceState) {
      setContentView(R.layout.activity_quotes_view);
      
      downloadTxt          = (TextView) findViewById(R.id.quote_view_download_txt);
      topMenuLyt           = (ViewGroup) findViewById(R.id.quotes_view_lyt_top_menu);
      baseLyt              = (ViewGroup) findViewById(R.id.quotes_view_base_lyt);            

      listTitle            = (TextView) findViewById(R.id.quotes_view_txt_title);
      backBtn              = (Button) findViewById(R.id.quotes_view_btn_back);
      reloadQuotesBtn      = (Button) findViewById(R.id.quotes_view_btn_reload);
      menuBtn              = (Button) findViewById(R.id.quotes_view_btn_menu);
      refreshBtn           = (Button) findViewById(R.id.quotes_view_btn_refresh);
      reloadQuotesLyt      = (ViewGroup) findViewById(R.id.quotes_lyt_reload_quotes);
      quotesMenu           = new QuotesMenu((ViewGroup) findViewById(R.id.quotes_view_lyt_menu), this, savedInstanceState);
      downloadQuotesView   = (ViewGroup) findViewById(R.id.quote_view_download_view);

      backBtn.setOnClickListener(this);
      reloadQuotesBtn.setOnClickListener(this);
      menuBtn.setOnClickListener(this);
      quotesMenu.setOnClickListener(this);
      refreshBtn.setOnClickListener(this);

      initQuickMenuGUI();
      initNightMode();
   }
   
   

   private final void initNightMode() {
      if (applicationSettings.isNightModeEnabled()) {
         downloadTxt.setTextColor(getResources().getColor(R.color.night_mode_text));
         listTitle.setTextColor(getResources().getColor(R.color.night_mode_text));         
         
         baseLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         reloadQuotesLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         topMenuLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quotes_title_background));
         downloadQuotesView.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_download_view_background));
         
         quickMenuAbyssBestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         quickMenuNewQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         if(quickMenuRandomQuotesBtn != null)
            quickMenuRandomQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
      } else {
         downloadTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
         listTitle.setTextColor(getResources().getColor(R.color.light_mode_text));
         
         baseLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         reloadQuotesLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         topMenuLyt.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quotes_title_background));
         downloadQuotesView.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_download_view_background));  
         
         quickMenuAbyssBestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         quickMenuNewQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         if(quickMenuRandomQuotesBtn != null)
            quickMenuRandomQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
      }
   }

   private final void initQuickMenuGUI() {
      quickMenuAbyssBestBtn = (Button) findViewById(R.id.quotes_view_quick_menu_btn_abyss_best);
      quickMenuNewQuotesBtn = (Button) findViewById(R.id.quotes_view_quick_menu_btn_new_quotes);
      quickMenuRandomQuotesBtn = (Button) findViewById(R.id.quotes_view_quick_menu_btn_random_quotes);

      quickMenuAbyssBestBtn.setOnClickListener(this);
      quickMenuNewQuotesBtn.setOnClickListener(this);
      if (quickMenuRandomQuotesBtn != null)
         quickMenuRandomQuotesBtn.setOnClickListener(this);
   }

//   private final void refreshListTitle() {
//      switch (quotesManager.getState()) {
//      case QuotesManager.STATE_BEST_QUOTES:
//         listTitle.setText("Лучшие");
//         break;
//      case QuotesManager.STATE_NEW_QUOTES:
//         listTitle.setText("Новые");
//         break;
//      case QuotesManager.STATE_RANDOM_QUOTES:
//         listTitle.setText("Случайные");
//         break;
//      case QuotesManager.STATE_BY_RATING_QUOTES:
//         listTitle.setText("По рейтингу");
//         break;
//      case QuotesManager.STATE_LIKED_QUOTES:
//         listTitle.setText("Понравившиеся");
//         break;
//
//      case QuotesManager.STATE_ABYSS:
//         listTitle.setText("Бездна");
//         break;
//      case QuotesManager.STATE_ABYSS_TOP:
//         listTitle.setText("Топ бездны");
//         break;
//      case QuotesManager.STATE_ABYSS_BEST:
//         listTitle.setText("Лучшее бездны");
//         break;
//      }
//   }      
   
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
         if (quotesMenu.isMenuVisible()) {
            quotesMenu.toggleMenu();
            return true;
         }

         exit();
      }
      return true;
   }

   private final void exit() {
      finish();
      overridePendingTransition(R.anim.animation_activity_exit_out, R.anim.animation_activity_exit_in);
   }

   @Override
   public void onClick(View v) {
      switch (v.getId()) {
      case R.id.quotes_view_btn_back:
    	 FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_BACK_BTN_PRESSED);
         exit();
         break;
         
      case R.id.quotes_view_btn_reload:
    	 FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_RELOAD_BTN_PRESSED);
         quotesSectionManager.loadNextPage();
         reloadQuotesLyt.setVisibility(View.GONE);
         break;
         
      case R.id.quotes_view_btn_menu:
    	 FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_VIEW_MENU_BTN_PRESSED);
         quotesMenu.toggleMenu();
         break;
         
      case R.id.quotes_view_btn_refresh:
    	 FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_REFRESH_BTN_PRESSED);
//         isNewList = true;
//         quotesManager.setFromCache(false);
//         quotesManager.clearList();
//         quotesManager.setState(quotesManager.getState());
//         quotesManager.loadCitations();
//         reloadQuotesLyt.setVisibility(View.GONE);
//         quotesListView.setVisibility(View.GONE);
         break;

      case R.id.quotes_view_menu_btn_new_quotes:
    	 FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_NEW_QUOTE_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.setFromCache(false);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_NEW_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();         
         break;
         
      case R.id.quotes_view_menu_btn_random_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_RANDOM_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_RANDOM_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;
         
      case R.id.quotes_view_menu_btn_best_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_BEST_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_BEST_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;
         
      case R.id.quotes_view_menu_btn_by_rating:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_BY_RATING_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_BY_RATING_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;
         
      case R.id.quotes_view_menu_btn_liked:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_LIKED_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_LIKED_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_abyss:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_ABYSS_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_ABYSS);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_abyss_top:
    	   FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_ABYSS_TOP_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_ABYSS_TOP);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;
         
      case R.id.quotes_view_menu_btn_abyss_best:
    	   FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_ABYSS_BEST_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_ABYSS_BEST);
//         quotesManager.loadCitations();
//         refreshListTitle();
//         quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_quick_menu_btn_abyss_best:
    	   FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_QM_ABYSS_BEST_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_ABYSS_BEST);
//         quotesManager.loadCitations();
//         refreshListTitle();
         break;
         
      case R.id.quotes_view_quick_menu_btn_new_quotes:
    	   FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_QM_NEW_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.setFromCache(false);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_NEW_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
         break;
         
      case R.id.quotes_view_quick_menu_btn_random_quotes:
    	   FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_QM_RANDOM_BTN_PRESSED);
//         isNewList = true;
//         quotesListView.setVisibility(View.GONE);
//         quotesManager.clearList();
//         quotesManager.setState(QuotesManager.STATE_RANDOM_QUOTES);
//         quotesManager.loadCitations();
//         refreshListTitle();
         break;
      }
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
//      quotesManager.saveState(outState);
      quotesMenu.saveState(outState);
      
      quotesSectionManager.saveState(outState);
   }

//   @Override
//   public void onCitationsReady() {
//      quotesAdapter.notifyDataSetChanged();
//      quotesListView.setVisibility(View.VISIBLE);
//      if (isNewList) {
//         quotesListView.setSelectionAfterHeaderView();
//         isNewList = false;
//      }
//   }

//   @Override
//   public void onCitationPrepareError() {
//      if (quotesManager.getState() == QuotesManager.STATE_LIKED_QUOTES) {
//         Toast.makeText(this, "Да Вам вообще ничего не нравится! Что Вы тут ожидали увидеть?", Toast.LENGTH_SHORT).show();
//      } else
//         reloadQuotesLyt.setVisibility(View.VISIBLE);
//   }

//   @Override
//   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//      if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + ITEMS_COUNT_BEFORE_LOAD_NEW_PAGE)) {
////         if (!quotesManager.isFromCache())
////            quotesManager.loadCitations();
//         
//         quotesSectionManager.loadNextPage();
//      }
//   }


   @Override
   public void onNewQuoteReady(List<Quote> quotes) {
      Log.i("NEW_QUOTES_MANAGER", "Quotes ready");
      for(Quote quote: quotes) {
         Log.i("NEW_QUOTES_MANAGER", quote.toJSONString());
      }
      
      quoteListView.addQuotes(quotes);
   }


   @Override
   public void onLoadNewQuotesError() {
      Log.i("NEW_QUOTES_MANAGER", "Quote load error");      
   }


   @Override
   public void onNeedLoadMoreQuotes() {
      if(!quotesSectionManager.isNewQuotesPreparing())
         quotesSectionManager.loadNextPage();
   }

}
