package ru.majestic.bashimreader.quotes.view;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;
import ru.majestic.bashimreader.view.IView;

public interface IQuoteListViewAdapter extends IView {
   
   public void setOnNeedLoadMoreQuotesListener  (OnNeedLoadMoreQuotesListener onNeedLoadMoreQuotesListener);
   public void addQuotes                        (List<Quote> quotes);
   public void clear                            ();
   
}
