/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import java.util.HashMap;

/**
 *
 * @author Henrique
 */
public class Carro {

    private int id;
    private String nome;
    private int equipeId;
    
    //Inicializados com valor default = 1. Esse valor muda de acordo com orçamento da equipe e o calculo da performance
    private int nivelMotor = 1;
    private int nivelAero = 1;
    private int nivelTransmissao = 1;
    private int nivelSuspensao = 1;

    //Constructors
    public Carro(){
    }

    public Carro(String nome, int equipeId) {
        this.nome = nome;
        this.equipeId = equipeId;
    }

    //Construtor para Select
    public Carro(int id, String nome, int equipeId, int nivelMotor, int nivelAero, int nivelTransmissao, int nivelSuspensao){
        this.id = id;
        this.nome = nome;
        this.equipeId = equipeId;
        this.nivelMotor = nivelMotor;
        this.nivelAero = nivelAero;
        this.nivelTransmissao = nivelTransmissao;
        this.nivelSuspensao = nivelSuspensao;
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public int getEquipeId(){
        return equipeId;
    }

    public int getNivelMotor(){
        return nivelMotor;
    }

    public int getNivelAero(){
        return nivelAero;
    }

    public int getNivelTransmissao(){
        return nivelTransmissao;
    }

    public int getNivelSuspensao(){
        return nivelSuspensao;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    public void setEquipeId(int equipeId){
        this.equipeId = equipeId;
    }

    public void setNivelMotor(int nivelMotor){
        this.nivelMotor = nivelMotor;
    }

    public void setNivelAero(int nivelAero){
        this.nivelAero = nivelAero;
    }

    public void setNivelTransmissao(int nivelTransmissao){
        this.nivelTransmissao = nivelTransmissao;
    }
    public void setNivelSuspensao(int nivelSuspensao){
        this.nivelSuspensao = nivelSuspensao;
    }

    public int calcularPerformance() {
        return (nivelMotor * 40) + (nivelAero * 30) + (nivelTransmissao * 20) + (nivelSuspensao * 10);
    }

    public static Carro converterRegistros(HashMap<String, Object> registros) {
        int idCarro = (int) registros.get("id");
        String nome = (String) registros.get("nome");
        int idEquipe = (int) registros.get("equipe_id");
        int nivelMotor = (int) registros.get("nivel_motor");
        int nivelAero = (int) registros.get("nivel_aero");
        int nivelTransmissao = (int) registros.get("nivel_transmissao");
        int nivelSuspensao = (int) registros.get("nivel_suspensao");

        return new Carro(idCarro, nome, idEquipe, nivelMotor, nivelAero, nivelTransmissao, nivelSuspensao);
    }


}
