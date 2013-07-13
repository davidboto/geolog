package br.ufc.rmfs.app.model;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Tag_old {

	private static final String TAG = Tag_old.class.getName();
	
	private JSONObject tag = new JSONObject();
	private JSONObject tagContent = new JSONObject();
	private JSONObject tagCoords = new JSONObject();
	private JSONObject tagImageAttached = new JSONObject();

	public Tag_old(){
	
	}
	
	/*
	 * title: nil, content: nil, url: nil, owner_id: nil, votes: nil, location:
	 * nil, created_at: nil
	 */
	public Tag_old(String title, String content, String url, String owner_id, String imagePath,
			int votes, float lng, float lat) {

		try {
			tagContent.put("title", title);
			tagContent.put("content", content);
			tagContent.put("owner_id", owner_id);
			tagContent.put("url", url);
			tagContent.put("votes", 0);

			tagCoords.put("long", lng);
			tagCoords.put("lat", lat);
			tagImageAttached.put("image", toBase64(imagePath));
			
			tag.put("tag", tagContent);
			tag.put("coords", tagCoords);
			tag.put("img", tagImageAttached);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toJsonString(){
		return tag.toString();
	}

	protected String toBase64(String imagePath){
		Bitmap bitmapOrg = BitmapFactory.decodeFile(imagePath);
		java.io.ByteArrayOutputStream bao = new java.io.ByteArrayOutputStream(); 
		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao); 
		byte [] ba = bao.toByteArray();
		String imageBase64 = br.ufc.rmfs.app.util.Base64.encodeBytes(ba);
		return imageBase64;
	}
	
}
