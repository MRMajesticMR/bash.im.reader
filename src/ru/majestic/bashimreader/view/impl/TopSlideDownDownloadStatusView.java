package ru.majestic.bashimreader.view.impl;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.view.IDownloadStatusView;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class TopSlideDownDownloadStatusView implements   IDownloadStatusView,
                                                         AnimationListener {

   private Context context;
   
   private Animation downloadViewOutAnimation;
   private Animation downloadViewInAnimation;
   
   private ViewGroup       downloadQuotesView;
   
   public TopSlideDownDownloadStatusView(Activity activity) {
      this.context               = activity;
      
      downloadQuotesView         = (ViewGroup) activity.findViewById(R.id.quote_view_download_view);
      
      downloadViewInAnimation    = AnimationUtils.loadAnimation(activity, R.anim.download_view_in);
      downloadViewOutAnimation   = AnimationUtils.loadAnimation(activity, R.anim.download_view_out);
      
      downloadViewOutAnimation.setAnimationListener(this);
   }
   
   @Override
   public void show() {
      downloadQuotesView.setVisibility(View.VISIBLE);
      downloadQuotesView.startAnimation(downloadViewInAnimation);
   }

   @Override
   public void hide() {
      downloadQuotesView.startAnimation(downloadViewOutAnimation);
   }
   
   @Override
   public void enableNightMode(boolean enable) {
      if(enable) {
         downloadQuotesView.setBackground(context.getResources().getDrawable(R.drawable.night_mode_download_view_background));
      } else {
         downloadQuotesView.setBackground(context.getResources().getDrawable(R.drawable.light_mode_download_view_background));
      }
   }

   @Override
   public void changeFontSize(int fontSize) {
      //.      
   }
   
   @Override
   public void onAnimationEnd(Animation animation) {
      downloadQuotesView.setVisibility(View.GONE);      
   }

   @Override
   public void onAnimationStart(Animation animation) {
      //.     
   }   

   @Override
   public void onAnimationRepeat(Animation animation) {
      //.      
   }   

}
