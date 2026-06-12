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

    //POST - Insere um piloto novo
    public void inserir(Piloto piloto){
        pilotoDAO.inserir(piloto);
    }

    //GET - Obtem um piloto cadastrado por ID
    public Piloto obterPorId(int id){
        return pilotoDAO.obterPorId(id);
    }

    //GET - Obtem todos os pilotos
    public List<Piloto> obterTodos(){
        return pilotoDAO.obterTodos();
    }

    //GET - obtem os pilotos por equipe
    public List<Piloto> obterPorEquipe(int id){
        return pilotoDAO.obterPorEquipe(id);
    }

    //UPDATE - atualiza os dados de um piloto via ID
    public void atualizar(int id, Piloto p){
        pilotoDAO.atualizar(id, p);
    }

    //DELETE - deleta um piloto via ID
    public void deletar(int id){
        pilotoDAO.deletar(id);
    }

}

