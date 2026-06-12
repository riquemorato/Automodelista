/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

/**
 *
 * @author Henrique
 */
public record PosicaoCampeonatoRecord(int posicao, String nomePiloto, String nomeEquipe, int pontos, int gapParaLider) {
    public PosicaoCampeonatoRecord {
        //validações
        if (posicao < 1){
           throw new IllegalArgumentException("Posição inválida"); 
        }     
        if (gapParaLider < 0) {
            throw new IllegalArgumentException("Gap inválido");
        }
    }

    public boolean isLider(){
        return posicao == 1;
    }
}
