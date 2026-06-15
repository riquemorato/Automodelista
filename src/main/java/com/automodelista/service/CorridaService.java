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
import com.automodelista.model.Piloto;
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

    private static final String[] ESTRATEGIAS = {"CONSERVADORA", "BALANCEADA", "AGRESSIVA"};
    private static final String[] COMPOSTOS   = {"MACIO", "MEDIO", "DURO"};

    //Para gerar a corrida, pega-se o id da mesma que foi cadastrado.
    public Corrida obterPorId(int id) {
        return corridaDAO.obterPorId(id);
    }

    //Lista todos os participantes da corrida, via ParticipacaoCorrida
    public List<ParticipacaoCorrida> obterParticipacoes(int corridaId) {
        List<ParticipacaoCorrida> lista = participacaoDAO.obterPorCorrida(corridaId);
        
        //Pega o id de cada piloto e insere na lista de participacao
        for (ParticipacaoCorrida participacao : lista) {
            participacao.setPiloto(pilotoDAO.obterPorId(participacao.getPilotoId()));
        }

        return lista;
    }

    //No CRUD - POST >> Insere o piloto selecionado no formulario de inscricao
    public void inscreverPiloto(int pilotoId, int corridaId, String tipoEstrategia, String compound) {
        participacaoDAO.inserir(new ParticipacaoCorrida(pilotoId, corridaId, tipoEstrategia, compound));
    }

    //Inicia uma corrida: Verifica o estado da corrida (ENUM. Se for for diferente de Pendente, a corrida não pode ser inciada.)
    // Se for pendente, a corrida pode estar em andamento.
    public void iniciarCorrida(int corridaId) {
        Corrida corrida = corridaDAO.obterPorId(corridaId);
        
        //DONE
        if (corrida.getStatus() != StatusCorrida.PENDENTE){
            throw new IllegalStateException("Corrida já foi iniciada ou encerrada.");
        }  
        corridaDAO.atualizarStatus(corridaId, StatusCorrida.EM_ANDAMENTO.name());
    }

    //Garante que um grid esteja preenchido:

    public void garantirGridPreenchido(int corridaId) {

        //Pega o ID criado para a corrida
        Corrida corrida = corridaDAO.obterPorId(corridaId);
        
        //Valida o status da corrida. Se o status != Pendente, ela está bloqueada
        if (!corrida.isBloqueada()) {
            return;
        } 

        //Coloca os participantes em uma lista. Se a lista estiver vazia, a corrida não pode ser iniciada
        List<ParticipacaoCorrida> participacoes = participacaoDAO.obterPorCorrida(corridaId);
        if (!participacoes.isEmpty()) {
            return;
        }
        

        for (Piloto piloto : pilotoDAO.obterTodos()) {
            
            //Verifica se o piloto é bloqueado
            //Se o piloto for bloqueado, pula seu cadastro na na participacao => Automaticamente cadastrado.
            if (!piloto.isBloqueado()) {
                continue;
            } 

            //Escolhe um elemento aleatório dentro do array de objetos. Como estratégias e compostos são enumerados,
            // o valor número gerado pela quantidade de strat/compostos disponíveis no enumerado corresponde a um tipo de strat/composto

            //Pega um enumerador, gera um array com seus valores e utiliza o math.random para escolher um valor aleatorio dentro dos enumerados.
            String estrategia = ESTRATEGIAS[(int)(Math.random() * ESTRATEGIAS.length)];
            String composto   = COMPOSTOS[(int)(Math.random() * COMPOSTOS.length)];

            //Gera estrategia e escolha de composto aleatorias para os pilotos pré inscritos (SEED)
            participacaoDAO.inserir(new ParticipacaoCorrida(piloto.getId(), corridaId, estrategia, composto));
        }
    }
}