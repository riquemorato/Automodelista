// IMPLEMENTATION CHECK: OK
// TESTS CHECK: 

package com.bd.aulabd.model;
import java.util.HashMap;

public class Campeonato {

    private int idCampeonato;
    private String nome;
    private int temporada;
    private boolean ativo;

    //Constructors
    public Campeonato() {
    }

    public Campeonato(String nome, int temporada) {
        this.nome = nome;
        this.temporada = temporada;
        this.ativo = true;
    }

    public Campeonato(int id, String nome, int temporada, boolean ativo) {
        this.idCampeonato = id;
        this.nome = nome;
        this.temporada = temporada;
        this.ativo = true;
    }

    //Getters
    public int getId() {
        return idCampeonato;
    }

    public String getNome() {
        return nome;
    }

    public int getTemporada() {
        return temporada;
    }

    public boolean isAtivo() {
        return ativo;
    }

    //Setters
    public void setId(int id) {
        this.idCampeonato = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    //Conversão de registro 
    public static Campeonato converterRegistros(HashMap<String, Object> registro) {

        int idCampeonato = (int) registro.get("id");
        String nome = (String) registro.get("nome");
        int temporada = (int) registro.get("temporada");
        boolean ativo = (boolean) registro.get("ativo");

        return new Campeonato(idCampeonato, nome, temporada, ativo);

    }
}
