/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.CampeonatoDAO;
import com.automodelista.dao.CorridaDAO;
import com.automodelista.model.Campeonato;
import com.automodelista.model.Corrida;

/**
 *
 * @author Henrique
 */

//A CLASSE CAMPEONATO SERVICE É A RESPONSÁVEL POR CRIAR NOVAS INSTANCIAS DE CORRIDA
@Service
public class CampeonatoService {
    @Autowired CampeonatoDAO campeonatoDAO;
    @Autowired CorridaDAO    corridaDAO;

    //Insere um novo campeonato
    public void inserir(Campeonato campeonato){
        campeonatoDAO.inserir(campeonato);
    }

    //Busca um campeonato por ID
    public Campeonato obterPorId(int id){
        return campeonatoDAO.obterPorId(id);
    }

    //READ - GET ALL Campeonatos
    public List<Campeonato> obterTodos(){
        return campeonatoDAO.obterTodos();
    }

    //CREATE
    public void criarCorrida(String nome, String circuito, int rodada, int campeonatoId) {
        corridaDAO.inserir(new Corrida(nome, circuito, rodada, campeonatoId));
    }

    //READ - GET ALL Corridas de um campeonato
    public List<Corrida> obterCorridas(int campeonatoId) {
        return corridaDAO.obterPorCampeonato(campeonatoId);
    }
}
