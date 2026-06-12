/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

/**
 *
 * @author Henrique
 */
public record ClassificacaoCampeonatoRecord (
        int    posicao,
        String nomePiloto,
        String nomeEquipe,
        int    pontos,
        int    diffParaLider
) {
    //Validacao da posição e do gap
    public ClassificacaoCampeonatoRecord {
        if (posicao < 1) {
            throw new IllegalArgumentException("Posição inválida");
        }   
        if (diffParaLider < 0) {
            throw new IllegalArgumentException("Gap não pode ser negativo");
        }  
    }

    public int getPosicao(){
        return posicao();
    }

    public String getNomePiloto(){
        return nomePiloto();
    }
    
    public String getNomeEquipe(){
        return nomeEquipe();
    }
    
    public int getPontos(){
        return pontos();
    }

    public int getDiffParaLider(){
        return diffParaLider();
    }

    //O lider do campeonato ocupa a primeira posicao
    public boolean isLider() {
        return posicao() == 1;
    }

    public String  formatado() {
        return String.format("P%d %-20s %3d pts %s",
            posicao(), nomePiloto(), pontos(),
            isLider() ? "LÍDER" : "-" + diffParaLider());
    }
}
