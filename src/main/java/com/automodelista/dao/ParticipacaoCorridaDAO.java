/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.dao;

import java.util.ArrayList;
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
            participacao.getCompoundPneu()
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
        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList("SELECT * FROM participacao WHERE corrida_id=? ORDER BY posicao_final", corridaId);
        
        ArrayList<ParticipacaoCorrida> lista = new ArrayList<>();
        
        for (Map<String,Object> registro : listaRegistros) {
            lista.add(ParticipacaoCorrida.converterRegistros(registro));  // sem cast
        }
    
        return lista;
    }

        // obterPorId — padrão idêntico ao obterCliente()
    public ParticipacaoCorrida obterPorId(int id) {
        String sql = "SELECT * FROM participacao WHERE id=?";
        return ParticipacaoCorrida.converterRegistros(jdbcTemplate.queryForMap(sql, id));
    }

    // obterTodos — padrão idêntico ao obterTodosClientes()
    public List<ParticipacaoCorrida> obterTodos() {
        String sql = "SELECT * FROM participacao ORDER BY nome";

        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList(sql);

        ArrayList<ParticipacaoCorrida> arrayListAuxiliar = new ArrayList<>();

        for (Map<String,Object> registro : listaRegistros) {
            arrayListAuxiliar.add(ParticipacaoCorrida.converterRegistros(registro));
        }
        return arrayListAuxiliar;
    }    

    //UPDATE: Implementação da deleção de um piloto do sistema.
    // Para um piloto ser deletado, é preciso primeiro deletar todas as suas entradas nas corridas.
    // Se ele já correu alguma vez, o seu registro/historico de corridas será deletado para que ele possa ser excluído do sistema.
    public void deletarPiloto(int id) {
        jdbcTemplate.update("DELETE FROM participacao WHERE piloto_id=?", id);
    }
}