package ru.majestic.bashimreader.menu;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.preference.ApplicationSettings;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class QuotesMenu implements AnimationListener, OnClickListener {

   private static final String MENU_VISIBLE_STATE = "MENU_VISIBLE_STATE";

   private ViewGroup baseLyt, titleLyt;
   private TextView sectionTitleQuotes, sectionTitleAbyss;
   
   private ViewGroup parentLayout;

   private Button newQuotesBtn, randomQuotesBtn, bestQuotesBtn, byRatingQuotesBtn, likedBtn;
   private Button abyssBtn, abyssTopBtn, abyssBestBtn;
   private Button closeBtn;
   
   private Animation inAnimation, outAnimation;
   
   private ApplicationSettings applicationSettings;

   public QuotesMenu(ViewGroup parentLayout, Context context, Bundle savedState) {
      this.parentLayout = parentLayout;
      
      baseLyt = (ViewGroup) parentLayout.findViewById(R.id.quotes_view_lyt_menu);
      titleLyt = (ViewGroup) parentLayout.findViewById(R.id.quotes_view_menu_title);
      sectionTitleQuotes = (TextView) parentLayout.findViewById(R.id.quotes_view_menu_section_title_quotes);
      sectionTitleAbyss = (TextView) parentLayout.findViewById(R.id.quotes_view_menu_section_title_abyss);

      newQuotesBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_new_quotes);
      randomQuotesBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_random_quotes);
      bestQuotesBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_best_quotes);
      byRatingQuotesBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_by_rating);
      likedBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_liked);
      
      abyssBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_abyss);
      abyssTopBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_abyss_top);
      abyssBestBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_abyss_best);
      
      closeBtn = (Button) parentLayout.findViewById(R.id.quotes_view_menu_btn_close);

      closeBtn.setOnClickListener(this);

      inAnimation = AnimationUtils.loadAnimation(context, R.anim.quotes_menu_in);
      outAnimation = AnimationUtils.loadAnimation(context, R.anim.quotes_menu_out);
      outAnimation.setAnimationListener(this);

      if (savedState != null && savedState.containsKey(MENU_VISIBLE_STATE)) {
         if (savedState.getBoolean(MENU_VISIBLE_STATE))
            parentLayout.setVisibility(View.VISIBLE);
         else
            parentLayout.setVisibility(View.GONE);
      } else {
         parentLayout.setVisibility(View.GONE);
      }
      
      applicationSettings = new ApplicationSettings(context);
      initNightMode(context);
   }
   
   private void initNightMode(Context context) {
      if(applicationSettings.isNightModeEnabled()) {
         sectionTitleQuotes.setTextColor(context.getResources().getColor(R.color.night_mode_text));
         sectionTitleAbyss.setTextColor(context.getResources().getColor(R.color.night_mode_text));
         
         baseLyt.setBackgroundColor(context.getResources().getColor(R.color.night_mode_background));
         titleLyt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quotes_title_background));
         
         newQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         randomQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         bestQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         byRatingQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         likedBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         
         abyssBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         abyssTopBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));
         abyssBestBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.night_mode_quote_menu_sub_item_click_background));         
      } else {
         sectionTitleQuotes.setTextColor(context.getResources().getColor(R.color.light_mode_text));
         sectionTitleAbyss.setTextColor(context.getResources().getColor(R.color.light_mode_text));
         
         baseLyt.setBackgroundColor(context.getResources().getColor(R.color.light_mode_quotes_menu_background));
         titleLyt.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quotes_title_background));
         
         newQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         randomQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         bestQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         byRatingQuotesBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         likedBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         
         abyssBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         abyssTopBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
         abyssBestBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.light_mode_quote_menu_sub_item_click_background));
      }
   }

   public void setOnClickListener(OnClickListener onClickListener) {
      newQuotesBtn.setOnClickListener(onClickListener);
      randomQuotesBtn.setOnClickListener(onClickListener);
      bestQuotesBtn.setOnClickListener(onClickListener);
      byRatingQuotesBtn.setOnClickListener(onClickListener);
      likedBtn.setOnClickListener(onClickListener);
      
      abyssBtn.setOnClickListener(onClickListener);
      abyssTopBtn.setOnClickListener(onClickListener);
      abyssBestBtn.setOnClickListener(onClickListener);
   }

   public void toggleMenu() {
      if (parentLayout.getVisibility() == View.GONE) {
         parentLayout.setVisibility(View.VISIBLE);
         parentLayout.startAnimation(inAnimation);
      } else
         parentLayout.startAnimation(outAnimation);
   }

   public boolean isMenuVisible() {
      return parentLayout.getVisibility() == View.VISIBLE;
   }

   public void saveState(Bundle saveState) {
      saveState.putBoolean(MENU_VISIBLE_STATE, parentLayout.getVisibility() == View.VISIBLE);
   }

   @Override
   public void onAnimationEnd(Animation animation) {
      parentLayout.setVisibility(View.GONE);
   }

   @Override
   public void onAnimationRepeat(Animation animation) {
   }

   @Override
   public void onAnimationStart(Animation animation) {
   }

   @Override
   public void onClick(View v) {
      toggleMenu();
   }

}
