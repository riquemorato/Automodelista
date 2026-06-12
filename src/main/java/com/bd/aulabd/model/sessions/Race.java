//CLASSE RESPONSÁVEL PELO MODELO Corrida --> Utilizado em SessaoDAO
package com.bd.aulabd.model.sessions;

import java.util.HashMap;

import com.bd.aulabd.model.abstracts.SessaoAbstract;
import com.bd.aulabd.model.enums.TipoSessao;

public class Race extends SessaoAbstract {
    
        //@SessaoAbstract:
    //    protected int id;
    //    protected int corridaId;
    //    protected int duracaoMinutos;

    //Vinculo com a classe abstrata SessaoAbstract
    @Override
    public TipoSessao GetTipo() {
        return TipoSessao.CORRIDA;
    }

    //Pontuacao para os 10 primeiros, como na formula 1
    private static int[] TabelaPontos = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    //CONSTRUCTORS
    public Race(){

    }

    public Race(int id, int corridaId, int duracaoMinutos) {
        super(id, corridaId, duracaoMinutos);
    }
    //TODO: Implementar logica de negócios da Corrida:
    //Simulacao
    //Sistema de Pontuacao

    //Converter Registros
    public static Race converterRegistros(HashMap<String, Object> registros) {

        int id = (int) registros.get("id");
        int corridaId = (int) registros.get("corridaId");
        int duracaoMinutos = (int) registros.get("duracaoMinutos");

        return new Race(id, corridaId, duracaoMinutos);
    }

}
