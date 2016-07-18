package br.com.intelligence;

import generic.ScreenResolution;

import java.util.concurrent.ExecutionException;

import model.Atividades;
import model.Credenciador;
import model.Eventos;
import model.Inscricao;
import model.Inscritos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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
import dao.CredenciadorDAO;
import dao.EventosDAO;
import dao.InscricoesDAO;
import dao.InscritosDAO;

@SuppressWarnings("deprecation")
public class ActivityLogin extends Activity implements ScreenResolution {

	// declarando componetes
	private EditText login, senha;
	private CheckBox cbxMostarSeha;
	private ProgressDialog dialog;
	// declarando Entidades/Minhas Classes
	private EventosDAO eventosDAO;
	private Eventos modelEventos;
	private Atividades modelAtividades;
	private Inscritos modelInscritos;
	private Credenciador modelCredenciador;
	private Inscricao modelInscricao;
	// declarando os gerenciadores da classes
	private CredenciadorDAO credenciadorDAO;
	private AtividadesDAO atividadesDAO;
	private InscritosDAO inscritosDAO;
	private InscricoesDAO inscricoesDAO;
	private ConexaoServidor conexaoServidor;
	// local onde ficaram salvas as credencias do login
	// criando o SharedPreferences do adm
	public static final String PREF_NAME = "PreferenciasLogin";
	// url de entrada para a conexão com o servidor php
	private final String url = ("http://www.inscrevaseonline.com.br/enucomp/credenciamento/qrcode.php");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pegaScreenResolution();
		setContentView(R.layout.activity_login);
		login = (EditText) findViewById(R.id.editTextLogin);
		senha = (EditText) findViewById(R.id.editTextSenha);
		credenciadorDAO = new CredenciadorDAO(this);
		eventosDAO = new EventosDAO(this);
		atividadesDAO = new AtividadesDAO(this);
		inscritosDAO = new InscritosDAO(this);
		inscricoesDAO = new InscricoesDAO(this);
		dialog = new ProgressDialog(getBaseContext());
		// verificando se existe session salva do login
		SharedPreferences preferenciasUser = getSharedPreferences(PREF_NAME,
				MODE_PRIVATE);
		String lo = preferenciasUser.getString("nomeCredenciador", "");
		login.setText(lo);
		String se = preferenciasUser.getString("senhaCredenciador", "");
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
				if (!isChecked) {
					senha.setTransformationMethod(new PasswordTransformationMethod());
				} else {
					senha.setTransformationMethod(null);
				}
			}
		});
	}

	// verifica se os campos estão vázios.... Login e Senha
	public boolean camposIsNull() {
		if (this.login.getText().toString() == null
				|| this.login.getText().toString().equals("")
				|| this.login.getText().toString().length() < 1) {
			this.login.setError("Campo não pode ser nulo!");
			login.requestFocus();
			return true;
		} else if (this.senha.getText().toString() == null
				|| this.senha.getText().toString().equals("")
				|| this.senha.getText().toString().length() < 1) {
			this.senha.setError("Campo não pode ser nulo!");
			senha.requestFocus();
			return true;
		} else {
			return false;
		}
	}

	// verificando o tamanho da tela do dispositivo
	public void logar(View view) {
		// recebendo o texto dos componetes
		final String l = this.login.getText().toString();
		final String s = this.senha.getText().toString();
		boolean conexao = verificaConexao();
		if (conexao == true) {
			if (camposIsNull() == false) {
				/*
				 * criando um nova linha de execução do dispositivo para obter
				 * uma conexão com o servidor
				 */
				conexaoServidor = new ConexaoServidor(ActivityLogin.this);
				conexaoServidor.execute(url.toString(), l.toString(),
						s.toString());
				// crinado uma nova linda de execução
				new Thread() {
					public void run() {
						// tratando o retorno da AsyncTask
						try {
							if (conexaoServidor.get() == null) {
								Log.i("Script",
										"Resultado JSON - "
												+ conexaoServidor.get()
												+ " Serviço indisponivel no momento, "
												+ "verifique se à conexão com internet no seu dispositivo");
								runOnUiThread(new Runnable() {
									public void run() {
										msgAlerta();
									}
								});
							} else {
								Log.i("Script", "Status da conexão: "
										+ conexaoServidor.getStatus()
												.toString());
								trataRetornServi(conexaoServidor.get());
							}
						} catch (InterruptedException e) {
							Log.i("Script",
									"Serviço indisponivel no momento, "
											+ "verifique se à conexão com internet no seu dispositivo");
							e.printStackTrace();
						} catch (ExecutionException e) {
							Log.i("Script",
									"Serviço indisponivel no momento, "
											+ "verifique se à conexão com internet no seu dispositivo");
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
	}
	
	private void msgAlerta() {
		// criando uma caixa de confirmação usando AlertDialog
		AlertDialog.Builder alerta = new AlertDialog.Builder(getBaseContext(), 3);
		// definindo o titulo
		alerta.setTitle(R.string.alerta_atencao);
		// difinindo a msg
		alerta.setMessage("Serviço indisponivel no momento verifique se à conexão com internet no seu dispositivo");
		// se clicar em Sim
		alerta.setPositiveButton("Tentar novamente",
				new DialogInterface.OnClickListener() {
					// metodo verifica condição sai da aplicação
					@Override
					public void onClick(DialogInterface dialog, int arg1) {
					
					}
				});
		// se clicar em não
		alerta.setNegativeButton(R.string.alerta_cancelar,
				new DialogInterface.OnClickListener() {
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

	public void trataRetornServi(final JSONObject jsonObj) {
		/*
		 * tratar o retorno do metod doInBeckground pegar a resp e usar JSON
		 */
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					// recebendo dados do JSON
					boolean aut = jsonObj.getBoolean("login");
					// verificando se login é válido = true
					if (aut == true) {
						Intent intent = new Intent(getBaseContext(),
								IntelligenceMain.class);

						JSONArray atividades = jsonObj
								.getJSONArray("atividades");
						JSONArray inscritos = jsonObj.getJSONArray("inscritos");
						JSONArray eventos = jsonObj.getJSONArray("eventos");
						// percorrendo lista de eventos e mostrando a mesma
						for (int i = 0; i < eventos.length(); i++) {
							JSONObject jsonObject = (JSONObject) eventos.get(i);
							// add no dados do json no sqlite aqui
							long id = Long
									.parseLong(jsonObject.getString("id"));
							String nomeEv = jsonObject.getString("nome");
							modelEventos = new Eventos();
							modelEventos.setId(id);
							modelEventos.setNome(nomeEv);
							try {
								eventosDAO.inserirEv(modelEventos);
								Log.i("Script",
										"Eventos = [" + modelEventos.getId()
												+ ", " + modelEventos.getNome()
												+ "]");
							} catch (Exception e) {
								Log.i("Script", "Não inseriu Eventos - Erro = "
										+ e.getMessage());
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
							modelAtividades.setId(id_atv);
							modelAtividades.setNome(nomeAtv);
							modelAtividades.setId_evento(id_evt_atv);
							try {
								atividadesDAO.inserirAtv(modelAtividades);
								Log.i("Script", "Atividades = ["
										+ modelAtividades.getId() + ", "
										+ modelAtividades.getNome() + ", "
										+ modelAtividades.getId_evento() + "]");
							} catch (Exception e) {
								// TODO: handle exception
								Log.i("Script",
										"Não inseriu Atividades - Erro = "
												+ e.getMessage());
							}
						}
						for (int l = 0; l < inscritos.length(); l++) {
							JSONObject jsonObject = (JSONObject) inscritos
									.get(l);
							long id_insc = jsonObject.getInt("inscrito");
							modelInscritos = new Inscritos();
							modelInscritos.setId_inscritos(id_insc);
							Log.i("Script",
									"Buscar por inscrito = " + id_insc
											+ " Resultado = "
											+ inscritosDAO.buscarDado(id_insc));
							try {
								inscritosDAO.inserirInsc(modelInscritos);
								Log.i("Script", "Inscritos = ["
										+ modelInscritos.getId_inscritos()
										+ "]");
							} catch (Exception e) {
								Log.i("Script",
										"Não inseriu Inscritos - Erro = "
												+ e.getMessage());
							}
						}
						for (int i = 0; i < inscritos.length(); i++) {
							JSONObject jsonObject = (JSONObject) inscritos
									.get(i);
							long id_insc = jsonObject.getInt("inscrito");
							long id_atv = jsonObject.getInt("atividade");
							modelInscricao = new Inscricao();
							modelInscricao.setIdInscrito(id_insc);
							modelInscricao.setIdAtividade(id_atv);
							Log.i("Script",
									"Buscar por inscrição = "
											+ id_insc
											+ "/"
											+ id_atv
											+ " Resultado = "
											+ inscricoesDAO.buscarInscricao(
													id_insc, id_atv));
							try {
								inscricoesDAO.inserirInscricao(modelInscricao);
								Log.i("Script", "Inscrição = ["
										+ modelInscricao.getIdInscrito() + ", "
										+ modelInscricao.getIdAtividade() + "]");
							} catch (Exception e) {
								Log.i("Script",
										"Não inseriu Inscricao - Erro = "
												+ e.getMessage());
							}
						}
						// criando SharedPreferences para adm que estiver logado
						// , salvando dados da session.
						SharedPreferences preferenciasUser = getSharedPreferences(
								PREF_NAME, MODE_PRIVATE);
						SharedPreferences.Editor editor = preferenciasUser
								.edit();
						modelCredenciador = new Credenciador();
						long idCred = jsonObj.getLong("id");
						String idC = String.valueOf(idCred);
						editor.putString("idCredenciador", idC);
						modelCredenciador.set_Id(idCred);
						// salvando dados
						String email = login.toString();
						editor.putBoolean("Status Login", aut);
						if (email.contains("@")) {
							editor.putString("emailCredenciador",
									email.toString());
							modelCredenciador.setEmail(email);
						} else {
							editor.putString("emailCredenciador", null);
							modelCredenciador.setEmail(null);
						}
						String nome_usuario = jsonObj.getString("nome");
						editor.putString("nomeCredenciador",
								nome_usuario.substring(0, 1).toUpperCase()
										.concat(nome_usuario.substring(1)));
						modelCredenciador.setNome(nome_usuario);
						String senhaUser = senha.getText().toString();
						editor.putString("senhaCredenciador", senhaUser);
						modelCredenciador.setSenha(senhaUser);
						// percorrendo lista de eventos e mostrando a mesma
						editor.commit();
						credenciadorDAO.inserirCredenciador(modelCredenciador);
						Log.i("Script",
								"Dados do adm - \n" + "IdCred: "
										+ modelCredenciador.get_Id() + "\n"
										+ "Nome: "
										+ modelCredenciador.getNome() + "\n"
										+ "Senha: "
										+ modelCredenciador.getSenha() + "\n"
										+ "Email: "
										+ modelCredenciador.getEmail() + "\n"
										+ "Status do Login: " + aut);
						dialog.setMessage(getBaseContext().getResources().getString(
								R.string.alerta_login_sucesso));
						startActivity(intent);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.i("Script", "Erro no login -  " + e.getMessage());
				}
			}
		});
	}

	@Override
	public void pegaScreenResolution() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		Log.i("Script", " Tela do dispositivo - " + height + "x" + width);
		// pegar - largura por altura
		if (width <= 780 && height <= 1020) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
	}

	// metodo sair...
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Toast.makeText(getBaseContext(), R.string.toast_saiu_app,
				Toast.LENGTH_LONG).show();
		this.finishActivity(0);
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
			Toast.makeText(getBaseContext(), R.string.toast_info_sem_net,
					Toast.LENGTH_LONG).show();
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
			Toast.makeText(this, R.string.toast_is_net_wifi, Toast.LENGTH_LONG)
					.show();
		}
		// Verifica o 3G
		else if (conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.isConnected()) {
			conectado = true;
			Toast.makeText(this, R.string.toast_is_net_3g, Toast.LENGTH_LONG)
					.show();
		} else {
			conectado = false;
		}
		return conectado;
	}
}
