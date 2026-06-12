/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

//COMO FUNCIONA A SIMULAÇÃO: 

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.CarroDAO;
import com.automodelista.dao.CorridaDAO;
import com.automodelista.dao.ParticipacaoCorridaDAO;
import com.automodelista.dao.PilotoDAO;
import com.automodelista.model.Carro;
import com.automodelista.model.Corrida;
import com.automodelista.model.ParticipacaoCorrida;
import com.automodelista.model.ResultadoCorridaRecord;
import com.automodelista.model.abstracts.EstrategiaAbstract;
import com.automodelista.model.enums.StatusCorrida;

// Ao clicar no botão "Simular Corrida" na UI
// 1. Validação - Verifica se a corrida em questão já não foi executada ou finalizada antes. 
// 2. Busca uma lista de pilotos inscritos com Participações. Para a corrida acontecer, precisa de pelo menos um piloto inscrito.
// 3. A lista de participações utiliza apenas os IDs (PK) dos pilotos. 
// 4. Busca o carro cadastrado da equipe
//Inscrição do piloto: Busca todos os dados do Piloto (atributos PessoaAbstract + Piloto) e o inclui na lista de participantes da corrida.

// 5. Durante a Corrida: Loop para cada piloto cadastrado na corrida, determina-se a estratégia escolhida + calcula o ScoreSimulacao =>
//           habilidade + performance + bonus strat + RAND. Determina-se, entre 0 ou 1, se ele sofrerá um DNF.
// Condição DNF => RAND 0 e 1 < 0.05 x risco.
// TABELA DE CHANCE:
// Estratégia agressiva: multiplier 2.0 => 10%
// Estratégia Conservadora: multiplier 0.5 => 2.5%

// Final da corrida: Ordenação de posições
// Critérios de Ordenação:
// DNF => No fim da lista/classificacao
// !DNF => Ordena todos os pilotos que terminaram, Do maior score para o menor.

// Resultado Oficial da corrida:
// Array PONTOS: Contem a pontuação por posição, conforme regra da FIA/F1: 1o ao 10o colocados pontuam.
// 25, 18, 15, 12, 10, 8, 6, 4, 2, 1
// Objeto ResultadoCorrida: São os dados que serão exibidos na tela para o usuário.
// Record: Atualiza a pontuação e ranking no campeonato.
// Encerra a corrida: StatusCorrida = FINALIZADA. ==> Impossível simular de novo.

//Obs: 
// Se a corrida já foi simulada, throws exception
// Se a corrida não tiver nenhum piloto inscrito, throws exception
// Se a equipe não tiver um carro cadastrado, throws exception

@Service
public class SimulacaoService {

    @Autowired CorridaDAO corridaDAO;
    @Autowired ParticipacaoCorridaDAO participacaoDAO;
    @Autowired PilotoDAO pilotoDAO;
    @Autowired CarroDAO carroDAO;

    private static final int[] TABELA_PONTOS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    // Ficha temporária de cada piloto antes de definir a ordem final
    private record Ficha(ParticipacaoCorrida participacao, ScoreSimulacao score, boolean abandonou) {}

    //Método Orchestrador
    public List<ResultadoCorridaRecord> simularCorrida(int corridaId, int equipeId) {
        validarCorridaNaoFinalizada(corridaId);

        List<ParticipacaoCorrida> participantes = carregarParticipantesComPiloto(corridaId);
        Carro carro = carregarCarroDaEquipe(equipeId);

        List<Ficha> fichas = calcularFichas(participantes, carro);
        ordenarPorDesempenho(fichas);

        List<ResultadoCorridaRecord> resultados = montarResultados(fichas);
        salvarResultados(resultados, fichas);

        corridaDAO.atualizarStatus(corridaId, StatusCorrida.FINALIZADA.name());
        return resultados;
    }

    // Validação - Simulação da corrida => Não pode ter sido finalizada ou simulada
    private void validarCorrida(int corridaId) {
        Corrida corrida = corridaDAO.obterPorId(corridaId);
        
        //TODO: Corrigir esse problema
        if(corrida.getStatus() == StatusCorrida.FINALIZADA) {
            throw new IllegalStateException("Corrida já foi simulada ou finalizada.");
        }
    }

    // Passo 2: quem está correndo?
    private List<ParticipacaoCorrida> carregarPilotosParticipantes(int corridaId) {
        List<ParticipacaoCorrida> participacoes = participacaoDAO.obterPorCorrida(corridaId);
        if (participacoes.isEmpty())
            throw new IllegalStateException("Nenhum piloto inscrito nesta corrida.");

        for (ParticipacaoCorrida pilotoParticipante : participacoes) {
            pilotoParticipante.setPiloto(pilotoDAO.obterPorId(pilotoParticipante.getPilotoId()));
        }
        return participacoes;
    }

    // Passo 3: qual carro vamos usar como referência?
    private Carro carregarCarroEquipe(int equipeId) {
        Carro carroEquipe = carroDAO.obterPorEquipe(equipeId);
        
        //equipe precisa ter um carro cadastrado
        if (carroEquipe == null){
            throw new IllegalStateException("Equipe não possui carro cadastrado.");
        }
            
        return carroEquipe;
    }

    // Passo 4: calcula o score de cada piloto e sorteia falha mecânica
    private List<Ficha> calcularFichas(List<ParticipacaoCorrida> participantes, Carro carro) {
        List<Ficha> fichas = new ArrayList<>();
        for (ParticipacaoCorrida pilotoParticipante : participantes) {
            EstrategiaAbstract estrategia = EstrategiaAbstract.criar(p.getTipoEstrategia());
            ScoreSimulacao score = ScoreSimulacao.calcular(pilotoParticipante.getPiloto(), carro, estrategia);
            boolean abandonou = sortearFalhaMecanica(estrategia);
            fichas.add(new Ficha(pilotoParticipante, score, abandonou));
        }
        return fichas;
    }

    private boolean sortearFalhaMecanica(EstrategiaAbstract estrategia) {
        double riscoBase = 0.05;
        return Math.random() < riscoBase * estrategia.getMultiplicadorRisco();
    }

    // Passo 5: define quem ficou na frente de quem
    private void ordenarPorDesempenho(List<Ficha> fichas) {
        fichas.sort(Comparator
            .comparing(Ficha::abandonou)                                        // não-DNF primeiro
            .thenComparing(f -> f.score().total(), Comparator.reverseOrder())); // maior score primeiro
    }

    // Passo 6: transforma a ordem em posição (P1, P2...) e pontos
    private List<ResultadoCorridaRecord> montarResultados(List<Ficha> fichas) {
        List<ResultadoCorridaRecord> resultados = new ArrayList<>();
        for (int i = 0; i < fichas.size(); i++) {
            Ficha ficha = fichas.get(i);
            int posicao = i + 1;
            int pontos  = calcularPontos(posicao, ficha.abandonou());

            resultados.add(new ResultadoCorridaRecord(
                ficha.participacao().getPiloto(),
                ficha.score().total(),
                ficha.abandonou(),
                posicao,
                pontos
            ));
        }
        return resultados;
    }

    private int calcularPontos(int posicao, boolean abandonou) {
        if (abandonou || posicao > TABELA_PONTOS.length) return 0;
        return TABELA_PONTOS[posicao - 1];
    }

    // Passo 7: grava posição, pontos e atualiza o campeonato
    private void salvarResultados(List<ResultadoCorridaRecord> resultados, List<Ficha> fichas) {
        for (int i = 0; i < resultados.size(); i++) {
            ResultadoCorridaRecord resultado = resultados.get(i);
            ParticipacaoCorrida participacao = fichas.get(i).participacao();

            participacao.setPosicaoFinal(resultado.posicao());
            participacao.setPontosObtidos(resultado.pontosObtidos());
            participacao.setAbandonou(resultado.abandonou());
            participacaoDAO.atualizarResultado(participacao);

            if (resultado.pontuou()) {
                pilotoDAO.atualizarPontos(participacao.getPilotoId(), resultado.pontosObtidos());
            }
        }
    }
}
