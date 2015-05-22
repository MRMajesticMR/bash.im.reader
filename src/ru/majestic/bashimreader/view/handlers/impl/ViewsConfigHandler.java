package ru.majestic.bashimreader.view.handlers.impl;

import java.util.ArrayList;
import java.util.List;

import ru.majestic.bashimreader.view.IView;
import ru.majestic.bashimreader.view.handlers.IViewsConfigHandler;

public class ViewsConfigHandler implements IViewsConfigHandler {
   
   private List<IView> views;
   
   public ViewsConfigHandler() {
      views = new ArrayList<IView>();
   }

   @Override
   public void addView(IView view) {
      if(!views.contains(view))
         views.add(view);
      else
         throw new IllegalStateException("View already exist!");
   }

   @Override
   public void enableNightMode(boolean enable) {
      for(IView view: views)
         view.enableNightMode(enable);
   }

   @Override
   public void onFontSizeChanged(int fontSize) {
      for(IView view: views)
         view.changeFontSize(fontSize);
   }

}
