/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.bd.aulabd.model.DAO.PilotoDAO;
import com.bd.aulabd.model.Piloto;

/**
 *
 * @author Henrique
 */
public class PilotoService {
    
    @Autowired PilotoDAO pilotoDAO;

    public void inserir(Piloto p){
        pilotoDAO.inserir(p);
    }

    public Piloto obterPilotoPorId(int id){
        return pilotoDAO.obterPilotoPorId(id);
    }

    public List<Piloto> obterTodosPilotos(){
        return pilotoDAO.obterTodosPilotos();
    }

    public void atualizar(int id, Piloto piloto) {
        pilotoDAO.atualizarPiloto(id, piloto);
    }

    public List<Piloto> obterPilotoPorEquipe(int equipeId) {
        return pilotoDAO.obterPilotoPorEquipe(equipeId);
    }

    //TODO: método deletar
}
