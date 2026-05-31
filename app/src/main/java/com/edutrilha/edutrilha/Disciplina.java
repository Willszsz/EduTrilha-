package com.edutrilha.edutrilha;

import java.io.Serializable;

public class Disciplina implements Serializable {
    public String nome;
    public double nota;
    public int iconRes;
    public int corRes;

    public Disciplina(String nome, double nota, int iconRes, int corRes) {
        this.nome = nome;
        this.nota = nota;
        this.iconRes = iconRes;
        this.corRes = corRes;
    }

    public String statusTexto() {
        if (nota >= 8.0) return "Excelente";
        if (nota >= 7.0) return "Bom";
        if (nota >= 6.0) return "Atenção";
        return "Crítico";
    }
}
