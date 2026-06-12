// IMPLEMENTATION CHECK: OK
// TESTS CHECK: PENDING

package com.bd.aulabd.model;

import java.util.HashMap;

import com.bd.aulabd.model.enums.StatusCorrida;

/**
 *
 * @author Henrique
 */
public class Corrida {

    private int idCorrida;
    private String nome;
    private String circuito;
    private int rodada;
    private StatusCorrida statusCorrida; //ENUM StatusCorrida
    private int campeonatoId;

    //Construtores
    public Corrida(){
    }

    public Corrida(int idCorrida, String nome, String circuito, int rodada, int campeonatoId, StatusCorrida statusCorrida) {
        this.idCorrida = idCorrida;
        this.nome = nome;
        this.circuito = circuito;
        this.rodada = rodada;
        this.campeonatoId = campeonatoId;
        this.statusCorrida = StatusCorrida.PENDENTE;
    }

    //Getters 
    public int getId(){
        return idCorrida;
    }

    public String  getNome(){ 
        return nome;
    }
    public String getCircuito(){ 
        return circuito;
    }

    public int getRodada() { 
        return rodada;
    }

    public StatusCorrida getStatus(){
        return statusCorrida; 
    }

    public int getCampeonatoId() {
        return campeonatoId; 
    }

    //Setters

    public void setId(int id){
        this.idCorrida = id;
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

    public void setStatus(StatusCorrida status){
        this.statusCorrida = status;
    }

    public void setCampeonatoId(int campeonatoId){
        this.campeonatoId = campeonatoId;
    }

    //Conversão de Registros
    public static Corrida converterRegistros(HashMap<String, Object> registro) {

        int idCorrida = (int) registro.get("idCorrida");
        String nome = (String) registro.get("nome");
        String circuito = (String) registro.get("circuito");
        int rodada = (int) registro.get("rodada");
        StatusCorrida statusCorrida = (StatusCorrida) registro.get("statusCorrida"); //Tirar dúvida: Conversão de registro em herança de classes
        int campeonatoId = (int) registro.get("campeonatoId");

        return new Corrida(idCorrida, nome, circuito, rodada, campeonatoId, statusCorrida);
    }

}
