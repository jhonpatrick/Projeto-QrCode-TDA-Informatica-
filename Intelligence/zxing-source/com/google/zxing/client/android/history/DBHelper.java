/*
 * Copyright (C) 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android.history;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	private static final int DB_VERSION = 7;
	private static final String DB_NAME = "tda.db";

	public static final String CREDENCIADOR = "credenciador";
	public static final String ID_CREDENCIADOR = "_idCred";
	public static final String NOME_CREDENCIADOR = "nomeCred";
	public static final String EMAIL_CREDENCIADOR = "emailCred";
	public static final String SENHA_CREDENCIADOR = "senhaCred";

	// Tabela Historico
	public static final String TABLE_NAME = "historico";
	public static final String ID_COL = "_id";
	public static final String TEXT_COL = "qrcode";
	public static final String FORMAT_COL = "format";
	public static final String DISPLAY_COL = "display";
	public static final String TIMESTAMP_COL = "timestamp";

	// Tabela de eventos
	public static final String EVENTOS = "eventos";
	public static final String ID_EVENTO = "_idEventos";
	public static final String NOME_EVENTO = "nomeEvento";

	// Tabela de atividades
	public static final String ATIVIDADES = "atividades";
	public static final String ID_ATIVIDADE = "_idAtividade";
	public static final String NOME_ATIVIDADE = "nomeAtividade";
	public static final String ID_EVENTO_ATV = "idEvento";
	

	// Tabela de inscritos
	public static final String INSCRITOS = "inscritos";
	public static final String ID_INSCRITOS = "_idInscritos";

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		// criando tables no sqlite
		String sql = "CREATE TABLE " + CREDENCIADOR + "(" + ID_CREDENCIADOR
				+ " INTEGER PRIMARY KEY, " + NOME_CREDENCIADOR
				+ " VARCHAR(150), " + SENHA_CREDENCIADOR + " VARCHAR(150), "
				+ EMAIL_CREDENCIADOR + " VARCHAR(150)" + ");";
		sqLiteDatabase.execSQL(sql);
		Log.i("Script", sql);

		String sql1 = "CREATE TABLE " + EVENTOS + "(" + ID_EVENTO
				+ " INTEGER PRIMARY KEY, " + NOME_EVENTO + " TEXT" + ");";
		sqLiteDatabase.execSQL(sql1);
		Log.i("Script", sql1);

		String sql2 = "CREATE TABLE " + ATIVIDADES + "(" + ID_ATIVIDADE
				+ " INTEGER PRIMARY KEY, " + NOME_ATIVIDADE + " TEXT, "
				+ ID_EVENTO_ATV + " INTEGER, " + "foreign key(" + ID_EVENTO_ATV
				+ ") references " + EVENTOS + "(" + ID_EVENTO + ")" + ");";
		sqLiteDatabase.execSQL(sql2);
		Log.i("Script", sql2);

		String sql3 = "CREATE TABLE " + INSCRITOS + "(" + ID_INSCRITOS
				+ " INTEGER PRIMARY KEY );";
		sqLiteDatabase.execSQL(sql3);
		Log.i("Script", sql3);

		String sql5 = "CREATE TABLE " + TABLE_NAME + " (" + ID_COL
				+ " INTEGER PRIMARY KEY, " + TEXT_COL + " TEXT, " + FORMAT_COL
				+ " TEXT, " + DISPLAY_COL + " TEXT, " + TIMESTAMP_COL
				+ " TIMESTAMP);";
		sqLiteDatabase.execSQL(sql5);
		Log.i("Script", sql5);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion,
			int newVersion) {
		String sql = "DROP TABLE IF EXISTS " + CREDENCIADOR + ";";
		sqLiteDatabase.execSQL(sql);
		Log.i("Script", sql);

		String sql1 = "DROP TABLE IF EXISTS " + EVENTOS + ";";
		sqLiteDatabase.execSQL(sql1);
		Log.i("Script", sql1);

		String sql2 = "DROP TABLE IF EXISTS " + ATIVIDADES + ";";
		sqLiteDatabase.execSQL(sql2);
		Log.i("Script", sql2);

		String sql3 = "DROP TABLE IF EXISTS " + INSCRITOS + ";";
		sqLiteDatabase.execSQL(sql3);
		Log.i("Script", sql3);

		String sql5 = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
		sqLiteDatabase.execSQL(sql5);
		Log.i("Script", sql5);

		onCreate(sqLiteDatabase);
	}

}