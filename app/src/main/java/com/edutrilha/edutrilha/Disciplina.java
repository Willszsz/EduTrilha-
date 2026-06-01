package com.edutrilha.edutrilha;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        if (nota >= 8) return "EXCELENTE";
        if (nota >= 7) return "BOM";
        if (nota >= 6) return "ATENÇÃO";
        return "CRÍTICO";
    }

    public List<String> trilhaTopicos() {
        switch (nome) {
            case "Matemática":
                return new ArrayList<>(Arrays.asList(
                        "Revisão de Equações",
                        "Funções Básicas",
                        "Exercícios Práticos de Álgebra",
                        "Geometria Plana"
                ));
            case "Programação":
                return new ArrayList<>(Arrays.asList(
                        "Lógica de Programação",
                        "Estruturas de Decisão",
                        "Estruturas de Repetição",
                        "Funções e Modularização"
                ));
            case "Banco de Dados":
                return new ArrayList<>(Arrays.asList(
                        "Modelagem Relacional",
                        "Consultas SQL Avançadas",
                        "Normalização",
                        "Índices e Performance"
                ));
            case "Redes":
                return new ArrayList<>(Arrays.asList(
                        "Modelo OSI",
                        "Protocolos TCP/IP",
                        "Endereçamento IP",
                        "Roteamento Básico"
                ));
            case "Português":
                return new ArrayList<>(Arrays.asList(
                        "Interpretação de Texto",
                        "Concordância Verbal",
                        "Redação Dissertativa",
                        "Revisão Gramatical"
                ));
            default:
                return new ArrayList<>(Arrays.asList(
                        "Diagnóstico Inicial",
                        "Conteúdo Base",
                        "Exercícios",
                        "Avaliação"
                ));
        }
    }

    public String recomendacao() {
        if (nota < 6) return "Foco intensivo: revise os fundamentos antes de avançar.";
        if (nota < 7) return "Reforce os pontos com dificuldade e pratique exercícios.";
        if (nota < 8) return "Bom desempenho — aprofunde os tópicos avançados.";
        return "Excelente! Mantenha o ritmo com desafios extras.";
    }
}
