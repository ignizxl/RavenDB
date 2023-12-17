package com.example.raven.entity;

public class Produto {

    String id;
    String nome;
    
    public Produto() {
    }

    public Produto(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", nome=" + nome + '}';
    }
    
}
