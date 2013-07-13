package br.ufc.rmfs.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity  {

	public static final String PREFS_NAME = "MyPrefernces";
	public static final String PREFS_HOST_AND_PORT = "hostNport";
	
	private static final String TAG = MainActivity.class.getName();
	
	private Button btnText;
	
	// Realocar ( Somente para v. de teste )
	private Button btnSalvar;
	private EditText edTxtHostIp;
	//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnText = (Button) findViewById(R.id.btnText);
		
		// Realocar ( Somente para v. de teste )
		edTxtHostIp = (EditText) findViewById(R.id.edTxtHostIp);
		btnSalvar = (Button) findViewById(R.id.btnSalvar);;
		//
		

		btnText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(	MainActivity.this, TextActivity.class);
				startActivity(intent);
			}
		});
		
		// Realocar ( Somente para v. de teste )
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	    String url = settings.getString(PREFS_HOST_AND_PORT, "http://192.168.0.100:3000");
	    edTxtHostIp.setText(url);
	    btnSalvar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				   SharedPreferences.Editor editor = settings.edit();
				   editor.putString(PREFS_HOST_AND_PORT, edTxtHostIp.getText().toString());
				   editor.commit();
			}
		});
	    
	}

}
