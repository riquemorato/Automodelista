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

import com.automodelista.model.Carro;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */
@Repository
public class CarroDAO {
    @Autowired DataSource dataSource;

    JdbcTemplate jdbcTemplate;

    @PostConstruct private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int inserir(Carro carro) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO carro(nome, equipe_id, nivel_motor, nivel_aero, nivel_transmissao, nivel_suspensao) VALUES(?,?,?,?,?,?) RETURNING id",
            Integer.class,
            carro.getNome(),
            carro.getEquipeId(),
            carro.getNivelMotor(),
            carro.getNivelAero(),
            carro.getNivelTransmissao(),
            carro.getNivelSuspensao()
        );
    }

    public void atualizar(int id, Carro carro) {
        jdbcTemplate.update("UPDATE carro SET nivel_motor=?, nivel_aero=?, nivel_transmissao=?, nivel_suspensao=? WHERE id=?",
            carro.getNivelMotor(),
            carro.getNivelAero(),
            carro.getNivelTransmissao(),
            carro.getNivelSuspensao(),
            id
        );
    }

    //TODO: Corrigir esse bug - o que é?
    public Carro obterPorEquipe(int equipeId) {
        List<Map<String,Object>> lista = jdbcTemplate.queryForList("SELECT * FROM carro WHERE equipe_id=? LIMIT 1", equipeId);
        if (lista.isEmpty()) {
            return null;
        } 
        return Carro.converterRegistros((HashMap<String,Object>) lista.get(0));
    }

    // obterPorId — padrão idêntico ao obterCliente()
    public Carro obterPorId(int id) {
        String sql = "SELECT * FROM equipe WHERE id=?";
        return Carro.converterRegistros(jdbcTemplate.queryForMap(sql, id));
    }

    // obterTodos — padrão idêntico ao obterTodosClientes()
    public List<Carro> obterTodos() {
        String sql = "SELECT * FROM equipe ORDER BY nome";

        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList(sql);

        ArrayList<Carro> arrayListAuxiliar = new ArrayList<>();

        for (Map<String,Object> registro : listaRegistros) {
            arrayListAuxiliar.add(Carro.converterRegistros(registro));
        }
        return arrayListAuxiliar;
    }

    public void deletar(int id) {
        jdbcTemplate.update("DELETE FROM equipe WHERE id=?", id);
    }
}
