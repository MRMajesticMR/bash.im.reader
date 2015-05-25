package ru.majestic.bashimreader.quotes.sections;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import ru.majestic.bashimreader.cache.IQuotesCacher;
import ru.majestic.bashimreader.cache.impl.EmptyQuotesCacher;
import ru.majestic.bashimreader.cache.listeners.QuotesCacheStatusListener;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.loaders.IPageLoader;
import ru.majestic.bashimreader.loaders.impl.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.listeners.OnQuotesPageCountParsedListener;
import ru.majestic.bashimreader.parsers.listeners.OnQuotesPagesParsedListener;
import ru.majestic.bashimreader.parsers.pagecont.IQuotesPageCountParser;
import ru.majestic.bashimreader.parsers.pagecont.impl.EmptyQuotePageCountParser;
import ru.majestic.bashimreader.quotes.filters.IQuotesFilter;
import ru.majestic.bashimreader.quotes.filters.impl.NullQuotesFilter;
import ru.majestic.bashimreader.quotes.filters.listeners.OnQuotesFilteredListener;
import ru.majestic.bashimreader.quotes.sections.listeners.OnNewQuotesReadyListener;
import android.os.Bundle;
import android.util.Log;

public abstract class IQuoteSectionManagerSkeleton implements IQuotesSectionManager, 
                                                               OnPageLoadListener, 
                                                               OnQuotesPagesParsedListener,
                                                               OnQuotesPageCountParsedListener,
                                                               QuotesCacheStatusListener,
                                                               OnQuotesFilteredListener {

   private static final String CITATION_CURRENT_PAGE_COUNT     = "CITATION_CURRENT_PAGE_COUNT";
   private static final String SAVED_QUOTES                    = "SAVED_QUOTES";
   
	protected List<Quote>              quotes;	
	protected int                      nextPage;
   protected String                   lastLoadPageContent;   
   protected int                      maxPageCount;
   protected IPageLoader 	           pageLoader;
   protected IQuotesPageParser        quotesPageParser;
   protected IQuotesPageCountParser   quotePageCountParser;
   protected IQuotesCacher            quotesCacher;   
   protected IQuotesFilter            quotesFilter;
   protected boolean                  newQuotesPrepearing;   
	protected OnNewQuotesReadyListener onNewQuotesReadyListener;
	
	public IQuoteSectionManagerSkeleton() {
		quotes 			       = new LinkedList<Quote>();
		reset();
		
		pageLoader		= new PageLoader();
		pageLoader.setOnPageLoadListener(this);
		
		quotesPageParser = getQuotesPageParser();
		quotesPageParser.setOnQuotesPageParsedListener(this);
		
		quotePageCountParser = getQuotesPageCountParser();
		quotePageCountParser.setOnQuotesPageCountParsedListener(this);
		
		quotesCacher = getQuotesCacher();
		quotesCacher.setQuotesCacheStatusListener(this);
		
		quotesFilter = getQuotesFilter();
		quotesFilter.setOnQuotesFilteredListener(this);
	}
	
	
	
	public boolean isNeedLoadMorePage() {
	   return true;
	}
	
	public boolean isNoQuotes() {
	   return quotes.isEmpty();
	}
	
	public boolean isNewQuotesPreparing() {
	   return newQuotesPrepearing;
	}
	
	public void reset() {
	   quotes.clear();
	   
	   maxPageCount          = -1;
      nextPage              = 0;
      newQuotesPrepearing   = false;
	}

	@Override
	public void loadNextPage() {
	   newQuotesPrepearing = true;
	   pageLoader.load(generateNextPageDownloadUrl());
	}

	@Override
	public void setOnNewQuotesReadyListener(OnNewQuotesReadyListener onNewQuotesReadyListener) {
		this.onNewQuotesReadyListener = onNewQuotesReadyListener;
	}
	
	@Override
   public void saveState(Bundle savedInstanceState) {
	   savedInstanceState.putInt(CITATION_CURRENT_PAGE_COUNT, nextPage);
      saveQuotes(savedInstanceState);
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
	
	@Override
   public void restoreState(Bundle savedInstanceState) {
	   if(savedInstanceState != null) { 
   	   if (!savedInstanceState.containsKey(SAVED_QUOTES))
            return;
   
         try {
            final JSONArray quotesJSONArray = new JSONArray(savedInstanceState.getString(SAVED_QUOTES));
            for (int i = 0; i < quotesJSONArray.length(); i++) {
               quotes.add(new Quote(quotesJSONArray.getJSONObject(i)));
            }
         } catch (JSONException e) {
            quotes.clear();
         }
         
         this.onNewQuotesReadyListener.onNewQuoteReady(quotes);
	   }
   }
	
	@Override
   public void onPageLoad(String content) {
	   if(maxPageCount == -1) {
	      lastLoadPageContent = content;
	      quotePageCountParser.parse(content);
	   } else {
	      quotesPageParser.parsePage(content);
	   }      
   }

   @Override
   public void onPageLoadError() {
      if(quotesCacher.hasQuotes()) {
         quotesCacher.loadSavedQuotes();
      } else {
         this.onNewQuotesReadyListener.onLoadNewQuotesError();
         this.newQuotesPrepearing = false;
      }
   }
   
   @Override
   public void onQuotesPageParsed(List<Quote> quotes) {
      quotesFilter.startFilter(quotes);      
   }

   @Override
   public void onQuotePageParseError() {
      if(quotesCacher.hasQuotes()) {
         quotesCacher.loadSavedQuotes();
      } else {
         this.onNewQuotesReadyListener.onLoadNewQuotesError();
         this.newQuotesPrepearing = false;
      }
   }
   
   @Override
   public void onQuotesPageCountParsed(int pageCount) {
      maxPageCount = pageCount;
      quotesPageParser.parsePage(lastLoadPageContent);
   }
	
	protected int getNextPage() {
	   return nextPage;
	}
	
	protected int changeNextPage(int currentPage) {
	   return ++currentPage;
	}
	
	protected IQuotesPageCountParser getQuotesPageCountParser() {
	   return new EmptyQuotePageCountParser();
	}
	
	protected IQuotesCacher getQuotesCacher() {
      return new EmptyQuotesCacher();
   }
	
	protected IQuotesFilter getQuotesFilter() {
      return new NullQuotesFilter();
   }
	
	@Override
   public void onQuotesSaved() {
      Log.i("CACHER_LISTENER", "Quotes saved in cache");
   }

   @Override
   public void onQuotesLoaded(List<Quote> quotes) {
      this.quotes.addAll(quotes);
      this.onNewQuotesReadyListener.onNewQuoteReady(quotes);
      this.newQuotesPrepearing = false;
   }      

   @Override
   public void onQuotesFiltered(List<Quote> filteredQuotes) {
      this.nextPage = changeNextPage(getNextPage());
      this.quotes.addAll(filteredQuotes);
      this.quotesCacher.saveQuotes(filteredQuotes);     
      this.onNewQuotesReadyListener.onNewQuoteReady(filteredQuotes);
      this.newQuotesPrepearing = false;
   }    


   protected abstract String             generateNextPageDownloadUrl   ();
   protected abstract IQuotesPageParser  getQuotesPageParser           ();
	
}
