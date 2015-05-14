package ru.majestic.bashimreader.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ru.majestic.bashimreader.data.Comics;
import ru.majestic.bashimreader.utils.ComicsDictionary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ComicsFileManager {


   private static final void savePicture(final Comics pComics) {
      File comicsDir = getComicsRootFolder();

      File file = new File(comicsDir, pComics.getTitle() + "." + pComics.getFormat());
      if (!file.exists()) {
         try {
            FileOutputStream out = new FileOutputStream(file);
            if (pComics.getFormat().equals("jpg"))
               pComics.getImage().compress(Bitmap.CompressFormat.JPEG, 100, out);
            else
               pComics.getImage().compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
            out.close();

         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }
   
   public static final void saveComics(final Comics pComics) {
      savePicture(pComics);
   }
   
   private static final File getComicsRootFolder() {
      String root = Environment.getExternalStorageDirectory().toString();

      File comicsDir = new File(root + ComicsDictionary.COMICS_ROOT_PATH);
      if (!comicsDir.exists())
         comicsDir.mkdirs();
      
      return comicsDir;
   }
   
   public static final Bitmap getPicture(final Comics pComics) throws FileNotFoundException {
      File comics = new File(getComicsRootFolder(), pComics.getTitle() + "." + pComics.getFormat());
      if(comics.exists()) {
         return BitmapFactory.decodeFile(comics.getPath());
      } else {
         throw new FileNotFoundException();
      }
   }

}
