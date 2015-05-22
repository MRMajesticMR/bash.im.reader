package ru.majestic.bashimreader.quotes.view.listeners;

import ru.majestic.bashimreader.data.Quote;

public interface OnVoteQuoteButtonClicked {

   public void onVoteUpButtonClicked   (Quote quote);
   public void onVoteDownButtonClicked (Quote quote);   
   
}
