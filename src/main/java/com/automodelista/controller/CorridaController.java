/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    
    //DBContext
    @Autowired ApplicationContext context;

    @GetMapping("/{id}")
    public String detalhe(@PathVariable int id, Model model) {
        CorridaService corridaService = context.getBean(CorridaService.class);
        corridaService.garantirGridPreenchido(id);

        List<ParticipacaoCorrida> participacoes = corridaService.obterParticipacoes(id);

        Set<Integer> idsInscritos = new HashSet<>();
        for (ParticipacaoCorrida p : participacoes) {
            idsInscritos.add(p.getPilotoId());
        }

        List<Piloto> disponiveis = new ArrayList<>();
        for (Piloto p : context.getBean(PilotoService.class).obterTodos()) {
            if (!idsInscritos.contains(p.getId())) disponiveis.add(p);
        }

        model.addAttribute("corrida", corridaService.obterPorId(id));
        model.addAttribute("participacoes", participacoes);
        model.addAttribute("pilotos", disponiveis);
        model.addAttribute("equipes", context.getBean(EquipeService.class).obterTodas());
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

        @PostMapping("/{id}/simular")
        public String simular(@PathVariable int id, Model model) {
            try {
                context.getBean(CorridaService.class).iniciarCorrida(id);
            } catch (IllegalStateException ignored) {}

            List<ResultadoCorridaRecord> resultados = context.getBean(SimulacaoService.class).simularCorrida(id);
            model.addAttribute("resultados", resultados);
            model.addAttribute("corrida", context.getBean(CorridaService.class).obterPorId(id));
        return "corrida/resultado";
    }
}
