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
public class EstrategiaBalanceada extends EstrategiaAbstract {

    //CAMADA DE SIMULAÇÃO: ESTRATÉGIA BALANCEADA: Default
    //Bonus de performance default em 10%
    //Degradacao: Valor padrão de degradacao pro tipo de composto de pneu
    //Risco: Valor padrão de risco do carro, calculado conforme orçamento da equipe, habilidade do piloto e qualidade do carro.

    @Override
    public int calcularBonus(int habilidade, int performance){
        return (int)((habilidade + performance) * 0.10);
    }

    @Override 
    public double getFatorDegradacao(){
        return 1.00;
    }

    @Override 
    public double getMultiplicadorRisco(){
        return 1.00;
    }

    @Override 
    public String getNome(){
        return "Balanceada";
    }

    @Override 
    public String getTipo(){
        return "BALANCEADA";
    }
}

