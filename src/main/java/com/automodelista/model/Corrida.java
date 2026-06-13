/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import java.util.Map;

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
    private boolean bloqueada;

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
    public Corrida(int id, String nome, String circuito,int rodada, StatusCorrida status, int campeonatoId, boolean bloqueada) {
        this.id = id;
        this.nome = nome;
        this.circuito = circuito;
        this.rodada = rodada;
        this.status = status;
        this.campeonatoId = campeonatoId;
        this.bloqueada = bloqueada;
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

    public boolean isBloqueada(){
        return bloqueada;
    }
    
    //Setters
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

    public void setBloqueada(boolean b){
        this.bloqueada = b;
    }

    //Conversao de registros
    public static Corrida converterRegistros(Map<String, Object> registros) {
        int id            = (int) registros.get("id");
        String nome       = (String) registros.get("nome");
        String circuito   = (String) registros.get("circuito");
        int rodada        = (int) registros.get("rodada");
        StatusCorrida status = StatusCorrida.valueOf((String) registros.get("status"));
        int campeonatoId  = (int) registros.get("campeonato_id");
        boolean bloqueada = registros.get("bloqueada") != null && (boolean) registros.get("bloqueada");

        return new Corrida(id, nome, circuito, rodada, status, campeonatoId, bloqueada);
    }

}