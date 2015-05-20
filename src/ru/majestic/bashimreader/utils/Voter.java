package ru.majestic.bashimreader.utils;

import ru.majestic.bashimreader.loaders.impl.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import android.content.Context;

public class Voter {

   private static final String VOTE_BASE_URL = "http://bash.im/quote";

   public static final String VOTE_RULEZ = "rulez";
   public static final String VOTE_SUX = "sux";
   
   public static void vote(Context context, OnPageLoadListener listener, int quoteId, String type) {
      PageLoader pageLoader = new PageLoader();
      pageLoader.setOnPageLoadListener(listener);
      pageLoader.load(VOTE_BASE_URL + "/" + quoteId + "/" + type);      
   }

}
