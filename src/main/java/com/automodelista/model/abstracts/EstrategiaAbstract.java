//Classe abstrata que implementa as assinaturas dos métodos de estratégia durante a corrida

package com.automodelista.model.abstracts;

import com.automodelista.model.enums.CompostoPneu;

/**
 * @author Henrique
 */

public abstract class EstrategiaAbstract {
    public abstract int    calcularBonus(int habilidadePiloto, int performanceCarro);
    public abstract double getFatorDegradacao();
    public abstract double getMultiplicadorRisco();
    public abstract String getNome();
    public abstract String getTipo();

    //Calculo da performance do pneu utilizando os parametros especificos de cada enumerador
    public int calcularPerformancePneu(CompostoPneu compound, int voltasRodadas) {
        return compound.calcularPerformanceEfetiva((int)(voltasRodadas * getFatorDegradacao()));
    }
}
