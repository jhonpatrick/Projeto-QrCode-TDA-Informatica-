package br.com.intelligence;

import generic.ScreenResolution;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.history.HistoryActivity;
import com.google.zxing.client.android.history.HistoryManager;

public class IntelligenceMain extends Activity implements ScreenResolution{

	TextView user;
	public static final String PREF_NAME = "PreferenciasLogin";

	private String dados_recolhidos;
	HistoryManager historyManager;
	HistoryActivity historyActivity;
	public static final int REQUEST_CODE = 0;
	ProgressDialog progDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intelligence);
		pegaScreenResolution();

		// pegando user direto do PreferencesLogin/ Session salva
		user = (TextView) findViewById(R.id.txtUser);
		progDialog = new ProgressDialog(this);
		SharedPreferences perfLogin = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);

		String logado = perfLogin.getString("Usuario", "");
		historyManager = new HistoryManager(this);
		// mostrando o adm logado
		user.setText(getBaseContext().getResources().getString(R.string.txtvw_ola_user) + " "+ logado);

	}

	@Override
	public void pegaScreenResolution() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int y = metrics.heightPixels;

		int x = metrics.widthPixels;
		Log.i("Script", " " + y + " por " + x);
		if(y <= 1000 && x <= 550){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}

	}
	public void digitalizar(View view) {
		boolean contemHistory = historyManager.historyIsVazio();
		if(contemHistory == true){
			Intent intent = new Intent(this, com.google.zxing.client.android.CaptureActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
		}else{
			Intent intent = new Intent(this,
					br.com.intelligence.TelaDeAtividades.class);
			startActivityForResult(intent, REQUEST_CODE);
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {

			Bundle extras = data.getExtras();
			if (extras != null) {
				dados_recolhidos = "Resultado do SCANER: "
						+ extras.getString("SCAN_RESULT") + "( "
						+ extras.getString("SCAN_FORMAT") + ")";
				Log.d("dados", dados_recolhidos);
			}

		}

	}

	public void historicoQr(View view) {
		Intent passaTela = new Intent(this, HistoryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("envia_dados", "");
		passaTela.putExtras(bundle);
		startActivity(passaTela);

	}

	// metodo sair...
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();

	}

	// criando menu de opções

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_intelligence_op, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.menu_intelligence_sobre:
			// mandar pra tela de sobre o app
			break;

		case R.id.menu_intelligence_sair:
			// sair da apicação, fechar session do user que está logado
			// verificando se existe dados na session anterior
			boolean contemHistory = historyManager.historyIsVazio();
			if (contemHistory) {
				AlertDialog.Builder alerta = new AlertDialog.Builder(this, 3);
				alerta.setTitle(user.getText());
				alerta.setMessage(getBaseContext().getResources().getString(R.string.historico_com_dados));
				alerta.setCancelable(true);
				alerta.setPositiveButton(getBaseContext().getResources().getString(R.string.alerta_deseja_envialos),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int i2) {

								/*
								 * mandando somente um dado para o servidor
								 */
								boolean conectado = verificaConexao();
								if (conectado) {
									try {
										progDialog.setTitle(R.string.alerta_enviando_dados);
										progDialog
												.setMessage(getBaseContext().getResources().
														getString(R.string.alerta_dados_sendo_enviados));
										progDialog
												.setIcon(android.R.drawable.ic_menu_upload);
										progDialog.show();
										Log.i("Script",
												"Histórico: "
														+ historyManager
																.listarTodosQr());
										progDialog.dismiss();
										Intent passaTela2 = new Intent(getBaseContext(),
												HistoryActivity.class);
										Bundle bundle = new Bundle();
										bundle.putString("envia_dados",
												"envia_dados");
										passaTela2.putExtras(bundle);
										startActivity(passaTela2);

									} catch (Exception ex) {
										// TODO: handle exception
										ex.printStackTrace();
										Log.i("Script", "Erro! " + ex.getMessage());
									}
								}
								dialog.dismiss();
							}
						});
				alerta.setNegativeButton(R.string.button_cancel, null);
				alerta.show();
			} else {
				msgAlerta();

			}

			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void msgAlerta() {
		// criando uma caixa de confirmação usando AlertDialog
		AlertDialog.Builder alerta = new AlertDialog.Builder(this, 3);
		// definindo o titulo

		alerta.setTitle(R.string.alerta_atencao);
		// difinindo a msg
		alerta.setMessage(getBaseContext().getResources().getString(R.string.alerta_deseja_sair));
		// se clicar em Sim
		alerta.setPositiveButton(getBaseContext().getResources().getString(R.string.sair), 
				new DialogInterface.OnClickListener() {
			// metodo verifica condição sai da aplicação
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				msgSair();

			}
		});
		// se clicar em não
		alerta.setNegativeButton(R.string.alerta_cancelar,
				new DialogInterface.OnClickListener() {
					// metodo verifica condição e volta para a aplicaÃ§Ã£o
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				});
		// cria o AlertDialog
		alerta.create();
		// exibi o AlertDialog
		alerta.show();
	}

	public void msgSair() {
		// saindo do aplicativo e apagando os dados do SharedPreferences/
		// PreferenciasLogin
		SharedPreferences prefLogin = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = prefLogin.edit();
		editor.clear().commit();

		Toast.makeText(this, R.string.toast_saiu_app, Toast.LENGTH_LONG)
				.show();
		this.finishActivity(0);
		finish();
	}

	// método para vericar conexão com intenet
	public boolean verificaConexao() {
		boolean conectado = false;
		ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conectivtyManager.getActiveNetworkInfo() != null
				&& conectivtyManager.getActiveNetworkInfo().isAvailable()
				&& conectivtyManager.getActiveNetworkInfo().isConnected()) {
			conectado = true;
		} else {
			conectado = false;
			Toast.makeText(getBaseContext(),R.string.toast_info_sem_net, Toast.LENGTH_LONG).show();
		}
		return conectado;
	}
}
