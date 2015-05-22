package ru.majestic.bashimreader.quotes.sections;

import ru.majestic.bashimreader.quotes.sections.impl.NewQuotesSectionManager;

public class QuoteSectionManagersFactory {

   public enum SectionType {
      NEW_QUOTES
   }
   
   public static IQuotesSectionManager generateQuotesSectionManger(SectionType sectionType) {
      switch(sectionType) {
      case NEW_QUOTES :
         return new NewQuotesSectionManager();
         
      default:
         return null;
      }
   }
   
}
