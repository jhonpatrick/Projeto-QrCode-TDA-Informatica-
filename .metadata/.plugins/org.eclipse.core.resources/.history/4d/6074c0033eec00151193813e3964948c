package br.com.intelligence;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TelaDeAtividades extends Activity {

	private String dados_recolhidos;
	public static final int REQUEST_CODE = 0;
	Intent intent;
	public static final String  PREF_NAME = "PreferenciasLogin";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela_de_atividades);
		
		SharedPreferences perfLogin = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		Toast.makeText(getBaseContext(), perfLogin.getString("Eventos", " Eventos não foi salvo "), Toast.LENGTH_LONG).show();
		Toast.makeText(getBaseContext(), perfLogin.getString("Atividades", " Atividades não foi salva "), Toast.LENGTH_LONG).show();
		
	}

	// tratando o botão iniciar
	public void iniciar(View view) {
		intent = new Intent(this,
				com.google.zxing.client.android.CaptureActivity.class);
		startActivityForResult(intent, REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		
		if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
			
			Bundle extras = data.getExtras();
			if(extras != null){
				dados_recolhidos = "Resultado do SCANER: "
						+ extras.getString("SCAN_RESULT") + "( "
						+ extras.getString("SCAN_FORMAT") + ")";
				Log.d("dados", dados_recolhidos);
			}
			
		}
		
	}

	// saindo
	public void onBackPressed() {
		super.onBackPressed();
		this.finishActivity(0);
	}
}
