package br.ufc.rmfs.app;

import org.json.JSONObject;

import br.ufc.rmfs.app.model.Tag;
import br.ufc.rmfs.app.model.Tag_old;
import br.ufc.rmfs.app.util.MediaUtility;
import br.ufc.rmfs.app.util.QueryService;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class TextActivity extends Activity implements MyResultReceiver.Receiver {

	public final static int LOADIMAGE = 100;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private Uri fileUri;


	private String imageBasePath;
	private ImageView loadedImage;
	private Button btnSend, btnLoadImage, btnLoadCam;
	private EditText edtTxtContent;
	private EditText edTxtTitle;
	private EditText edTxtLong;
	private EditText edTxtLat;
	public MyResultReceiver mReceiver;
	private double[] coords = new double[2];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text);

		mReceiver = new MyResultReceiver(new Handler());
		mReceiver.setReceiver(this);

		loadedImage = (ImageView) findViewById(R.id.loadedImage);
		btnLoadImage = (Button) findViewById(R.id.btnLoadImage);
		btnLoadCam = (Button) findViewById(R.id.btnLoadCam);
		btnSend = (Button) findViewById(R.id.btnSend);
		edtTxtContent = (EditText) findViewById(R.id.edTxtContent);
		edTxtTitle = (EditText) findViewById(R.id.edTxtTitle);
		edTxtLong = (EditText) findViewById(R.id.edTxtLong);
		edTxtLat = (EditText) findViewById(R.id.edTxtLat);

		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				// Called when a new location is found by the network location
				// provider.
				makeUseOfNewLocation(location);
			}

			private void makeUseOfNewLocation(Location location) {
				// TODO Auto-generated method stub
				coords[0] = location.getLongitude();
				coords[1] = location.getLatitude();
				edTxtLong.setText(String.valueOf(coords[0]));
				edTxtLat.setText(String.valueOf(coords[1]));
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

		// Define ações para os botões
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tag novaTag = new Tag(edTxtTitle.getText().toString(),
						edtTxtContent.getText().toString().toString(),
						"no url", "0", imageBasePath , 0, (float) coords[0], (float) coords[1]);
				final Intent intent = new Intent(Intent.ACTION_SYNC, null,
						TextActivity.this, QueryService.class);
				intent.putExtra("receiver", mReceiver);
				
				//intent.putExtra("command", "query");
				//intent.putExtra("json_request", novaTag.toJsonString());

				intent.putExtra("command", "add_tag_post");
				intent.putExtra("tag", novaTag);
				//intent.putExtra("tag", "teste");
				
				startService(intent);

			}

		});

		btnLoadImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, LOADIMAGE);
			}
		});
		
		btnLoadCam.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			    // create Intent to take a picture and return control to the calling application
			    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			    fileUri = MediaUtility.getOutputMediaFileUri(MediaUtility.MEDIA_TYPE_IMAGE); // create a file to save the image
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
			}
		});
		
		// Habilita/desabilita botão para aquisição de imagem da camera.
		if(haveCameraHardware(getApplicationContext())) 
			btnLoadCam.setEnabled(true);
		else 
			btnLoadCam.setEnabled(false);

		
	}

	
	/*
	 * public void onPause() { mReceiver.setReceiver(null); // clear receiver so
	 * no leaks. }
	 */

	// Identifica se o dispositivo tem camera	
	private boolean haveCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LOADIMAGE && resultCode == RESULT_OK && null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String imagePath = cursor.getString(columnIndex);
			cursor.close();
			loadedImage.setImageBitmap(BitmapFactory.decodeFile(imagePath));	
			imageBasePath = imagePath;
		}
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Image saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        }
	    }

	    /*if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Video captured and saved to fileUri specified in the Intent
	            Toast.makeText(this, "Video saved to:\n" +
	                     data.getData(), Toast.LENGTH_LONG).show();
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the video capture
	        } else {
	            // Video capture failed, advise user
	        }
	    }*/
	}

	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		Toast t = null;
		switch (resultCode) {
		case QueryService.STATUS_RUNNING:
			break;
		case QueryService.STATUS_FINISHED:
			break;
		case QueryService.STATUS_ERROR:
			break;
		case QueryService.STATUS_TAG_OK:
			t = Toast.makeText(getApplicationContext(), "Tag Adicionada!",
					Toast.LENGTH_SHORT);
			t.show();
			edtTxtContent.setText("");
			edTxtTitle.setText("");
			break;
		case QueryService.STATUS_TAG_NOK:
			t = Toast.makeText(getApplicationContext(), "!Tag Adicionada",
					Toast.LENGTH_SHORT);
			t.show();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text, menu);
		return true;
	}

	
}
