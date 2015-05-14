package ru.majestic.bashimreader.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import ru.majestic.bashimreader.data.Quote;

public class BufferSaver {

   
   public static final void saveInBuffer(final Context context, final Quote quote) {
      ClipboardManager man = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
      ClipData data = ClipData.newPlainText("Quote", Html.fromHtml(quote.getContent()).toString());
      man.setPrimaryClip(data);
   }
}
