package ru.majestic.bashimreader.quotes.vote;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.vote.listener.VoteResultListener;

public interface IVoter {

   public enum VoteStatus {
      UP,
      DOWN
   }
   
   public void vote                 (Quote quote, VoteStatus voteStatus);
   public void setVoteResultListener(VoteResultListener voteResultListener);
   
}
