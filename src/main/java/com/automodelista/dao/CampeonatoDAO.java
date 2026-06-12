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

import com.automodelista.model.Campeonato;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */
@Repository
public class CampeonatoDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct private void init(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int inserir(Campeonato campeonato) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO campeonato(nome, temporada) VALUES(?,?) RETURNING id",
            Integer.class,
            campeonato.getNome(),
            campeonato.getTemporada()
        );
    }

    public Campeonato obterPorId(int id) {
        return Campeonato.converterRegistros(
            (HashMap<String,Object>) jdbcTemplate.queryForMap("SELECT * FROM campeonato WHERE id=?", id));
    }

    public List<Campeonato> obterTodos() {
        List<Campeonato> lista = new ArrayList<>();
        for (Map<String,Object> resultado : jdbcTemplate.queryForList("SELECT * FROM campeonato ORDER BY temporada DESC")){
            lista.add(Campeonato.converterRegistros((HashMap<String,Object>) resultado));
        }
            
        return lista;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}