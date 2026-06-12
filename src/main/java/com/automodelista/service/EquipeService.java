/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.CarroDAO;
import com.automodelista.dao.EquipeDAO;
import com.automodelista.model.Carro;
import com.automodelista.model.Equipe;

/**
 *
 * @author Henrique
 */

@Service
public class EquipeService {
    @Autowired EquipeDAO equipeDAO;
    @Autowired CarroDAO  carroDAO;

    public void inserir(Equipe equipe){
        int id = equipeDAO.inserir(equipe);
        
        Carro carro = new Carro(equipe.getNome() + " — Carro", id);
        carroDAO.inserir(carro);
    }

    public Equipe obterPorId(int id){
        return equipeDAO.obterPorId(id);
    }

    public List<Equipe> obterTodas(){
        return equipeDAO.obterTodos();
    }

    public void atualizar(int id, Equipe e){
        equipeDAO.atualizar(id, e);
    }
    public void deletar(int id){
        equipeDAO.deletar(id);
    }
}
