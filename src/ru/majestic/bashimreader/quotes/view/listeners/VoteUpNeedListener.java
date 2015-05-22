package ru.majestic.bashimreader.quotes.view.listeners;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.quotes.vote.IVoter;
import ru.majestic.bashimreader.quotes.vote.IVoter.VoteStatus;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VoteUpNeedListener implements OnClickListener {

   private Quote     quote;
   private IVoter    voter;
   
   private Button upQuoteRatingBtn;
   private Button downQuoteRatingBtn;
   
   public VoteUpNeedListener(Quote quote, Button upQuoteRatingBtn, Button downQuoteRatingBtn) {
      this.quote              = quote;            
      this.upQuoteRatingBtn   = upQuoteRatingBtn;
      this.downQuoteRatingBtn = downQuoteRatingBtn;
   }
   
   public void setVoter(IVoter voter) {
      this.voter = voter;
   }
   
   @Override
   public void onClick(View v) {
      upQuoteRatingBtn.setVisibility(View.INVISIBLE);
      downQuoteRatingBtn.setVisibility(View.INVISIBLE);
      voter.vote(quote, VoteStatus.UP);      
   }

}
