/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import java.util.HashMap;

import com.automodelista.model.enums.StatusCorrida;


/**
 *
 * @author Henrique
 */
public class Corrida {
    private int id;
    private String nome;
    private String circuito;
    private int rodada;
    private StatusCorrida status;
    private int campeonatoId;

    //Constructors
    public Corrida() {
    }

    public Corrida(String nome, String circuito, int rodada, int campeonatoId) {
        this.nome = nome; this.circuito = circuito;
        this.rodada = rodada;
        this.campeonatoId = campeonatoId;
        this.status = StatusCorrida.PENDENTE;
    }

    //Construtor para Select
    public Corrida(int id, String nome, String circuito,int rodada, StatusCorrida status, int campeonatoId) {
        this.id = id;
        this.nome = nome;
        this.circuito = circuito;
        this.rodada = rodada;
        this.status = status;
        this.campeonatoId = campeonatoId;
    }


    //Getters
    public int getId(){
        return id;
    }
    
    public String getNome(){
        return nome;
    }

    public String getCircuito(){
        return circuito;
    }
    
    public int getRodada(){
        return rodada;
    }
    
    public StatusCorrida getStatus(){
        return status;
    }

    public int getCampeonatoId(){
        return campeonatoId;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCircuito(String circuito){
        this.circuito = circuito;
    }

    public void setRodada(int rodada){
        this.rodada = rodada;
    }

    public void setStatus(StatusCorrida statusCorrida){
        this.status = statusCorrida;
    }

    public void setCampeonatoId(int campeonatoId){
        this.campeonatoId = campeonatoId;
    }

    //Conversao de registros
    public static Corrida converterRegistros(HashMap<String, Object> registros) {
        
        int IdCorrida = (int) registros.get("id");
        String Nome = (String) registros.get("nome");
        String Circuito = (String) registros.get("circuito");
        int Rodada = (int) registros.get("rodada");
        StatusCorrida Status = StatusCorrida.valueOf((String) registros.get("status"));
        int CampeonatoId = (int) registros.get("campeonato_id");

        return new Corrida(IdCorrida, Nome, Circuito, Rodada, Status, CampeonatoId); //Tomar cuidado com a ordem.

    }

}