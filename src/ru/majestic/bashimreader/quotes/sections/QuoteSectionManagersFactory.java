package ru.majestic.bashimreader.quotes.sections;

import android.os.Bundle;
import ru.majestic.bashimreader.quotes.sections.impl.AbyssQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.AbyssTopQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.BestQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.ByRatingQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.LikedQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.NewQuotesSectionManager;
import ru.majestic.bashimreader.quotes.sections.impl.RandomQuotesSectionManager;

public class QuoteSectionManagersFactory {

   public static final int SECTION_TYPE_NEW           = 1;
   public static final int SECTION_TYPE_RANDOM        = 2;
   public static final int SECTION_TYPE_BEST          = 3;
   public static final int SECTION_TYPE_BY_RATING     = 4;
   public static final int SECTION_TYPE_LIKED         = 5;
   
   public static final int SECTION_TYPE_ABYSS         = 6;
   public static final int SECTION_TYPE_ABYSS_TOP     = 7;
   
   private static final String SAVED_SECTION_TYPE = "SAVED_SECTION_TYPE";
   
   private int currentSectionType;
   
   public QuoteSectionManagersFactory() {
      currentSectionType = SECTION_TYPE_NEW;
   }
   
   public void saveState(Bundle savedInstanceState) {
      savedInstanceState.putInt(SAVED_SECTION_TYPE, currentSectionType);
   }
   
   public void restoreState(Bundle savedInstanceState) {
      if(savedInstanceState != null)
         currentSectionType = savedInstanceState.getInt(SAVED_SECTION_TYPE, 1);
   }
   
   public void setCurrentSectionType(int sectionType) {
      this.currentSectionType = sectionType;
   }
   
   public int getCurrentSectionType() {
      return currentSectionType;
   }
   
   public IQuotesSectionManager generateQuotesSectionManger() {      
      switch(currentSectionType) {
      case SECTION_TYPE_NEW:
         return new NewQuotesSectionManager();
         
      case SECTION_TYPE_RANDOM:
         return new RandomQuotesSectionManager();
         
      case SECTION_TYPE_BEST:
         return new BestQuotesSectionManager();
         
      case SECTION_TYPE_BY_RATING:
         return new ByRatingQuotesSectionManager();
         
      case SECTION_TYPE_LIKED:
         return new LikedQuotesSectionManager();
         
      case SECTION_TYPE_ABYSS:
         return new AbyssQuotesSectionManager();
         
      case SECTION_TYPE_ABYSS_TOP:
         return new AbyssTopQuotesSectionManager();
         
      default:
         return null;
      }
   }
   
}
