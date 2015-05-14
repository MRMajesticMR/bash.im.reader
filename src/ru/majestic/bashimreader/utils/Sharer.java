package ru.majestic.bashimreader.utils;

import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.data.Quote;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.widget.Toast;

public class Sharer {
   
   private static final String SHARE_APPLICATION_ANNOTATION = "Отправлено с помощью Bash im Reader. \n " +
   		"https://play.google.com/store/apps/details?id=ru.majestic.bashimreader";

   public static final void share(Context context, Quote quote) {
      final String text = Html.fromHtml(quote.getContent()).toString();
      
      final Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setType("text/plain");
      intent.putExtra(Intent.EXTRA_TEXT, text + "\n\n" + QuotesDictionary.URL_QUOTES_NEW + QuotesDictionary.PREFIX_SINGLE_QUOTES_PAGE + quote.getId() + "\n\n" + SHARE_APPLICATION_ANNOTATION);
      try {
         context.startActivity(Intent.createChooser(intent, "Куда отправим?"));
      } catch (ActivityNotFoundException ex) {
         Toast.makeText(context, "Упс... У Вас нет ни одного приложения, через которое можно отправить цитату...", Toast.LENGTH_LONG).show();
      }
   }
   
   public static final void share(final Context context, final Comics comics) {
	      final Intent intent = new Intent(Intent.ACTION_SEND);
	      intent.setType("text/plain");
	      intent.putExtra(Intent.EXTRA_TEXT, "Зацени комикс с bash.im! \n\n" + ComicsDictionary.URL_COMICS_HOST + comics.getTitle() + "." + comics.getFormat() + "\n\n" + SHARE_APPLICATION_ANNOTATION);
	      try {
	         context.startActivity(Intent.createChooser(intent, "Куда отправим?"));
	      } catch (ActivityNotFoundException ex) {
	         Toast.makeText(context, "Упс... У Вас нет ни одного приложения, через которое можно отправить комикс...", Toast.LENGTH_LONG).show();
	      }
	   }

}
