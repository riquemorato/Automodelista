/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.automodelista.model.ParticipacaoCorrida;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */
@Repository
public class ParticipacaoCorridaDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;
    
    @PostConstruct private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void inserir(ParticipacaoCorrida participacao) {
        jdbcTemplate.update("INSERT INTO participacao(piloto_id, corrida_id, tipo_estrategia, compound_pneu) VALUES(?,?,?,?)",
            participacao.getPilotoId(),
            participacao.getCorridaId(),
            participacao.getTipoEstrategia(),
            participacao.getCompostoPneu()
        );
    }

    public void atualizarResultado(ParticipacaoCorrida participacao) {
        jdbcTemplate.update("UPDATE participacao SET posicao_final=?, pontos_obtidos=?, abandonou=? WHERE id=?",
            participacao.getPosicaoFinal(),
            participacao.getPontosObtidos(),
            participacao.isAbandonou(),
            participacao.getId()
        );
    }

    public List<ParticipacaoCorrida> obterPorCorrida(int corridaId) {
        List<ParticipacaoCorrida> lista = new ArrayList<>();
        for (Map<String,Object> resultado : jdbcTemplate.queryForList("SELECT * FROM participacao WHERE corrida_id=? ORDER BY posicao_final", corridaId)) {
            lista.add(ParticipacaoCorrida.converterRegistros((HashMap<String,Object>) resultado));
        }
            
        return lista;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}