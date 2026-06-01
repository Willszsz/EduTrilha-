package com.edutrilha.edutrilha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsuarioDAO {

    private final BancoSQLite banco;

    public UsuarioDAO(Context context) {
        banco = new BancoSQLite(context);
    }

    public long cadastrar(Usuario usuario) {

        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome", usuario.getNome());
        values.put("email", usuario.getEmail());
        values.put("senha", usuario.getSenha());

        return db.insert("usuarios", null, values);
    }

    public boolean emailExiste(String email) {

        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM usuarios WHERE email = ?",
                new String[]{email}
        );

        boolean existe = cursor.moveToFirst();

        cursor.close();

        return existe;
    }

    public Usuario login(String email, String senha) {

        SQLiteDatabase db = banco.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM usuarios WHERE email = ? AND senha = ?",
                new String[]{email, senha}
        );

        Usuario usuario = null;

        if (cursor.moveToFirst()) {

            usuario = new Usuario(
                    cursor.getString(cursor.getColumnIndexOrThrow("nome")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getString(cursor.getColumnIndexOrThrow("senha"))
            );

            usuario.setId(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            );
        }

        cursor.close();

        return usuario;
    }
}