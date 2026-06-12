/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.automodelista.model.Piloto;
import com.automodelista.service.EquipeService;
import com.automodelista.service.PilotoService;

/**
 *
 * @author Henrique
 */

//Classe responsavel por popular a UI do usuário com os dados a partir dos models
//Copiado do boilerplate gerado em ClienteController/MainController

@Controller
@RequestMapping("/pilotos")
public class PilotoController {

    //context com DB
    @Autowired ApplicationContext context;

    @GetMapping //Mapeamento HTTP GET
    public String listar(Model model) {
        model.addAttribute("pilotos", context.getBean(PilotoService.class).obterTodos());
        return "piloto/lista";
    }

    @GetMapping("/novo") //Mapeamento HTTP GET
    public String formulario(Model model) {
        model.addAttribute("piloto", new Piloto());
        model.addAttribute("equipes", context.getBean(EquipeService.class).obterTodas());
        return "piloto/form";
    }

    @PostMapping("/novo") //Mapeamento HTTP POST
    public String salvar(@ModelAttribute Piloto piloto) {
        context.getBean(PilotoService.class).inserir(piloto);
        return "redirect:/pilotos";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("piloto", context.getBean(PilotoService.class).obterPorId(id));
        model.addAttribute("equipes", context.getBean(EquipeService.class).obterTodas());
        model.addAttribute("id", id);
        return "piloto/form-editar";
    }

    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable int id, @ModelAttribute Piloto piloto) {
        context.getBean(PilotoService.class).atualizar(id, piloto);
        return "redirect:/pilotos";
    }
}