package br.com.inteligence;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
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
	int tentativas = 3;
	CheckBox cbxMostarSeha, cbxManterConectado;
	Context con;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
	}

	public void logar(View view) {
		// recebendo o texto dos componetes

		// tratando os evt

		//if (verificaConexao() == false) {
			//Toast.makeText(con, "Sertifique-se de sua conexão com a internet", Toast.LENGTH_SHORT).show();
		//} else {
			
			new Thread(){
				public void run(){
					login = (EditText) findViewById(R.id.editTextLogin);
					senha = (EditText) findViewById(R.id.editTextSenha);
					
					postHttp(login.getText().toString(), senha.getText().toString());

					
				}
			}.start();
			
			
		//}

		

	}

	private void tentativas() {
		--tentativas;
		// se a tentativa for igual a 0, ou seja, o usuario errar mais de 3
		// vezes mostrar msg e fecha a aplicação
		if (tentativas == 0) {
			// msg
			Toast.makeText(this, "Suas tentativas acabaram!", Toast.LENGTH_LONG)
					.show();
			// fechando a aplicação
			finish();
		} else {
			
			// /mensagem a ser mostrada caso o usuario erre

			Toast.makeText(this,
					"Restam apenas " + (tentativas) + " tentativas",
					Toast.LENGTH_LONG).show();
		}
	}

	private void msgAlerta() {
		// criando uma caixa de confirmação usando AlertDialog
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		// definindo o titulo
		alerta.setTitle("Atenção");
		// difinindo a msg
		alerta.setMessage("Deseja realmente sair?");
		// se clicar em Sim
		alerta.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			// metodo verifica condição e sai da aplicação
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub

				try {
					finalize();
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		// se clicar em Não
		alerta.setNegativeButton("Não!", new DialogInterface.OnClickListener() {
			// metodo verifica condição e volta para a aplicação
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

	public void mostrarSenha(View view) {
		cbxMostarSeha = (CheckBox) findViewById(R.id.chbxMostrarSenha);

		final Object o = new PasswordTransformationMethod();

		cbxMostarSeha.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) { // TODO Auto-generated method stub //

				if (isChecked) {
					senha.setTransformationMethod(null);
				} else {
					senha.setTransformationMethod((TransformationMethod) o);
				}
			}
		});

	}
	
	// metodo sair...
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finishActivity(0);
	}

	public void postHttp(String login, String senha) {
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
						//Toast.makeText(getBaseContext(), resp.toString(), Toast.LENGTH_SHORT).show();
						if (resp.equals("valido")) {

							Intent intent = new Intent(getBaseContext(),
									MainActivity.class);
							startActivity(intent);
							finish();

						} else if (resp.equals("invalido")) {

							//Toast.makeText(getBaseContext(), "Login inválido!", Toast.LENGTH_SHORT).show();
							tentativas();
							
						}
					} catch (ParseException e) {
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

	public boolean verificaConexao() {
		boolean conectado;
		ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conectivtyManager.getActiveNetworkInfo() != null
				&& conectivtyManager.getActiveNetworkInfo().isAvailable()
				&& conectivtyManager.getActiveNetworkInfo().isConnected()) {
			conectado = true;
			Toast.makeText(this, "Conectado!", Toast.LENGTH_LONG).show();
		} else {
			conectado = false;
			Toast.makeText(this,
					"Não Conectado! Verifique sua conexão com a internet",
					Toast.LENGTH_LONG).show();
		}
		return conectado;
	}

}