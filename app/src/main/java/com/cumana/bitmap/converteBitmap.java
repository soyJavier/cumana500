package com.cumana.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class converteBitmap {

	public Bitmap StringToBitMap(String encodedString){
	     try{
	       byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
	       Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	       return bitmap;
	     }catch(Exception e){
	       e.getMessage();
	       return null;
	     }
	}
	
	public String BitMapToString(Bitmap bitmap){
	     ByteArrayOutputStream ByteStream=new ByteArrayOutputStream();
	     bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
	     byte [] b=ByteStream.toByteArray();
	     String temp= Base64.encodeToString(b, Base64.DEFAULT);
	     
	     return temp;
	}
	
    public Bitmap BitmapDecode(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


	public byte[] DrawableToByte(Context xtx,int drawable){
		Drawable d = xtx.getResources().getDrawable(drawable); // the drawable (Captain Obvious, to the rescue!!!)
		Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		return stream.toByteArray();
	}

	public byte[] ByteDecode(Bitmap b){

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		b.compress(Bitmap.CompressFormat.PNG,100,stream);

		return stream.toByteArray();
	}
	
}
