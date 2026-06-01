package com.edutrilha.edutrilha;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {}

    public Usuario(String nome,
                   String email,
                   String senha) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}