/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.service;

//COMO FUNCIONA A SIMULAÇÃO: 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.automodelista.dao.CarroDAO;
import com.automodelista.dao.CorridaDAO;
import com.automodelista.dao.ParticipacaoCorridaDAO;
import com.automodelista.dao.PilotoDAO;
import com.automodelista.model.Carro;
import com.automodelista.model.Corrida;
import com.automodelista.model.FatorSimulacao;
import com.automodelista.model.ParticipacaoCorrida;
import com.automodelista.model.Piloto;
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

    private static final int[] PONTOS = {25, 18, 15, 12, 10, 8, 6, 4, 2, 1};

    // record temporário com os dados de desempenho de cada piloto antes de definir a ordem final da corrida
    // Por que record? uma vez sorteado, não é possível mudar a sorte do piloto
    private record DadosDesempenho(ParticipacaoCorrida participacao, FatorSimulacao fatorDesempenho, boolean abandonou) {}

    //Método Orchestrador
    public List<ResultadoCorridaRecord> simularCorrida(int corridaId) {
        
        //Chamando os métodos em sequencia:
        validarCorrida(corridaId);

        List<ParticipacaoCorrida> participantes = listarPilotosParticipantes(corridaId);

        Map<Integer, Carro> carrosPorEquipe = carregarCarrosPorEquipe(participantes);

        List<DadosDesempenho> listaDesempenho = calcularDesempenho(participantes, carrosPorEquipe);
        ordenarPorDesempenho(listaDesempenho);

        List<ResultadoCorridaRecord> resultados = calcularResultado(listaDesempenho);
        salvarResultados(resultados, listaDesempenho);

        corridaDAO.atualizarStatus(corridaId, StatusCorrida.FINALIZADA.name());
        return resultados;
    } 

    //MÉTODOS AUXILIARES PARA SIMULAR A CORRIDA.

    // Validação - Simulação da corrida => Não pode ter sido finalizada ou simulada
    private void validarCorrida(int corridaId) {
        
        Corrida corrida = corridaDAO.obterPorId(corridaId);
        
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

    // Carrega o carro de CADA equipe envolvida na corrida, uma única vez por equipe.
    private Map<Integer, Carro> carregarCarrosPorEquipe(List<ParticipacaoCorrida> participantesCorrida) {
        Map<Integer, Carro> carrosPorEquipe = new HashMap<>();

        for (ParticipacaoCorrida participante : participantesCorrida) {
            int equipeId = participante.getPiloto().getEquipeId();

            // Já buscamos o carro dessa equipe antes (ex: 2 pilotos do mesmo time)? Não busca de novo.
            if (carrosPorEquipe.containsKey(equipeId)) {
                continue;
            }

            Carro carro = carroDAO.obterPorEquipe(equipeId);

            //Se a equipe não tiver um carro, throws exception
            if (carro == null) {
                throw new IllegalStateException(
                    "A equipe de " + participante.getPiloto().getNome() + " não possui carro cadastrado.");
            }

            carrosPorEquipe.put(equipeId, carro);
        }

        return carrosPorEquipe;
    }

    //Calcular o score de desempenho da corrida para cada piloto cadastrado + sorteio de falha mecanica
    private List<DadosDesempenho> calcularDesempenho(List<ParticipacaoCorrida> participantesCorrida, Map<Integer, Carro> carrosPorEquipe){
        List<DadosDesempenho> desempenhoPilotos = new ArrayList<>();

        //para cada piloto participante na lista, obtem-se a estratégia, o carro da SUA equipe,
        //o score total de simulacao e se ele sofrerá DNF ou não
        for(ParticipacaoCorrida participante : participantesCorrida) {
            Piloto piloto = participante.getPiloto();
            Carro carroDoPiloto = carrosPorEquipe.get(piloto.getEquipeId());

            EstrategiaAbstract estrategia = EstrategiaAbstract.criar(participante.getTipoEstrategia());
            FatorSimulacao fatorDesempenho = FatorSimulacao.calcular(piloto, carroDoPiloto, estrategia);

            boolean isDnf = randomDNF(estrategia);
            desempenhoPilotos.add(new DadosDesempenho(participante, fatorDesempenho, isDnf));
        }

        return desempenhoPilotos;
    }

    //Método para calcular a chance de DNF do piloto, utilizando em CalcularDesempenho
    private boolean randomDNF(EstrategiaAbstract estrategia) {
        double riscoBase = 0.05;
        return Math.random() < riscoBase * estrategia.getMultiplicadorRisco();
    }

    private List<DadosDesempenho> ordenarPorDesempenho(List<DadosDesempenho> dadosDesempenhoGeral) {
        //Separa os pilotos por desempenho em duas listas: os que terminaram e os que não terminaram (DNF)
        //Os que terminaram tem seu fatorDesempenho calculado, para determinar a ordem de melhor performance -> pior performance (sem DNF)
        List<DadosDesempenho> terminaramCorrida  = new ArrayList<>();
        List<DadosDesempenho> abandonaramCorrida = new ArrayList<>();

        for (DadosDesempenho dadosPiloto : dadosDesempenhoGeral) {
            if (dadosPiloto.abandonou()){
                abandonaramCorrida.add(dadosPiloto);
            }
            else {
                terminaramCorrida.add(dadosPiloto);
            }               
        }

        // Só uma regra de comparação: maior score primeiro
        // DNF: os que abandoraram a corrida não precisam ser comparados
        terminaramCorrida.sort((a, b) -> b.fatorDesempenho().total() - a.fatorDesempenho().total());

        // Merge de listas ordenando por fator de desempenho.
        List<DadosDesempenho> resultado = new ArrayList<>();
        resultado.addAll(terminaramCorrida);
        resultado.addAll(abandonaramCorrida);
        
        return resultado;
    }

    //Gera o resultado final da corrida como Record
     private List<ResultadoCorridaRecord> calcularResultado(List<DadosDesempenho> dadosDesempenhoPiloto) {

        //Lista os resultados dos pilotos
        List<ResultadoCorridaRecord> resultados = new ArrayList<>();

        //Para cada piloto, pega os dados de desempenho ordenados e calcula sua posição na classificacao final e sua pontuacao
        for (int i = 0; i < dadosDesempenhoPiloto.size(); i++) {
            DadosDesempenho dadosDesempenhoIndividual = dadosDesempenhoPiloto.get(i);
            int posicao = i + 1;
            int pontos  = calcularPontos(posicao, dadosDesempenhoIndividual.abandonou());

            //adiciona os resultados do piloto na lista para o Record.
            resultados.add(new ResultadoCorridaRecord(
                dadosDesempenhoIndividual.participacao().getPiloto(),
                dadosDesempenhoIndividual.fatorDesempenho().total(),
                dadosDesempenhoIndividual.abandonou(),
                posicao,
                pontos
            ));
        }
        return resultados;
    }

    //Calcula a pontuação utilizando o array PONTOS. cada posicao tem sua pontuação especifica, do primeiro ao décimo colocado. Utilizado em calcularResultado
    private int calcularPontos(int posicao, boolean abandonou) {
        if (abandonou || posicao > PONTOS.length) return 0;
        return PONTOS[posicao - 1];
    }

    //Salva os resultados, posicao, pontuacao e atualiza o campeonato
    // Passo final: grava posição, pontos e atualiza o campeonato
    private void salvarResultados(List<ResultadoCorridaRecord> resultados, List<DadosDesempenho> fichas) {
        
        //Para cada resultado armazenado na lista recebida
        for (int i = 0; i < resultados.size(); i++) {

            ResultadoCorridaRecord resultado = resultados.get(i);
            ParticipacaoCorrida participacao = fichas.get(i).participacao();

            //Determina a posicao final na classificacao da corrida
            participacao.setPosicaoFinal(resultado.posicao());
            //Determina os pontos obtidos ao final da corrida
            participacao.setPontosObtidos(resultado.pontosObtidos());
            //Determina se o piloto teve DNF ou nao
            participacao.setAbandonou(resultado.abandonou());
            //Atualiza o resultado 
            participacaoCorridaDAO.atualizarResultado(participacao);

            if (resultado.pontuou()) {
                pilotoDAO.atualizarPontos(participacao.getPilotoId(), resultado.pontosObtidos());
            }
        }
    }

}
