package dao;

import model.Inscritos;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.zxing.client.android.history.DBHelper;

public final class InscritosDAO {
	
	private SQLiteDatabase db = null;
	private DBHelper helper;
	
	public InscritosDAO(Context context){
		helper = new DBHelper(context);
	}
	
	public String inserirInsc(Inscritos insc) {
		ContentValues valores; 
		long resultado; 
		db = helper.getWritableDatabase(); 
		valores = new ContentValues(); 
		valores.put(helper.ID_INSCRITOS, insc.getId_inscritos());
		valores.put(helper.ID_ATIVIDADE_REF, insc.getId_atividades()); 
		resultado = db.insert(helper.INSCRITOS, null, valores); 
		db.close(); 
		if (resultado ==-1) return "Erro ao inserir registro"; 
		else {
			return "Registro Inserido com sucesso”"; 
		}

	}

	public Inscritos consultar(long id) {
		// TODO Auto-generated method stub
		Inscritos insc;
		db = helper.getReadableDatabase();
		String[] columns = new String[] { helper.ID_INSCRITOS, helper.ID_ATIVIDADE_REF };
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		Cursor c = db.query(helper.INSCRITOS, columns, helper.ID_INSCRITOS + " = ?", args, null,
				null, helper.ID_INSCRITOS);

		c.moveToFirst();
		insc = new Inscritos();
		insc.setId_inscritos(c.getLong(0));
		insc.setId_atividades(c.getLong(1));
		return insc;

	}

	public Inscritos buscarDado(long id){
		Inscritos insc = null;
		db = helper.getReadableDatabase();
		String[] columns = new String[] { helper.ID_INSCRITOS};
		String _id = String.valueOf(id);
		String[] args = new String[] { _id };
		Cursor c = db.query(helper.INSCRITOS, columns, helper.ID_INSCRITOS + " = ?", args, null,
				null, helper.ID_INSCRITOS);
		if(c != null && c.getCount() > 0){
			c.moveToFirst();
			insc = new Inscritos();
			insc.setId_inscritos(c.getLong(0));
		}
		c.close();
		return insc;
	}
}
