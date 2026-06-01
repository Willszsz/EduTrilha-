package com.edutrilha.edutrilha;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class ProgressoStore {

    private static final String PREFS = "edutrilha_progresso";

    private final SharedPreferences prefs;
    private final Context context;

    public ProgressoStore(Context context) {

        this.context = context.getApplicationContext();

        this.prefs = this.context.getSharedPreferences(
                PREFS,
                Context.MODE_PRIVATE
        );
    }

    private String key(String disciplina) {

        SessaoUsuario sessao = new SessaoUsuario(context);

        int usuarioId = sessao.getUsuarioId();

        if (usuarioId < 0) {
            return "anonimo::concluidos::" + disciplina;
        }

        return usuarioId
                + "::concluidos::"
                + disciplina;
    }

    public Set<String> getConcluidos(String disciplina) {

        return new HashSet<>(
                prefs.getStringSet(
                        key(disciplina),
                        new HashSet<>()
                )
        );
    }

    public boolean isConcluido(
            String disciplina,
            String topico
    ) {

        return getConcluidos(disciplina)
                .contains(topico);
    }

    public void setConcluido(
            String disciplina,
            String topico,
            boolean concluido
    ) {

        Set<String> atuais =
                getConcluidos(disciplina);

        if (concluido) {
            atuais.add(topico);
        } else {
            atuais.remove(topico);
        }

        prefs.edit()
                .putStringSet(
                        key(disciplina),
                        atuais
                )
                .apply();
    }

    public int totalConcluidos(String disciplina) {

        return getConcluidos(disciplina)
                .size();
    }

    public void limparProgresso(String disciplina) {

        prefs.edit()
                .remove(key(disciplina))
                .apply();
    }
}