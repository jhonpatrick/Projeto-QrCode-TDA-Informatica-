package dao;

import java.util.ArrayList;
import java.util.List;

import model.Eventos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.zxing.client.android.history.DBHelper;

public class EventosDAO {

	private SQLiteDatabase db = null;
	private DBHelper helper = null;
	private Cursor cursor;
	private int indexId;
	private int indexNome;

	public EventosDAO(Context context) {
		helper = new DBHelper(context);
	}

	// implementando os metodo de inserir
	public String inserirEv(Eventos eventos) {

		ContentValues valores; 
		long resultado; 
		db = helper.getWritableDatabase(); 
		valores = new ContentValues(); 
		valores.put(helper.ID_EVENTO, eventos.get_id()); 
		valores.put(helper.NOME_EVENTO, eventos.getNome()); 
		resultado = db.insert(helper.EVENTOS, null, valores); 
		db.close(); 
		if (resultado ==-1) return "Erro ao inserir registro"; 
		else {
			return "Registro Inserido com sucesso”"; 
		}

	}

	public boolean deletar(long id) {

		String where = helper.ID_EVENTO + " = ?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int retorno = this.db.delete(helper.EVENTOS, where, whereArgs);

		if (retorno != 0)
			return true;
		else
			return false;
	}

	public boolean atualiza(Eventos eventos) {
		// TODO Auto-generated method stub

		ContentValues values = new ContentValues();
		values.put(helper.ID_EVENTO, eventos.get_id());
		values.put(helper.NOME_EVENTO, eventos.getNome());

		String where = helper.ID_EVENTO + " = ?";
		String _id = String.valueOf(eventos.get_id());
		String[] whereArgs = new String[] { _id };

		int retorno = this.db.update(helper.EVENTOS, values, where,
				whereArgs);

		if (retorno != 0)
			return true;
		else
			return false;
	}

	public Eventos consultar(long id) {
		// TODO Auto-generated method stub
		Eventos evento;
		String[] columns = new String[] { helper.ID_EVENTO,
				helper.NOME_EVENTO };
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		Cursor c = db.query(helper.EVENTOS, columns, helper.NOME_EVENTO + " = ?", args, null,
				null, helper.NOME_EVENTO);

		c.moveToFirst();
		evento = new Eventos();
		evento.set_id(c.getLong(0));
		evento.setNome(c.getString(1));
		return evento;

	}

	public void configuraIndex() {
		this.indexId = this.cursor.getColumnIndex(helper.ID_EVENTO);
		this.indexNome = this.cursor.getColumnIndex(helper.NOME_EVENTO);
	}

	public Cursor carregaDados() {
		Cursor c;
		String[] campos = { helper.ID_EVENTO, helper.NOME_EVENTO };
		db = helper.getReadableDatabase();
		c = db.query(helper.EVENTOS, campos, null, null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		db.close();
		return cursor;
	}

	public Eventos listarPorId(String id) {
		Eventos eventos = null;

		this.cursor = this.db.rawQuery("SELECT * FROM " + helper.EVENTOS
				+ "WHERE _id = '%d'", null);
		configuraIndex();

		if (this.cursor.moveToFirst()) {
			eventos = new Eventos(this.cursor.getLong(this.indexId),
					this.cursor.getString(this.indexNome));
		}

		return eventos;
	}

	public void deletarTabelaEventos() {
		db = helper.getWritableDatabase();
		String sql = "DROP TABLE IF EXISTS" + helper.EVENTOS;
		db.execSQL(sql);
	}

	public List<String> listarNomesEventos() {
		List<String> s = new ArrayList<String>();
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT " + helper.NOME_EVENTO + " FROM "
				+ helper.EVENTOS, null);
		if (c != null) {
			c.moveToFirst();
			do {
				s.add(c.getString(c.getColumnIndex(helper.NOME_EVENTO)));
			} while (c.moveToNext());
		}
		c.close();
		return s;
	}
	
	
}
