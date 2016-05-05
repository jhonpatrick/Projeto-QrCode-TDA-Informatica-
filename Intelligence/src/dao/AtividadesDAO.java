package dao;

import java.util.ArrayList;
import java.util.List;

import model.Atividades;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.zxing.client.android.history.DBHelper;

public class AtividadesDAO {
	
	private SQLiteDatabase db = null;
	private DBHelper helper;
	private Cursor cursor;
	private int indexId;
	private int indexNome;
	private int indexIdEventos;

	public AtividadesDAO(Context context) {
		helper = new DBHelper(context);
	}

	// implementando os metodo de inserir
	public String inserirAtv(Atividades atv) {
		ContentValues valores; 
		long resultado; 
		db = helper.getWritableDatabase(); 
		valores = new ContentValues(); 
		valores.put(helper.ID_ATIVIDADE, atv.get_id()); 
		valores.put(helper.NOME_ATIVIDADE, atv.getNome());
		valores.put(helper.ID_EVENTO_ATV, atv.getId_evento());
		resultado = db.insert(helper.ATIVIDADES, null, valores); 
		db.close(); 
		if (resultado ==-1) return "Erro ao inserir registro"; 
		else {
			return "Registro Inserido com sucesso”"; 
		}

	}

	public boolean deletar(long id) {
		String where = helper.ID_ATIVIDADE + " = ?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[] { _id };

		int retorno = this.db.delete(helper.ATIVIDADES, where, whereArgs);

		if (retorno != 0)
			return true;
		else
			return false;
	}

	public boolean atualiza(Atividades atv) {
		// TODO Auto-generated method stub

		ContentValues values = new ContentValues();
		values.put(helper.ID_ATIVIDADE, atv.get_id());
		values.put(helper.NOME_ATIVIDADE, atv.getNome());
		values.put(helper.ID_EVENTO_ATV, atv.getId_evento());

		String where = helper.ID_ATIVIDADE + " = ?";
		String _id = String.valueOf(atv.get_id());
		String[] whereArgs = new String[] { _id };

		int retorno = this.db.update(helper.ATIVIDADES, values, where,
				whereArgs);

		if (retorno != 0)
			return true;
		else
			return false;
	}

	public Atividades consultar(long id) {
		// TODO Auto-generated method stub
		Atividades atv;
		String[] columns = new String[] { helper.ID_ATIVIDADE,
				helper.NOME_ATIVIDADE, helper.ID_EVENTO_ATV };
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		Cursor c = db.query(helper.ATIVIDADES, columns, "nome = ?", args, null,
				null, "nome");

		c.moveToFirst();
		atv = new Atividades();
		atv.set_id(c.getLong(0));
		atv.setNome(c.getString(1));
		atv.setId_evento(c.getLong(2));
		return atv;

	}

	public void configuraIndex() {
		this.indexId = this.cursor.getColumnIndex(helper.ID_EVENTO);
		this.indexNome = this.cursor.getColumnIndex(helper.NOME_EVENTO);
		this.indexIdEventos = this.cursor.getColumnIndex(helper.ID_EVENTO_ATV);
	}

	public Cursor carregaDados() {
		Cursor c;
		String[] campos = { helper.ID_ATIVIDADE, helper.NOME_ATIVIDADE, helper.ID_EVENTO_ATV};
		db = helper.getReadableDatabase();
		c = db.query(helper.ATIVIDADES, campos, null, null, null, null, null,
				null);
		if (c != null) {
			c.moveToFirst();
		}
		db.close();
		return cursor;
	}

	public Atividades listarPorId(String id) {
		Atividades atv = null;

		this.cursor = this.db.rawQuery("SELECT * FROM " + helper.ATIVIDADES
				+ "WHERE _id = '%d'", null);
		configuraIndex();

		if (this.cursor.moveToFirst()) {
			atv = new Atividades(
					this.cursor.getLong(this.indexId),
					this.cursor.getString(this.indexNome),
					this.cursor.getLong(this.indexIdEventos));
		}

		return atv;
	}

	public void deletarTabelaAtividades(Atividades atv) {
		db = helper.getReadableDatabase();
		db.delete(helper.ATIVIDADES, null, null);
	}

	public List<String> listarNomesAtividades(String nomeEvt) {
		List<String> s = new ArrayList<String>();
		db = helper.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT " + helper.ATIVIDADES+"."+helper.NOME_ATIVIDADE +
				" FROM " + helper.ATIVIDADES + 
				" WHERE " + helper.ATIVIDADES+"."+helper.ID_EVENTO_ATV + 
				" in (SELECT " + helper.EVENTOS+"."+helper.ID_EVENTO + 
				" FROM " + helper.EVENTOS + 
				" WHERE " + helper.EVENTOS+"."+helper.NOME_EVENTO + " = '" + nomeEvt + "')", null);
		/*
select atividade.nome_atividade from atividade where atividade.id_evento 
in (select evento.id_evento from evento where evento.nome_evento = nomeAtv);
		 */
		while (c != null  && c.moveToNext()) {
			s.add(c.getString(c.getColumnIndex(helper.NOME_ATIVIDADE)));
		}
		c.close();
		return s;
	}
	
}
