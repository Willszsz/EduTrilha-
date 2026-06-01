package com.edutrilha.edutrilha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaDAO {

    private final BancoSQLite banco;

    public DisciplinaDAO(Context context) {
        banco = new BancoSQLite(context);
    }

    public long salvar(int usuarioId,
                       String nome,
                       double nota) {

        SQLiteDatabase db =
                banco.getWritableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM disciplinas WHERE usuario_id = ? AND nome = ?",
                        new String[]{
                                String.valueOf(usuarioId),
                                nome
                        }
                );

        boolean existe =
                cursor.moveToFirst();

        cursor.close();

        if (existe) {
            return -1;
        }

        ContentValues values =
                new ContentValues();

        values.put("usuario_id", usuarioId);
        values.put("nome", nome);
        values.put("nota", nota);

        return db.insert(
                "disciplinas",
                null,
                values
        );
    }

    public List<Disciplina> listar(int usuarioId) {

        List<Disciplina> disciplinas =
                new ArrayList<>();

        SQLiteDatabase db =
                banco.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT * FROM disciplinas WHERE usuario_id = ?",
                        new String[]{
                                String.valueOf(usuarioId)
                        }
                );

        while (cursor.moveToNext()) {

            String nome =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("nome")
                    );

            double nota =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("nota")
                    );

            int icone;
            int cor;

            switch (nome) {

                case "Matemática":
                    icone = R.drawable.ic_calculator;
                    cor = R.color.danger;
                    break;

                case "Programação":
                    icone = R.drawable.ic_code;
                    cor = R.color.warning;
                    break;

                case "Banco de Dados":
                    icone = R.drawable.ic_database;
                    cor = R.color.success;
                    break;

                case "Redes":
                    icone = R.drawable.ic_network;
                    cor = R.color.primary;
                    break;

                case "Português":
                    icone = R.drawable.ic_book;
                    cor = R.color.success;
                    break;

                default:
                    icone = R.drawable.ic_book;
                    cor = R.color.primary;
                    break;
            }

            disciplinas.add(
                    new Disciplina(
                            nome,
                            nota,
                            icone,
                            cor
                    )
            );
        }

        cursor.close();

        return disciplinas;
    }

    public void atualizar(int usuarioId,
                          String nome,
                          double novaNota) {

        SQLiteDatabase db =
                banco.getWritableDatabase();

        ContentValues values =
                new ContentValues();

        values.put("nota", novaNota);

        db.update(
                "disciplinas",
                values,
                "usuario_id = ? AND nome = ?",
                new String[]{
                        String.valueOf(usuarioId),
                        nome
                }
        );
    }

    public void excluir(int usuarioId,
                        String nome) {

        SQLiteDatabase db =
                banco.getWritableDatabase();

        db.delete(
                "disciplinas",
                "usuario_id = ? AND nome = ?",
                new String[]{
                        String.valueOf(usuarioId),
                        nome
                }
        );
    }

    public boolean possuiDisciplinas(int usuarioId) {

        SQLiteDatabase db =
                banco.getReadableDatabase();

        Cursor cursor =
                db.rawQuery(
                        "SELECT COUNT(*) FROM disciplinas WHERE usuario_id = ?",
                        new String[]{
                                String.valueOf(usuarioId)
                        }
                );

        boolean possui = false;

        if (cursor.moveToFirst()) {
            possui = cursor.getInt(0) > 0;
        }

        cursor.close();

        return possui;
    }
}