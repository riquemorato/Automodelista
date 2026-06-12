package com.bd.aulabd.model.DAO;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bd.aulabd.model.abstracts.SessaoAbstract;
import com.bd.aulabd.model.enums.TipoSessao;
import com.bd.aulabd.model.sessions.Classificacao;
import com.bd.aulabd.model.sessions.Race;
import com.bd.aulabd.model.sessions.TreinoLivre;

import jakarta.annotation.PostConstruct;

//    protected int id;
//    protected int corridaId;
//   protected int duracaoMinutos;

@Repository
public class SessaoDAO {
    @Autowired DataSource dataSource;
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void initialize(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void inserirSessao(SessaoAbstract sessao){
        String sqlQuery = "INSERT INTO sessao (tipo, corridaId, duracaoMinutos) VALUES (?, ?, ?)";
        Object[] obj = new Object[3];
        obj[0] = (TipoSessao) sessao.GetTipo();
        obj[1] = (int) sessao.getCorridaId();
        obj[2] = (int) sessao.getDuracaoMinutos();

        jdbcTemplate.update(sqlQuery, obj);
    }

    //Cria a sessão conforme o tipo ENUM TipoSessao
    public SessaoAbstract criarSessao(HashMap<String, Object> registro){
        String tipoSessao = (String) registro.get("tipo");

        return switch(tipoSessao){
            case "TREINO_LIVRE" -> TreinoLivre.converterRegistros(registro);
            case "QUALIFICACAO" -> Classificacao.converterRegistros(registro);
            case "CORRIDA"      -> Race.converterRegistros(registro);
            default -> throw new IllegalArgumentException("Tipo de sessão desconhecido: " + tipoSessao);
        };
        

    }

    

}
