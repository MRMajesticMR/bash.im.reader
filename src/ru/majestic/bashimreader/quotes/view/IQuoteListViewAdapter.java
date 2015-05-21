package ru.majestic.bashimreader.quotes.view;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;

public interface IQuoteListViewAdapter {
   
   public void setOnNeedLoadMoreQuotesListener  (OnNeedLoadMoreQuotesListener onNeedLoadMoreQuotesListener);
   public void addQuotes                        (List<Quote> quotes);
   public void clear                            ();
   
}
