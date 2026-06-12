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

    // obterPorId — padrão idêntico ao obterCliente()
    public Campeonato obterPorId(int id) {
        String sql = "SELECT * FROM equipe WHERE id=?";
        return Campeonato.converterRegistros(jdbcTemplate.queryForMap(sql, id));
    }

    // obterTodos — padrão idêntico ao obterTodosClientes()
    public List<Campeonato> obterTodos() {
        String sql = "SELECT * FROM equipe ORDER BY nome";

        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList(sql);

        ArrayList<Campeonato> arrayListAuxiliar = new ArrayList<>();

        for (Map<String,Object> registro : listaRegistros) {
            arrayListAuxiliar.add(Campeonato.converterRegistros(registro));
        }
        return arrayListAuxiliar;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}