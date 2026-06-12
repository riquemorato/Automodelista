/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model.estrategia;

import com.automodelista.model.abstracts.EstrategiaAbstract;

/**
 *
 * @author Henrique
 */
public class EstrategiaConservadora extends EstrategiaAbstract {

    //CAMADA DE SIMULAÇÃO: ESTRATÉGIA CONSERVADORA: menor risco, menor bonus
    //Bonus de performance reduzido ==> carro sendo conservado, menos risco e degradacao
    //Degradacao: Piloto está conservando pneus, durando mais voltas do que o default do composto
    //Risco: Piloto/Carro não esta no limite, risco reduzido. Fator multiplicador reduzido.


    @Override 
    public int calcularBonus(int habilidade, int performance) {
        return (int)(performance * 0.05);
    }

    @Override
    public double getFatorDegradacao(){
        return 0.70;
    }

    @Override public double getMultiplicadorRisco(){
        return 0.60;
    }

    @Override public String getNome(){
        return "Conservadora";
    }

    @Override public String getTipo(){
        return "CONSERVADORA";
    }
}