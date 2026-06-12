/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import java.util.HashMap;

import com.automodelista.model.abstracts.PessoaAbstract;

/**
 *
 * @author Henrique
 */
public class Piloto extends PessoaAbstract {
    private int numeroCarro;
    private int habilidade;
    private int consistencia;
    private int pontosCampeonato;
    private int equipeId;

    //Constructors
    public Piloto() {
    }

    public Piloto (String nome, String nacionalidade, int idade, int numeroCarro, int habilidade, int consistencia){
        super(nome, nacionalidade, idade);
        this.numeroCarro = numeroCarro;
        this.habilidade = habilidade;
        this.consistencia = consistencia;
    }

    //construtor para Select
    public Piloto(int id, String nome, String nacionalidade, int idade, int numeroCarro, int habilidade, int consistencia, int pontosCampeonato, int equipeId) {
    super(id, nome, nacionalidade, idade);
    this.numeroCarro = numeroCarro;
    this.habilidade = habilidade;
    this.consistencia = consistencia;
    this.pontosCampeonato = pontosCampeonato;
    this.equipeId = equipeId;
    }

    //Getters
    public int getNumeroCarro(){
        return numeroCarro;
    }

    public int getHabilidade(){
        return habilidade;
    }

    public int getConsistencia(){
        return consistencia;
    }

    public int getPontosCampeonato(){
        return pontosCampeonato;
    }

    public int getEquipeId(){
        return equipeId;
    }

    //Setters
    public void setNumeroCarro(int numero){
        this.numeroCarro = numero;
    }

    public void setHabilidade(int habilidade){
        this.habilidade = habilidade;
    }

    public void setConsistencia(int consistencia){
        this.consistencia = consistencia;
    }

    public void setPontosCampeonato(int pontos){
        this.pontosCampeonato = pontos;
    }

    public void setEquipeId(int e){
        this.equipeId = e;
    }
    //Métodos
    //Override getTipo de PessoaAbstract
     @Override 
     public String getTipo(){
        return "Piloto";
    } 

    //Conversão de Registro - ref classe Cliente
    public static Piloto converterRegistros(HashMap<String, Object> registros) {
        //herdados de PessoaAbstract
        int IdPiloto = (int) registros.get("id");
        String Nome = (String) registros.get("nome");
        String Nacionalidade = (String) registros.get("nacionalidade");
        int Idade = (int) registros.get("idade");

        //da classe Piloto
        int NumeroCarro = (int) registros.get("numeroCarro");
        int Habilidade = (int) registros.get("habilidade");
        int Consistencia = (int) registros.get("consistencia");
        int PontosCampeonato = (int) registros.get("pontos_campeonato");
        int IdEquipe = (int) registros.get("idEquipe");

        return new Piloto(IdPiloto,Nome, Nacionalidade, Idade, NumeroCarro, Habilidade, Consistencia, PontosCampeonato, IdEquipe);
            
    }

    //Calculo de score de Habilidade
    public int calcularScore() {
        return (int)(habilidade * (consistencia / 100.0));
    }

}
