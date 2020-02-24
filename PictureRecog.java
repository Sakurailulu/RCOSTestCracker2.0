//https://stackabuse.com/tesseract-simple-java-optical-character-recognition/

<dependency> 
 <groupId>net.sourceforge.tess4j</groupId> 
 <artifactId>tess4j</artifactId> 
 <version>3.2.1</version> 
</dependency>


package com.amudabadmus.awfa;
import net.sourceforge.tess4j.*;
import java.io.*;
public class App {
    public String getImgText(String imageLocation) {
      ITesseract instance = new Tesseract();
      try 
      {
         String imgText = instance.doOCR(new File(imageLocation));
         return imgText;
      } 
      catch (TesseractException e) 
      {
         e.getMessage();
         return "Error while reading image";
      }
   }
   public static void main ( String[] args)
   {
      App app = new App();
      System.out.println(app.getImgText("C:\\Users\\User\\Pictures\\img.png"));
   }
}
