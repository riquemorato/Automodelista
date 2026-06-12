/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bd.aulabd.model.Campeonato;
import com.bd.aulabd.services.CampeonatoService;

@Controller
@RequestMapping("/campeonatos")
public class CampeonatoController {
    @Autowired ApplicationContext context;

    //GET MAPPING
    @GetMapping 
    public String listar(Model model) {
        model.addAttribute("campeonatos", context.getBean(CampeonatoService.class).obterTodos());
        return "campeonato/lista";
    }

    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("campeonato", new Campeonato());
        return "campeonato/form";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable int id, Model model) {
        CampeonatoService campeonatoService = context.getBean(CampeonatoService.class);
        model.addAttribute("campeonato", campeonatoService.obterPorId(id));
        model.addAttribute("corridas", campeonatoService.obterCorridasDoCampeonato(id));
        return "campeonato/detalhe";
    }

    //POST MAPPING
    @PostMapping("/novo")
    public String salvar(@ModelAttribute Campeonato campeonato) {
        context.getBean(CampeonatoService.class).inserir(campeonato);
        return "redirect:/campeonatos";
    }
}
