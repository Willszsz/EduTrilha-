package com.edutrilha.edutrilha;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoSQLite extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "edutrilha.db";
    private static final int VERSAO = 2;

    public BancoSQLite(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE usuarios (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nome TEXT NOT NULL," +
                        "email TEXT UNIQUE NOT NULL," +
                        "senha TEXT NOT NULL)"
        );

        db.execSQL(
                "CREATE TABLE progresso (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "usuario_id INTEGER," +
                        "disciplina TEXT," +
                        "topico TEXT," +
                        "concluido INTEGER," +
                        "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))"
        );

        db.execSQL(
                "CREATE TABLE disciplinas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "usuario_id INTEGER NOT NULL," +
                        "nome TEXT NOT NULL," +
                        "nota REAL NOT NULL," +
                        "FOREIGN KEY(usuario_id) REFERENCES usuarios(id))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS disciplinas");
        db.execSQL("DROP TABLE IF EXISTS progresso");
        db.execSQL("DROP TABLE IF EXISTS usuarios");

        onCreate(db);
    }
}