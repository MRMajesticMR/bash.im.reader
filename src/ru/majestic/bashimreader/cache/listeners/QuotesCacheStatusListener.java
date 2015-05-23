package ru.majestic.bashimreader.cache.listeners;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;

public interface QuotesCacheStatusListener {

   public void onQuotesSaved();
   public void onQuotesLoaded(List<Quote> quotes);
   
}
