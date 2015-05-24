package ru.majestic.bashimreader.parsers.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.parsers.IQuotesPageParserSkeleton;

public class AbyssBestQuotesParser extends IQuotesPageParserSkeleton {      

   @Override
   protected List<Quote> parsePageContent(String pageContent) throws IllegalArgumentException {

      try {
         Document doc = Jsoup.parse(pageContent);           
         return getQuotesFromPage(doc);
      } catch (Exception e) {
         throw new IllegalAccessError(e.toString());
      }
   }      
   
   private List<Quote> getQuotesFromPage(Document doc) {
      final Elements texts    = doc.getElementsByClass("text");
      final Elements dates    = doc.getElementsByClass("date");

      final List<Quote> downloadedQuotes = new ArrayList<Quote>();
      
      for (int i = 0; i < texts.size(); i++) {         
         downloadedQuotes.add(new Quote(0, Quote.QUOTE_RATING_NO_VALUE, texts.get(i).html(), dates.get(i).html()));
      }
      
      return downloadedQuotes;
   }

}
