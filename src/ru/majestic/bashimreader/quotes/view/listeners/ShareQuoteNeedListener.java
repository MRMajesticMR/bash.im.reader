package ru.majestic.bashimreader.quotes.view.listeners;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.dialogs.SelectedQuoteActionsListDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

public class ShareQuoteNeedListener implements OnClickListener {

   private Quote     quote;
   private Context   context;
   
   public ShareQuoteNeedListener(Context context, Quote quote) {
      this.context   = context;
      this.quote     = quote;
   }
   
   @Override
   public void onClick(View v) {
      final SelectedQuoteActionsListDialog dialog = new SelectedQuoteActionsListDialog(context, quote);
      dialog.show();
   }

   
   
}
