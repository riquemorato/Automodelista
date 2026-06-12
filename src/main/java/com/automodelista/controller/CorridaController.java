/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.automodelista.model.ResultadoCorridaRecord;
import com.automodelista.service.CorridaService;
import com.automodelista.service.EquipeService;
import com.automodelista.service.PilotoService;

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
        model.addAttribute("corrida", corridaService.obterPorId(id));
        model.addAttribute("participacoes", corridaService.obterParticipacoes(id));
        model.addAttribute("pilotos", context.getBean(PilotoService.class).obterTodos());
        model.addAttribute("equipes", context.getBean(EquipeService.class).obterTodas());
        return "corrida/detalhe";
    }

    @PostMapping("/{id}/inscrever")
    public String inscrever(
        @PathVariable int id,
        @RequestParam int pilotoId,
        @RequestParam String tipoEstrategia,
        @RequestParam String compoundPneu) {
            context.getBean(CorridaService.class).inscreverPiloto(pilotoId, id, tipoEstrategia, compoundPneu);
            return "redirect:/corridas/" + id;
    }

    @PostMapping("/{id}/simular")
    public String simular(
        @PathVariable int id,
        @RequestParam int equipeId,
        Model model) {
            context.getBean(CorridaService.class).iniciarCorrida(id);

            List<ResultadoCorridaRecord> resultados = context.getBean(SimulacaoService.class).simularCorrida(id, equipeId);
            model.addAttribute("resultados", resultados);
            model.addAttribute("corrida", context.getBean(CorridaService.class).obterPorId(id));
            return "corrida/resultado";
    }

}
