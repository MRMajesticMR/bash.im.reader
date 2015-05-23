package ru.majestic.bashimreader.quotes.sections;

import android.os.Bundle;
import ru.majestic.bashimreader.quotes.sections.listeners.OnNewQuotesReadyListener;

public interface IQuotesSectionManager {

   public boolean isNoQuotes();
   public boolean isNewQuotesPreparing();
   public boolean isNeedLoadMorePage();
   public void reset();
	public void loadNextPage();
	public void setOnNewQuotesReadyListener(OnNewQuotesReadyListener onNewQuotesReadyListener);
	
	public void restoreState  (Bundle savedInstanceState);
	public void saveState     (Bundle savedInstanceState);
	
}
