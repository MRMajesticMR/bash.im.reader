package ru.majestic.bashimreader.adapters;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.dialogs.SelectedQuoteActionsListDialog;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.managers.QuotesManager;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import ru.majestic.bashimreader.utils.Voter;
import android.content.Context;
import android.content.res.Configuration;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class QuotesAdapter extends BaseAdapter implements OnPageLoadListener {

	private Context context;
	private ApplicationSettings settings;
	private QuotesManager quotesManager;

	public QuotesAdapter(Context context, QuotesManager quotesManager) {
		this.context 		= context;
		this.settings 		= new ApplicationSettings(context);
		this.quotesManager 	= quotesManager;
	}

	@Override
	public int getCount() {
		return quotesManager.getQuotes().size();
	}

	@Override
	public Object getItem(int position) {
		return quotesManager.getQuotes().get(position);
	}

	@Override
	public long getItemId(int position) {
		return quotesManager.getQuotes().get(position).getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.view_qoute, null);

		LinearLayout mainLyt 				= (LinearLayout) view.findViewById(R.id.quote_view_lyt_main);

		TextView quoteTextTxt 				= (TextView) 	view.findViewById(R.id.quote_text);
		TextView quoteIdTxt 				= (TextView) 	view.findViewById(R.id.quote_id);
		TextView quoteDateTxt 				= (TextView) 	view.findViewById(R.id.quote_date);
		TextView quoteRatingTxt 			= (TextView) 	view.findViewById(R.id.quote_rating);
		final Button upQuoteRatingBtn 		= (Button) 		view.findViewById(R.id.quote_rating_btn_up);
		final Button downQuoteRatingBtn 	= (Button) 		view.findViewById(R.id.quote_rating_btn_down);

		quoteTextTxt.setText(Html.fromHtml(quotesManager.getQuotes().get(position).getContent()));
		quoteIdTxt.setText("#" + quotesManager.getQuotes().get(position).getId());
		
		if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			quoteDateTxt.setVisibility(View.GONE);
		} else {
			quoteDateTxt.setText(quotesManager.getQuotes().get(position).getDateString());
		}

		if (quotesManager.getQuotes().get(position).getRating() == -99999)
			quoteRatingTxt.setText("???");
		else if (quotesManager.getQuotes().get(position).getRating() == -99998)
			quoteRatingTxt.setText("...");
		else if (quotesManager.getQuotes().get(position).getRating() == -99997) {
			quoteRatingTxt.setText("#" + (position + 1));
			upQuoteRatingBtn.setVisibility(View.INVISIBLE);
			downQuoteRatingBtn.setVisibility(View.INVISIBLE);
			quoteIdTxt.setVisibility(View.INVISIBLE);
		} else
			quoteRatingTxt.setText(String.valueOf(quotesManager.getQuotes().get(position).getRating()));

		upQuoteRatingBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				upQuoteRatingBtn.setVisibility(View.INVISIBLE);
				downQuoteRatingBtn.setVisibility(View.INVISIBLE);
				Voter.vote(context, QuotesAdapter.this, quotesManager.getQuotes().get(position).getId(), Voter.VOTE_RULEZ);
				try {
					QuotesAdapter.this.quotesManager.getQuotesDatebaseManager().saveLikedQuotes(quotesManager.getQuotes().get(position));
				} catch (Exception e) {
					Log.e("QUOTES", "Не удалось сохранить понравившуюся цитату: " + e.toString());
				}
			}
		});

		downQuoteRatingBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				upQuoteRatingBtn.setVisibility(View.INVISIBLE);
				downQuoteRatingBtn.setVisibility(View.INVISIBLE);
				Voter.vote(context, QuotesAdapter.this, quotesManager.getQuotes().get(position).getId(), Voter.VOTE_SUX);
			}
		});

		OnClickListener shareQuoteClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				final SelectedQuoteActionsListDialog dialog = new SelectedQuoteActionsListDialog(context, quotesManager.getQuotes().get(position));
				dialog.show();
			}
		};

		quoteIdTxt.setOnClickListener(shareQuoteClickListener);
		quoteTextTxt.setOnClickListener(shareQuoteClickListener);

		// Инициализация шрифтов
		int baseFontSize = settings.getQuotesTextSize();

		quoteTextTxt.setTextSize(baseFontSize);
		quoteIdTxt.setTextSize(baseFontSize - 2);
		quoteDateTxt.setTextSize(baseFontSize - 2);
		quoteRatingTxt.setTextSize(baseFontSize - 2);
		upQuoteRatingBtn.setTextSize(baseFontSize - 2);
		downQuoteRatingBtn.setTextSize(baseFontSize - 2);

		// Инициализация ночного режима
		if (settings.isNightModeEnabled()) {
			quoteDateTxt.setTextColor(context.getResources().getColor(R.color.night_mode_quote_date_text));
			quoteRatingTxt.setTextColor(context.getResources().getColor(R.color.night_mode_text));
			quoteTextTxt.setTextColor(context.getResources().getColor(R.color.night_mode_text));

			mainLyt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
			quoteDateTxt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
			quoteRatingTxt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));

			quoteTextTxt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_background));
		} else {
			quoteDateTxt.setTextColor(context.getResources().getColor(R.color.light_mode_quote_date_text));
			quoteRatingTxt.setTextColor(context.getResources().getColor(R.color.light_mode_text));
			quoteTextTxt.setTextColor(context.getResources().getColor(R.color.light_mode_text));

			mainLyt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_background));
			quoteDateTxt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_background));
			quoteRatingTxt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_background));

			quoteTextTxt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_background));
		}

		return view;
	}		

	@Override
	public void onPageLoad(String content) {
		Toast.makeText(context, "^_^ Спасибо за голос! ^_^", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPageLoadError() {
		Toast.makeText(context, "Опаньки! Почему-то голос не был отправлен...", Toast.LENGTH_SHORT).show();

	}
}
