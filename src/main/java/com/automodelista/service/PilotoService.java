/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.PilotoDAO;
import com.automodelista.model.Piloto;

/**
 *
 * @author Henrique
 */

//BOILERPLATE baseado no ClienteService do projeto original realizado em sala.

@Service
public class PilotoService {
    @Autowired PilotoDAO pilotoDAO;

    public void inserir(Piloto piloto){
        pilotoDAO.inserir(piloto);
    }

    public Piloto obterPorId(int id){
        return pilotoDAO.obterPorId(id);
    }

    public List<Piloto> obterTodos(){
        return pilotoDAO.obterTodos();
    }

    public List<Piloto> obterPorEquipe(int id){
        return pilotoDAO.obterPorEquipe(id);
    }

    public void atualizar(int id, Piloto p){
        pilotoDAO.atualizar(id, p);
    }

    public void deletar(int id){
        pilotoDAO.deletar(id);
    }

}

