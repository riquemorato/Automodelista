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
public class EstrategiaAgressiva extends EstrategiaAbstract{


    //CAMADA DE SIMULAÇÃO: ESTRATÉGIA AGRESSIVA: Piloto no limite 
    //Maior bonus de performance, maior degração, maior risco
    //CalcularBonus: +20% habilidade +15% performance do carro
    //Degradação: Degradação dos pneus 50% mais rápida que o normal do composto 
    //Risco: Maior chance de falha mecanica e abandono durante a corrida ==> 2.0 = Dobro do risco 

    @Override
    public int calcularBonus(int habilidade, int performance) {
        return (int)(habilidade * 0.20 + performance * 0.15);
    }

    @Override
    public double getFatorDegradacao(){
        return 1.50;
    }

    @Override
    public double getMultiplicadorRisco(){
        return 2.00;
    }

    @Override
    public String getNome(){ return "Agressiva"; }

    @Override public String getTipo(){ return "AGRESSIVA"; }
}
