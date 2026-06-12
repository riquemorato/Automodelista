
package com.bd.aulabd.model.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bd.aulabd.model.Corrida;
import com.bd.aulabd.model.enums.StatusCorrida;

import jakarta.annotation.PostConstruct;

/**
 *
 * @author Henrique
 */

@Repository
public class CorridaDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    //CREATE
    public void inserir(Corrida corrida){
        String sqlQuery = "INSERT INTO corrida (nome, circuito, rodada, status, campeonatoId) VALUES (?, ?, ?, ?, ?)";
        Object[] obj = new Object[5];
        obj[0] = (String) corrida.getNome();
        obj[1] = (String) corrida.getCircuito();
        obj[2] = (int) corrida.getRodada();
        obj[3] = (StatusCorrida) corrida.getStatus();
        obj[4] = (int) corrida.getCampeonatoId();

        jdbcTemplate.update(sqlQuery, obj);
    }

    //UPDATE
    public void atualizar(int idCorrida, Corrida corrida){
        String sqlQuery = "UPDATE corrida SET nome = ?, circuito = ?, rodada = ? where id = ?";
        Object[] obj = new Object[3];

        obj[0] = (String) corrida.getNome();
        obj[1] = (String) corrida.getCircuito();
        obj[2] = (int) corrida.getRodada();

        jdbcTemplate.update(sqlQuery, obj);
    }

    //READ
    public Corrida obterCorridaPorId(int id){
        String sqlQuery = "SELECT * FROM corrida WHERE id = ?";
        return Corrida.converterRegistros((HashMap<String, Object>) jdbcTemplate.queryForMap(sqlQuery, id));
    }

    //Obtem todas as corridas registradas
    public List<Corrida> obterTodasCorridas() {

        String sqlQuery = "SELECT * FROM corrida";
        List <Map<String,Object>> listarRegistros = jdbcTemplate.queryForList(sqlQuery);

        ArrayList<Corrida> auxiliar = new ArrayList<>();

        for (Map<String,Object> registro : listarRegistros) {
            auxiliar.add(Corrida.converterRegistros((HashMap) registro));
        }

        return auxiliar;
    }

    //Listar as corridas por campeonato => CampeonatoID
    public List<Corrida> obterPorCampeonato(int campeonatoId) {
        List<Map<String,Object>> lista = jdbcTemplate.queryForList( "SELECT * FROM corrida WHERE campeonato_id=? ORDER BY rodada", campeonatoId);
        
        List<Corrida> resultado = new ArrayList<>();
        for (Map<String,Object> reg : lista) {
            resultado.add(Corrida.converterRegistros((HashMap) reg));
        }
        return resultado;
    }

    //DELETE
    //TODO: Delete

}
