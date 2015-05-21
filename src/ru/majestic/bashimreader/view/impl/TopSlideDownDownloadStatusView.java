package ru.majestic.bashimreader.view.impl;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.view.IDownloadStatusView;

public class TopSlideDownDownloadStatusView implements   IDownloadStatusView,
                                                         AnimationListener {

   private Animation downloadViewOutAnimation;
   private Animation downloadViewInAnimation;
   
   private ViewGroup       downloadQuotesView;
   
   public TopSlideDownDownloadStatusView(Activity activity) {      
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
