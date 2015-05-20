package ru.majestic.bashimreader.quotes.sections.listeners;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;

public interface OnNewQuotesReadyListener {

	public void onNewQuoteReady        (List<Quote> quotes);
	public void onLoadNewQuotesError   ();
	
}
