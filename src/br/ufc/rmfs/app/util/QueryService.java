package br.ufc.rmfs.app.util;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufc.rmfs.app.MainActivity;
import br.ufc.rmfs.app.model.Point;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

/*
 * Chamar essa Intent para fazer as requisicoes as URLs.
 */

public class QueryService extends IntentService {

	private static final String TAG = QueryService.class.getName();

	public static final int STATUS_RUNNING = 1;
	public static final int STATUS_FINISHED = 2;
	public static final int STATUS_ERROR = 4;
	public static final int STATUS_TAG_OK = 10;
	public static final int STATUS_TAG_NOK = -10;

	private String url = "http://dbpedia.org";

	public QueryService() {
		super("QueryService");
		// TODO Auto-generated constructor stub
	}

	protected void onHandleIntent(Intent intent) {

		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		JSONObject result = null;
		String command = intent.getStringExtra("command");
		//String entity = intent.getStringExtra("query");
		ArrayList<Point> listaDePontos = new ArrayList<Point>();

		Bundle data = new Bundle();

		if (command.equals("query_points")) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);

			HttpCaller httpcaller = new HttpCaller();

			result = httpcaller.get(url + "/sparql?default-graph-uri="
					+ URLEncoder.encode("http://dbpedia.org") + "&query="
					+ URLEncoder.encode(MainActivity.queryString) + "&output=json");

//			Log.d(TAG, result.toString());

			JSONObject jo_results;
			try {
				jo_results = result.getJSONObject("results");

//				Log.d(TAG, jo_results.toString());
				JSONArray jo_bind_arrays = jo_results.getJSONArray("bindings");
//				Log.d(TAG, String.valueOf(jo_bind_arrays.length()));
				for (int i = 0; i < jo_bind_arrays.length(); i++) {
					JSONObject place = jo_bind_arrays.getJSONObject(i);
					JSONObject label = place.getJSONObject("label");
					JSONObject lat = place.getJSONObject("lat");
					JSONObject lng = place.getJSONObject("long");
					Point novaTag = new Point(label.getString("value"), "", "", "",
							 0,  lng.getDouble("value"), lat.getDouble("value"));
					listaDePontos.add(novaTag);
				}
				data.putParcelableArrayList("results", listaDePontos);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			receiver.send(STATUS_FINISHED, data);

		}

		this.stopSelf();
	}

}