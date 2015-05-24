package ru.majestic.bashimreader.managers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.QuotesDatebaseManager;
import ru.majestic.bashimreader.loaders.impl.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.managers.listeners.CitationListener;
import ru.majestic.bashimreader.utils.EndAnimationListener;
import ru.majestic.bashimreader.utils.QuotesDictionary;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class QuotesManager implements OnPageLoadListener {

	public static final int STATE_NEW_QUOTES = 1;
	public static final int STATE_RANDOM_QUOTES = 2;
	public static final int STATE_BEST_QUOTES = 3;
	public static final int STATE_BY_RATING_QUOTES = 4;
	public static final int STATE_LIKED_QUOTES = 5;

	public static final int STATE_ABYSS = 11;
	public static final int STATE_ABYSS_TOP = 12;
	public static final int STATE_ABYSS_BEST = 13;

	private static final String CITATION_CURRENT_PAGE_COUNT = "CITATION_CURRENT_PAGE_COUNT";
	private static final String CITATION_MAX_PAGE_COUNT = "CITATION_MAX_PAGE_COUNT";
	private static final String SAVED_QUOTES = "SAVED_QUOTES";
	private static final String QUOTES_MANAGER_STATE = "QUOTES_MANAGER_STATE";
	
	private static final String QUOTE_RATING_NEW = "???";
	private static final String QUOTE_RATING_UNKNOWN = "...";   

	private static final long DAY_MILLISECONDS = 86400000;
	
	private final Context mContext;
	private final QuotesDatebaseManager quotesDatebaseManager;
	
	private int maxPageCount, currentPageCount;
	private Date abyssBestDate;

	private List<Quote> quotes = new ArrayList<Quote>();
	private CitationListener citationListener;
	private ViewGroup downloadingView;

	private int state;
	private boolean loading;
	private boolean isFromCache;
	

	private final Animation downloadViewInAnimation, downloadViewOutAnimation;
	private final EndAnimationListener animationListener = new EndAnimationListener() {
      
      @Override
      public void onAnimationEnd(Animation animation) {
         downloadingView.setVisibility(View.GONE);
         
      }
   }; 

	public QuotesManager(Bundle savedInstanceState, Context context) {
		this.mContext = context;
		
		downloadViewInAnimation = AnimationUtils.loadAnimation(context, R.anim.download_view_in);
		downloadViewOutAnimation = AnimationUtils.loadAnimation(context, R.anim.download_view_out);
		downloadViewOutAnimation.setAnimationListener(animationListener);
		
		quotesDatebaseManager = new QuotesDatebaseManager(context);
		if (savedInstanceState != null && savedInstanceState.containsKey(CITATION_CURRENT_PAGE_COUNT)) {
			currentPageCount = savedInstanceState.getInt(CITATION_CURRENT_PAGE_COUNT);
			maxPageCount = savedInstanceState.getInt(CITATION_MAX_PAGE_COUNT);
			state = savedInstanceState.getInt(QUOTES_MANAGER_STATE);
			restoreQuotesState(savedInstanceState);
		} else {
			currentPageCount = maxPageCount = 0;
			state = STATE_NEW_QUOTES;
		}
		loading = false;
		isFromCache = false;
	}

	public void setDownloadingView(ViewGroup view) {
		this.downloadingView = view;
	}

	public void setState(int state) {
		this.state = state;
		currentPageCount = maxPageCount = 0;
		abyssBestDate = Calendar.getInstance().getTime();
	}

	public void setCitationListener(CitationListener listener) {
		this.citationListener = listener;
	}

	public void saveState(Bundle savedInstanceState) {
		savedInstanceState.putInt(CITATION_CURRENT_PAGE_COUNT, currentPageCount);
		savedInstanceState.putInt(CITATION_MAX_PAGE_COUNT, maxPageCount);
		saveQuotes(savedInstanceState);
		savedInstanceState.putInt(QUOTES_MANAGER_STATE, state);
	}

	public final void saveQuotes(final Bundle outState) {
		if (quotes.size() == 0)
			return;

		final StringBuffer buffer = new StringBuffer("[");
		buffer.append(quotes.get(0).toJSONString());
		for(int i = 1; i < quotes.size(); i++)
		   buffer.append(", " + quotes.get(i).toJSONString());		

		buffer.append("]");
	   
		outState.putString(SAVED_QUOTES, buffer.toString());
	}

	public void restoreQuotesState(Bundle outState) throws IllegalArgumentException {
		if (!outState.containsKey(SAVED_QUOTES))
			return;

		try {
			final JSONArray quotesJSONArray = new JSONArray(outState.getString(SAVED_QUOTES));
			for (int i = 0; i < quotesJSONArray.length(); i++) {
				JSONObject object = quotesJSONArray.getJSONObject(i);
				quotes.add(new Quote(object.getInt("id"), object.getInt("rating"), object.getString("content"), object.getString("date")));
			}
		} catch (JSONException e) {
			quotes.clear();
		}
	}

	public void loadCitations() {
		if (loading)
			return;
		loading = true;
		downloadingView.setVisibility(View.VISIBLE);
		downloadingView.startAnimation(downloadViewInAnimation);
		final PageLoader pageLoader = new PageLoader();
		pageLoader.setOnPageLoadListener(this);
		switch (state) {
		case STATE_NEW_QUOTES:
			if (currentPageCount == 0)
				pageLoader.load(QuotesDictionary.URL_QUOTES_NEW);
			else
				pageLoader.load(QuotesDictionary.URL_QUOTES_NEW + QuotesDictionary.PREFIX_NEW_QUOTES_PAGE + currentPageCount);
			break;
		case STATE_RANDOM_QUOTES:
			pageLoader.load(QuotesDictionary.URL_QUOTES_RANDOM);
			break;
		case STATE_BEST_QUOTES:
			if (quotes.size() == 0) {
				pageLoader.load(QuotesDictionary.URL_QUOTES_BEST);
			}
			break;
		case STATE_BY_RATING_QUOTES:
			if (currentPageCount == 0)
				pageLoader.load(QuotesDictionary.URL_QUOTES_BY_RATING);
			else
				pageLoader.load(QuotesDictionary.URL_QUOTES_BY_RATING + "/" + currentPageCount);
			break;
		case STATE_LIKED_QUOTES:
			loading = false;
//			quotes = quotesDatebaseManager.getLikedQuotes();
			downloadingView.startAnimation(downloadViewOutAnimation);
			if(quotes.size() == 0)
				citationListener.onCitationPrepareError();
			else
				citationListener.onCitationsReady();
			break;

		case STATE_ABYSS:
			pageLoader.load(QuotesDictionary.URL_ABYSS);
			break;
		case STATE_ABYSS_TOP:
			if (quotes.size() == 0)
				pageLoader.load(QuotesDictionary.URL_ABYSS_TOP);
			break;
		case STATE_ABYSS_BEST:
			pageLoader.load(QuotesDictionary.URL_ABYSS_BEST + new SimpleDateFormat("yyyyMMdd").format(abyssBestDate));
			break;
		}
	}

	@Override
	public void onPageLoad(String content) {
		loading = false;		
		downloadingView.startAnimation(downloadViewOutAnimation);
		Document doc = Jsoup.parse(content);
		getQuotesFromPage(doc);
		getPageCountFromPage(doc);
		nextPage();
		citationListener.onCitationsReady();
	}

	@Override
	public void onPageLoadError() {
		loading = false;
		downloadingView.startAnimation(downloadViewOutAnimation);
		if (state == STATE_NEW_QUOTES && !isFromCache) {
			isFromCache = true;
			quotes = quotesDatebaseManager.getNewQuotes();
			if (quotes.size() != 0) {
				Toast.makeText(mContext, "Загружено из кэша", Toast.LENGTH_SHORT).show();
				citationListener.onCitationsReady();
				return;
			}
		}
		citationListener.onCitationPrepareError();
	}

	private final void getQuotesFromPage(Document doc) {
		final Elements texts = doc.getElementsByClass("text");
		final Elements ids = doc.getElementsByClass("id");
		final Elements ratings = doc.getElementsByClass("rating");	
		final Elements dates;
		
		if (state == STATE_ABYSS_TOP)
			dates = doc.getElementsByClass("abysstop-date");
		else
			dates = doc.getElementsByClass("date");

		final List<Quote> downloadedQuotes = new ArrayList<Quote>();
		
		for (int i = 0; i < texts.size(); i++) {
			final Quote quote;
			if (state == STATE_ABYSS_TOP)
				quote = new Quote(0, -99997, texts.get(i).html(), dates.get(i).html());

			else if (state == STATE_ABYSS_BEST)
				quote = new Quote(0, -99997, texts.get(i).html(), dates.get(i).html());
			else if (ratings.get(i).html().equals(QUOTE_RATING_NEW))
				quote = new Quote(Integer.valueOf(ids.get(i).html().replace("#", "")), -99999, texts.get(i).html(), dates.get(i).html());
			else if (ratings.get(i).html().equals(QUOTE_RATING_UNKNOWN))
				quote = new Quote(Integer.valueOf(ids.get(i).html().replace("#", "")), -99998, texts.get(i).html(), dates.get(i).html());
			else
				quote = new Quote(Integer.valueOf(ids.get(i).html().replace("#", "")), Integer.valueOf(ratings.get(i).html()), texts.get(i).html(), dates.get(i).html());
			
			quotes.add(quote);
			downloadedQuotes.add(quote);
		}
		
		quotesDatebaseManager.saveDownloadedQuotesList(downloadedQuotes);
	}
	
	private void getPageCountFromPage(Document doc) {
		final Elements element = doc.getElementsByClass("page");
		if (element.size() == 0)
			return;
		try {
			maxPageCount = Integer.valueOf(element.get(0).attr("max"));
		} catch (NumberFormatException e) {
			maxPageCount = 0;
		}
		if (state == STATE_NEW_QUOTES) {
			if (currentPageCount == 0)
				currentPageCount = maxPageCount;
			return;
		}

		if (state == STATE_BY_RATING_QUOTES) {
			if (currentPageCount == 0)
				currentPageCount = 1;
			return;
		}
	}

	public void clearList() {
		quotes.clear();
	}

	public List<Quote> getQuotes() {
		return quotes;
	}

	public int getMaxPageCount() {
		return maxPageCount;
	}

	public int getCurrentPageCount() {
		return currentPageCount;
	}

	public void setMaxPageCount(int max) {
		this.maxPageCount = max;
	}

	public void setCurrentPageCount(int currentPageCount) {
		this.currentPageCount = currentPageCount;
	}

	public void nextPage() {
		if (state == STATE_BY_RATING_QUOTES) {
			currentPageCount++;
			return;
		}

		if (state == STATE_NEW_QUOTES) {
			currentPageCount--;
			return;
		}

		if (state == STATE_ABYSS_BEST) {
			abyssBestDate.setTime(abyssBestDate.getTime() - DAY_MILLISECONDS);
		}
	}

	public void previousPage() {
		if (state == STATE_BY_RATING_QUOTES) {
			currentPageCount--;
			return;
		}

		if (state == STATE_NEW_QUOTES) {
			currentPageCount++;
			return;
		}

		if (state == STATE_ABYSS_BEST) {
			abyssBestDate.setTime(abyssBestDate.getTime() + DAY_MILLISECONDS);
		}
	}

	public int getState() {
		return state;
	}

	public QuotesDatebaseManager getQuotesDatebaseManager() {
		return quotesDatebaseManager;
	}

	public boolean isFromCache() {
		return isFromCache;
	}

	public void setFromCache(boolean value) {
		isFromCache = value;
	}

}
