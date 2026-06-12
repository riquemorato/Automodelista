/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

//COMO FUNCIONA A SIMULAÇÃO: 
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
// Tabela PONTOS: Contem a pontuação por posição, conforme regra da FIA/F1: 1o ao 10o colocados pontuam.
// 25, 18, 15, 12, 10, 8, 6, 4, 2, 1
// Objeto ResultadoCorrida: São os dados que serão exibidos na tela para o usuário.
// Record: Atualiza a pontuação e ranking no campeonato.
// Encerra a corrida: StatusCorrida = FINALIZADA.

//Obs: 
// Se a corrida já foi simulada, throws exception
// Se a corrida não tiver nenhum piloto inscrito, throws exception
// Se a equipe não tiver um carro cadastrado, throws exception

class SimulacaoService {

}
