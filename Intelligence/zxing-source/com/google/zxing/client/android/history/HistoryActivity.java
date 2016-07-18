/*
 * Copyright 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.history;

import generic.ScreenResolution;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.intelligence.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

public final class HistoryActivity extends ListActivity implements
		ScreenResolution {

	private static final String TAG = HistoryActivity.class.getSimpleName();

	private HistoryManager historyManager;
	private ArrayAdapter<HistoryItem> adapter;
	private CharSequence originalTitle;
	public static final String PREF_NAME = "PreferenciasLogin";
	ProgressDialog progDialog;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		pegaScreenResolution();
		this.historyManager = new HistoryManager(this);
		adapter = new HistoryItemAdapter(this);

		setListAdapter(adapter);
		View listview = getListView();
		registerForContextMenu(listview);
		originalTitle = getTitle();

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String txt = bundle.getString("envia_dados");
		if (txt.equals("envia_dados")) {
			Toast.makeText(this, R.string.toast_envia_dado_acima,
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void pegaScreenResolution() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		Log.i("Script", " Tela do dispositivo - " + height + "x" + width);
		// pegar - largura por altura
		if (width <= 780 && height <= 1080) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadHistoryItems();
	}

	private void reloadHistoryItems() {
		Iterable<HistoryItem> items = historyManager.buildHistoryItems();
		adapter.clear();
		for (HistoryItem item : items) {
			adapter.add(item);
		}
		setTitle(originalTitle + " (" + adapter.getCount() + ')');
		if (adapter.isEmpty()) {
			adapter.add(new HistoryItem(null, null));
		}
	}

	// tratando click no qrcode lido no histórico

	@Override
	protected void onListItemClick(ListView l, View v,
			final int positionItemList, long id) {
		progDialog = new ProgressDialog(this);

		if (adapter.getItem(positionItemList).getResult() != null) {
			Intent intent = new Intent(this, CaptureActivity.class);
			intent.putExtra(Intents.History.ITEM_NUMBER, positionItemList);
			setResult(Activity.RESULT_OK, intent);

			AlertDialog.Builder builder2 = new AlertDialog.Builder(this, 3);
			builder2.setMessage(getBaseContext().getResources().getString(
					R.string.alerta_enviar_dado));
			builder2.setCancelable(true);
			builder2.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i2) {

							/*
							 * mandando somente um dado para o servidor
							 */
							boolean conectado = verificaConexao();
							if (conectado) {
								try {
									progDialog
											.setTitle(getBaseContext()
													.getResources()
													.getString(
															R.string.alerta_enviando_dado));
									progDialog
											.setMessage(getBaseContext()
													.getResources()
													.getString(
															R.string.alerta_dado_sendo_enviado));
									progDialog
											.setIcon(android.R.drawable.ic_menu_upload);
									progDialog.show();

									String dado = adapter.getItem(
											positionItemList).getDisplay();
									enviarItemHistorico(dado);

								} catch (Exception ex) {
									// TODO: handle exception
									ex.printStackTrace();
								}
							}
							dialog.dismiss();
							reloadHistoryItems();
						}
					});
			builder2.setNegativeButton(R.string.button_cancel, null);
			builder2.show();

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (historyManager.hasHistoryItems()) {
			MenuInflater menuInflater = getMenuInflater();
			menuInflater.inflate(R.menu.history, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		progDialog = new ProgressDialog(this);

		switch (item.getItemId()) {
		case R.id.menu_history_upload_text:

			enviaTodosDados();

			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void limpaHistorico() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
		builder.setMessage(R.string.msg_sure);
		builder.setCancelable(true);
		builder.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i2) {
						historyManager.clearHistory();
						dialog.dismiss();
						finish();
					}
				});
		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show();
	}

	public void enviaTodosDados() {
		/*
		 * enviar todos os qr's lidos
		 */

		AlertDialog.Builder builder1 = new AlertDialog.Builder(this, 3);
		builder1.setMessage(R.string.enviar_todos);
		builder1.setCancelable(true);
		builder1.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i2) {

						boolean conectado = verificaConexao();
						if (conectado) {
							try {
								progDialog
										.setTitle(R.string.alerta_enviando_dados);
								progDialog
										.setMessage(getBaseContext()
												.getResources()
												.getString(
														R.string.alerta_dados_sendo_enviados));
								progDialog
										.setIcon(android.R.drawable.ic_menu_upload);
								progDialog.show();
								enviarListaHistorico();

							} catch (Exception e) {
								// TODO: handle exception
								progDialog
										.setIcon(android.R.drawable.ic_dialog_alert);
								progDialog.setTitle(R.string.alerta_erro);
								progDialog
										.setMessage(getBaseContext()
												.getResources()
												.getString(
														R.string.alerta_erro_ao_enviar_dados)
												+ " - " + e.getMessage());
								progDialog.dismiss();
							}
						}
						dialog.dismiss();
					}
				});

		builder1.setNegativeButton(R.string.button_cancel, null);
		builder1.show();
	}

	// metodo de enviar dados para o servidor
	public void enviarListaHistorico() {
		new Thread() {
			public void run() {
				// pegando amd na session
				SharedPreferences perfLogin = getSharedPreferences(PREF_NAME,
						MODE_PRIVATE);
				String logado = perfLogin.getString("idCredenciador", "");
				String eventos = perfLogin.getString("idEvento", "");
				String atividades = perfLogin.getString("idAtividade", "");
				String data_envio = getDateTime();

				// se tudo estiver certo, manda um postHttp
				try {
					postHttpItens(logado, eventos, atividades, data_envio);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}

	// enviar um dado
	public void enviarItemHistorico(final String dado) {
		new Thread() {
			public void run() {
				// pegando amd na session
				SharedPreferences perfLogin = getSharedPreferences(PREF_NAME,
						MODE_PRIVATE);
				String logado = perfLogin.getString("idCredenciador", "");
				String evento = perfLogin.getString("idEvento", "");
				String atividade = perfLogin.getString("idAtividade", "");
				String data_envio = getDateTime();

				// se tudo estiver certo, manda um postHttp
				try {
					postHttpItem(dado, logado, evento, atividade, data_envio);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}

	// metodo posthttp (manda todos os dados)

	@SuppressWarnings("deprecation")
	public void postHttpItens(String logado, String eventos, String atividades,
			String data_envio) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://www.inscrevaseonline.com.br/testeintelligence/credenciamento/retorno.php");

		try {
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("credenciador", logado.toString());
				jsonObject.put("atividade", atividades.toString());
				jsonObject.put("evento", eventos.toString());
				jsonObject.put("inscrito", historyManager.listarTodosQr());
				// jsonObject.put("data_envio",
				// data_envio.toString().replace("\"", ""));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			valores.add(new BasicNameValuePair("credenciamento", jsonObject
					.toString()));

			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			final HttpResponse envios = httpClient.execute(httpPost);
			Log.i("Script", valores.toString());
			final String resp = EntityUtils.toString(envios.getEntity());

			runOnUiThread(new Runnable() {
				public void run() {
					try {
						JSONObject respostJson = new JSONObject(resp);
						boolean retorno = respostJson.getBoolean("retorno");
						if (retorno == true) {
							historyManager.clearHistory();
							progDialog.dismiss();
							Toast.makeText(getBaseContext(),
									R.string.toast_dados_enviados,
									Toast.LENGTH_LONG).show();
							finish();
						} else {
							progDialog.dismiss();
							Toast.makeText(getBaseContext(),
									R.string.toast_dados_nao_enviados,
									Toast.LENGTH_LONG).show();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(getBaseContext(),
								R.string.toast_dados_nao_enviados,
								Toast.LENGTH_LONG).show();
					}
				}
			});
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// manda somente um dado
	@SuppressWarnings("deprecation")
	public void postHttpItem(final String dado, String logado, String evento,
			String atividade, String data_envio) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(
				"http://www.inscrevaseonline.com.br/testeintelligence/credenciamento/retorno.php");

		try {
			ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("credenciador", logado.toString());
				jsonObject.put("atividade", atividade.toString());
				jsonObject.put("evento", evento.toString());
				jsonObject.put("inscrito", historyManager.listarUmQr(dado));
				// jsonObject.put("data_envio",
				// data_envio.toString().replace("\"", ""));
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			valores.add(new BasicNameValuePair("credenciamento", jsonObject
					.toString()));

			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			final HttpResponse envio = httpClient.execute(httpPost);
			final String resp = EntityUtils.toString(envio.getEntity());

			runOnUiThread(new Runnable() {
				public void run() {
					try {
						JSONObject respostJson = new JSONObject(resp);
						boolean retorno = respostJson.getBoolean("retorno");
						if (retorno == true) {
							historyManager.deletePrevious(dado);
							progDialog.dismiss();
							Toast.makeText(getBaseContext(),
									R.string.toast_dado_enviado,
									Toast.LENGTH_LONG).show();
							finish();
						} else {
							progDialog.dismiss();
							Toast.makeText(getBaseContext(),
									R.string.toast_dado_nao_enviado,
									Toast.LENGTH_LONG).show();
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

	// metodo sair...
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
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
			Toast.makeText(getBaseContext(), R.string.toast_info_sem_net,
					Toast.LENGTH_LONG).show();
		}
		return conectado;
	}

	@SuppressLint("SimpleDateFormat")
	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		String timeStamp = dateFormat.format(date).replace("\"", "");
		return timeStamp.replace("\"", "");
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
