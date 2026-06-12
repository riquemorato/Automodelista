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

import com.automodelista.model.Piloto;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */

//BOILERPLATE baseado no ClienteDAO do projeto original realizado em sala.
//Não faço ideia do que significa @Autowired @PostConstruct.

@Repository
public class PilotoDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct private void init(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int inserir(Piloto piloto) {
        return jdbcTemplate.queryForObject(
            "INSERT INTO piloto(nome, nacionalidade, idade, numero_carro, habilidade, consistencia, pontos_campeonato, equipe_id) VALUES(?,?,?,?,?,?,?,?) RETURNING id",
            Integer.class, 
            piloto.getNome(),
            piloto.getNacionalidade(), 
            piloto.getIdade(),
            piloto.getNumeroCarro(),
            piloto.getHabilidade(),
            piloto.getConsistencia(),
            0,
            piloto.getEquipeId() == 0 ? null : piloto.getEquipeId()
        );
    }

    public void atualizar(int id, Piloto piloto) {
        jdbcTemplate.update("UPDATE piloto SET nome=?, nacionalidade=?, idade=?, numero_carro=?, habilidade=?, consistencia=?, equipe_id=? WHERE id=?",
            piloto.getNome(),
            piloto.getNacionalidade(),
            piloto.getIdade(),
            piloto.getNumeroCarro(),
            piloto.getHabilidade(),
            piloto.getConsistencia(),
            piloto.getEquipeId() == 0 ? null : piloto.getEquipeId(), id
        );
    }

    public void atualizarPontos(int id, int pontos) {
        jdbcTemplate.update("UPDATE piloto SET pontos_campeonato = pontos_campeonato + ? WHERE id=?", pontos, id);
    }

    public Piloto obterPorId(int id) {
        return Piloto.converterRegistros((HashMap<String,Object>) jdbcTemplate.queryForMap("SELECT * FROM piloto WHERE id=?", id));
    }

    public List<Piloto> obterTodos() {
        List<Piloto> lista = new ArrayList<>();
        for (Map<String,Object> resultado : jdbcTemplate.queryForList("SELECT * FROM piloto ORDER BY pontos_campeonato DESC")) {
            lista.add(Piloto.converterRegistros((HashMap<String,Object>) resultado));
        }

        return lista;
    }

    public List<Piloto> obterPorEquipe(int equipeId) {
        List<Piloto> lista = new ArrayList<>();
        for (Map<String,Object> resultado : jdbcTemplate.queryForList("SELECT * FROM piloto WHERE equipe_id=?", equipeId)){
            lista.add(Piloto.converterRegistros((HashMap<String,Object>) resultado));
        }
            
        return lista;
    }

    public void deletar(int id) { jdbcTemplate.update("DELETE FROM piloto WHERE id=?", id); }
}
