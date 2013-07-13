package br.ufc.rmfs.app.model;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * * @author Shriram Shri Shrikumar * * A basic object that can be parcelled to
 * * transfer between objects *
 */
public class Tag implements Parcelable {

	private static final String TAG = Tag.class.getName();

	private JSONObject tag = new JSONObject();
	private JSONObject tagContent = new JSONObject();
	private JSONObject tagCoords = new JSONObject();
	private JSONObject tagImageAttached = new JSONObject();

	private String title;
	private String content;
	private String url;
	private String owner_id = "0";
	private String imagePath = "noImage";
	private Integer votes = 0;
	private Float lng = 0f;
	private Float lat = 0f;

	public Tag(String title, String content, String url, String owner_id,
			String imagePath, int votes, float lng, float lat) {

		this.title = title;
		this.content = content;
		this.url = url;
		this.imagePath = imagePath;
		this.votes = votes;
		this.lng = lng;
		this.lat = lat;

	}

	public Tag() {
	};

	public Tag(Parcel in) {
		readFromParcel(in);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	public Float getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
/*		
  		String title, 
  		String content, 
  		String url, 
  		String owner_id,
		String imagePath, 
		int votes, 
		float lng, 
		float lat
*/	
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(url);
		dest.writeString(owner_id);
		dest.writeString(imagePath);
		dest.writeInt(votes);
		dest.writeFloat(lng);
		dest.writeFloat(lat);
	}

	private void readFromParcel(Parcel in) {
		
		title = in.readString();
		content = in.readString();
		url = in.readString();
		owner_id = in.readString();
		imagePath = in.readString();
		votes = in.readInt();
		lng = in.readFloat();
		lat = in.readFloat();
	}

	public String toJsonString() {
		try {
			tagContent.put("title", title);
			tagContent.put("content", content);
			tagContent.put("owner_id", owner_id);
			tagContent.put("url", url);
			tagContent.put("votes", 0);

			tagCoords.put("long", lng);
			tagCoords.put("lat", lat);
			tagImageAttached.put("image", "NOPATH");

			tag.put("tag", tagContent);
			tag.put("coords", tagCoords);
			tag.put("img", tagImageAttached);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tag.toString();
	}

	public ArrayList<NameValuePair> toNameValuePair(){
		ArrayList<NameValuePair> listValues = new ArrayList<NameValuePair>();
		listValues.add(new BasicNameValuePair("tag[title]", getTitle()));
		listValues.add(new BasicNameValuePair("tag[content]", getContent()));
		listValues.add(new BasicNameValuePair("tag[url]", getUrl()));
		listValues.add(new BasicNameValuePair("tag[owner_id]", getOwner_id()));
		listValues.add(new BasicNameValuePair("coords[long]", String.valueOf(getLng())));
		listValues.add(new BasicNameValuePair("coords[lat]",  String.valueOf(getLat())));
		listValues.add(new BasicNameValuePair("img[image]", getImagePath()));	
		return listValues;
	}
	
	// Codificar imagem em Base64 para enviar imagem utilizando JSON
	/*protected String toBase64(String imagePath) {
		Bitmap bitmapOrg = BitmapFactory.decodeFile(imagePath);
		java.io.ByteArrayOutputStream bao = new java.io.ByteArrayOutputStream();
		bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 90, bao);
		byte[] ba = bao.toByteArray();
		String imageBase64 = br.ufc.rmfs.app.util.Base64.encodeBytes(ba);
		return imageBase64;
	}*/


	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Tag createFromParcel(Parcel in) {
			return new Tag(in);
		}

		public Tag[] newArray(int size) {
			return new Tag[size];
		}
	};
}