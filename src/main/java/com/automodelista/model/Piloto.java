/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import java.util.HashMap;
import com.automodelista.abstracts.PessoaAbstract;

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

    //Conversão de Registro - ref classe Cliente
    public static Piloto converterRegistros(HashMap<String, Object> registro) {
        Piloto piloto = new Piloto();

        //herdados de PessoaAbstract
        piloto.setId((int) registro.get("id")); //herdados de PessoaAbstract
        piloto.setNome((String) registro.get("nome"));
        piloto.setNacionalidade((String) registro.get("nacionalidade"));
        piloto.setIdade((int) registro.get("idade"));
        //setters da classe piloto
        piloto.setNumeroCarro((int) registro.get("numero_carro"));
        piloto.setHabilidade((int) registro.get("habilidade"));
        piloto.setConsistencia((int) registro.get("consistencia"));
        piloto.setPontosCampeonato((int) registro.get("pontos_campeonato"));
        if (registro.get("equipe_id") != null) {
            piloto.setEquipeId((int) registro.get("equipe_id"));
        }
        return piloto;
    }

    //Calculo de score de Habilidade
    public int calcularScore() {
        return (int)(habilidade * (consistencia / 100.0));
    }

}
