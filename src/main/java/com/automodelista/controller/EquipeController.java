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

import com.automodelista.model.Equipe;
import com.automodelista.service.EquipeService;

/**
 *
 * @author Henrique
 */
@Controller
@RequestMapping("/equipes")
public class EquipeController {
    @Autowired ApplicationContext context;


    //Lista todas as equipes
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("equipes", context.getBean(EquipeService.class).obterTodas());
        return "equipe/lista";
    }

    //Adiciona uma nova equipe
    @GetMapping("/nova")
    public String formulario(Model model) {
        model.addAttribute("equipe", new Equipe());
        return "equipe/form";
    }

    //IMPLEMENTAÇÃO - Filtro de registro duplicado. 
    @PostMapping("/nova")
    public String salvar(@ModelAttribute Equipe equipe, Model model) {
        try {
            context.getBean(EquipeService.class).inserir(equipe);
            return "redirect:/equipes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("equipe", equipe);
            return "equipe/form";
        }
    }

    //Método GET >> Exibe o formulário preenchido com os dados quando o usuário clicar em "Editar Equipe"
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {
        model.addAttribute("equipe", context.getBean(EquipeService.class).obterPorId(id));
        model.addAttribute("id", id);
        return "equipe/form-editar";
    }

    //Método POST >> Envia os dados editados pelo usuário
    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable int id, @ModelAttribute Equipe equipe, Model model) {
        try {
            context.getBean(EquipeService.class).atualizar(id, equipe);
            return "redirect:/equipes";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("equipe", equipe);
            model.addAttribute("id", id);
            return "equipe/form-editar";
        }
    }
}