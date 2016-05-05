package br.com.intelligence;

import generic.ScreenResolution;

import java.io.IOException;
import java.util.ArrayList;

import model.Atividades;
import model.Eventos;
import model.Inscritos;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import dao.AtividadesDAO;
import dao.EventosDAO;
import dao.InscritosDAO;

@SuppressWarnings("deprecation")
public class ActivityLogin extends Activity implements ScreenResolution{

	EditText login, senha;
	// qnt de tentativas
	CheckBox cbxMostarSeha;

	// criando o SharedPreferences do adm
	public static final String PREF_NAME = "PreferenciasLogin";
	private EventosDAO eventosDAO;
	private Eventos modelEventos;
	private Atividades modelAtividades;
	private Inscritos modelInscritos;
	private AtividadesDAO atvDAO;
	private InscritosDAO inscDAO;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		pegaScreenResolution();

		eventosDAO = new EventosDAO(getBaseContext());
		atvDAO = new AtividadesDAO(getBaseContext());
		inscDAO = new InscritosDAO(getBaseContext());

		login = (EditText) findViewById(R.id.editTextLogin);
		senha = (EditText) findViewById(R.id.editTextSenha);

		// verificando se existe session salva do login
		SharedPreferences preferenciasUser = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);

		String lo = preferenciasUser.getString("Usuario", "");
		login.setText(lo);
		String se = preferenciasUser.getString("Senha", "");
		senha.setText(se);

		if (!lo.isEmpty() && !se.isEmpty()) {
			startActivity(new Intent(this, IntelligenceMain.class));
			finish();
		}
		cbxMostarSeha = (CheckBox) findViewById(R.id.chbxMostrarSenha);

		// metodo mostrar senha
		cbxMostarSeha.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (!isChecked) {
					senha.setTransformationMethod(new PasswordTransformationMethod());

				} else {
					senha.setTransformationMethod(null);
				}
			}
		});
		
	}

	//verificando o tamanho da tela do dispositivo
	
	
	public void logar(View view) {
		// recebendo o texto dos componetes

		final String l = login.getText().toString();
		final String s = senha.getText().toString();
		boolean conexao = verificaConexao();
		boolean isConected = netWorkdisponibilidade(this);
		
		if (conexao == true) {
			if (isConected == true) {
				// criando um nova linha de execução do dispositivo
				if(!l.equals(null) && !s.equals(null) || l != null && s != null){
					dialog = new ProgressDialog(this, 3);
					dialog.setTitle(R.string.alerta_logando);
					dialog.setMessage(getBaseContext().getResources().getString(R.string.alerta_carrega_dados));
					dialog.show();
					new Thread() {
						public void run() {
							// se tudo estiver certo, manda um postHttp
							postHttp(l.toString(), s.toString());
						}
					}.start();
				}
			}
		}
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

	// metodo sair...
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Toast.makeText(getBaseContext(), R.string.toast_saiu_app,
				Toast.LENGTH_LONG).show();
		this.finishActivity(0);
	}

	public void postHttp(final String login, final String senha) {

		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://www.inscrevaseonline.com.br/enucomp/testes/qrcode.php");
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			valores.add(new BasicNameValuePair("login", login));
			valores.add(new BasicNameValuePair("senha", senha));

			httpPost.setEntity(new UrlEncodedFormEntity(valores));

			final HttpResponse resposta = httpClient.execute(httpPost);
			final String resp = EntityUtils.toString(resposta.getEntity());

			// minhar var
			runOnUiThread(new Runnable() {
				public void run() {

					try {
						// recebendo dados do JSON
						JSONObject respostJson = new JSONObject(resp);
						boolean aut = respostJson.getBoolean("login");

						// verificando se login é válido = true
						if (aut) {
							Intent intent = new Intent(getBaseContext(),
									IntelligenceMain.class);

							JSONArray eventos = respostJson
									.getJSONArray("eventos");
							JSONArray atividades = respostJson
									.getJSONArray("atividades");
							JSONArray inscritos = respostJson
									.getJSONArray("inscritos");

							// percorrendo lista de eventos e mostrando a mesma
							for (int i = 0; i < eventos.length(); i++) {

								JSONObject jsonObject = (JSONObject) eventos
										.get(i);

								// add no dados do json no sqlite aqui
								long id = Long.parseLong(jsonObject
										.getString("id"));
								String nomeEv = jsonObject.getString("nome");
								modelEventos = new Eventos();
								modelEventos.set_id(id);
								modelEventos.setNome(nomeEv);
								try {
									eventosDAO.inserirEv(modelEventos);
									Log.i("Script", "Inseriu Eventos com sucesso!");
									Log.i("Script", "Eventos = [" + modelEventos.get_id() + ", " + modelEventos.getNome() + "]");
								} catch (Exception e) {
									// TODO: handle exception
									Log.i("Script", "Não inseriu Eventos - Erro = " + e.getMessage());
								}
							}

							for (int j = 0; j < atividades.length(); j++) {

								JSONObject jsonObject = (JSONObject) atividades
										.get(j);
								long id_atv = Long.parseLong(jsonObject
										.getString("id"));
								long id_evt_atv = Long.parseLong(jsonObject
										.getString("evento").replace("\"", ""));
								String nomeAtv = jsonObject.getString("nome");

								modelAtividades = new Atividades();
								modelAtividades.set_id(id_atv);
								modelAtividades.setNome(nomeAtv);
								modelAtividades.setId_evento(id_evt_atv);
								try {
									atvDAO.inserirAtv(modelAtividades);
									Log.i("Script", "Inseriu Atividades com sucesso!");
									Log.i("Script", "Atividades = [" + modelAtividades.get_id() + ", " 
											+ modelAtividades.getNome() +  ", " + modelAtividades.getId_evento() + "]");
								} catch (Exception e) {
									// TODO: handle exception
									Log.i("Script", "Não inseriu Atividades - Erro = " + e.getMessage());
								}

							}
							
							for (int l = 0; l < inscritos.length(); l++) {
								JSONObject jsonObject = (JSONObject) inscritos
										.get(l);
								long id_insc = jsonObject.getInt("inscrito");
								long id_atv = jsonObject.getInt("atividade");
								
								modelInscritos = new Inscritos();
								modelInscritos.setId_inscritos(id_insc);
								modelInscritos.setId_atividades(id_atv);
								try {
									inscDAO.inserirInsc(modelInscritos);
									Log.i("Script", "Inseriu Inscritos com sucesso!");
									Log.i("Script", "Inscritos = [" + modelInscritos.getId_inscritos() + ", " 
											+  modelInscritos.getId_atividades() + "]");
								} catch (Exception e) {
									// TODO: handle exception
									Log.i("Script", "Não inseriu Inscritos - Erro = " + e.getMessage());
								}
								
							}

							/*
							 * criando SharedPreferences para adm que estiver
							 * logado , salvando dados da session.
							 */

							SharedPreferences preferenciasUser = getSharedPreferences(
									PREF_NAME, MODE_PRIVATE);
							SharedPreferences.Editor editor = preferenciasUser
									.edit();

							// salvando dados
							String email = login.toString();
							editor.putBoolean("Status Login", aut);
							if(login.contains("@")) {
								editor.putString("Email", email);
							}else{
								email = "";
								editor.putString("Email", email);
							}
							String nome_usuario = respostJson.getString("nome");
							editor.putString("Usuario", nome_usuario.substring(0, 1).toUpperCase()
									.concat(nome_usuario.substring(1)));
							editor.putString("Senha", senha);

							// percorrendo lista de eventos e mostrando a mesma
							editor.commit();
							Log.i("Script", "Dados do adm - \n" + "Nome: " + nome_usuario + "\n" 
							+ "Senha: " + senha + "\n" + "Email: " + email + "\n" + "Status do Login: " + aut);
							dialog.setMessage(getBaseContext().getResources().getString(R.string.alerta_login_sucesso));
							dialog.dismiss();
							startActivity(intent);

						} else {

							Toast.makeText(getBaseContext(), R.string.toast_login_invalido,
									Toast.LENGTH_LONG).show();
							dialog.dismiss();

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.i("Script", "Erro no login -  " + e.getMessage());
					}
				}
			});
		} catch (ClientProtocolException e) {
			Log.i("Script", "Erro no login -  " + e.getMessage());
		} catch (IOException e) {
			Log.i("Script", "Erro no login -  " + e.getMessage());
			e.printStackTrace();
		}
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
			Toast.makeText(
					getBaseContext(), R.string.toast_info_sem_net, Toast.LENGTH_LONG).show();
		}
		return conectado;
	}
	
	public boolean netWorkdisponibilidade(Context cont) {
		boolean conectado = false;
		ConnectivityManager conmag;
		conmag = (ConnectivityManager) cont
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		conmag.getActiveNetworkInfo();
		// Verifica o WIFI
		if (conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
			conectado = true;
			Toast.makeText(this, R.string.toast_is_net_wifi, Toast.LENGTH_LONG).show();
		}
		// Verifica o 3G
		else if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnected()) {
			conectado = true;
			Toast.makeText(this, R.string.toast_is_net_3g, Toast.LENGTH_LONG).show();
		} else {
			conectado = false;
		}
		return conectado;
	}
}