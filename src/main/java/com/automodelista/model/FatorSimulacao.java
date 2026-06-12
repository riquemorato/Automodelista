/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.model;

import com.automodelista.model.abstracts.EstrategiaAbstract;

/**
 *
 * @author Henrique
 */
public record FatorSimulacao(
        int basePiloto,
        int performanceCarro,
        int bonusEstrategia,
        int variacao,
        int total
) {

    public FatorSimulacao {
        //valida que o valor de fator não pode ser negativo
        if (total < 0) throw new IllegalArgumentException("FatorSimulação não pode ser negativo");
    }

    //Getters
    public int getBasePiloto(){
        return basePiloto();
    }

    public int getPerformanceCarro(){
        return performanceCarro();
    }

    public int getBonusEstrategia(){
        return bonusEstrategia();
    }
    public int getVariacao(){
        return variacao();
    }

    public int getTotal(){
        return total(); 
    }

    //Padrão de projeto Factory: calcula o FatorSimulacao a partir dos valores 
    public static FatorSimulacao calcular(Piloto piloto, Carro carro, EstrategiaAbstract estrategia) {
        int base   = piloto.calcularScore();
        int perf   = carro.calcularPerformance();
        int bonus  = estrategia.calcularBonus(piloto.getHabilidade(), perf);
        
        //Calcula uma variacao RAND para que a classificacao não fique toda igual.
        int variacao = (int)(Math.random() * 20) - 10;
        
        //Calcula o fator total com base nos valores acima
        int total  = Math.max(0, base + perf + bonus + variacao);
        
        return new FatorSimulacao(base, perf, bonus, variacao, total);
    }
}