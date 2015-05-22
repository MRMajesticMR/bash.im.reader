package ru.majestic.bashimreader.quotes.view;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;
import ru.majestic.bashimreader.quotes.view.listeners.OnVoteQuoteButtonClicked;
import ru.majestic.bashimreader.view.IView;

public interface IQuoteListView extends IView {
   
   public void setOnVoteQuoteButtonClicked      (OnVoteQuoteButtonClicked onVoteQuoteButtonClicked);
   public void setOnNeedLoadMoreQuotesListener  (OnNeedLoadMoreQuotesListener onNeedLoadMoreQuotesListener);
   public void addQuotes                        (List<Quote> quotes);
   public void clear                            ();
   
}
