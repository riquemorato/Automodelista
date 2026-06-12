/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import java.util.Map;

/**
 *
 * @author Henrique
 */
public class Campeonato {

    private int    id;
    private String nome;
    private int    temporada;

    //Constructors
    public Campeonato() {
    }

    public Campeonato(String nome, int temporada) {
        this.nome = nome;
        this.temporada = temporada;
    }

    //Constructor para Select
    public Campeonato(int id, String nome, int temporada) {
        this.id = id;
        this.nome = nome;
        this.temporada = temporada;
    }

    //Getters

    public int getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public int getTemporada(){
        return temporada;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setTemporada(int temporada){
        this.temporada = temporada;
    }

    public static Campeonato converterRegistros(Map<String, Object> registros) {
        int idCampeonato = (int) registros.get("id");
        String nome      = (String) registros.get("nome");
        int temporada    = (int) registros.get("temporada");
        return new Campeonato(idCampeonato, nome, temporada);
    }
}
