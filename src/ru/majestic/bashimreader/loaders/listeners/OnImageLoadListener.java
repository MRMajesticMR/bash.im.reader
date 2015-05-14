package ru.majestic.bashimreader.loaders.listeners;

import android.graphics.Bitmap;

public interface OnImageLoadListener {

   public void onImageLoaded(Bitmap pImage);
   
   public void onImageLoadError();
   
}
