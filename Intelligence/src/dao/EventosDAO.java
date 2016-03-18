package dao;

import java.util.ArrayList;

import model.Eventos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import banco.CriaBanco;

public class EventosDAO {

	private SQLiteDatabase db = null;
	private CriaBanco criaBanco = null;
	private Cursor cursor;
	private int indexId;
	private int indexNome;

	public EventosDAO(Context context) {
		criaBanco = new CriaBanco(context);
	}

	// implementando os metodo de inserir

	public String inserir(Eventos eventos){ 
		ContentValues valores; 
		long resultado; 
		db = criaBanco.getWritableDatabase(); 
		valores = new ContentValues(); 
		valores.put(criaBanco.ID_EVENTO, eventos.get_id()); 
		valores.put(CriaBanco.NOME_EVENTO, eventos.getNome()); 
		resultado = db.insert(criaBanco.TABELA1, null, valores); 
		db.close(); 
		if (resultado ==-1) return "Erro ao inserir registro"; 
		else {
			return "Registro Inserido com sucesso”"; 
		}
	}

	public boolean deletar(long id) {

		Eventos eventos = new Eventos();
		String where = criaBanco.ID_EVENTO + " = ?";
		String _id = String.valueOf(eventos.get_id());
		String[] whereArgs = new String[] { _id };

		int retorno = this.db.delete(criaBanco.TABELA1, where, whereArgs);

		if (retorno != 0)
			return true;
		else
			return false;
	}

	public boolean atualiza(Eventos eventos) {
		// TODO Auto-generated method stub

		ContentValues values = new ContentValues();
		values.put(criaBanco.ID_EVENTO, eventos.get_id());
		values.put(criaBanco.NOME_EVENTO, eventos.getNome());

		String where = criaBanco.ID_EVENTO + " = ?";
		String _id = String.valueOf(eventos.get_id());
		String[] whereArgs = new String[] { _id };

		int retorno = this.db.update(criaBanco.TABELA1, values, where,
				whereArgs);

		if (retorno != 0)
			return true;
		else
			return false;
	}

	public Eventos consultar(long id) {
		// TODO Auto-generated method stub
		Eventos evento;
		String[] columns = new String[] { criaBanco.ID_EVENTO,
				criaBanco.NOME_EVENTO };
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		Cursor c = db.query(criaBanco.TABELA1, columns, "nome = ?", args, null,
				null, "nome");

		c.moveToFirst();
		evento = new Eventos();
		evento.set_id(c.getLong(0));
		evento.setNome(c.getString(1));
		return evento;

	}

	public void configuraIndex() {
		this.indexId = this.cursor.getColumnIndex(criaBanco.ID_EVENTO);
		this.indexNome = this.cursor.getColumnIndex(criaBanco.NOME_EVENTO);
	}

//	public ArrayList<Eventos> listarTodosEventos() {
//		ArrayList<Eventos> listEventos = new ArrayList<Eventos>();
//		Eventos eventos;
//
//		this.cursor = this.db.rawQuery("SELECT * FROM " + criaBanco.TABELA1,
//				null);
//		configuraIndex();
//
//		while (this.cursor.moveToNext()) {
//			eventos = new Eventos(this.cursor.getLong(this.indexId),
//					this.cursor.getString(this.indexNome));
//			listEventos.add(eventos);
//		}
//		return listEventos;
//	}
	
	public Cursor carregaDados(){ 
		Cursor cursor; String[] campos = {criaBanco.ID_EVENTO, criaBanco.NOME_EVENTO}; 
		db = criaBanco.getReadableDatabase(); 
		cursor = db.query(criaBanco.TABELA1, campos, null, null, null, null, null, null); 
		if(cursor != null){ 
			cursor.moveToFirst(); 
		} 
		db.close(); 
		return cursor; 
	}

	//	private static final String SQL_SELECT_ALL = "SELECT * FROM pessoa ORDER BY nome";
//	private static final String SQL_SELECT_NOME = "SELECT * FROM pessoa WHERE nome = '%s'";
	
	public ArrayList<Eventos> listarPorNome(String nome) {
		ArrayList<Eventos> listEventos = new ArrayList<Eventos>();
		Eventos eventos;

		this.cursor = this.db.rawQuery("SELECT * FROM " + criaBanco.NOME_EVENTO + "WHERE nome = '%s'",
				null);
		configuraIndex();

		while (this.cursor.moveToNext()) {
			eventos = new Eventos(this.cursor.getLong(this.indexId),
					this.cursor.getString(this.indexNome));
			listEventos.add(eventos);
		}
		return listEventos;
	}

	public Eventos listarPorId(String id) {
		Eventos eventos = null;

		this.cursor = this.db.rawQuery("SELECT * FROM " + criaBanco.ID_EVENTO + "WHERE _id = '%d'", null);
		configuraIndex();

		if (this.cursor.moveToFirst()) {
			eventos = new Eventos(this.cursor.getLong(this.indexId),
					this.cursor.getString(this.indexNome));
		}

		return eventos;
	}
	
	public void deletarTabelaEventos(){ 
		db.delete(criaBanco.TABELA1, null, null);
	}
}