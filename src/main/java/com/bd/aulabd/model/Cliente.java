package com.bd.aulabd.model;

//POJO - Plain Old Java Object

import java.util.HashMap;

public class Cliente {
    private int id;
    private String nome, cpf;

    // inicio do form
    public Cliente() {
        //insert
    }

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    // select
    public Cliente(int id, String nome, String cpf){
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

     public static Cliente converterRegistros(HashMap<String,Object> registros) {
        int idCliente = (int) registros.get("id");
        String nome = (String) registros.get("nome");
        String cpf = (String) registros.get("cpf");
        return new Cliente(idCliente,nome,cpf); //Tomar cuidado com a ordem.
        
    }

}
