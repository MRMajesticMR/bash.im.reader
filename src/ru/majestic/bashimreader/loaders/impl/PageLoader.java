package ru.majestic.bashimreader.loaders.impl;

import java.io.IOException;

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
import org.apache.http.util.EntityUtils;

import ru.majestic.bashimreader.loaders.IPageLoader;
import ru.majestic.bashimreader.loaders.listeners.OnPageLoadListener;
import android.os.AsyncTask;

public class PageLoader implements IPageLoader {
   
   private static final int TIMEOUT_CONNECTION 	= 3000;
   private static final int TIMEOUT_SOCKET 		= 5000;
   
   private OnPageLoadListener 					    onPageLoadListener;
   private HttpParams 							       mHttpParameters;
   private AsyncTask<String, Integer, String> 	 loader;
   private HttpClient							       httpClient;
   
   public PageLoader() {
      initHttpParams();
   }
   
   private final void initHttpParams() {
      mHttpParameters = new BasicHttpParams();

      HttpConnectionParams.setConnectionTimeout(mHttpParameters, TIMEOUT_CONNECTION);
      HttpConnectionParams.setSoTimeout(mHttpParameters, TIMEOUT_SOCKET);

      HttpProtocolParams.setContentCharset(mHttpParameters, "UTF-8");
      HttpProtocolParams.setHttpElementCharset(mHttpParameters, "UTF-8");
      
      httpClient = new DefaultHttpClient(mHttpParameters);
      httpClient.getParams().setParameter("http.protocol.content-charset", HTTP.UTF_8);
   }
   
   @Override
   public void load(String url) {
      loader = new AsyncTask<String, Integer, String>() {

         @Override
         protected String doInBackground(String... params) {
            try {
               final HttpResponse response = httpClient.execute(new HttpGet(params[0]));
               if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                  return null;

               return EntityUtils.toString(response.getEntity());
            } catch (ClientProtocolException e) {
               return null;
            } catch (IOException e) {
               return null;
            }
         }
         
         @Override
         protected void onPostExecute(String result) {
            if(result == null)
               onPageLoadListener.onPageLoadError();
            else
               onPageLoadListener.onPageLoad(result);
        }
      };
      loader.execute(url);
   }
   
   @Override
   public void setOnPageLoadListener(OnPageLoadListener onPageLoadListener) {
      this.onPageLoadListener = onPageLoadListener;
   }

}
