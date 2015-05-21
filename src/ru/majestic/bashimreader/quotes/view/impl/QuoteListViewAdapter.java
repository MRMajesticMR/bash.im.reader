package ru.majestic.bashimreader.quotes.view.impl;

import java.util.LinkedList;
import java.util.List;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.view.IQuoteListViewAdapter;
import ru.majestic.bashimreader.quotes.view.listeners.OnNeedLoadMoreQuotesListener;
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
import android.widget.ListView;
import android.widget.TextView;

public class QuoteListViewAdapter extends BaseAdapter implements IQuoteListViewAdapter,  OnScrollListener {

   private static final int ITEMS_COUNT_BEFORE_LOAD_NEW_PAGE = 10;
   
   private OnNeedLoadMoreQuotesListener   onNeedLoadMoreQuotesListener;
   private Context                        context;
   private List<Quote>                    quotes;
   private ListView                       listView;
   
   public QuoteListViewAdapter(Activity activity) {
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
   public View getView(int position, View convertView, ViewGroup parent) {
      
      final LayoutInflater inflater    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      final View view                  = inflater.inflate(R.layout.view_qoute, null);

      TextView quoteTextTxt            = (TextView)   view.findViewById(R.id.quote_text);
      TextView quoteIdTxt              = (TextView)   view.findViewById(R.id.quote_id);
      TextView quoteDateTxt            = (TextView)   view.findViewById(R.id.quote_date);
      TextView quoteRatingTxt          = (TextView)   view.findViewById(R.id.quote_rating);
      final Button upQuoteRatingBtn    = (Button)     view.findViewById(R.id.quote_rating_btn_up);
      final Button downQuoteRatingBtn  = (Button)     view.findViewById(R.id.quote_rating_btn_down);

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
      
      else if (quotes.get(position).getRating() == -99997) {
         quoteRatingTxt.setText("#" + (position + 1));
         upQuoteRatingBtn.setVisibility(View.INVISIBLE);
         downQuoteRatingBtn.setVisibility(View.INVISIBLE);
         quoteIdTxt.setVisibility(View.INVISIBLE);
      } else
         quoteRatingTxt.setText(String.valueOf(quotes.get(position).getRating()));
     
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

}
