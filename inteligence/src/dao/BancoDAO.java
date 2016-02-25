package dao;

import java.util.Date;
import java.util.List;

import model.ScannerQr;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import banco.CriaBanco;

public class BancoDAO{

	//private Context context;
	private SQLiteDatabase db;	
	private CriaBanco criaBanco;
	
	public BancoDAO(Context context) {
		//this.context = context;
		criaBanco = new CriaBanco(context);
	}

	//criando metodo de inserir

	public String inserir(String qr_code, Date data_leitura, long imei_cel) {
		// TODO Auto-generated method stub
		
		//CriaBanco db = new CriaBanco(context);
		//pegando os valores
		ContentValues valores;
		
		long resultado_inserir;
		
		valores = new ContentValues();
		//colocando o banco em modo de leitura
		//SQLiteDatabase sdb = db.getWritableDatabase();
		db = criaBanco.getWritableDatabase();
		
		//ContentValues values = new ContentValues();
		
		valores.put(CriaBanco.QR_CODE, qr_code);
		valores.put(CriaBanco.DATA_LEITURA, data_leitura.toString());
		valores.put(CriaBanco.IMEI_CEL, imei_cel);
		resultado_inserir = db.insert(CriaBanco.TABELA, null, valores);
		db.close();
		
		if(resultado_inserir == -1)
			return "Erro ao inserir registro de leitura";
		else
			return "Registro de leitura inserido com sucesso";
	}

	public void deletar(ScannerQr insc) {
		// TODO Auto-generated method stub
		
	}

	public void atualiza(ScannerQr insc) {
		// TODO Auto-generated method stub
		
	}

	public ScannerQr consultar(long id) {
		// TODO Auto-generated method stub
		String query = "Select _id from ScannerQr";
		
		return null;
	}

	public List<ScannerQr> listar() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
