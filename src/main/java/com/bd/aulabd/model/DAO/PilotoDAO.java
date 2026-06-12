/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.model.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.bd.aulabd.model.Equipe;
import com.bd.aulabd.model.Piloto;

import jakarta.annotation.PostConstruct;

/**    
 * @author Henrique
 */

public class PilotoDAO {
    @Autowired
    DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //CREATE
    public void inserir(Piloto piloto){
        String sqlQuery = "INSERT INTO pilotos (nome, nacionalidade, idade, numeroPiloto, habilidade, consistencia, equipeId) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Object[] obj = new Object[7];
        obj[0] = (String) piloto.getNome();
        obj[1] = (String) piloto.getNacionalidade();
        obj[2] = (int) piloto.getIdade();
        obj[3] = (int) piloto.getNumeroPiloto();
        obj[4] = (int) piloto.getHabilidade();
        obj[5] = (int) piloto.getConsistencia();
        obj[6] = (Equipe) piloto.getEquipeId();

        jdbcTemplate.update(sqlQuery, obj);
    }

    //READ
    public List<Piloto> obterTodosPilotos() {
        String sqlQuery = "SELECT * FROM pilotos";
           List<Map<String,Object>> listaRegistros =  jdbcTemplate.queryForList(sqlQuery);
            ArrayList<Piloto> aux = new ArrayList<>();
            for(Map<String,Object> registro : listaRegistros) {
                aux.add(Piloto.converterRegistros((HashMap) registro));
            }
        return aux;
    }

    public Piloto obterPilotoPorId(int id) {
        String sqlQuery = "SELECT * FROM pilotos WHERE id=?";
        return Piloto.converterRegistros((HashMap<String,Object>) jdbcTemplate.queryForMap(sqlQuery,id));//Tomar cuidado com a ordem.

    }

    public List<Piloto> obterPilotoPorEquipe(int equipeId){
        List<Map<String, Object>>listaPilotosEquipe = jdbcTemplate.queryForList("SELECT * FROM pilotos WHERE equipeId=?", equipeId);
        List<Piloto> resultado = new ArrayList<>();
        
        for (Map<String,Object> registro : listaPilotosEquipe) {
            resultado.add(Piloto.converterRegistros((HashMap) registro));
        }

        return resultado;
    }

    //UPDATE
    public void atualizarPiloto(int idPiloto, Piloto piloto){

        String sqlQuery = "UPDATE pilotos SET nome = ?, nacionalidade = ?, idade = ?, numeroPiloto = ?, habilidade = ?, consistencia = ?, equipeId = ?, where id = ?";
        
        Object[] obj = new Object[7];
        obj[0] = (String) piloto.getNome();
        obj[1] = (String) piloto.getNacionalidade();
        obj[2] = (int) piloto.getIdade();
        obj[3] = (int) piloto.getNumeroPiloto();
        obj[4] = (int) piloto.getHabilidade();
        obj[5] = (int) piloto.getConsistencia();
        obj[6] = (Equipe) piloto.getEquipeId();


        jdbcTemplate.update(sqlQuery, obj);
    }

    public void AtualizarPontuacao(int id, int pontos, Piloto piloto){
        jdbcTemplate.update("UPDATE piloto SET pontosCampeonato = pontosCampeonato + ? WHERE id = ?");
        Object[] obj = new Object[1];
        obj[0] = (int) piloto.getPontosCampeonato();

    }

    //DELETE
    //TODO: Delete 

}
