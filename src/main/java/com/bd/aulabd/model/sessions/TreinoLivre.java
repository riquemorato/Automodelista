/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.model.sessions;

import java.util.HashMap;

import com.bd.aulabd.model.abstracts.SessaoAbstract;
import com.bd.aulabd.model.enums.TipoSessao;

/**
 *
 * @author Henrique
 */
public class TreinoLivre extends SessaoAbstract {

    //@SessaoAbstract:
    //    protected int id;
    //    protected int corridaId;
    //    protected int duracaoMinutos;

    @Override
    public TipoSessao GetTipo() {
        return TipoSessao.TREINOLIVRE;
    }

    public TreinoLivre(int id, int corridaId, int duracaoMinutos) {
        super(id, corridaId, duracaoMinutos);
    }

    public static TreinoLivre converterRegistros(HashMap<String, Object> registros) {

        int id = (int) registros.get("id");
        int corridaId = (int) registros.get("corridaId");
        int duracaoMinutos = (int) registros.get("duracaoMinutos");

        return new TreinoLivre(id, corridaId, duracaoMinutos);
    }

}
