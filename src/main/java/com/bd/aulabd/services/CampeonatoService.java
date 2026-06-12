/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bd.aulabd.model.Campeonato;
import com.bd.aulabd.model.Corrida;
import com.bd.aulabd.model.DAO.CampeonatoDAO;
import com.bd.aulabd.model.DAO.CorridaDAO;
import com.bd.aulabd.model.DAO.SessaoDAO;

/**
 *
 * @author Henrique
 */
public class CampeonatoService {

    @Autowired CampeonatoDAO  campeonatoDAO;
    @Autowired CorridaDAO corridaDAO;
    @Autowired SessaoDAO sessaoDAO;

    public void inserir(Campeonato campeonato) {
        campeonatoDAO.inserir(campeonato);
    }

    public Campeonato obterPorId(int id){
        return campeonatoDAO.obterPorId(id);
    }

    public List<Campeonato> obterTodos() {
        return campeonatoDAO.obterTodos();
    }

    //Listar todas as corridas do Campeonato
    public List<Corrida> obterCorridasDoCampeonato(int campeonatoId) {
        return corridaDAO.obterPorCampeonato(campeonatoId);
    }

}
