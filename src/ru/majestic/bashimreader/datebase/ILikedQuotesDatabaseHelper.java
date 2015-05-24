package ru.majestic.bashimreader.datebase;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.tables.ITable;

public interface ILikedQuotesDatabaseHelper {

   public ITable        getHelperTable       ();
   
   public void          saveLikedQuote       (Quote quote);
   public boolean       isQuoteAlreadyLiked  (Quote quote);
   
   public List<Quote>   getLikedQuotes       ();
   public void          clearLikedQuotes     ();
   
   
}
