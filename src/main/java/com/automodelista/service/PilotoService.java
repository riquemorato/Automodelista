/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.EquipeDAO;
import com.automodelista.dao.ParticipacaoCorridaDAO;
import com.automodelista.dao.PilotoDAO;
import com.automodelista.model.Equipe;
import com.automodelista.model.Piloto;
import com.automodelista.model.PosicaoCampeonatoRecord;

/**
 *
 * @author Henrique
 */

//BOILERPLATE baseado no ClienteService do projeto original realizado em sala.

@Service
public class PilotoService {
    @Autowired PilotoDAO pilotoDAO;
    @Autowired EquipeDAO equipeDAO;
    @Autowired ParticipacaoCorridaDAO participacaoCorridaDAO;

    //ATUALIZADO - FILTRO DE DUPLICIDADE - valida antes de efetivar o cadastro
    public void inserir(Piloto piloto){
        //Existe nome
        if (pilotoDAO.existePorNome(piloto.getNome())) {
            throw new IllegalArgumentException("Já existe um piloto cadastrado com o nome \"" + piloto.getNome() + "\".");
    }
        //Existe numero ja cadastrado
        if (pilotoDAO.existePorNumeroCarro(piloto.getNumeroCarro())) {
            throw new IllegalArgumentException("Já existe um piloto cadastrado com o número " + piloto.getNumeroCarro() + ".");
    }
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

    //UPDATE - atualiza os dados de um piloto via ID - ATUALIZADO: FILTRO DE DUPLICIDADE - valida antes de efetivar o cadastro
    public void atualizar(int id, Piloto piloto){
        
        //Verifica se já existe um piloto com o nome digitado cadastrado
        if (pilotoDAO.existePorNome(piloto.getNome(), id)) {
            throw new IllegalArgumentException( "Já existe um piloto cadastrado com o nome \"" + piloto.getNome() + "\".");
        }
    
        //Verifica se já existe um número de piloto/carro cadastrado
        if (pilotoDAO.existePorNumeroCarro(piloto.getNumeroCarro(), id)) {
            throw new IllegalArgumentException( "Já existe um piloto cadastrado com o número " + piloto.getNumeroCarro() + ".");
        }

        pilotoDAO.atualizar(id, piloto);
    }

    //DELETE - deleta um piloto via ID - UPDATE: Para que um piloto seja deletado, é preciso que delete primeiro os registros de corrida dele.

    public void deletar(int id){
        //Obtem o id do piloto que será deletado
        Piloto piloto = pilotoDAO.obterPorId(id);
        
        //Joga uma excessão ao tentar excluir um piloto bloqueado.
        if(piloto.isBloqueado()) {
            throw new IllegalStateException("Pilotos de referência do campeonato não podem ser excluídos!");
        }
        participacaoCorridaDAO.deletarPiloto(id);

        pilotoDAO.deletar(id);
    }

    //OBTEM POSICAO NO CAMPEONATO
    public List<PosicaoCampeonatoRecord> gapParaLider() {
        List<Piloto> pilotos = pilotoDAO.obterTodos();
        int liderPts = pilotos.isEmpty() ? 0 : pilotos.get(0).getPontosCampeonato();

        Map<Integer, String> equipeNomes = new HashMap<>();
        for (Equipe equipe : equipeDAO.obterTodos()) {
            equipeNomes.put(equipe.getId(), equipe.getNome());
        }

        List<PosicaoCampeonatoRecord> standings = new ArrayList<>();
        for (int i = 0; i < pilotos.size(); i++) {
            Piloto piloto = pilotos.get(i);
            String nomeEquipe = equipeNomes.getOrDefault(piloto.getEquipeId(), "—");
            standings.add(new PosicaoCampeonatoRecord(
                i + 1, piloto.getNome(), nomeEquipe,
                piloto.getPontosCampeonato(), liderPts - piloto.getPontosCampeonato()));
        }

        return standings;
    }

    
}

