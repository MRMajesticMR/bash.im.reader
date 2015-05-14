package ru.majestic.bashimreader.loaders;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.utils.QuotesDictionary;
import ru.majestic.bashimreader.utils.ServerAnswerParser;

public class RandomQuoteLoader implements OnPageLoadListener {

	private OnLoadQuoteListener onLoadQuoteListener;	

	public void loadRandomQuote(OnLoadQuoteListener onLoadQuoteListener) {
		this.onLoadQuoteListener = onLoadQuoteListener;

		PageLoader pageLoader = new PageLoader();
		pageLoader.setOnPageLoadListener(onRandomQuoteURLLoadListener);
		pageLoader.execute(QuotesDictionary.URL_RANDOM_QUOTE_UTF_8);
	}	
	
	private OnPageLoadListener onRandomQuoteURLLoadListener = new OnPageLoadListener() {

		@Override
		public void onPageLoadError() {
			onLoadQuoteListener.onLoadQuoteError();
		}

		@Override
		public void onPageLoad(String content) {
			PageLoader pageLoader = new PageLoader();
			pageLoader.setOnPageLoadListener(RandomQuoteLoader.this);
			pageLoader.execute(ServerAnswerParser.getRandomQuoteURLFromServerAnswer(content));
		}
	};

	@Override
	public void onPageLoad(String content) {
		onLoadQuoteListener.onLoadQuoteSuccess(ServerAnswerParser.parseSingleQuotePage(content));
	}

	@Override
	public void onPageLoadError() {
		onLoadQuoteListener.onLoadQuoteError();
	}
	
	public interface OnLoadQuoteListener {

		public void onLoadQuoteSuccess(Quote quote);

		public void onLoadQuoteError();

	}
}
