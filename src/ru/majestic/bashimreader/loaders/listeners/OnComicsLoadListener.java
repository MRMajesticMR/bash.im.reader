package ru.majestic.bashimreader.loaders.listeners;

import ru.majestic.bashimreader.data.Comics;

public interface OnComicsLoadListener {
   
   public void onComicsLoad(Comics comics);
   public void onComicsLoadError();

}
