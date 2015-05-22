package ru.majestic.bashimreader.view.impl;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.quotes.sections.QuoteSectionManagersFactory;
import ru.majestic.bashimreader.view.ITopMenuViewSkeleton;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class QuotesTopMenuView extends ITopMenuViewSkeleton implements OnClickListener {

   private Context context;
   
   private ViewGroup topMenuLyt;
   private Button    backBtn;
   private Button    menuBtn;
   private Button    refreshListBtn;
   private TextView  sectionTitle;
   
   public QuotesTopMenuView(Activity activity) {
      this.context = activity;      
      
      topMenuLyt        = (ViewGroup)  activity.findViewById(R.id.quotes_view_lyt_top_menu);
      sectionTitle      = (TextView)   activity.findViewById(R.id.quotes_view_txt_title);
      refreshListBtn    = (Button)     activity.findViewById(R.id.quotes_view_btn_refresh);
      backBtn           = (Button)     activity.findViewById(R.id.quotes_view_btn_back);
      menuBtn           = (Button)     activity.findViewById(R.id.quotes_view_btn_menu);      
      
      refreshListBtn.setOnClickListener(this);
      backBtn.setOnClickListener(this);
      menuBtn.setOnClickListener(this);                        
   }
   
   @Override
   public void refreshSectionTitle(int sectionType) {
      switch(sectionType) {
      case QuoteSectionManagersFactory.SECTION_TYPE_NEW:
         sectionTitle.setText("Новые");
         break;
      }
   }

   @Override
   public void onClick(View v) {
      switch(v.getId()) {
      case R.id.quotes_view_btn_refresh:
         topMenuStateListener.onRefreshButtonClicked();
         break;
         
      case R.id.quotes_view_btn_back:
         topMenuStateListener.onBackButtonClicked();
         break;
         
      case R.id.quotes_view_btn_menu:
         topMenuStateListener.onMenuButtonClicked();
         break;
      }
   }

   @Override
   public void enableNightMode(boolean enable) {
      if(enable) {
         sectionTitle.setTextColor(context.getResources().getColor(R.color.night_mode_text));
         topMenuLyt.setBackground(context.getResources().getDrawable(R.drawable.night_mode_quotes_title_background));         
      } else {
         sectionTitle.setTextColor(context.getResources().getColor(R.color.light_mode_text));
         topMenuLyt.setBackground(context.getResources().getDrawable(R.drawable.light_mode_quotes_title_background));
      }
   }

   @Override
   public void changeFontSize(int fontSize) {
      //.
   }

}
