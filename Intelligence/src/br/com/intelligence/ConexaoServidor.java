package br.com.intelligence;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.intelligence.R;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ConexaoServidor extends AsyncTask<String, String, JSONObject> {

	private ProgressDialog dialog;
	private Context context;

	public ConexaoServidor(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context, 3);
		dialog.setTitle(context.getResources().getString(
				R.string.alerta_logando));
		dialog.setMessage(context.getResources().getString(
				R.string.alerta_carrega_dados));
		dialog.show();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 * 
	 * params[0] = url params[1] = login params[2] = senha
	 * 
	 * Dados que seram mandados para o servidor php, para valida o login e dá
	 * uma retorno
	 */

	@Override
	protected JSONObject doInBackground(String... params) {
		int timeOut = 3000;
		JSONObject jsonObject = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeOut);
		HttpConnectionParams.setSoTimeout(httpParams, timeOut);
		HttpPost httpPost = new HttpPost(params[0]);
		ArrayList<NameValuePair> valores = new ArrayList<NameValuePair>();
		valores.add(new BasicNameValuePair("login", params[1]));
		valores.add(new BasicNameValuePair("senha", params[2]));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(valores));
			final HttpResponse resposta = httpClient.execute(httpPost);
			final String resp = EntityUtils.toString(resposta.getEntity());
			jsonObject = new JSONObject(resp.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	protected void onProgressUpdate(String... params) {
		dialog.setMessage(context.getResources().getString(
				R.string.alerta_carrega_dados));
	}

	@Override
	protected void onPostExecute(JSONObject params) {
		dialog.dismiss();
	}
}
