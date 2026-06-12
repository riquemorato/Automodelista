
package com.bd.aulabd.model.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bd.aulabd.model.Campeonato;

import jakarta.annotation.PostConstruct;

@Repository
public class CampeonatoDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize() {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void inserir(Campeonato campeonato) {
        String sqlQuery = "INSERT INTO campeonato (nome, local, data) VALUES (?, ?, ?)";
        Object[] obj = new Object[3];
        obj[0] = (String) campeonato.getNome();
        obj[1] = (int) campeonato.getTemporada();
        obj[2] = (boolean) campeonato.isAtivo();

        jdbcTemplate.update(sqlQuery, obj);
    }

    public Campeonato obterPorId(int id) {
        return Campeonato.converterRegistros((HashMap<String,Object>) jdbcTemplate.queryForMap("SELECT * FROM campeonato WHERE id=?", id));
    }

    public List<Campeonato> obterTodos() {
        List<Map<String,Object>> lista = jdbcTemplate.queryForList("SELECT * FROM campeonato ORDER BY temporada DESC");
        List<Campeonato> resultado = new ArrayList<>();
        for (Map<String,Object> registro : lista) {
            resultado.add(Campeonato.converterRegistros((HashMap) registro));
        }
        return resultado;
    }

    //TODO: DELETE CAMPEONATO
}
