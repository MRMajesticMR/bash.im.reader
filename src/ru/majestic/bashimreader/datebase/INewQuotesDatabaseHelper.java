package ru.majestic.bashimreader.datebase;

import java.util.List;

import ru.majestic.bashimreader.data.Quote;
import ru.majestic.bashimreader.datebase.tables.ITable;

public interface INewQuotesDatabaseHelper {

   public ITable        getHelperTable       ();
   
   public void          saveNewQuote         (Quote quote);
   public boolean       isQuoteAlreadySaved  (Quote quote);
   public boolean       hasQuotes            ();
   
   public List<Quote>   getNewQuotes         ();
   public void          clearNewQuotes       ();
   
}
