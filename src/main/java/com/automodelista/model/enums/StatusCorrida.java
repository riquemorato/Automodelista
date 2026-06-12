/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */

package com.automodelista.model.enums;

/**
 *
 * @author Henrique
 */

public enum StatusCorrida {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    FINALIZADA("Finalizada");

    private final String descricao;

    //Pega a string relacionada com cada ENUM
    StatusCorrida(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}

