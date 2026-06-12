package com.automodelista.model.enums;

/**
 *
 * @author Henrique
 */
public enum CompostoPneu {

    //Um composto de pneu contém 3 atributos diferentes
    //  1.performanceBase - o quao rapido o pneu é por volta (similar a F1)
    //  2.degradacaoPorVolta - o quao rapido o pneu degrada por volta
    //      a.MACIO >> Degrada mais rapidamente, stint mais curto e volta mais rapida
    //      b.MEDIO >> Degradacao "padrão"
    //      c.DURO  >> Degrada lentamente, sting mais logo e volta mais lenta
    //  3.duracaoIdeal - valor de duração em cenário ideal, considerando um piloto "default"

    //Estrutura Enumeradores >> TIPO_PNEU(perfBase, degVolta, durTotal, desc)
    MACIO(100, 0.85, 15, "Macio"), MEDIO(80,  0.55, 28, "Médio"), DURO(60,   0.30, 45, "Duro");

    private int performanceBase;
    private double degradacaoPorVolta;
    private int duracaoIdeal;
    private String descricao;

    CompostoPneu(int performanceBase, double degradacao, int duracao, String descricao) {
        this.performanceBase = performanceBase;
        this.degradacaoPorVolta = degradacao;
        this.duracaoIdeal = duracao;
        this.descricao = descricao;
    }

    //Getters
    public int getPerformanceBase(){
        return performanceBase;
    }

    public String getDescricao(){
        return descricao;
    }

    //Calcula o grip restante do pneu, entre 0.1 e 1.0 - Pneu começa sempre com 1.0 (NOVO)
    //Multiplica o fator de degracao pela quantidade de voltas realizadas para calcular o quanto o pneu ja foi gasto
    //Divide pela duracao ideal.
    public double calcularGrip(int voltasRodadas) {
        return Math.max(0.1, 1.0 - (degradacaoPorVolta * voltasRodadas / duracaoIdeal));
    }

    //Calcula a performance utiizando o resultado de calcularGrip
    //ex: Performance base = 100 (MACIO) * 0.7(gripAtual) ==> 70% de performance restante 
    public int calcularPerformanceEfetiva(int voltasRodadas) {
        return (int)(performanceBase * calcularGrip(voltasRodadas));
    }
}