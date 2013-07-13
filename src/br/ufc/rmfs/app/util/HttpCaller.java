package br.ufc.rmfs.app.util;

import java.io.BufferedReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

// Imports necessários para upload de imagens
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import android.util.Log;

public class HttpCaller {

	private static final String TAG = HttpCaller.class.getName();

	public JSONObject post(String url, List<NameValuePair> nameValuePairs) {
		
		JSONObject jsonReqReturn = null;
		
		HttpClient httpClient = new DefaultHttpClient();
		
		HttpContext localContext = new BasicHttpContext();
		
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Accept", "application/json");

		try {
			MultipartEntity entity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int index = 0; index < nameValuePairs.size(); index++) {
				if (nameValuePairs.get(index).getName()
						.equalsIgnoreCase("img[image]")) {
					// If the key equals to "image", we use FileBody to transfer
					// the data
					entity.addPart(nameValuePairs.get(index).getName(),
							new FileBody(new File(nameValuePairs.get(index)
									.getValue())));
				} else {
					// Normal string data
					entity.addPart(
							nameValuePairs.get(index).getName(),
							new StringBody(nameValuePairs.get(index).getValue()));
				}
			}

			httpPost.setEntity(entity);

			HttpResponse response = httpClient.execute(httpPost, localContext);
			
			HttpEntity responseEntity = response.getEntity();

			if (responseEntity != null) {

				InputStream instream = responseEntity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					total.append(line);
				}

				Log.d(TAG, total.toString());

				try {
					jsonReqReturn = new JSONObject(total.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "JSON Exception");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return jsonReqReturn;
	}

	public JSONObject post(String url, String content) {
		JSONObject jsonRequest = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			Log.d(TAG, url);

			StringEntity se;
			se = new StringEntity(content);

			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setEntity(se);

			HttpResponse response = httpClient.execute(httpPost);

			Log.d(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					total.append(line);
				}

				Log.d(TAG, total.toString());

				try {
					jsonRequest = new JSONObject(total.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.e(TAG, "JSON Exception");
				}

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} /*
		 * catch (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		return jsonRequest;
	}

	public JSONArray request(String url) {
		JSONArray json = null;

		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(url);
		// String name;
		try {

			Log.d(TAG, url);
			// httpGet.setHeader("Content-Type", "text/html");
			HttpResponse response = httpClient.execute(httpGet);
			Log.d(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				StringBuilder total = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					total.append(line);
				}
				Log.d(TAG, total.toString());
				json = new JSONArray(total.toString());

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

}