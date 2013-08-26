package br.ufc.rmfs.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Point implements Parcelable {

	private static final String TAG = Point.class.getName();

	private String title;
	private String content;
	private String url;
	private String imagePath = "noImage";
	private double lng = 0f;
	private double lat = 0f;

	public Point(String title, String content, String url, String imagePath,
			int votes, double lng, double lat) {

		this.title = title;
		this.content = content;
		this.url = url;
		this.imagePath = imagePath;
		this.lng = lng;
		this.lat = lat;

	}

	public Point() {
	};

	public Point(Parcel in) {
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

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(Float lng) {
		this.lng = lng;
	}

	public double getLat() {
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
		 * String title, String content, String url, String owner_id, String
		 * imagePath, int votes, float lng, float lat
		 */
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(url);
		dest.writeString(imagePath);
		dest.writeDouble(lng);
		dest.writeDouble(lat);
	}

	private void readFromParcel(Parcel in) {

		title = in.readString();
		content = in.readString();
		url = in.readString();
		imagePath = in.readString();
		lng = in.readFloat();
		lat = in.readFloat();
	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public Point createFromParcel(Parcel in) {
			return new Point(in);
		}

		public Point[] newArray(int size) {
			return new Point[size];
		}
	};
}