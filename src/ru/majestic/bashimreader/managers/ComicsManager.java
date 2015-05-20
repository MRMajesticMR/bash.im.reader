package ru.majestic.bashimreader.managers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.datebase.QuotesDatebaseManager;
import ru.majestic.bashimreader.file.ComicsFileManager;
import ru.majestic.bashimreader.loaders.ComicsLoader;
import ru.majestic.bashimreader.loaders.impl.PageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnComicsLoadListener;
import ru.majestic.bashimreader.loaders.listeners.OnImageLoadListener;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import ru.majestic.bashimreader.utils.ComicsDictionary;

public class ComicsManager implements OnPageLoadListener, OnImageLoadListener {
   
   private static final String LAST_COMICS_PAGE_URL = "LAST_COMICS_PAGE_URL";

   private PageLoader mPageLoader;
   private String mLastComicsPageURL;
   private OnComicsLoadListener mOnComicsLoadListener;
   private Comics mComics;
   private boolean mLoading;
   private final QuotesDatebaseManager mQuotesDatebaseManager;
   
   public ComicsManager(final Context pContext, final Bundle pSavedState) {
      this.mQuotesDatebaseManager = new QuotesDatebaseManager(pContext);  
      if(pSavedState != null && pSavedState.containsKey(LAST_COMICS_PAGE_URL)) {
         mLastComicsPageURL = pSavedState.getString(LAST_COMICS_PAGE_URL);
      }
   }
   
   public final void saveState(final Bundle pSavedInstance) {
      pSavedInstance.putString(LAST_COMICS_PAGE_URL, mLastComicsPageURL);
   }

   public void start() {
      if(mLastComicsPageURL == null) {
         mLoading = true;
         mPageLoader = new PageLoader();
         mPageLoader.setOnPageLoadListener(this);
         mPageLoader.load(ComicsDictionary.URL_ROOT_PAGE);
      }
   }

   public void setOnComicsLoadListener(OnComicsLoadListener pOnComicsLoaderListener) {
      this.mOnComicsLoadListener = pOnComicsLoaderListener;
   }

   public void loadNextComics() {
      if (!mLoading) {
         mLoading = true;
         mPageLoader = new PageLoader();
         mPageLoader.setOnPageLoadListener(this);
         mPageLoader.load(ComicsDictionary.URL_ROOT_PAGE + mLastComicsPageURL);
      }
   }

   @Override
   public void onPageLoad(final String pContent) {
      mLastComicsPageURL = getLastPageURL(pContent);
      mComics = getComicsFromPage(pContent);
      startLoadComicsPicture(mComics);
   }

   private Comics getComicsFromPage(final String pContent) {
      Document doc = Jsoup.parse(pContent);
      return new Comics(getComicsTitle(doc), "", -1, getComicsType(doc), getComicsDate(doc));
   }

   private String getComicsTitle(final Document pDocument) {
      Element comicsImage = pDocument.getElementById("cm_strip");
      String imageUrl = comicsImage.attr("src");
      return imageUrl.replace(ComicsDictionary.URL_COMICS_HOST, "").replace(".png", "").replace(".jpg", "").replace(".jpeg", "");
   }

   private String getComicsType(final Document pDocument) {
      Element comicsImage = pDocument.getElementById("cm_strip");
      String imageUrl = comicsImage.attr("src");
      return getComicsType(imageUrl);
   }

   private String getComicsType(final String pUrl) {
      if (pUrl.endsWith(".jpg") || pUrl.endsWith(".jpeg")) {
         return "jpg";
      } else {
         return "png";
      }
   }

   private String getComicsDate(final Document pDocument) {
      Elements dateElements = pDocument.getElementsByClass("current");
      String date = dateElements.get(3).text();
      return date;
   }

   private String getLastPageURL(final String pContent) {
      Document doc = Jsoup.parse(pContent);
      Elements lastPageElement = doc.getElementsByClass("arr");
      return lastPageElement.get(1).attr("href").replace("comics/", "");
   }

   private final void startLoadComicsPicture(final Comics pComics) {
      ComicsLoader comicsLoader = new ComicsLoader();
      comicsLoader.setOnImageLoadListener(this);
      comicsLoader.execute(pComics);
   }

   @Override
   public void onPageLoadError() {
      mLoading = false;
      mOnComicsLoadListener.onComicsLoadError();
   }

   @Override
   public void onImageLoaded(Bitmap pImage) {
      mLoading = false;
      mComics.setImage(pImage);
      try{
         mQuotesDatebaseManager.saveNewComics(mComics);
         ComicsFileManager.saveComics(mComics);
      } catch (Exception e) {
         Log.w("COMICS", "Comics not saved in cache." + e.toString());
      }
      mOnComicsLoadListener.onComicsLoad(mComics);
   }

   @Override
   public void onImageLoadError() {
      mLoading = false;
      mOnComicsLoadListener.onComicsLoadError();
   }
}
