package com.edutrilha.edutrilha;

import android.content.Context;
import android.content.SharedPreferences;

public class SessaoUsuario {

    private static final String PREFS = "sessao_usuario";

    private static final String ID = "usuario_id";
    private static final String NOME = "usuario_nome";
    private static final String EMAIL = "usuario_email";

    private final SharedPreferences prefs;

    public SessaoUsuario(Context context) {

        prefs = context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        );
    }

    public void login(Usuario usuario) {

        prefs.edit()
                .putInt(ID, usuario.getId())
                .putString(NOME, usuario.getNome())
                .putString(EMAIL, usuario.getEmail())
                .apply();
    }

    public void logout() {

        prefs.edit().clear().apply();
    }

    public boolean isLogado() {

        return prefs.contains(ID);
    }

    public int getUsuarioId() {

        return prefs.getInt(ID, -1);
    }

    public String getNome() {

        return prefs.getString(NOME, "");
    }

    public String getEmail() {

        return prefs.getString(EMAIL, "");
    }
}