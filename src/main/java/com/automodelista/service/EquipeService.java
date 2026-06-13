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

    //POST - Insere uma nova equipe - ATUALIZADO - FILTRO DE DUPLICIDADE - valida antes de efetivar o cadastro
    public void inserir(Equipe equipe){
        if (equipeDAO.existePorNome(equipe.getNome())) {
            throw new IllegalArgumentException("Já existe uma equipe cadastrada com o nome \"" + equipe.getNome() + "\".");
        }
        int id = equipeDAO.inserir(equipe);

        Carro carro = new Carro(equipe.getNome() + " — Carro", id);
        carroDAO.inserir(carro);
    }

    //GET - Obtem uma equipe por ID
    public Equipe obterPorId(int id){
        return equipeDAO.obterPorId(id);
    }

    //GET - Obtem todas as equipes cadastradas
    public List<Equipe> obterTodas(){
        return equipeDAO.obterTodos();
    }

    //UPDATE - atualiza uma equipe por ID - ATUALIZADO - FILTRO DE DUPLICIDADE - valida antes de efetivar o cadastro
    public void atualizar(int id, Equipe equipe){
        if (equipeDAO.existePorNome(equipe.getNome(), id)) {
            throw new IllegalArgumentException("Já existe uma equipe cadastrada com o nome \"" + equipe.getNome() + "\".");
        }
        equipeDAO.atualizar(id, equipe);
    }

    //DELETE - Deleta uma equipe por ID
    public void deletar(int id){
        equipeDAO.deletar(id);
    }
}
