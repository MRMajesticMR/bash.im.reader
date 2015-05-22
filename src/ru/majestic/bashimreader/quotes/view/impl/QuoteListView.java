package ru.majestic.bashimreader.quotes.view.impl;

import java.util.LinkedList;
import java.util.List;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.view.IQuoteListView;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;
import ru.majestic.bashimreader.quotes.view.listeners.OnVoteQuoteButtonClicked;
import ru.majestic.bashimreader.quotes.view.listeners.ShareQuoteNeedListener;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class QuoteListView extends BaseAdapter implements IQuoteListView,  OnScrollListener {

   private static final int ITEMS_COUNT_BEFORE_LOAD_NEW_PAGE = 10;
   
   private OnNeedLoadMoreQuotesListener   onNeedLoadMoreQuotesListener;
   private OnVoteQuoteButtonClicked       onVoteQuoteButtonClicked;
   
   private Context                        context;
   private List<Quote>                    quotes;
   private ListView                       listView;
   private int                            fontSize;
   private boolean                        nightMode;
   
   public QuoteListView(Activity activity) {
      this.context   = activity;
      this.quotes    = new LinkedList<Quote>();
      this.listView  = (ListView) activity.findViewById(R.id.quotes_view_list_view);
      
      listView.setAdapter(this);
      listView.setOnScrollListener(this);
   }
   
   @Override
   public int getCount() {
      return quotes.size();
   }

   @Override
   public Object getItem(int position) {
      return quotes.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View getView(final int position, View convertView, ViewGroup parent) {
      
      final LayoutInflater inflater    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      final View view                  = inflater.inflate(R.layout.view_qoute, null);

      
      LinearLayout mainLyt             = (LinearLayout)  view.findViewById(R.id.quote_view_lyt_main);
      TextView quoteTextTxt            = (TextView)      view.findViewById(R.id.quote_text);
      TextView quoteIdTxt              = (TextView)      view.findViewById(R.id.quote_id);
      TextView quoteDateTxt            = (TextView)      view.findViewById(R.id.quote_date);
      TextView quoteRatingTxt          = (TextView)      view.findViewById(R.id.quote_rating);
      final Button upQuoteRatingBtn    = (Button)        view.findViewById(R.id.quote_rating_btn_up);
      final Button downQuoteRatingBtn  = (Button)        view.findViewById(R.id.quote_rating_btn_down);

      
      if (nightMode) {
         quoteDateTxt.setTextColor(context.getResources().getColor(R.color.night_mode_quote_date_text));
         quoteRatingTxt.setTextColor(context.getResources().getColor(R.color.night_mode_text));
         quoteTextTxt.setTextColor(context.getResources().getColor(R.color.night_mode_text));
         mainLyt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
         quoteDateTxt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
         quoteRatingTxt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
         quoteTextTxt.setBackground(context.getResources().getDrawable(R.drawable.night_mode_quote_background));
      } else {
         quoteDateTxt.setTextColor(context.getResources().getColor(R.color.light_mode_quote_date_text));
         quoteRatingTxt.setTextColor(context.getResources().getColor(R.color.light_mode_text));
         quoteTextTxt.setTextColor(context.getResources().getColor(R.color.light_mode_text));
         mainLyt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_background));
         quoteDateTxt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_background));
         quoteRatingTxt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_background));
         quoteTextTxt.setBackground(context.getResources().getDrawable(R.drawable.light_mode_quote_background));
      }
      
      quoteTextTxt.setTextSize(fontSize);
      quoteIdTxt.setTextSize(fontSize - 2);
      quoteDateTxt.setTextSize(fontSize - 2);
      quoteRatingTxt.setTextSize(fontSize - 2);
      upQuoteRatingBtn.setTextSize(fontSize - 2);
      downQuoteRatingBtn.setTextSize(fontSize - 2);
      
      
      quoteTextTxt.setText(Html.fromHtml(quotes.get(position).getContent()));
      quoteIdTxt.setText("#" + quotes.get(position).getId());
      
      if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
         quoteDateTxt.setVisibility(View.GONE);
      } else {
         quoteDateTxt.setText(quotes.get(position).getDateString());
      }

      if (quotes.get(position).getRating() == Quote.QUOTE_RATING_NEW_VALUE)
         quoteRatingTxt.setText(Quote.QUOTE_RATING_NEW);
      
      else if (quotes.get(position).getRating() == Quote.QUOTE_RATING_UNKNOWN_VALUE)
         quoteRatingTxt.setText(Quote.QUOTE_RATING_UNKNOWN);
      
      else if (quotes.get(position).getRating() == Quote.QUOTE_RATING_NO_VALUE) {
         quoteRatingTxt.setText("#" + (position + 1));
         upQuoteRatingBtn.setVisibility(View.INVISIBLE);
         downQuoteRatingBtn.setVisibility(View.INVISIBLE);
         quoteIdTxt.setVisibility(View.INVISIBLE);
      } else
         quoteRatingTxt.setText(String.valueOf(quotes.get(position).getRating()));
     
      ShareQuoteNeedListener shareQuoteNeedListener = new ShareQuoteNeedListener(context, quotes.get(position));           
      
      quoteIdTxt.setOnClickListener(shareQuoteNeedListener);
      quoteTextTxt.setOnClickListener(shareQuoteNeedListener);
      
      upQuoteRatingBtn.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            upQuoteRatingBtn.setVisibility(View.INVISIBLE);
            downQuoteRatingBtn.setVisibility(View.INVISIBLE);
            onVoteQuoteButtonClicked.onVoteUpButtonClicked(quotes.get(position));
         }
      });

      downQuoteRatingBtn.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
            upQuoteRatingBtn.setVisibility(View.INVISIBLE);
            downQuoteRatingBtn.setVisibility(View.INVISIBLE);
            onVoteQuoteButtonClicked.onVoteDownButtonClicked(quotes.get(position));
         }
      });
      
      return view;
   }
   
   @Override
   public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
      if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + ITEMS_COUNT_BEFORE_LOAD_NEW_PAGE)) {
         if(onNeedLoadMoreQuotesListener != null) {
            onNeedLoadMoreQuotesListener.onNeedLoadMoreQuotes();
         }
      }
   }
   
   @Override
   public void onScrollStateChanged(AbsListView view, int scrollState) {
      //.      
   }

   @Override
   public void addQuotes(List<Quote> quotes) {
      this.quotes.addAll(quotes);
      notifyDataSetChanged();
      listView.setVisibility(View.VISIBLE);
   }

   @Override
   public void clear() {            
      this.quotes.clear();
      notifyDataSetChanged();
      listView.setVisibility(View.GONE);
   }

   @Override
   public void setOnNeedLoadMoreQuotesListener(OnNeedLoadMoreQuotesListener onNeedLoadMoreQuotesListener) {
      this.onNeedLoadMoreQuotesListener = onNeedLoadMoreQuotesListener;      
   }

   @Override
   public void enableNightMode(boolean enable) {
      this.nightMode = enable;      
   }

   @Override
   public void changeFontSize(int fontSize) {
      this.fontSize = fontSize;      
   }

   @Override
   public void setOnVoteQuoteButtonClicked(OnVoteQuoteButtonClicked onVoteQuoteButtonClicked) {
      this.onVoteQuoteButtonClicked = onVoteQuoteButtonClicked;
   }   

}
