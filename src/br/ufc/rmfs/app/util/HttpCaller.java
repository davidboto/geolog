package br.ufc.rmfs.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufc.rmfs.app.model.Point;

import android.util.Log;

public class HttpCaller {

	private static final String TAG = HttpCaller.class.getName();

	public JSONObject get(String url) {
		JSONObject jsonRequest = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);

		try {
			Log.d(TAG, url);

			httpGet.setHeader("Content-type", "application/json");

			HttpResponse response = httpClient.execute(httpGet);

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

}