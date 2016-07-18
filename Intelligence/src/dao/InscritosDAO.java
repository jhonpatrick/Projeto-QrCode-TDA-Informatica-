package dao;

import model.Inscritos;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.zxing.client.android.history.DBHelper;

public final class InscritosDAO {

	private Cursor cursor;
	private final Activity activity;

	public InscritosDAO(Activity activity) {
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

	public String inserirInsc(Inscritos insc) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		ContentValues values;
		long resultado;
		values = new ContentValues();
		values.put(DBHelper.ID_INSCRITOS, insc.getId_inscritos());
		try {
			db = helper.getWritableDatabase();
			resultado = db.insert(DBHelper.INSCRITOS, null, values);
			if (resultado == -1) {
				Log.i("Script", "Erro ao inserir registro(" + values.toString()
						+ ")");
				return "Erro ao inserir registro";
			} else {
				Log.i("Script", "Registro(" + values.toString()
						+ ") Inserido com sucesso");
				return "Registro Inserido com sucesso";
			}
		} finally {
			close(null, db);
		}
	}

	public Inscritos buscarDado(long id_insc) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Inscritos insc = null;
		String _id_insc = String.valueOf(id_insc);
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery("SELECT " + DBHelper.ID_INSCRITOS + " FROM "
					+ DBHelper.INSCRITOS + " WHERE " + DBHelper.ID_INSCRITOS
					+ " = '" + _id_insc.toString() + "'", null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				insc = new Inscritos();
				insc.setId_inscritos(cursor.getLong(0));
			}
			return insc;
		} finally {
			close(cursor, db);
		}
	}

	public void clearInscritos() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.INSCRITOS, null, null);
		} finally {
			close(null, db);
		}
	}
}
