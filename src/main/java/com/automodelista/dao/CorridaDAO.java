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

import com.automodelista.model.Corrida;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */
@Repository
public class CorridaDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int inserir(Corrida corrida) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO corrida(nome, circuito, rodada, status, campeonato_id) VALUES(?,?,?,?,?) RETURNING id",
            Integer.class,
            corrida.getNome(),
            corrida.getCircuito(),
            corrida.getRodada(),
            corrida.getStatus().name(),
            corrida.getCampeonatoId()
        );
    }

    //Atualiza o Status da corrida conforme ENUM 
    public void atualizarStatus(int id, String status) {
        jdbcTemplate.update("UPDATE corrida SET status=? WHERE id=?", status, id);
    }

    // obterPorId — padrão idêntico ao obterCliente()
    public Corrida obterPorId(int id) {
        String sql = "SELECT * FROM equipe WHERE id=?";
        return Corrida.converterRegistros(jdbcTemplate.queryForMap(sql, id));
    }

    // obterTodos — padrão idêntico ao obterTodosClientes()
    public List<Corrida> obterTodos() {
        String sql = "SELECT * FROM equipe ORDER BY nome";

        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList(sql);

        ArrayList<Corrida> arrayListAuxiliar = new ArrayList<>();

        for (Map<String,Object> registro : listaRegistros) {
            arrayListAuxiliar.add(Corrida.converterRegistros(registro));
        }
        return arrayListAuxiliar;
    }

    public List<Corrida> obterPorCampeonato(int campeonatoId) {
        List<Corrida> lista = new ArrayList<>();
        for (Map<String,Object> resultado : jdbcTemplate.queryForList(
                "SELECT * FROM corrida WHERE campeonato_id=? ORDER BY rodada", campeonatoId))
            lista.add(Corrida.converterRegistros((HashMap<String,Object>) resultado));
        return lista;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}
