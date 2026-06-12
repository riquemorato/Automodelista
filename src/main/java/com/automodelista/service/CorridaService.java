/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.CorridaDAO;
import com.automodelista.dao.ParticipacaoCorridaDAO;
import com.automodelista.dao.PilotoDAO;
import com.automodelista.model.Corrida;
import com.automodelista.model.ParticipacaoCorrida;
import com.automodelista.model.enums.StatusCorrida;

/**
 *
 * @author Henrique
 */
@Service
public class CorridaService {
    @Autowired CorridaDAO      corridaDAO;
    @Autowired ParticipacaoCorridaDAO participacaoDAO;
    @Autowired PilotoDAO       pilotoDAO;

    public Corrida obterPorId(int id) { return corridaDAO.obterPorId(id); }

    public List<ParticipacaoCorrida> obterParticipacoes(int corridaId) {
        List<ParticipacaoCorrida> lista = participacaoDAO.obterPorCorrida(corridaId);
        
        for (ParticipacaoCorrida participacao : lista) {
            participacao.setPiloto(pilotoDAO.obterPorId(participacao.getPilotoId()));
        }

        return lista;
    }

    public void inscreverPiloto(int pilotoId, int corridaId, String tipoEstrategia, String compound) {
        participacaoDAO.inserir(new ParticipacaoCorrida(pilotoId, corridaId, tipoEstrategia, compound));
    }

    public void iniciarCorrida(int corridaId) {
        Corrida corrida = corridaDAO.obterPorId(corridaId);
        //TODO: Why??
        if (corrida.getStatus() != StatusCorrida.PENDENTE){
            throw new IllegalStateException("Corrida já foi iniciada ou encerrada.");
        }  
        corridaDAO.atualizarStatus(corridaId, StatusCorrida.EM_ANDAMENTO.name());
    }
}
