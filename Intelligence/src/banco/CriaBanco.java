package banco;

import android.R.string;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper {

	//criando tabelas e tuplas no sqlite
	public static final String NOME_BANCO = "EventosAtividades.db";
	
	//Tabela de eventos
	public static final String TABELA1 = "eventos";
	public static final String ID_EVENTO = "_id";
	public static final String NOME_EVENTO = "nome";
	
	//Tabela de atividades
	public static final String TABELA2 = "atividades";
	public static final String ID_ATIVIDADE = "_id";
	public static final String NOME_ATIVIDADE = "nome";
	
	public static final int VERSAO = 1;

	public CriaBanco(Context context) {
		super(context, NOME_BANCO, null, VERSAO);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		//CRIANDO TABELAS
		String sql = "CREATE TABLE " + TABELA1 + "(" + 
				ID_EVENTO + " integer primary key, " +
				NOME_EVENTO + " text" + ");" + 
				"CREATE TABLE " + TABELA2 + "(" + 
				ID_ATIVIDADE + " integer primary key, " +
				NOME_ATIVIDADE + " text" + ");";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// apagando a tabela
		String sql = "DROP TABLE IF EXISTS" + TABELA1 + ";" 
		+ "DROP TABLE IF EXISTS" + TABELA2 + ";";
		db.execSQL(sql);
		// recriando a tabela
		onCreate(db);

	}

}
