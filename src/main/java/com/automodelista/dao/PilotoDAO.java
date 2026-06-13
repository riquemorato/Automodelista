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

    public void inserir(Piloto piloto) {
        String sql = "INSERT INTO piloto(nome, nacionalidade, idade, numero_carro, habilidade, consistencia, pontos_campeonato, equipe_id, bloqueado) VALUES(?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(
            sql,
            piloto.getNome(),
            piloto.getNacionalidade(),
            piloto.getIdade(),
            piloto.getNumeroCarro(),
            piloto.getHabilidade(),
            piloto.getConsistencia(),
            0,
            piloto.getEquipeId() == 0 ? null : piloto.getEquipeId(),
            piloto.isBloqueado()
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

    // obterPorId — padrão idêntico ao obterCliente()
    public Piloto obterPorId(int id) {
        String sql = "SELECT * FROM piloto WHERE id=?";
        return Piloto.converterRegistros(jdbcTemplate.queryForMap(sql, id));
    }

    //CORREÇÃO: Ordernar por pontuação para mostrar na classificação do campeonato
    public List<Piloto> obterTodos() {
        String sql = "SELECT * FROM piloto ORDER BY pontos_campeonato DESC, nome ASC";
        List<Map<String,Object>> listaRegistros = jdbcTemplate.queryForList(sql);
        ArrayList<Piloto> arrayListAuxiliar = new ArrayList<>();
        for (Map<String,Object> registro : listaRegistros) {
            arrayListAuxiliar.add(Piloto.converterRegistros(registro));
        }
        return arrayListAuxiliar;
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
