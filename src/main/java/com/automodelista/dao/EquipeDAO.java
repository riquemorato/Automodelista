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

    // obterPorId — padrão idêntico ao obterCliente()
    public Equipe obterPorId(int id) {
        String sql = "SELECT * FROM equipe WHERE id=?";
        return Equipe.converterRegistros(jdbcTemplate.queryForMap(sql, id));
    }

    // obterTodos — padrão idêntico ao obterTodosClientes()
    public List<Equipe> obterTodos() {
        String sql = "SELECT * FROM equipe ORDER BY nome";

        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList(sql);

        ArrayList<Equipe> arrayListAuxiliar = new ArrayList<>();

        for (Map<String,Object> registro : listaRegistros) {
            arrayListAuxiliar.add(Equipe.converterRegistros(registro));
        }
        return arrayListAuxiliar;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}
