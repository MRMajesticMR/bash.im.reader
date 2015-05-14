package ru.majestic.bashimreader.datebase.exceptions;

import android.util.Log;

public class ChangeTableException extends Exception {
   
   private String errorMessage;
   
   public ChangeTableException(String errorMessage) {
      this.errorMessage = errorMessage;      
   }
   
   @Override
   public String toString() {
      return errorMessage;
   }
   
   @Override
   public void printStackTrace() {
      Log.e("DATABASE_BASH", errorMessage);
   }

}
