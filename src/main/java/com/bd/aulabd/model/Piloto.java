/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.model;

import java.util.HashMap;

import com.bd.aulabd.model.abstracts.PessoaAbstract;

/**
 *
 * @author Henrique
 */
public class Piloto extends PessoaAbstract {

    //Habilidade e Consistencia -> valores simulados de 0-100 (similar a jogo de corrida)
    private int numeroPiloto;
    private int habilidade;
    private int consistencia;
    private int pontosCampeonato;
    private Equipe equipeId;

    //Construtores
    public Piloto(){
    }

    public Piloto (int id, String nome, String nacionalidade, int idade, int numeroPiloto, int habilidade, int consistencia){
        super(id, nome, nacionalidade, idade);
        this.numeroPiloto = numeroPiloto;
        this.habilidade = habilidade;
        this.consistencia = consistencia;

    }

    public Piloto(int id, String nome, String nacionalidade, int idade, int numeroPiloto, int habilidade, int consistencia, int pontosCampeonato, int EquipeId){
        super(id, nome, nacionalidade, idade);
        this.numeroPiloto = numeroPiloto;
        this.habilidade = habilidade;
        this.consistencia = consistencia;
        this.pontosCampeonato = pontosCampeonato;
        this.equipeId = equipeId;
    }

    //Sobreposição do método GetTipo da classe Abstrata PessoaAbstrata
    public String getTipo() {
        return "Piloto";
    }

    //Getters 
    public int getNumeroPiloto(){ 
        return numeroPiloto;
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

    public Equipe getEquipeId(){ 
        return equipeId; 
    }

    //Setters
    public void setNumeroPiloto(int numPiloto){
        this.numeroPiloto = numPiloto; 
    }

    public void setHabilidade(int habilidade){
         this.habilidade = habilidade; 
    }

    public void setConsistencia(int consistencia){ 
        this.consistencia = consistencia; 
    }

    public void setPontosCampeonato(int pontosCampeonato){ 
        this.pontosCampeonato = pontosCampeonato; 
    }

    public void setEquipeId(Equipe equipeId){ 
        this.equipeId = equipeId; 
    }

    //Calculo de pontuação: Calcula a chance de pontuar em uma corrida na camada de simulação
    // fatorConsistencia(double) = consistencia/100;
    // returns habilidade * fatorConsistencia;

    public int CalcularPontuacao(){
        double fatorConsistencia = consistencia/100;
        int chance = (int) habilidade * (int)fatorConsistencia;
        return chance;
    }

    public static Piloto converterRegistros(HashMap<String, Object> registro){
        int id = (int) registro.get("id");
        String nome = (String) registro.get("nome");
        String nacionalidade = (String) registro.get("nacionalidade");
        int idade = (int) registro.get("idade");
        int numeroPiloto = (int) registro.get("numeroPiloto");
        int habilidade = (int) registro.get("habilidade");
        int consistencia = (int) registro.get("consistencia");
        int pontosCampeonato = (int) registro.get("pontosCampeonato");
        int equipeId = (int) registro.get("equipeId");

        //Como passar o ID de uma classe abstrata para uma classe concreta?
        return new Piloto(id, nome, nacionalidade, idade, numeroPiloto, habilidade, consistencia, pontosCampeonato, equipeId);
    }
     



}
