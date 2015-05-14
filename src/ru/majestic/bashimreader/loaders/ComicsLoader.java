package ru.majestic.bashimreader.loaders;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.file.ComicsFileManager;
import ru.majestic.bashimreader.loaders.listeners.OnComicsLoadListener;
import ru.majestic.bashimreader.loaders.listeners.OnImageLoadListener;
import ru.majestic.bashimreader.utils.ComicsDictionary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ComicsLoader extends AsyncTask<Comics, Integer, Bitmap> {

   private static final int TIMEOUT_CONNECTION = 5000;
   private static final int TIMEOUT_SOCKET = 30000;

   private HttpParams mHttpParameters;
   private OnImageLoadListener mOnImageLoadListener;

   private Comics mLoadingComics;

   public ComicsLoader() {
      initHttpParameters();
   }

   private final void initHttpParameters() {
      mHttpParameters = new BasicHttpParams();

      HttpConnectionParams.setConnectionTimeout(mHttpParameters, TIMEOUT_CONNECTION);
      HttpConnectionParams.setSoTimeout(mHttpParameters, TIMEOUT_SOCKET);

      HttpProtocolParams.setContentCharset(mHttpParameters, "UTF-8");
      HttpProtocolParams.setHttpElementCharset(mHttpParameters, "UTF-8");
   }

   public void setOnImageLoadListener(OnImageLoadListener pOnImageLoadListener) {
      this.mOnImageLoadListener = pOnImageLoadListener;
   }

   @Override
   protected Bitmap doInBackground(Comics... params) {
      mLoadingComics = params[0];

      try {         
         return ComicsFileManager.getPicture(mLoadingComics);
      } catch (FileNotFoundException e) {}

      String url = ComicsDictionary.URL_COMICS_HOST + mLoadingComics.getTitle() + "." + mLoadingComics.getFormat();

      HttpClient httpclient = new DefaultHttpClient(mHttpParameters);
      httpclient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);

      HttpGet httpget = new HttpGet(url);

      try {
         HttpResponse response = httpclient.execute(httpget);
         if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
            return null;
         InputStream in = response.getEntity().getContent();
         return BitmapFactory.decodeStream(in);
      } catch (ClientProtocolException e) {
         return null;
      } catch (IOException e) {
         return null;
      }
   }

   @Override
   protected void onPostExecute(Bitmap result) {
      if (result == null)
         mOnImageLoadListener.onImageLoadError();
      else {
         mOnImageLoadListener.onImageLoaded(result);
      }
   }
}
