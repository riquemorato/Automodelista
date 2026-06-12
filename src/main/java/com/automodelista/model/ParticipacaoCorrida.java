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

public class ParticipacaoCorrida {

    //Classe responsável por registrar a participação de um piloto em uma corrida. 
    private int id;
    private int pilotoId;
    private int corridaId;
    private int posicaoFinal;
    private int pontosObtidos;
    private String tipoEstrategia;
    private String compostoPneu;
    private boolean abandonou;
    private Piloto piloto;

    public ParticipacaoCorrida() {
    }

    public ParticipacaoCorrida(int pilotoId, int corridaId, String tipoEstrategia, String compostoPneu) {
        this.pilotoId = pilotoId; this.corridaId = corridaId;
        this.tipoEstrategia = tipoEstrategia; this.compostoPneu = compostoPneu;
    }

    //Constructor para select
    public ParticipacaoCorrida(int id, int pilotoId, int corridaId, int posicaoFinal, int pontosObtidos, String tipoEstrategia, String compostoPneu, boolean abandonou, Piloto piloto) {
        this.id = id; 
        this.pilotoId = pilotoId;
        this.corridaId = corridaId;
        this.posicaoFinal = posicaoFinal;
        this.pontosObtidos = pontosObtidos;
        this.tipoEstrategia = tipoEstrategia;
        this.compostoPneu = compostoPneu;
        this.abandonou = abandonou;
        this.piloto = piloto;
    }

    //Getters
    public int getId(){
        return id;
    }

    public int getPilotoId(){
        return pilotoId;
    }

    public int getCorridaId(){
        return corridaId;
    }

    public int getPosicaoFinal(){
        return posicaoFinal;
    }

    public int getPontosObtidos(){
        return pontosObtidos;
    }

    public String getTipoEstrategia(){
        return tipoEstrategia;
    }

    public String getCompostoPneu(){
        return compostoPneu;
    }

    public boolean isAbandonou(){
        return abandonou;
    }

    public Piloto getPiloto(){
        return piloto;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }

    public void setPilotoId(int pilotoId){
        this.pilotoId = pilotoId;
    }

    public void setCorridaId(int corridaId){
        this.corridaId = corridaId;
    }

    public void setPosicaoFinal(int posicaoFinal){
        this.posicaoFinal = posicaoFinal;
    }

    public void setPontosObtidos(int pontosObtidos){ 
        this.pontosObtidos = pontosObtidos;
    }

    public void setTipoEstrategia(String tipoEstrategia){ 
        this.tipoEstrategia = tipoEstrategia; 
    }

    public void setCompostoPneu(String compostoPneu){
        this.compostoPneu = compostoPneu;
    }

    public void setAbandonou(boolean abandonou){
        this.abandonou = abandonou;
    }

    public void setPiloto(Piloto piloto){
        this.piloto = piloto;
    }
    
    public static ParticipacaoCorrida converterRegistros(HashMap<String, Object> registros) {

        int IdParticipacao = (int) registros.get("id");
        int IdPiloto = (int) registros.get("pilotoId");
        int IdCorrida = (int) registros.get("corridaId");
        int PosicaoFinal = (int) registros.get("posicaoFinal");
        int PontosObtidos = (int) registros.get("pontosObtidos");
        String TipoEstrategia = (String) registros.get("tipoEstrategia");
        String CompostoPneu = (String) registros.get("compostoPneu");
        boolean Abandonou = (boolean) registros.get("abandonou");
        Piloto Piloto = (Piloto) registros.get("piloto");

        return new ParticipacaoCorrida(IdParticipacao, IdPiloto, IdCorrida, PosicaoFinal, PontosObtidos, TipoEstrategia, CompostoPneu, Abandonou, Piloto); //Tomar cuidado com a ordem.

    }
}