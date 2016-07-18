package dao;

import model.Credenciador;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.zxing.client.android.history.DBHelper;

public class CredenciadorDAO {

	private final Activity activity;
	private Cursor cursor;

	public CredenciadorDAO(Activity activity) {
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
	public String inserirCredenciador(Credenciador cred) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		ContentValues valores;
		long resultado;
		valores = new ContentValues();
		valores.put(DBHelper.ID_CREDENCIADOR, cred.get_Id());
		valores.put(DBHelper.NOME_CREDENCIADOR, cred.getNome());
		valores.put(DBHelper.SENHA_CREDENCIADOR, cred.getSenha());
		valores.put(DBHelper.EMAIL_CREDENCIADOR, cred.getEmail());
		try {
			db = helper.getWritableDatabase();
			resultado = db.insert(DBHelper.CREDENCIADOR, null, valores);
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

	public String pegaIdCred(String nomeCred) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.rawQuery("SELECT " + DBHelper.ID_CREDENCIADOR
					+ " FROM " + DBHelper.CREDENCIADOR + " WHERE "
					+ DBHelper.NOME_CREDENCIADOR + " = '" + nomeCred + "'",
					null);
			if (cursor != null && cursor.moveToFirst()) {
				return cursor.getString(cursor
						.getColumnIndex(DBHelper.ID_CREDENCIADOR));

			} else {
				return "Credenciador not found!";
			}
		} finally {
			close(cursor, db);
		}
	}

	public void clearCredenciadores() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.CREDENCIADOR, null, null);
		} finally {
			close(null, db);
		}
	}

}
