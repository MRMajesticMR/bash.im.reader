package ru.majestic.bashimreader.parsers.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.parsers.IQuotesPageParserSkeleton;

public class RandomQuotesPageParser extends IQuotesPageParserSkeleton {      

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
      final Elements ids      = doc.getElementsByClass("id");
      final Elements ratings  = doc.getElementsByClass("rating");  
      final Elements dates    = doc.getElementsByClass("date");

      final List<Quote> downloadedQuotes = new ArrayList<Quote>();
      
      for (int i = 0; i < texts.size(); i++) {         
         final Quote quote = new Quote(Integer.valueOf(ids.get(i).html().replace("#", "")), quoteRatingToInteger(ratings.get(i).html()), texts.get(i).html(), dates.get(i).html());
         
         downloadedQuotes.add(quote);
      }
      
      return downloadedQuotes;
   }
   
   private int quoteRatingToInteger(String rating) {
      if (rating.equals(Quote.QUOTE_RATING_NEW)) 
         return Quote.QUOTE_RATING_NEW_VALUE;
      
      if(rating.equals(Quote.QUOTE_RATING_UNKNOWN)) 
         return Quote.QUOTE_RATING_UNKNOWN_VALUE;
      

      return Integer.valueOf(rating);
   }

}
