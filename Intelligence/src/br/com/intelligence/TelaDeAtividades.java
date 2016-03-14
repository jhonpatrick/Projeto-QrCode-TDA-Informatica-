package br.com.intelligence;

import br.com.intelligence.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TelaDeAtividades extends Activity {

	private String dados_recolhidos;
	public static final int REQUEST_CODE = 0;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tela_de_atividades);
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
		

//		dados_recolhidos = "Resultado do SCANER: "
//				+ data.getStringExtra("SCAN_RESULT") + "( "
//				+ data.getStringExtra("SCAN_FORMAT") + ")";
	}

	// saindo

	public void onBackPressed() {
		super.onBackPressed();
		this.finishActivity(0);
	}
}