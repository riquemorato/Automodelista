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
    @Autowired ParticipacaoCorridaDAO participacaoCorridaDAO;
    @Autowired PilotoDAO pilotoDAO;
    @Autowired CarroDAO carroDAO;

    private static final int[] TABELA_PONTOS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    // record temporário com os dados de desempenho de cada piloto antes de definir a ordem final da corrida
    // Por que record? uma vez sorteado, não é possível mudar a sorte do piloto
    private record DadosDesempenho(ParticipacaoCorrida participacao, ScoreSimulacao score, boolean abandonou) {}

    //Método Orchestrador
    public List<ResultadoCorridaRecord> simularCorrida(int corridaId, int equipeId) {
        //TODO: Implementar chamada dos métodos
    }

    // Validação - Simulação da corrida => Não pode ter sido finalizada ou simulada
    private void validarCorrida(int corridaId) {
        
        Corrida corrida = corridaDAO.obterPorId(corridaId);
        
        //TODO: Corrigir esse problema
        if(corrida.getStatus() == StatusCorrida.FINALIZADA) {
            throw new IllegalStateException("Corrida já foi simulada ou finalizada.");
        }
    }

    // Listar pilotos cadastrados na corrida
    private List<ParticipacaoCorrida> listarPilotosParticipantes(int corridaId) {
        
        List<ParticipacaoCorrida> participantesCorrida = participacaoCorridaDAO.obterPorCorrida(corridaId);

        //A corrida só poderá ser valida se houver pelo menos um piloto participante (List participantes não pode estar vazia)
        if (participantesCorrida.isEmpty()){
            throw new IllegalStateException("Nenhum piloto inscrito nesta corrida.");
        }
            

        for (ParticipacaoCorrida pilotoParticipante : participantesCorrida) {
            pilotoParticipante.setPiloto(pilotoDAO.obterPorId(pilotoParticipante.getPilotoId()));
        }

        return participantesCorrida;
    }

    // Carregar o carro da equipe que será simulada
    private Carro carroDaEquipeLoader(int equipeId){
        Carro carro = carroDAO.obterPorEquipe(equipeId);
        
        //Se a equipe não tiver um carro, throws exception
        if(carro == null){
             throw new IllegalStateException("Equipe não possui carro cadastrado.");
        }

        return carro;
    }

    //Calcular o score de desempenho da corrida para cada piloto cadastrado + sorteio de falha mecanica
    private List<DadosDesempenho> calcularDesempenho(List<ParticipacaoCorrida> participantesCorrida, Carro carro){
        List<DadosDesempenho> desempenhoPilotos = new ArrayList<>();

        //para cada piloto participante na lista, obtem-se a estratégia que será utilizada, o score total de simulacao e se ele sofrerá DNF ou não
        for(ParticipacaoCorrida participante : participantesCorrida) {
            EstrategiaAbstract estrategia = EstrategiaAbstract.criar(participante.getTipoEstrategia());
            FatorSimulacao score = FatorSimulacao.calcular(participante.getPiloto(), carro, estrategia)
        }
    }

}
