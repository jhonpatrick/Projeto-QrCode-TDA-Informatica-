package br.com.inteligence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends Activity {

	EditText login, senha;
	// qnt de tentativas
	int tentativas = 3;
	CheckBox cbxMostarSeha, cbxManterConectado;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	public void logar(View view) {
		// tratando os evt

		// recebendo o texto dos componetes
		login = (EditText) findViewById(R.id.editTextLogin);
		senha = (EditText) findViewById(R.id.editTextSenha);

		// validando os campos
		if (login.getText().toString().equals("teste")) {
			// se login estiver certo executa codigo abaixo
			if (senha.getText().toString().equals("teste")) {
				// se tudo estiver certo mostrar msg
				Toast.makeText(this, "Login com sucesso!", Toast.LENGTH_LONG)
						.show();
				// se login e senha estiverem certas. cria uma nova intent
				Intent intent = new Intent(this, MainActivity.class);
				// estartando a intent
				startActivity(intent);
				// finalizando a intent
				finish();
			} else {
				tentativas();
				// se senha estiver errada, a tetativa decrementa e mostrar msg
				senha.setError("Senha inválida");
				senha.requestFocus();
			}

		} else {
			tentativas();
			login.setError("Login inválido");
			login.requestFocus();
		}
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

	public void limpar(View view) {
		// limpando os componentes
		if (login.equals("") || login == null && senha.equals("")
				|| senha == null) {

			Toast.makeText(ActivityLogin.this,
					"Campos vazios!" + "\n" + "Por favor, preencha os campos.",
					Toast.LENGTH_SHORT).show();

		} else {
			login.setText("");
			senha.setText("");
		}
	}

	public void sair() {
		// fechando o sistema
		msgAlerta();
	}

	private void msgAlerta() {
		// criando uma caixa de confirmação usando AlertDialog
		AlertDialog.Builder alerta = new AlertDialog.Builder(this);
		// definindo o titulo
		alerta.setTitle("Aten��o");
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
		alerta.setNegativeButton("N�o!", new DialogInterface.OnClickListener() {
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

	public void MostrarSenha(View view) {
		senha = (EditText) findViewById(R.id.editTextSenha);
		cbxMostarSeha = (CheckBox) findViewById(R.id.chbxMostrarSenha);

		Object o = new PasswordTransformationMethod();
		if (cbxMostarSeha.isChecked()) {
			senha.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
		} else {
			senha.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		/*
		 * cbxMostarSeha.setOnCheckedChangeListener(new
		 * OnCheckedChangeListener() {
		 * 
		 * @Override public void onCheckedChanged(CompoundButton buttonView,
		 * boolean isChecked) { // TODO Auto-generated method stub Object o =
		 * new PasswordTransformationMethod(); if (isChecked) {
		 * senha.setTransformationMethod(null); } else {
		 * 
		 * senha.setTransformationMethod((TransformationMethod) o); } } });
		 */

	}

	public void manterConectado() {
		cbxManterConectado = (CheckBox) findViewById(R.id.manterConectado);
		if (cbxManterConectado.isChecked()) {

		}
	}

}
