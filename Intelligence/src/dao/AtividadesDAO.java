package dao;

import java.util.ArrayList;
import java.util.List;

import model.Atividades;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.zxing.client.android.history.DBHelper;

public final class AtividadesDAO {

	private int indexId;
	private int indexNome;
	private int indexIdEventos;
	private Cursor cursor;
	private final Activity activity;

	public AtividadesDAO(Activity activity) {
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
	public String inserirAtv(Atividades atv) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		ContentValues valores;
		long resultado;
		valores = new ContentValues();
		valores.put(DBHelper.ID_ATIVIDADE, atv.getId());
		valores.put(DBHelper.NOME_ATIVIDADE, atv.getNome());
		valores.put(DBHelper.ID_EVENTO_ATV, atv.getId_evento());
//		valores.put(key, value)
		try {
			db = helper.getWritableDatabase();
			resultado = db.insert(DBHelper.ATIVIDADES, null, valores);
			if (resultado == -1) {
				Log.i("Script",
						"Erro ao inserir registro(" + valores.toString() + ")");
				return "Erro ao inserir registro";
			} else {
				Log.i("Script", "Registro(" + valores.toString()
						+ ") Inserido com sucesso");
				return "Registro Inserido com sucesso”";
			}
		} finally {
			close(null, db);
		}
	}

	public boolean deletar(long id) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		String where = DBHelper.ID_ATIVIDADE + " = ?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };
		try {
			int retorno = db.delete(DBHelper.ATIVIDADES, where, whereArgs);
			if (retorno != 0)
				return true;
			else
				return false;
		} finally {
			close(null, db);
		}
	}

	public boolean atualiza(Atividades atv) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		ContentValues values = new ContentValues();
		values.put(DBHelper.ID_ATIVIDADE, atv.getId());
		values.put(DBHelper.NOME_ATIVIDADE, atv.getNome());
		values.put(DBHelper.ID_EVENTO_ATV, atv.getId_evento());
		String where = DBHelper.ID_ATIVIDADE + " = ?";
		String _id = String.valueOf(atv.getId());
		String[] whereArgs = new String[] { _id };
		try {
			int retorno = db.update(DBHelper.ATIVIDADES, values, where,
					whereArgs);
			if (retorno != 0)
				return true;
			else
				return false;
		} finally {
			close(null, db);
		}
	}

	public Atividades consultar(long id) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Atividades atv;
		String[] columns = new String[] { DBHelper.ID_ATIVIDADE,
				DBHelper.NOME_ATIVIDADE, DBHelper.ID_EVENTO_ATV };
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		try {
			cursor = db.query(DBHelper.ATIVIDADES, columns,
					DBHelper.NOME_ATIVIDADE + " = ?", args, null, null, "nome");
			cursor.moveToFirst();
			atv = new Atividades();
			atv.setId(cursor.getLong(0));
			atv.setNome(cursor.getString(1));
			atv.setId_evento(cursor.getLong(2));
			return atv;
		} finally {
			close(cursor, db);
		}
	}

	public void configuraIndex() {
		this.indexId = this.cursor.getColumnIndex(DBHelper.ID_EVENTO);
		this.indexNome = this.cursor.getColumnIndex(DBHelper.NOME_EVENTO);
		this.indexIdEventos = this.cursor
				.getColumnIndex(DBHelper.ID_EVENTO_ATV);
	}

	public Cursor carregaDados() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		String[] campos = { DBHelper.ID_ATIVIDADE, DBHelper.NOME_ATIVIDADE,
				DBHelper.ID_EVENTO_ATV };
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(DBHelper.ATIVIDADES, campos, null, null, null,
					null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
			}
			return cursor;
		} finally {
			close(cursor, db);
		}
	}

	public Atividades listarPorId(String id) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Atividades atv = null;
		configuraIndex();
		try {
			this.cursor = db.rawQuery("SELECT * FROM " + DBHelper.ATIVIDADES
					+ "WHERE _id = '%d'", null);
			if (this.cursor.moveToFirst()) {
				atv = new Atividades(this.cursor.getLong(this.indexId),
						this.cursor.getString(this.indexNome),
						this.cursor.getLong(this.indexIdEventos));
			}
			return atv;
		} finally {
			close(null, db);
		}
	}

	public List<String> listarNomesAtividades(String nomeEvt) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		List<String> s = new ArrayList<String>();
		try {
			db = helper.getWritableDatabase();
			cursor = db.rawQuery("SELECT " + DBHelper.ATIVIDADES + "."
					+ DBHelper.NOME_ATIVIDADE + " FROM " + DBHelper.ATIVIDADES
					+ " WHERE " + DBHelper.ATIVIDADES + "."
					+ DBHelper.ID_EVENTO_ATV + " in (SELECT "
					+ DBHelper.EVENTOS + "." + DBHelper.ID_EVENTO + " FROM "
					+ DBHelper.EVENTOS + " WHERE " + DBHelper.EVENTOS + "."
					+ DBHelper.NOME_EVENTO + " = '" + nomeEvt + "')", null);
			while (cursor != null && cursor.moveToNext()) {
				s.add(cursor.getString(cursor
						.getColumnIndex(DBHelper.NOME_ATIVIDADE)));
			}
			return s;
		} finally {
			close(cursor, db);
		}
	}

	public String pegaId(String nomeAtv) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery("SELECT " + DBHelper.ID_ATIVIDADE + " FROM "
					+ DBHelper.ATIVIDADES + " WHERE " + DBHelper.NOME_ATIVIDADE
					+ " = '" + nomeAtv + "'", null);
			if (cursor != null && cursor.moveToFirst()) {
				return cursor.getString(cursor
						.getColumnIndex(DBHelper.ID_ATIVIDADE));

			} else {
				return "Atividade not found!";
			}
		} finally {
			close(cursor, db);
		}
	}

	public void clearAtividades() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.ATIVIDADES, null, null);
		} finally {
			close(null, db);
		}
	}
}
