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
import org.springframework.web.bind.annotation.RequestParam;

import com.automodelista.model.Campeonato;
import com.automodelista.service.CampeonatoService;
import com.automodelista.service.PilotoService;

/**
 *
 * @author Henrique
 */
@Controller
public class ClassificacaoController {

    @Autowired ApplicationContext context;

    @GetMapping("/classificacao")
    public String classificacao(@RequestParam(required = false) Integer campeonatoId, Model model) {
        CampeonatoService campeonatoService = context.getBean(CampeonatoService.class);
        List<Campeonato> campeonatos = campeonatoService.obterTodos();

        Campeonato selecionado = null;
        if (campeonatoId != null) {
            selecionado = campeonatoService.obterPorId(campeonatoId);
        } 
        else if (!campeonatos.isEmpty()) {
            selecionado = campeonatos.get(0);
        }

        model.addAttribute("campeonatos", campeonatos);
        model.addAttribute("campeonato", selecionado);
        model.addAttribute("classificacao", context.getBean(PilotoService.class).gapParaLider());
        return "classificacao/index";
    }
}
