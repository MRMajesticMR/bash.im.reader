package ru.majestic.bashimreader.activities;

import com.flurry.android.FlurryAgent;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.adapters.QuotesAdapter;
import ru.majestic.bashimreader.managers.QuotesManager;
import ru.majestic.bashimreader.managers.listeners.CitationListener;
import ru.majestic.bashimreader.menu.QuotesMenu;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.wapstart.plus1.sdk.Plus1BannerAsker;
import ru.wapstart.plus1.sdk.Plus1BannerView;
import ru.wapstart.plus1.sdk.Plus1Request;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuotesViewActivity extends Activity implements OnClickListener, CitationListener, OnScrollListener {

   private static final int ITEMS_COUNT_BEFORE_LOAD_NEW_PAGE = 10;

   private TextView downloadTxt;
   private ViewGroup topMenuLyt, baseLyt;
   
   private Button backBtn, reloadQuotesBtn, menuBtn, refreshBtn;
   private Button quickMenuAbyssBestBtn, quickMenuNewQuotesBtn, quickMenuRandomQuotesBtn;
   private ListView quotesListView;
   private QuotesAdapter quotesAdapter;
   private ViewGroup reloadQuotesLyt;
   private QuotesMenu quotesMenu;
   private TextView listTitle;
   private ViewGroup downloadQuotesView;
    
   private QuotesManager quotesManager;   

   private ApplicationSettings applicationSettings;

   private boolean isNewList;
   
   private Plus1BannerAsker 	bannerAsker;
   private Plus1BannerView 		bannerView;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      quotesManager = new QuotesManager(savedInstanceState, this);
      quotesManager.setCitationListener(this);
      applicationSettings = new ApplicationSettings(this);
      initGUI(savedInstanceState);
      showQuotes(savedInstanceState);
      initAds();
      isNewList = false;
   }
   

   private final void initGUI(final Bundle savedInstanceState) {
      setContentView(R.layout.activity_quotes_view);
      
      downloadTxt = (TextView) findViewById(R.id.quote_view_download_txt);
      topMenuLyt = (ViewGroup) findViewById(R.id.quotes_view_lyt_top_menu);
      baseLyt = (ViewGroup) findViewById(R.id.quotes_view_base_lyt);            

      listTitle = (TextView) findViewById(R.id.quotes_view_txt_title);
      backBtn = (Button) findViewById(R.id.quotes_view_btn_back);
      reloadQuotesBtn = (Button) findViewById(R.id.quotes_view_btn_reload);
      menuBtn = (Button) findViewById(R.id.quotes_view_btn_menu);
      refreshBtn = (Button) findViewById(R.id.quotes_view_btn_refresh);
      quotesListView = (ListView) findViewById(R.id.quotes_view_list_view);
      reloadQuotesLyt = (ViewGroup) findViewById(R.id.quotes_lyt_reload_quotes);
      quotesAdapter = new QuotesAdapter(this, quotesManager);
      quotesMenu = new QuotesMenu((ViewGroup) findViewById(R.id.quotes_view_lyt_menu), this, savedInstanceState);
      downloadQuotesView = (ViewGroup) findViewById(R.id.quote_view_download_view);

      backBtn.setOnClickListener(this);
      reloadQuotesBtn.setOnClickListener(this);
      menuBtn.setOnClickListener(this);
      quotesManager.setDownloadingView(downloadQuotesView);
      quotesListView.setAdapter(quotesAdapter);
      quotesListView.setOnScrollListener(this);
      quotesMenu.setOnClickListener(this);
      refreshBtn.setOnClickListener(this);
      refreshListTitle();

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

   private final void refreshListTitle() {
      switch (quotesManager.getState()) {
      case QuotesManager.STATE_BEST_QUOTES:
         listTitle.setText("������");
         break;
      case QuotesManager.STATE_NEW_QUOTES:
         listTitle.setText("�����");
         break;
      case QuotesManager.STATE_RANDOM_QUOTES:
         listTitle.setText("���������");
         break;
      case QuotesManager.STATE_BY_RATING_QUOTES:
         listTitle.setText("�� ��������");
         break;
      case QuotesManager.STATE_LIKED_QUOTES:
         listTitle.setText("�������������");
         break;

      case QuotesManager.STATE_ABYSS:
         listTitle.setText("������");
         break;
      case QuotesManager.STATE_ABYSS_TOP:
         listTitle.setText("��� ������");
         break;
      case QuotesManager.STATE_ABYSS_BEST:
         listTitle.setText("������ ������");
         break;
      }
   }

   private final void showQuotes(Bundle savedInstanceState) {
      if (quotesManager.getQuotes().size() != 0) {
         quotesAdapter.notifyDataSetChanged();
         quotesListView.setVisibility(View.VISIBLE);
      }
   }
   
   private void initAds() {
	   bannerView = (Plus1BannerView) findViewById(R.id.quotes_view_menu_ad_view);	
		
	   bannerAsker = new Plus1BannerAsker(new Plus1Request().setApplicationId(12736), bannerView.enableAnimationFromBottom()).setCallbackUrl("wsp1bart://ru.majestic.bashimreader").setRefreshDelay(10);
	   bannerAsker.refreshBanner();
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
         exit();
         break;
      case R.id.quotes_view_btn_reload:
         quotesManager.loadCitations();
         reloadQuotesLyt.setVisibility(View.GONE);
         break;
      case R.id.quotes_view_btn_menu:
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_btn_refresh:
         isNewList = true;
         quotesManager.setFromCache(false);
         quotesManager.clearList();
         quotesManager.setState(quotesManager.getState());
         quotesManager.loadCitations();
         reloadQuotesLyt.setVisibility(View.GONE);
         quotesListView.setVisibility(View.GONE);
         break;

      case R.id.quotes_view_menu_btn_new_quotes:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.setFromCache(false);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_NEW_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_random_quotes:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_RANDOM_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_best_quotes:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_BEST_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_by_rating:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_BY_RATING_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_liked:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_LIKED_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_menu_btn_abyss:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_ABYSS);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_abyss_top:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_ABYSS_TOP);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;
      case R.id.quotes_view_menu_btn_abyss_best:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_ABYSS_BEST);
         quotesManager.loadCitations();
         refreshListTitle();
         quotesMenu.toggleMenu();
         break;

      case R.id.quotes_view_quick_menu_btn_abyss_best:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_ABYSS_BEST);
         quotesManager.loadCitations();
         refreshListTitle();
         break;
      case R.id.quotes_view_quick_menu_btn_new_quotes:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.setFromCache(false);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_NEW_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         break;
      case R.id.quotes_view_quick_menu_btn_random_quotes:
         isNewList = true;
         quotesListView.setVisibility(View.GONE);
         quotesManager.clearList();
         quotesManager.setState(QuotesManager.STATE_RANDOM_QUOTES);
         quotesManager.loadCitations();
         refreshListTitle();
         break;
      }
   }

   @Override
   protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);
      quotesManager.saveState(outState);
      quotesMenu.saveState(outState);
   }

   @Override
   public void onCitationsReady() {
      quotesAdapter.notifyDataSetChanged();
      quotesListView.setVisibility(View.VISIBLE);
      if (isNewList) {
         quotesListView.setSelectionAfterHeaderView();
         isNewList = false;
      }
   }

   @Override
   public void onCitationPrepareError() {
      if (quotesManager.getState() == QuotesManager.STATE_LIKED_QUOTES) {
         Toast.makeText(this, "�� ��� ������ ������ �� ��������! ��� �� ��� ������� �������?", Toast.LENGTH_SHORT).show();
      } else
         reloadQuotesLyt.setVisibility(View.VISIBLE);
   }

   @Override
   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
      if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + ITEMS_COUNT_BEFORE_LOAD_NEW_PAGE)) {
         if (!quotesManager.isFromCache())
            quotesManager.loadCitations();
      }
   }

   @Override
   public void onScrollStateChanged(AbsListView view, int scrollState) {
   }

}