/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.automodelista.model.Equipe;
import com.automodelista.model.ParticipacaoCorrida;
import com.automodelista.model.Piloto;
import com.automodelista.model.ResultadoCorridaRecord;
import com.automodelista.service.CorridaService;
import com.automodelista.service.EquipeService;
import com.automodelista.service.PilotoService;
import com.automodelista.service.SimulacaoService;

/**
 *
 * @author Henrique
 */
@Controller
@RequestMapping("/corridas")
public class CorridaController {

    @Autowired ApplicationContext context;

    @GetMapping("/{id}")
    public String detalhe(@PathVariable int id, Model model) {
        
        CorridaService corridaService = context.getBean(CorridaService.class);
        corridaService.garantirGridPreenchido(id);

        //Lista todos os participantes por ID
        List<ParticipacaoCorrida> participacoes = corridaService.obterParticipacoes(id);
        Set<Integer> idsInscritos = new HashSet<>();
        for (ParticipacaoCorrida participante : participacoes) {
            idsInscritos.add(participante.getPilotoId());
        }
        
        List<Equipe> equipes = context.getBean(EquipeService.class).obterTodas();
        Map<Integer, String> equipeNomes = new HashMap<>();
        for (Equipe equipe : equipes) {
            equipeNomes.put(equipe.getId(), equipe.getNome());
        }

        //CHANGELOG: Agora só estão disponíveis pra inscrição pilotos não inscritos e com equipe.
        List<Piloto> disponiveis = new ArrayList<>();
        Set<Integer> equipeIdsComDisponiveis = new HashSet<>();
        
        for (Piloto piloto : context.getBean(PilotoService.class).obterTodos()) {
            
            //Verifica se o EquipeID do piloto != 0. Se for == 0, não é possivel inscrevê-lo
            if (!idsInscritos.contains(piloto.getId()) && piloto.getEquipeId() != 0) {
                disponiveis.add(piloto);
                equipeIdsComDisponiveis.add(piloto.getEquipeId());
            }
        }

        //Mapeia as equipes disponíveis com pelo menos 1 piloto inscrito.
        Map<Integer, String> equipesDisponiveis = new LinkedHashMap<>(); //LinkedHashMap mantem a ordem de inserção correta.
        for (Equipe equipe : equipes) {
            if (equipeIdsComDisponiveis.contains(equipe.getId())) {
                equipesDisponiveis.put(equipe.getId(), equipe.getNome());
            }
        }

        model.addAttribute("corrida", corridaService.obterPorId(id));
        model.addAttribute("participacoes", participacoes);
        model.addAttribute("pilotos", disponiveis);
        model.addAttribute("equipeNomes", equipeNomes);
        model.addAttribute("equipesDisponiveis", equipesDisponiveis);
        return "corrida/detalhe";
    }

    @PostMapping("/{id}/inscrever")
    public String inscrever(
        @PathVariable int id,
        @RequestParam int pilotoId,
        @RequestParam String tipoEstrategia,
        @RequestParam String compoundPneu,
        RedirectAttributes redirectAttributes) {
            context.getBean(CorridaService.class).inscreverPiloto(pilotoId, id, tipoEstrategia, compoundPneu);
            redirectAttributes.addFlashAttribute("mensagem", "Piloto inscrito com sucesso!");
            
            return "redirect:/corridas/" + id;
    }

    //Inicia a simulação da corrida
    @PostMapping("/{id}/simular")
    public String simular(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        
        CorridaService corridaService = context.getBean(CorridaService.class);
        PilotoService pilotoService   = context.getBean(PilotoService.class);

        List<ParticipacaoCorrida> participacoes = corridaService.obterParticipacoes(id);
        List<Piloto> pilotosSemEquipe = new ArrayList<>();
        
        for (ParticipacaoCorrida participante : participacoes) {
            Piloto piloto = pilotoService.obterPorId(participante.getPilotoId());
            if (piloto.getEquipeId() == 0) {
                pilotosSemEquipe.add(piloto);
            }
        }

        if (!pilotosSemEquipe.isEmpty()) {
            redirectAttributes.addFlashAttribute("pilotosSemEquipe", pilotosSemEquipe);
            return "redirect:/corridas/" + id;
        }

        //Tenta iniciar uma corrida
        try {
            corridaService.iniciarCorrida(id);
        }
        catch (IllegalStateException ignored) {}

        //Tenta adicionar o resultado da corrida ao Record
        try {
            List<ResultadoCorridaRecord> resultados = context.getBean(SimulacaoService.class).simularCorrida(id);
            model.addAttribute("resultados", resultados);
            model.addAttribute("corrida", corridaService.obterPorId(id));
            return "corrida/resultado";
        } 
        catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
            return "redirect:/corridas/" + id;
        }
    }
}