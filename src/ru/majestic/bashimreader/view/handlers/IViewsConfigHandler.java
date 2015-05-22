package ru.majestic.bashimreader.view.handlers;

import ru.majestic.bashimreader.view.IView;

public interface IViewsConfigHandler {

   public void addView           (IView view);
   public void enableNightMode   (boolean enable);
   public void onFontSizeChanged (int fontSize);
   
}
