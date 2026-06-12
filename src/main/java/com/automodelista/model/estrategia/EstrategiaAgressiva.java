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

    @Override
    public int calcularBonus(int habilidade, int performance) {
        return (int)(habilidade * 0.20 + performance * 0.15);
    }

    @Override
    public double getFatorDegradacao(){ return 1.50; }

    @Override
    public double getMultiplicadorRisco(){ return 2.00; }

    @Override
    public String getNome(){ return "Agressiva"; }

    @Override public String getTipo(){ return "AGRESSIVA"; }
}
