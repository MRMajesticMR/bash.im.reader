package ru.majestic.bashimreader.cache;

import java.util.List;

import ru.majestic.bashimreader.cache.listeners.QuotesCacheStatusListener;
import ru.majestic.bashimreader.data.Quote;

public interface IQuotesCacher {

   public void          saveQuotes        (List<Quote> quotes);
   public void          loadSavedQuotes   ();
   public boolean       hasQuotes         ();

   public void          setQuotesCacheStatusListener(QuotesCacheStatusListener quotesCacheStatusListener);
}
