package br.ufc.rmfs.app.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import br.ufc.rmfs.app.MainActivity;
import br.ufc.rmfs.app.model.Tag;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
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

	private String url;
	
	public QueryService() {
		super("QueryService");
		// TODO Auto-generated constructor stub
	}

	protected void onHandleIntent(Intent intent) {
		SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
	    url = settings.getString(MainActivity.PREFS_HOST_AND_PORT, "http://192.168.0.100:3000");
		
		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		JSONObject result = null;
		String command = intent.getStringExtra("command");
		String entity = intent.getStringExtra("json_str");
		//String img_loc = intent.getStringExtra("img_loc");

		Bundle data = new Bundle();

		if (command.equals("query")) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			Log.d(TAG, "STATUS_RUNNING");

			HttpCaller httpcaller = new HttpCaller();

			result = httpcaller.post(url + "/tags", entity);

			try {

				if (result.getString("result").equals("Ok"))
					receiver.send(STATUS_TAG_OK, data);
				else
					receiver.send(STATUS_TAG_NOK, data);
				// Log.d(TAG, result.getString("result"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			receiver.send(STATUS_FINISHED, data);

			// receiver.send(STATUS_ERROR, data);
			// Log.d(TAG, "STATUS_ERROR");

		}
		if (command.equals("add_tag_post")) {
			receiver.send(STATUS_RUNNING, Bundle.EMPTY);
			Log.d(TAG, "STATUS_RUNNING");

			
			HttpCaller httpcaller = new HttpCaller();
			
			Tag novaTag = intent.getParcelableExtra("tag"); 
			
			List<NameValuePair> listValues = novaTag.toNameValuePair();
			
			result = httpcaller.post(url + "/tags", listValues);

			try {

				if (result.getString("result").equals("Ok"))
					receiver.send(STATUS_TAG_OK, data);
				else
					receiver.send(STATUS_TAG_NOK, data);
				// Log.d(TAG, result.getString("result"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			receiver.send(STATUS_FINISHED, data);

			// receiver.send(STATUS_ERROR, data);
			// Log.d(TAG, "STATUS_ERROR");

		}
		this.stopSelf();
		this.stopSelf();
	}

}