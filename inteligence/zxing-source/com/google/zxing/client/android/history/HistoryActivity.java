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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import br.com.inteligence.MainActivity;
import br.com.inteligence.R;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;

public final class HistoryActivity extends ListActivity {

	private static final String TAG = HistoryActivity.class.getSimpleName();

	private HistoryManager historyManager;
	private ArrayAdapter<HistoryItem> adapter;
	private CharSequence originalTitle;


	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.historyManager = new HistoryManager(this);
		adapter = new HistoryItemAdapter(this);
		setListAdapter(adapter);
		View listview = getListView();
		registerForContextMenu(listview);
		originalTitle = getTitle();
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (adapter.getItem(position).getResult() != null) {
			Intent intent = new Intent(this, CaptureActivity.class);
			intent.putExtra(Intents.History.ITEM_NUMBER, position);
			setResult(Activity.RESULT_OK, intent);
			Toast.makeText(this, "Clique e segure para vê opções!", Toast.LENGTH_SHORT).show();
			// finish();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
		
			if (position >= adapter.getCount()|| adapter.getItem(position).getResult() != null) {
				menu.add(Menu.NONE, position, position, R.string.history_clear_one_history_text);
				menu.add(Menu.NONE, position, position, R.string.history_enviar_um);
			}
		} // else it's just that dummy "Empty" message


	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		/*if(item.getItemId() == R.id.menu_history_clear_text){
				
			int position = item.getItemId();
		    historyManager.deleteHistoryItem(position);
		    Toast.makeText(this, "Apagado com sucesso!", Toast.LENGTH_SHORT).show();
		    reloadHistoryItems();
		    return true;
		    
		}else if(item.getItemId() == R.id.menu_history_upload_text){

			int position = item.getItemId();
			Toast.makeText(this, "Enviado com sucesso!", Toast.LENGTH_SHORT).show();
		    historyManager.deleteHistoryItem(position);
		    reloadHistoryItems();
		    return true;
		    
		}
	    return true;*/
		
		final int position = item.getItemId();
		
		
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setMessage("Enviar?");
		builder1.setCancelable(true);
		builder1.setPositiveButton(R.string.button_ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int i2) {
						
						/*
						 * substituir tudo pelo metodo enviarDados()
						 */
						
						historyManager.deleteHistoryItem(position);
						dialog.dismiss();
						reloadHistoryItems();
//						Toast.makeText(this, "Enviando todos...", Toast.LENGTH_SHORT)
//						.show();
					}
				});
		Toast.makeText(this, "Enviados com sucesso!", Toast.LENGTH_SHORT)
		.show();
		builder1.setNegativeButton(R.string.button_cancel, null);
		builder1.show();
		return true;
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
		switch (item.getItemId()) {
		case R.id.menu_history_upload_text:
			/*
			 * enviar todos os qr's lidos
			 */
			
			AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
			builder1.setMessage(R.string.msg_confirmar_envio);
			builder1.setCancelable(true);
			builder1.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int i2) {
							historyManager.clearHistory();
							dialog.dismiss();
							finish();
//							Toast.makeText(this, "Enviando todos...", Toast.LENGTH_SHORT)
//							.show();
						}
					});
			Toast.makeText(this, "Enviados com sucesso!", Toast.LENGTH_SHORT)
			.show();
			builder1.setNegativeButton(R.string.button_cancel, null);
			builder1.show();
			
			
			/*CharSequence history = historyManager.buildHistory();
			Parcelable historyFile = HistoryManager.saveHistory(history
					.toString());
			if (historyFile == null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.msg_unmount_usb);
				builder.setPositiveButton(R.string.button_ok, null);
				builder.show();
			} else {
				Intent intent = new Intent(Intent.ACTION_SEND,
						Uri.parse("mailto:"));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				String subject = getResources().getString(
						R.string.history_email_title);
				intent.putExtra(Intent.EXTRA_SUBJECT, subject);
				intent.putExtra(Intent.EXTRA_TEXT, subject);
				intent.putExtra(Intent.EXTRA_STREAM, historyFile);
				intent.setType("text/csv");
				try {
					startActivity(intent);
				} catch (ActivityNotFoundException anfe) {
					Log.w(TAG, anfe.toString());
				}
			}*/
			break;
		case R.id.menu_history_clear_text:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	
	
	//meus metodos
	
	/*public  boolean verificaConexao() {
	    boolean conectado;
		ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (conectivtyManager.getActiveNetworkInfo() != null
	            && conectivtyManager.getActiveNetworkInfo().isAvailable()
	            && conectivtyManager.getActiveNetworkInfo().isConnected()) {
	    	conectado = true;
	    	Toast.makeText(this, "Conectado!", Toast.LENGTH_LONG).show();
	    } else {
	        conectado = false;
	        Toast.makeText(this, "Não Conectado! Verifique sua conexão com a internet", Toast.LENGTH_LONG).show();
	    }
	    return conectado;
	}*/
	
	// verifica se tem conexão 
	public boolean netWorkdisponibilidade(Context cont){
        boolean conectado = false;
        ConnectivityManager conmag;
        conmag = (ConnectivityManager)cont.getSystemService(Context.CONNECTIVITY_SERVICE);
        conmag.getActiveNetworkInfo();
        //Verifica o WIFI
        if(conmag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
            conectado = true;
            Toast.makeText(this, "WI-Fi Conectado!", Toast.LENGTH_LONG).show();
        }
       //Verifica o 3G
        else if(conmag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()){
            conectado = true;
            Toast.makeText(this, "3G Conectado!", Toast.LENGTH_LONG).show();
        }
        else{
            conectado = false;
        }
        return conectado;
    }
	
	//metodo de enviar dados para o servidor
	public void enviarDados(){
		String sql = "SELECT qrcode, ";
	}
	
	// metodo http
	
	public void postHttp(){
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent voltar = new Intent(this, MainActivity.class);
		startActivity(voltar);
		finish();
	}	
	
	  //pegando a data do sistema
	  
	 
	
}
