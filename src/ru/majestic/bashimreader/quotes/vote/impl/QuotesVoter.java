package ru.majestic.bashimreader.quotes.vote.impl;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.loaders.IPageLoader;
import ru.majestic.bashimreader.loaders.impl.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.quotes.vote.IVoter;
import ru.majestic.bashimreader.quotes.vote.listener.VoteResultListener;

public class QuotesVoter implements IVoter, OnPageLoadListener {

   private static final String VOTE_BASE_URL = "http://bash.im/quote";

   public static final String VOTE_RULEZ  = "rulez";
   public static final String VOTE_SUX    = "sux";
   
   private VoteResultListener    voteResultListener;
   private IPageLoader           pageLoader;   
   
   public QuotesVoter() {
      pageLoader = new PageLoader();
      pageLoader.setOnPageLoadListener(this);
   }
   
   @Override
   public void vote(Quote quote, VoteStatus voteStatus) {
      pageLoader.load(VOTE_BASE_URL + "/" + quote.getId() + "/" + voteTypeToString(voteStatus));      
   }

   @Override
   public void setVoteResultListener(VoteResultListener voteResultListener) {
      this.voteResultListener = voteResultListener;
   }           

   @Override
   public void onPageLoad(String content) {
      voteResultListener.onVoteSuccess();
   }

   @Override
   public void onPageLoadError() {
      voteResultListener.onVoteFailed();
   }
   
   private String voteTypeToString(VoteStatus voteStatus) {
      switch (voteStatus) {
      case UP:
         return VOTE_RULEZ;
         
      case DOWN:
         return VOTE_SUX;

      default:
         return null;
      }
   }


}
