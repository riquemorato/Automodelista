/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.tests;


//Classe demonstrativa do processo iterativo para o desenvolvimento do SimulationService

//Esta classe tem como funcionalidade mostrar como cheguei no desenvolvimento final do SimulationService, quais pontos seriam abordados, como tudo seria calculado, etc.

//Piloto: Elementos principais para simulacao: nome, habilidade, consistencia
record PilotoTeste (String nome, int habilidade, int consistencia) {}

//Carro: Elementos principais: motor, aero, transmissao, suspensao
record CarroTeste (int motor, int aero, int transmissao, int suspensao) {}

enum CompostoPneu{
    MACIO,
    MEDIO,
    DURO,
}

enum EstrategiaTeste {
    
    CONSERVADORA(0.70, 0.60),
    BALANCEADA(1.00, 1.00),
    AGRESSIVA(1.50, 2.00);

    double degradacaoMultiplier;
    double riscoMultiplier;

     
    EstrategiaTeste(double degradacaoMultiplier, double riscoMultiplier) {
        this.degradacaoMultiplier = degradacaoMultiplier;
        this.riscoMultiplier = riscoMultiplier;
    }
}

class SimulationIteration {

    //"Temp" para executar calculos;
    String pilotoNome;
    int pilotoHabilidade; // 0-100
    int pilotoConsistencia; // 0-100

    int pilotoSuperHab = 100;
    int pilotoSuperCons = 100;

    int pilotoMedianoHab = 70;
    int pilotoMedianoCons = 70;

    int pilotoHabilidosoHab = 100;
    int pilotoHabilidosoCons = 60;

    int pilotoConsistenteHab = 60;
    int pilotoConsistenteCons = 100;

    int carroMotor, multiplicadorMotor = 40;
    int carroAero, multiplicadorAero = 30;
    int carroTransmissao, multiplicadorTransmissao = 20;
    int carroSuspensao, multiplicadorSuspensao = 10;

    //Um valor de variação que é utilizado para que o resultado não seja previsivel
    int variacao = (int) (Math.random() * 20) - 10;

    double multiplicadorConservador = 0.05;
    double multiplicadorDefault = 0.10;
    double multiplicadorAgressivo = 0.15;

    //Instancias

    //Calcular a base do piloto:
    //basePiloto = habilidade x (consistencia / 100)
    double basePiloto = pilotoHabilidade * (pilotoConsistencia / 100.0);

    //Calcular a performance do carro:
    //Os valores multiplicadores, assim como na vida real, mostram o itens do carro que mais afetam a performance (vide mercedes em 22/23/24):
    // Maior para menor: motor, aero, transmissao, suspensao
    int performanceCarro = (carroMotor * multiplicadorMotor) + (carroAero * multiplicadorAero) + (carroTransmissao * multiplicadorSuspensao) + (carroSuspensao * multiplicadorSuspensao);
    double MultiplicadorPerformance = 0.15;

    //Calcular Estratégia: ENUM ESTRATEGIA * Multiplicador de uso do carro 
    //Podem existir 3 tipos de estratégia:
    //   Conservadora -> performance * multiplicador baixo => o piloto está poupando o carro, não faz tanto uso de sua habilidade
    //   Balanceada -> (habilidade + performance) * multiplicador default => a habilidade do piloto começa a ser considerada
    //   Agressiva -> (habilidade * 0.2 + (performance * 0.15) => Um piloto mais habilidoso consegue ser mais agressivo e consistente 

    
    double estrategiaConservadora = performanceCarro * multiplicadorConservador;
    double estrategiaDefault = (pilotoHabilidade * performanceCarro) * multiplicadorDefault;
    double estrategiaAgressiva = (pilotoHabilidade * multiplicadorAgressivo) + (performanceCarro * MultiplicadorPerformance);

    //Calculo do score total de cada piloto:
    int scoreFinalConservador = (int) basePiloto + performanceCarro + variacao + (int) estrategiaConservadora;
    int scoreFinalDefault = (int) basePiloto + performanceCarro + variacao + (int) estrategiaDefault;
    int scoreFinalAgressivo = (int) basePiloto + performanceCarro + variacao + (int) estrategiaAgressiva;
      
}

