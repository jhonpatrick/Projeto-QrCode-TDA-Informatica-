package dao;

import java.util.ArrayList;
import java.util.List;

import model.Eventos;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.zxing.client.android.history.DBHelper;

public class EventosDAO {

	private Cursor cursor;
	private int indexId;
	private int indexNome;
	private final Activity activity;

	public EventosDAO(Activity activity) {
		this.activity = activity;
	}

	private static void close(Cursor cursor, SQLiteDatabase database) {
		if (cursor != null) {
			cursor.close();
		}
		if (database != null) {
			database.close();
		}
	}

	// implementando os metodo de inserir
	public String inserirEv(Eventos eventos) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		ContentValues valores;
		long resultado;
		valores = new ContentValues();
		valores.put(DBHelper.ID_EVENTO, eventos.getId());
		valores.put(DBHelper.NOME_EVENTO, eventos.getNome());
		try {
			db = helper.getWritableDatabase();
			resultado = db.insert(DBHelper.EVENTOS, null, valores);
			if (resultado == -1) {
				Log.i("Script",
						"Erro ao inserir registro(" + valores.toString() + ")");
				return "Erro ao inserir registro";
			} else {
				Log.i("Script", "Registro(" + valores.toString()
						+ ") Inserido com suces");
				return "Registro Inserido com sucesso”";
			}
		} finally {
			close(null, db);
		}
	}

	public boolean deletar(long id) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		String where = DBHelper.ID_EVENTO + " = ?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };
		try {
			int retorno = db.delete(DBHelper.EVENTOS, where, whereArgs);
			if (retorno != 0)
				return true;
			else
				return false;
		} finally {
			close(null, db);
		}
	}

	public boolean atualiza(Eventos eventos) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		ContentValues values = new ContentValues();
		values.put(DBHelper.ID_EVENTO, eventos.getId());
		values.put(DBHelper.NOME_EVENTO, eventos.getNome());
		String where = DBHelper.ID_EVENTO + " = ?";
		String _id = String.valueOf(eventos.getId());
		String[] whereArgs = new String[] { _id };
		try {
			int retorno = db.update(DBHelper.EVENTOS, values, where, whereArgs);
			if (retorno != 0)
				return true;
			else
				return false;
		} finally {
			close(null, db);
		}
	}

	public Eventos consultar(long id) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Eventos evento;
		String[] columns = new String[] { DBHelper.ID_EVENTO,
				DBHelper.NOME_EVENTO };
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		try {
			cursor = db.query(DBHelper.EVENTOS, columns, DBHelper.NOME_EVENTO
					+ " = ?", args, null, null, DBHelper.NOME_EVENTO);
			cursor.moveToFirst();
			evento = new Eventos();
			evento.setId(cursor.getLong(0));
			evento.setNome(cursor.getString(1));
			return evento;
		} finally {
			close(cursor, db);
		}
	}

	public void configuraIndex() {
		this.indexId = this.cursor.getColumnIndex(DBHelper.ID_EVENTO);
		this.indexNome = this.cursor.getColumnIndex(DBHelper.NOME_EVENTO);
	}

	public Cursor carregaDados() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		String[] campos = { DBHelper.ID_EVENTO, DBHelper.NOME_EVENTO };
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(DBHelper.EVENTOS, campos, null, null, null, null,
					null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			return cursor;
		} finally {
			close(cursor, db);
		}
	}

	public Eventos listarPorId(String id) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Eventos eventos = null;
		configuraIndex();
		try {
			cursor = db.rawQuery("SELECT * FROM " + DBHelper.EVENTOS
					+ "WHERE _id = '%d'", null);
			if (this.cursor.moveToFirst()) {
				eventos = new Eventos(this.cursor.getLong(this.indexId),
						this.cursor.getString(this.indexNome));
			}
			return eventos;
		} finally {
			close(cursor, db);
		}
	}

	public List<String> listarNomesEventos() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		List<String> listEV = new ArrayList<String>();
		try {
			db = helper.getWritableDatabase();
			cursor = db.rawQuery("SELECT " + DBHelper.NOME_EVENTO + " FROM "
					+ DBHelper.EVENTOS, null);
			if (cursor != null) {
				cursor.moveToFirst();
				do {
					listEV.add(cursor.getString(cursor
							.getColumnIndex(DBHelper.NOME_EVENTO)));
				} while (cursor.moveToNext());
			}
			return listEV;
		} finally {
			close(cursor, db);
		}
	}

	public String pegaId(String nomeEvent) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery("SELECT " + DBHelper.ID_EVENTO + " FROM "
					+ DBHelper.EVENTOS + " WHERE " + DBHelper.NOME_EVENTO
					+ " = '" + nomeEvent + "'", null);
			if (cursor != null && cursor.moveToFirst()) {
				return cursor.getString(cursor
						.getColumnIndex(DBHelper.ID_EVENTO));

			} else {
				return "Evento not found!";
			}
		} finally {
			close(cursor, db);
		}
	}

	public void clearEventos() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.EVENTOS, null, null);
		} finally {
			close(null, db);
		}
	}
}
