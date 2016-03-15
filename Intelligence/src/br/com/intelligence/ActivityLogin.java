package br.com.intelligence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class ActivityLogin extends Activity {

	EditText login, senha;
	// qnt de tentativas
	CheckBox cbxMostarSeha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		login = (EditText) findViewById(R.id.editTextLogin);
		senha = (EditText) findViewById(R.id.editTextSenha);
		cbxMostarSeha = (CheckBox) findViewById(R.id.chbxMostrarSenha);

		// metodo mostrar senha
		cbxMostarSeha.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				// Object o = new PasswordTransformationMethod();
				if (!isChecked) {
					senha.setTransformationMethod(new PasswordTransformationMethod());

				} else {
					senha.setTransformationMethod(null);
				}
			}
		});
	}

	// login de teste
	public void logar2(View view) {
		// validando os campos
		String user = login.getText().toString();
		String password = senha.getText().toString();

		if (user.equals("Teste")) {
			// se login estiver certo executa codigo abaixo
			if (password.equals("teste")) {
				// se login e senha estiverem certas. cria uma nova intent
				Bundle passaDados = new Bundle();
				Intent intent = new Intent(getBaseContext(),
						IntelligenceMain.class);
				passaDados.putString("login", user);
				intent.putExtras(passaDados);
				startActivity(intent);
				// se tudo estiver certo mostrar msg
				Toast.makeText(this, "Login com sucesso!", Toast.LENGTH_LONG)
						.show();
				// finalizando a intent
				finish();
			} else {
				// se senha estiver errada, a tetativa decrementa e mostrar msg
				senha.setError("Senha inválida!");
				senha.requestFocus();
			}

		} else {
			login.setError("Login inválido!");
			login.requestFocus();
		}
	}

	public void logar(View view) {
		// recebendo o texto dos componetes

		boolean conexao = verificaConexao();
		if (conexao == true) {

			new Thread() {
				public void run() {
					// se tudo estiver certo, manda os dados pro servidor
					postHttp(login.getText().toString(), senha.getText()
							.toString());

				}
			}.start();

		}

	}

	// metodo sair...
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Toast.makeText(this, "Você saiu do Intelligence", Toast.LENGTH_LONG)
				.show();
		this.finishActivity(0);
	}

	public void postHttp(final String login, final String senha) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://www.inscrevaseonline.com.br/enucomp/testes/qrcode.php");

		try {
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			valores.add(new BasicNameValuePair("login", login));
			valores.add(new BasicNameValuePair("senha", senha));

			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			final HttpResponse resposta = httpClient.execute(httpPost);

			// minhar var
			final String resp = EntityUtils.toString(resposta.getEntity());

			runOnUiThread(new Runnable() {
				public void run() {

					try {

						JSONObject respostJson = new JSONObject(resp);
						boolean aut = respostJson.getBoolean("login");
						JSONArray eventos =  respostJson.getJSONArray("eventos");
						JSONArray atividades =  respostJson.getJSONArray("atividades");

						if (aut) {

							Bundle passaDados = new Bundle();
							Intent intent = new Intent(getBaseContext(),
									IntelligenceMain.class);
							passaDados.putString("login", login);
							intent.putExtras(passaDados);
							Toast.makeText(getApplicationContext(), eventos.toString(), Toast.LENGTH_LONG).show();
							Toast.makeText(getApplicationContext(), atividades.toString(), Toast.LENGTH_LONG).show();
							startActivity(intent);
							// finish();

						} else {

							Toast.makeText(getBaseContext(), "Login inválido!",
									Toast.LENGTH_SHORT).show();

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
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
					this,
					"Você não está conectado em nunhuma rede! Verifique sua conexão com a internet",
					Toast.LENGTH_LONG).show();
		}
		return conectado;

	}
}