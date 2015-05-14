package ru.majestic.bashimreader.widgets;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.activities.SingleQuoteActivity;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.loaders.RandomQuoteLoader;
import ru.majestic.bashimreader.utils.QuotesDictionary;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.RemoteViews;

public class RandomQuoteWidgetProvider extends AppWidgetProvider implements RandomQuoteLoader.OnLoadQuoteListener {

	private Context context;
	private RemoteViews remoteViews;
	private AppWidgetManager appWidgetManager;
	private int[] appWidgetsIds;

	private Quote currentQuote;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		this.context = context;
		this.appWidgetManager = appWidgetManager;
		this.appWidgetsIds = appWidgetIds;

		final int N = appWidgetIds.length;

		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_random_quote_layout);
			if (currentQuote != null)
				remoteViews.setOnClickPendingIntent(R.id.widget_random_quote_txt_quote, getLaunchSingleQuoteActivityPendingIntent(context));
			
			appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
		}		
		
		beginLoadingQuote();
	}
	
	private void beginLoadingQuote() {
		remoteViews.setTextViewText(R.id.widget_random_quote_txt_quote, "");
		remoteViews.setViewVisibility(R.id.widget_random_quote_prgs_loading_new_quote, View.VISIBLE);
		remoteViews.setViewVisibility(R.id.widget_random_quote_btn_refresh, View.GONE);		
		
		RandomQuoteLoader randomQuoteLoader = new RandomQuoteLoader();
		randomQuoteLoader.loadRandomQuote(this);
	}

	private PendingIntent getLaunchSingleQuoteActivityPendingIntent(Context context) {
		Intent intent = new Intent(context, SingleQuoteActivity.class);
		intent.setData(Uri.parse(QuotesDictionary.URL_SINGLE_QUOTE + currentQuote.getId()));
		return PendingIntent.getActivity(context, 0, intent, 0);
	}

	@Override
	public void onLoadQuoteSuccess(Quote quote) {
		remoteViews.setTextViewText(R.id.widget_random_quote_txt_quote, Html.fromHtml(quote.getContent()));
		remoteViews.setViewVisibility(R.id.widget_random_quote_prgs_loading_new_quote, View.GONE);
		for (int i = 0; i < appWidgetsIds.length; i++)
			appWidgetManager.updateAppWidget(appWidgetsIds[i], remoteViews);
	}

	@Override
	public void onLoadQuoteError() {
		remoteViews.setTextViewText(R.id.widget_random_quote_txt_quote, context.getString(R.string.widget_random_quote_txt_download_error));
		remoteViews.setViewVisibility(R.id.widget_random_quote_prgs_loading_new_quote, View.GONE);
		remoteViews.setViewVisibility(R.id.widget_random_quote_btn_refresh, View.VISIBLE);
	}

}
