package ru.majestic.bashimreader.view;

import ru.majestic.bashimreader.view.listeners.TopMenuStateListener;

public interface ITopMenuView {
   
   public void refreshSectionTitle     (int sectionType);
   public void setTopMenuStateListener (TopMenuStateListener topMenuStateListener);

}
