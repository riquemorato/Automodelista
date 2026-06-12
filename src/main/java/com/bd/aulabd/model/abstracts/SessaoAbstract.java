/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.model.abstracts;

import com.bd.aulabd.model.enums.TipoSessao;

/**
 *
 * @author Henrique
 */
public abstract class SessaoAbstract {

    protected int id;
    protected int corridaId;
    protected int duracaoMinutos;

    protected SessaoAbstract() {
    }

    protected SessaoAbstract(int corridaId, int duracaoMinutos){
        this.corridaId = corridaId;
        this.duracaoMinutos = duracaoMinutos;
    }

    protected SessaoAbstract(int id, int corridaId, int duracaoMinutos){
        this.id = id;
        this.corridaId = corridaId;
        this.duracaoMinutos = duracaoMinutos;
    }

    //Utiliza ENUM TipoSessao
    public abstract TipoSessao GetTipo();

    //GETTERS
    public int getId(){
        return id;
    }

    public int getCorridaId() {
        return corridaId;
    }

    public int getDuracaoMinutos(){
        return duracaoMinutos;
    }

    //SETTERS
    public void setId(int id){
        this.id = id;
    }

    public void setCorridaId(int corridaId) {
        this.corridaId = corridaId;
    }

    public void setDuracaoMinutos(int duracaoMinutos){
        this.duracaoMinutos = duracaoMinutos;
    }
    
}
