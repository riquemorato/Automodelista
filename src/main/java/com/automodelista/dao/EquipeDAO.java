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

import com.automodelista.model.Equipe;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */
@Repository
public class EquipeDAO {
    
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;
    
    @PostConstruct private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int inserir(Equipe equipe) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO equipe(nome, orcamento) VALUES(?,?) RETURNING id",
            Integer.class,
            equipe.getNome(),
            equipe.getOrcamento()
        );
    }

    public void atualizar(int id, Equipe equipe) {
        jdbcTemplate.update("UPDATE equipe SET nome=?, orcamento=? WHERE id=?",
            equipe.getNome(),
            equipe.getOrcamento(), id
        );
    }

    public Equipe obterPorId(int id) {
        return Equipe.converterRegistros(
            (HashMap<String,Object>) jdbcTemplate.queryForMap("SELECT * FROM equipe WHERE id=?", id));
    }

    public List<Equipe> obterTodos() {
        List<Equipe> lista = new ArrayList<>();
        for (Map<String,Object> resultado : jdbcTemplate.queryForList("SELECT * FROM equipe ORDER BY nome")) {
            lista.add(Equipe.converterRegistros((HashMap<String,Object>) resultado));
        }
        return lista;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}
