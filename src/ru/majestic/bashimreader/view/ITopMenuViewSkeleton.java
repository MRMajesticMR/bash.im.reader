package ru.majestic.bashimreader.view;

import ru.majestic.bashimreader.view.listeners.TopMenuStateListener;

public abstract class ITopMenuViewSkeleton implements ITopMenuView {

   protected TopMenuStateListener topMenuStateListener;

   @Override
   public void setTopMenuStateListener(TopMenuStateListener topMenuStateListener) {
      this.topMenuStateListener = topMenuStateListener;
   }

}
