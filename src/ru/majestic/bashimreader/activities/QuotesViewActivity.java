package ru.majestic.bashimreader.activities;

import java.util.List;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.ads.IAdManager;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.flurry.utils.FlurryLogEventsDictionary;
import ru.majestic.bashimreader.menu.QuotesMenu;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.quotes.sections.IQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.QuoteSectionManagersFactory;
import ru.majestic.bashimreader.quotes.sections.listeners.OnNewQuotesReadyListener;
import ru.majestic.bashimreader.quotes.view.IQuoteListViewAdapter;
import ru.majestic.bashimreader.quotes.view.impl.QuoteListView;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;
import ru.majestic.bashimreader.view.IDownloadStatusView;
import ru.majestic.bashimreader.view.ITopMenuView;
import ru.majestic.bashimreader.view.handlers.IViewsConfigHandler;
import ru.majestic.bashimreader.view.handlers.impl.ViewsConfigHandler;
import ru.majestic.bashimreader.view.impl.QuotesTopMenuView;
import ru.majestic.bashimreader.view.impl.TopSlideDownDownloadStatusView;
import ru.majestic.bashimreader.view.listeners.TopMenuStateListener;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

public class QuotesViewActivity extends Activity implements OnClickListener, 
                                                            OnNewQuotesReadyListener, 
                                                            OnNeedLoadMoreQuotesListener,
                                                            TopMenuStateListener {

   private TextView downloadTxt; 
   private ViewGroup baseLyt;
   private Button reloadQuotesBtn;
   private Button quickMenuAbyssBestBtn, quickMenuNewQuotesBtn, quickMenuRandomQuotesBtn;
   private ViewGroup reloadQuotesLyt;
   private QuotesMenu quotesMenu;

   private ApplicationSettings applicationSettings;

   private IAdManager adManager;
   private QuoteSectionManagersFactory quoteSectionManagersFactory;
   private IQuotesSectionManager quotesSectionManager;
   
   private IViewsConfigHandler viewConfigHandler;
   private IQuoteListViewAdapter quoteListView;
   private IDownloadStatusView downloadStatusView;
   private ITopMenuView topMenuView;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      applicationSettings = new ApplicationSettings(this);

      initGUI(savedInstanceState);

      quoteListView = new QuoteListView(this);
      quoteListView.setOnNeedLoadMoreQuotesListener(this);

      downloadStatusView = new TopSlideDownDownloadStatusView(this);
      
      topMenuView = new QuotesTopMenuView(this);
      topMenuView.setTopMenuStateListener(this);

      // adManager = new AppodealAdManager(this);
      // adManager.init();
      // adManager.showBanner();
      
      quoteSectionManagersFactory = new QuoteSectionManagersFactory();
      quoteSectionManagersFactory.restoreState(savedInstanceState);

      topMenuView.refreshSectionTitle(quoteSectionManagersFactory.getCurrentSectionType());
      quotesSectionManager = quoteSectionManagersFactory.generateQuotesSectionManger();
      quotesSectionManager.setOnNewQuotesReadyListener(this);
      quotesSectionManager.restoreState(savedInstanceState);
      
      viewConfigHandler = new ViewsConfigHandler();
      
      viewConfigHandler.addView(topMenuView);
      viewConfigHandler.addView(downloadStatusView);
      viewConfigHandler.addView(quoteListView);
      
      viewConfigHandler.enableNightMode(applicationSettings.isNightModeEnabled());
      viewConfigHandler.onFontSizeChanged(applicationSettings.getQuotesTextSize());

      if (quotesSectionManager.isNoQuotes()) {
         downloadStatusView.show();
         quotesSectionManager.loadNextPage();
      }

   }

   private final void initGUI(final Bundle savedInstanceState) {
      setContentView(R.layout.activity_quotes_view);

      downloadTxt = (TextView) findViewById(R.id.quote_view_download_txt);
      baseLyt = (ViewGroup) findViewById(R.id.quotes_view_base_lyt);
      reloadQuotesBtn = (Button) findViewById(R.id.quotes_view_btn_reload);
      reloadQuotesLyt = (ViewGroup) findViewById(R.id.quotes_lyt_reload_quotes);
      quotesMenu = new QuotesMenu((ViewGroup) findViewById(R.id.quotes_view_lyt_menu), this, savedInstanceState);

      reloadQuotesBtn.setOnClickListener(this);
      quotesMenu.setOnClickListener(this);

      initQuickMenuGUI();
      initNightMode();
   }

   private final void initNightMode() {
      if (applicationSettings.isNightModeEnabled()) {
         downloadTxt.setTextColor(getResources().getColor(R.color.night_mode_text));

         baseLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));
         reloadQuotesLyt.setBackgroundColor(getResources().getColor(R.color.night_mode_background));

         quickMenuAbyssBestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         quickMenuNewQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         if (quickMenuRandomQuotesBtn != null)
            quickMenuRandomQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
      } else {
         downloadTxt.setTextColor(getResources().getColor(R.color.light_mode_text));
         baseLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         reloadQuotesLyt.setBackgroundColor(getResources().getColor(R.color.light_mode_background));
         quickMenuAbyssBestBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         quickMenuNewQuotesBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         if (quickMenuRandomQuotesBtn != null)
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

   // private final void refreshListTitle() {
   // switch (quotesManager.getState()) {
   // case QuotesManager.STATE_BEST_QUOTES:
   // listTitle.setText("������");
   // break;
   // case QuotesManager.STATE_NEW_QUOTES:
   // listTitle.setText("�����");
   // break;
   // case QuotesManager.STATE_RANDOM_QUOTES:
   // listTitle.setText("���������");
   // break;
   // case QuotesManager.STATE_BY_RATING_QUOTES:
   // listTitle.setText("�� ��������");
   // break;
   // case QuotesManager.STATE_LIKED_QUOTES:
   // listTitle.setText("�������������");
   // break;
   //
   // case QuotesManager.STATE_ABYSS:
   // listTitle.setText("������");
   // break;
   // case QuotesManager.STATE_ABYSS_TOP:
   // listTitle.setText("��� ������");
   // break;
   // case QuotesManager.STATE_ABYSS_BEST:
   // listTitle.setText("������ ������");
   // break;
   // }
   // }

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

      case R.id.quotes_view_btn_reload:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_RELOAD_BTN_PRESSED);
         quotesSectionManager.loadNextPage();
         reloadQuotesLyt.setVisibility(View.GONE);
         break;

      case R.id.quotes_view_menu_btn_new_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_NEW_QUOTE_BTN_PRESSED);
         
         downloadStatusView.show();
         quoteListView.clear();
         quoteSectionManagersFactory.setCurrentSectionType(QuoteSectionManagersFactory.SECTION_TYPE_NEW);
         topMenuView.refreshSectionTitle(quoteSectionManagersFactory.getCurrentSectionType());         
         quotesSectionManager = quoteSectionManagersFactory.generateQuotesSectionManger();
         quotesSectionManager.loadNextPage();                  
         break;

      case R.id.quotes_view_menu_btn_random_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_RANDOM_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_RANDOM_QUOTES);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_best_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_BEST_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_BEST_QUOTES);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_by_rating:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_BY_RATING_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_BY_RATING_QUOTES);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_liked:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_LIKED_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_LIKED_QUOTES);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_abyss:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_ABYSS_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_ABYSS);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_abyss_top:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_ABYSS_TOP_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_ABYSS_TOP);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_abyss_best:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_ABYSS_BEST_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_ABYSS_BEST);
         // quotesManager.loadCitations();
         // refreshListTitle();
         // quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_quick_menu_btn_abyss_best:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_QM_ABYSS_BEST_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_ABYSS_BEST);
         // quotesManager.loadCitations();
         // refreshListTitle();
         break;

      case R.id.quotes_view_quick_menu_btn_new_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_QM_NEW_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.setFromCache(false);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_NEW_QUOTES);
         // quotesManager.loadCitations();
         // refreshListTitle();
         break;

      case R.id.quotes_view_quick_menu_btn_random_quotes:
         FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_QM_RANDOM_BTN_PRESSED);
         // isNewList = true;
         // quotesListView.setVisibility(View.GONE);
         // quotesManager.clearList();
         // quotesManager.setState(QuotesManager.STATE_RANDOM_QUOTES);
         // quotesManager.loadCitations();
         // refreshListTitle();
         break;
      }
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      quotesMenu.saveState(outState);
      

      quotesSectionManager.saveState(outState);
   }

   @Override
   public void onNewQuoteReady(List<Quote> quotes) {
      downloadStatusView.hide();
      quoteListView.addQuotes(quotes);
   }

   @Override
   public void onLoadNewQuotesError() {
      downloadStatusView.hide();
      reloadQuotesLyt.setVisibility(View.VISIBLE);
   }

   @Override
   public void onNeedLoadMoreQuotes() {
      if (!quotesSectionManager.isNewQuotesPreparing()) {
         downloadStatusView.show();
         quotesSectionManager.loadNextPage();
      }
   }

   @Override
   public void onBackButtonClicked() {
      FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_BACK_BTN_PRESSED);
      exit();
   }

   @Override
   public void onMenuButtonClicked() {
      FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_VIEW_MENU_BTN_PRESSED);
      quotesMenu.toggleMenu();      
   }

   @Override
   public void onRefreshButtonClicked() {
      FlurryAgent.logEvent(FlurryLogEventsDictionary.QUOTES_ACTIVITY_REFRESH_BTN_PRESSED);

      downloadStatusView.show();
      quoteListView.clear();
      quotesSectionManager.reset();
      quotesSectionManager.loadNextPage();      
   }

}
