/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.model;

import java.util.HashMap;

/**
 *
 * @author Henrique
 */
public class Equipe {
    private int id;
    private String nome;
    private double orcamento;

    //Constructors
    public Equipe() {}

    public Equipe(String nome, double orcamento) {
        this.nome = nome;
        this.orcamento = orcamento;
    }

    public Equipe(int id, String nome, double orcamento) {
        this.id = id;
        this.nome = nome;
        this.orcamento = orcamento;
    }

    //Getters
    public int getId(){
            return id;
    }
    
    public String getNome(){
        return nome;
    }

    public double getOrcamento(){
        return orcamento;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){ 
        this.nome = nome; 
    }

    public void setOrcamento(double orcamento){ 
        this.orcamento = orcamento; 
    }

    //Controle de orçamento -> saques e depositos
    public void sacarOrcamento(double valor) {
        if (valor > orcamento) {
            throw new IllegalStateException("Orçamento insuficiente. Disponível: " + orcamento);
        } 
        this.orcamento -= valor;
    }

    public void depositarOrcamento(double valor) {
        this.orcamento += valor;
    }

    //ConverterRegistro
        public static Equipe converterRegistro(HashMap<String, Object> registro){
        int id = (int) registro.get("id");
        String nome = (String) registro.get("nome");
        double orcamento = (double) registro.get("idade");

        return new Equipe(id, nome, orcamento);
    }

}
