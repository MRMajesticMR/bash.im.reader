package ru.majestic.bashimreader.view;

import ru.majestic.bashimreader.quotes.sections.QuoteSectionManagersFactory.SectionType;
import ru.majestic.bashimreader.view.listeners.TopMenuStateListener;

public interface ITopMenuView {
   
   public void refreshSectionTitle     (SectionType sectionType);
   public void setTopMenuStateListener (TopMenuStateListener topMenuStateListener);

}
