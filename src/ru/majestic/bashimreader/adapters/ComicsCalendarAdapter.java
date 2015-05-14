package ru.majestic.bashimreader.adapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import ru.majestic.bashimreader.R;
import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.preference.ApplicationSettings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ComicsCalendarAdapter extends BaseAdapter {
   
   private static final String SAVED_INSTANCE_STATE = "COMICS_ARRAY";
   
   private List<Comics> mComicses = new ArrayList<Comics>();
   private final Context mContext;
   private final ApplicationSettings mApplicationSettings;
   private int lastSelectedComicsIndex;
   
   public ComicsCalendarAdapter(final Context pContext, final Bundle pSavedInstance) {
      this.mContext = pContext;
      this.mApplicationSettings = new ApplicationSettings(pContext);
      if(pSavedInstance != null && pSavedInstance.containsKey(SAVED_INSTANCE_STATE)) {
         try{
            JSONArray array = new JSONArray(pSavedInstance.getString(SAVED_INSTANCE_STATE));
            for(int i = 0; i < array.length(); i++) {
               mComicses.add(new Comics(array.getJSONObject(i)));
            }
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }
   }
   
   public void addComics(Comics pComics) {
      mComicses.add(pComics);
      notifyDataSetChanged();
   }

   @Override
   public int getCount() {
      return mComicses.size();
   }

   @Override
   public Object getItem(int pPosition) {
      return mComicses.get(pPosition);
   }
   
   public final Comics getLasSelectedComics() {
	   return (Comics) getItem(lastSelectedComicsIndex);			   
   }
   
   public final void setLastSelectedComicsIndex(int lastSelectedComicsIndex) {
	   this.lastSelectedComicsIndex = lastSelectedComicsIndex;
   }

   @Override
   public long getItemId(int pPosition) {
      return pPosition;
   }
   
   public final void saveState(final Bundle pSaveState) {
      String str = "[";
      for(Comics comics: mComicses) {         
         str += comics.toJSONObject() + ", ";                  
      }
      str = str.substring(0, str.length() - 2) + "]";
      pSaveState.putString(SAVED_INSTANCE_STATE, str);
   }

   @Override
   public View getView(int position, View convertView, ViewGroup parent) {
      LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View view = inflater.inflate(R.layout.view_comics, null);
      
      ImageView comicsImage = (ImageView) view.findViewById(R.id.view_comics_img_comics);
      TextView comicsDate = (TextView) view.findViewById(R.id.view_comics_txt_date);
      
      if(mApplicationSettings.isNightModeEnabled()) {
         comicsDate.setTextColor(mContext.getResources().getColor(R.color.night_mode_text));
      } else {
         comicsDate.setTextColor(mContext.getResources().getColor(R.color.light_mode_text));
      }
      
      comicsImage.setImageBitmap(mComicses.get(position).getStripImage());
      comicsDate.setText(mComicses.get(position).getDate());
      
      return view;
   }

}
