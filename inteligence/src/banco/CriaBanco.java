package banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CriaBanco extends SQLiteOpenHelper{
	
	/*
	 * Criando um banco de dados para salvar todos os scaner lidos, com o qrcode, 
	 * dentro do qr --> está salvo o ID da table "Inscricoes" que vai ser salva no sqlite com os atributos:
	 * 
	 * create database tda_eventos;
	 * use tda_eventos;
	 * 
	 * create table scaner(
	 * _id int primary key auto_increment,
	 * qrcode varchar (8) not null,
	 * data_leitura dateTime not null,
	 * imeiCel varchar (30)
	 * );
	 * 
	 */
	
	public static final String NOME_BANCO = "tda_eventos.db";
	public static final String TABELA = "scaner";
	//id da inscrição
	public static final String ID = "_id";
	//numero da inscrição que vai ser mandada pra o servidor da tda
	public static final String QR_CODE = "qrcode";
	public static final String DATA_LEITURA = "data_leitura";
	public static final String IMEI_CEL = "imei_cel";
	
	public static final int VERSAO = 1;

	public CriaBanco(Context context) {
		super(context, NOME_BANCO, null, VERSAO);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String sql = "CREATE TABLE "+ TABELA + "(" + 
				ID + "integer primary key autoincrement," +
				QR_CODE + "integer" +
				DATA_LEITURA + "dateTime" + 
				IMEI_CEL + "varchar (30)" +
		")";
		
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		//apagando a tabela
		String sql = "DROP TABLE IF EXISTS" + TABELA;
		db.execSQL(sql);
		//recriando a tabela
		onCreate(db);
	
	}

}
