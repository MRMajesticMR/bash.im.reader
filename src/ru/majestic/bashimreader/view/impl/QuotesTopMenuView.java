package ru.majestic.bashimreader.view.impl;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.quotes.sections.QuoteSectionManagersFactory;
import ru.majestic.bashimreader.view.ITopMenuViewSkeleton;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class QuotesTopMenuView extends ITopMenuViewSkeleton implements OnClickListener {

   private Button    backBtn;
   private Button    menuBtn;
   private Button    refreshListBtn;
   private TextView  sectionTitle;
   
   public QuotesTopMenuView(Activity activity) {
      
      sectionTitle      = (TextView) activity.findViewById(R.id.quotes_view_txt_title);
      refreshListBtn    = (Button) activity.findViewById(R.id.quotes_view_btn_refresh);
      backBtn           = (Button) activity.findViewById(R.id.quotes_view_btn_back);
      menuBtn           = (Button) activity.findViewById(R.id.quotes_view_btn_menu);      
      
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

}
