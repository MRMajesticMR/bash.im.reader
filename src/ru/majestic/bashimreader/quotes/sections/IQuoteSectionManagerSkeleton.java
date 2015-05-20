package ru.majestic.bashimreader.quotes.sections;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.loaders.IPageLoader;
import ru.majestic.bashimreader.loaders.impl.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.parsers.IQuotesPageParser;
import ru.majestic.bashimreader.parsers.listeners.OnQuotesPagesParsedListener;
import ru.majestic.bashimreader.quotes.sections.listeners.OnNewQuotesReadyListener;
import android.os.Bundle;

public abstract class IQuoteSectionManagerSkeleton implements IQuotesSectionManager, OnPageLoadListener, OnQuotesPagesParsedListener {

   private static final String CITATION_CURRENT_PAGE_COUNT     = "CITATION_CURRENT_PAGE_COUNT";
   private static final String SAVED_QUOTES                    = "SAVED_QUOTES";
   
	private List<Quote>                quotes;
	
	private int 			              nextPage;
	private IPageLoader 	              pageLoader;
	private IQuotesPageParser          quotesPageParser;
	private OnNewQuotesReadyListener   onNewQuotesReadyListener;
	
	public IQuoteSectionManagerSkeleton() {
		quotes 			= new LinkedList<Quote>();
		nextPage 	   = 0;
		
		pageLoader		= new PageLoader();
		pageLoader.setOnPageLoadListener(this);
		
		quotesPageParser = getQuotesPageParser();
		quotesPageParser.setOnQuotesPageParsedListener(this);
	}

	@Override
	public void loadNextPage() {
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
      quotesPageParser.parsePage(content);
   }

   @Override
   public void onPageLoadError() {
      this.onNewQuotesReadyListener.onLoadNewQuotesError();
   }
   
   @Override
   public void onQuotesPageParsed(List<Quote> quotes) {
      this.nextPage++;
      this.quotes.addAll(quotes);
      this.onNewQuotesReadyListener.onNewQuoteReady(quotes);
   }

   @Override
   public void onQuotePageParseError() {
      this.onNewQuotesReadyListener.onLoadNewQuotesError();
   }
	
	protected int getNextPage() {
	   return nextPage;
	}
	
	protected abstract String             generateNextPageDownloadUrl   ();
	protected abstract IQuotesPageParser  getQuotesPageParser           ();     
	
}
