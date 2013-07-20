package br.ufc.rmfs.app;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.View.OnClickListener;
import br.ufc.rmfs.app.model.Point;
import br.ufc.rmfs.app.util.QueryService;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements
		MyResultReceiver.Receiver {

	// private static final String queryString =
	// "http%3A%2F%2Fdbpedia.org&query=PREFIX+owl%3A+<http%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23>%0D%0APREFIX+xsd%3A+<http%3A%2F%2Fwww.w3.org%2F2001%2FXMLSchema%23>%0D%0APREFIX+rdfs%3A+<http%3A%2F%2Fwww.w3.org%2F2000%2F01%2Frdf-schema%23>%0D%0APREFIX+rdf%3A+<http%3A%2F%2Fwww.w3.org%2F1999%2F02%2F22-rdf-syntax-ns%23>%0D%0APREFIX+foaf%3A+<http%3A%2F%2Fxmlns.com%2Ffoaf%2F0.1%2F>%0D%0APREFIX+dc%3A+<http%3A%2F%2Fpurl.org%2Fdc%2Felements%2F1.1%2F>%0D%0APREFIX+%3A+<http%3A%2F%2Fdbpedia.org%2Fresource%2F>%0D%0APREFIX+dbpedia2%3A+<http%3A%2F%2Fdbpedia.org%2Fproperty%2F>%0D%0APREFIX+dbpedia%3A+<http%3A%2F%2Fdbpedia.org%2F>%0D%0APREFIX+skos%3A+<http%3A%2F%2Fwww.w3.org%2F2004%2F02%2Fskos%2Fcore%23>%0D%0APREFIX+geo%3A+<http%3A%2F%2Fwww.w3.org%2F2003%2F01%2Fgeo%2Fwgs84_pos%23>%0D%0ASELECT+%3Fsubject+%3Flabel+%3Flat+%3Flong+WHERE+{%0D%0A<http%3A%2F%2Fdbpedia.org%2Fresource%2FEiffel_Tower>+geo%3Alat+%3FeiffelLat.%0D%0A<http%3A%2F%2Fdbpedia.org%2Fresource%2FEiffel_Tower>+geo%3Along+%3FeiffelLong.%0D%0A%3Fsubject+geo%3Alat+%3Flat.%0D%0A%3Fsubject+geo%3Along+%3Flong.%0D%0A%3Fsubject+rdfs%3Alabel+%3Flabel.%0D%0AFILTER(%3Flat+-+%3FeiffelLat+<%3D+0.05+%26%26+%3FeiffelLat+-+%3Flat+<%3D+0.05+%26%26%0D%0A%3Flong+-+%3FeiffelLong+<%3D+0.05+%26%26+%3FeiffelLong+-+%3Flong+<%3D+0.05+%26%26%0D%0Alang(%3Flabel)+%3D+\"en\"%0D%0A).%0D%0A}+LIMIT+20&output=json";

	public static final String queryString = ""
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
			+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>"
			+ "PREFIX : <http://dbpedia.org/resource/>"
			+ "PREFIX dbpedia2: <http://dbpedia.org/property/>"
			+ "PREFIX dbpedia: <http://dbpedia.org/>"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
			+ "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> "
			+ "SELECT ?label ?lat ?long WHERE { "
			+ "?subject geo:lat ?lat. "
			+ "?subject geo:long ?long. "
			+ "?subject rdfs:label ?label. "
			+ "?subject dbpedia-owl:location <http://dbpedia.org/resource/Brazil>. "
			+ "?subject rdf:type dbpedia-owl:Place "
			+ "FILTER(lang(?label) = \"en\"). " + "} LIMIT 100 ";

	private static final String TAG = MainActivity.class.getName();
	private GoogleMap mMap;
	private Button btnCarregar;
	public MyResultReceiver mReceiver;
	private ProgressDialog mProgressBar ;
	private ArrayList<Point> listaDePontos = new ArrayList<Point>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);
		mProgressBar = new ProgressDialog(this);
		
		btnCarregar = (Button) findViewById(R.id.btnCarregar);

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		btnCarregar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Intent intent = new Intent(Intent.ACTION_SYNC, null,
						MainActivity.this, QueryService.class);
				intent.putExtra("receiver", mReceiver);
				intent.putExtra("command", "query_points");
//				intent.putExtra("query", queryString);
				startService(intent);
			}
		});
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case QueryService.STATUS_RUNNING:

			mProgressBar.show();
			Toast.makeText(this, 			"SELECT ?label ?lat ?long WHERE { "
			+ "?subject geo:lat ?lat. "
			+ "?subject geo:long ?long. "
			+ "?subject rdfs:label ?label. "
			+ "?subject dbpedia-owl:location <http://dbpedia.org/resource/Brazil>. "
			+ "?subject rdf:type dbpedia-owl:Place "
			+ "FILTER(lang(?label) = \"en\"). " + "} LIMIT 100 ", Toast.LENGTH_LONG).show();
			break;
		case QueryService.STATUS_FINISHED:
			
			listaDePontos = resultData.getParcelableArrayList("results");
			if (listaDePontos != null) {
				loadtags(listaDePontos);
			}
			mProgressBar.cancel();
			break;
		case QueryService.STATUS_ERROR:
			break;

		}
	}
	
	private void loadtags(ArrayList<Point> tagList2) {
		// TODO Auto-generated method stub
		for (Point tag : tagList2) {
			mMap.addMarker(new MarkerOptions()
			.position(new LatLng(tag.getLat(), tag.getLng()	))
			.title(tag.getTitle())
					);
			
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
					new LatLng(tag.getLat(), tag.getLng()), 5.0f));
			
//			Log.d(TAG, String.valueOf(tag.getLat()));
//			Log.d(TAG, String.valueOf(tag.getLng()));
		}
	}

}
