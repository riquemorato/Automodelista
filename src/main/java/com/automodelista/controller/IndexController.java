/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.automodelista.model.Campeonato;
import com.automodelista.model.Corrida;
import com.automodelista.model.Equipe;
import com.automodelista.model.Piloto;
import com.automodelista.model.enums.StatusCorrida;
import com.automodelista.service.CampeonatoService;
import com.automodelista.service.EquipeService;
import com.automodelista.service.PilotoService;

/**
 *
 * @author Henrique
 */
@Controller
public class IndexController {

    @Autowired ApplicationContext context;

    @GetMapping("/")
    public String index(Model model) {
        CampeonatoService campeonatoService = context.getBean(CampeonatoService.class);
        PilotoService     pilotoService     = context.getBean(PilotoService.class);
        EquipeService     equipeService     = context.getBean(EquipeService.class);

        List<Equipe> equipes = equipeService.obterTodas();
        List<Piloto> pilotos = pilotoService.obterTodos();

        // Mapa id -> nome da equipe, usado para exibir o time de cada piloto
        Map<Integer, String> equipeNomes = new HashMap<>();
        for (Equipe e : equipes) equipeNomes.put(e.getId(), e.getNome());

        model.addAttribute("equipes", equipes);
        model.addAttribute("pilotos", pilotos);
        model.addAttribute("equipeNomes", equipeNomes);
        model.addAttribute("standings", pilotoService.gapParaLider());

        List<Campeonato> campeonatos = campeonatoService.obterTodos();
        if (!campeonatos.isEmpty()) {
            Campeonato campeonato = campeonatos.get(0);
            model.addAttribute("campeonato", campeonato);

            List<Corrida> corridas = campeonatoService.obterCorridas(campeonato.getId());
            model.addAttribute("corridas", corridas);

            // Próxima corrida = primeira que ainda não foi finalizada (lista já vem ordenada por rodada)
            Corrida proxima = null;
            for (Corrida corrida : corridas) {
                if (corrida.getStatus() != StatusCorrida.FINALIZADA) {
                    proxima = corrida;
                    break;
                }
            }
            model.addAttribute("proximaCorrida", proxima);
        }

        return "index";
    }
}