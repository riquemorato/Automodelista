/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.automodelista.model.Piloto;
import com.automodelista.service.EquipeService;
import com.automodelista.service.PilotoService;

/**
 *
 * @author Henrique
 */

//Classe responsavel por popular a UI do usuário com os dados a partir dos models
//Copiado do boilerplate gerado em sala -> ClienteController/MainController

@Controller
@RequestMapping("/pilotos")
public class PilotoController {

    //context com DB
    @Autowired ApplicationContext context;

    //Lista todos os pilotos cadastrados.
    @GetMapping
    public String listar(Model model) {
        List<Piloto> pilotos = context.getBean(PilotoService.class).obterTodos();

        Map<Integer, String> equipeNomes = new HashMap<>();
        
        for (Equipe equipe : context.getBean(EquipeService.class).obterTodas()) {
        equipeNomes.put(equipe.getId(), equipe.getNome());
        }

        model.addAttribute("pilotos", pilotos);
        model.addAttribute("equipeNomes", equipeNomes);
        return "piloto/lista";
    }

    //Adiciona um novo registro de piloto
    @GetMapping("/novo")
    public String formulario(Model model) {
        model.addAttribute("piloto", new Piloto());
        adicionarEquipesDisponiveis(model);
        return "piloto/form";
    }


    //Método POST atualizado: validação de duplicidade
    //Não permite registrar dois pilotos com o mesmo nome.
    @PostMapping("/novo")
    public String salvar(@ModelAttribute Piloto piloto, Model model) {
        try {
            context.getBean(PilotoService.class).inserir(piloto);
            return "redirect:/pilotos";
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("piloto", piloto);
            adicionarEquipesDisponiveis(model);
            return "piloto/form";
        }
    }

    //Edita os pilotos cadastrados.
    //Update: Permite editar apenas os pilotos adicionados pelo usuário, e não o seed.
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, Model model) {


        Piloto piloto = context.getBean(PilotoService.class).obterPorId(id);
        
        //Verifica se o piloto que será editado é do seed (bloqueado)
        //Se for, faz um redirect para a lista inicial.
        if (piloto.isBloqueado()) {
            return "redirect:/pilotos?bloqueado=1";
        } 

        List<Equipe> disponiveis = new ArrayList<>();
        for (Equipe equipe : context.getBean(EquipeService.class).obterTodas()) {
            if (!equipe.isBloqueada()) disponiveis.add(equipe);
        }

        model.addAttribute("piloto", piloto);
        model.addAttribute("equipes", disponiveis);
        model.addAttribute("id", id);
        return "piloto/form-editar";
    }
    
    @PostMapping("/{id}/editar")
    public String atualizar(@PathVariable int id, @ModelAttribute Piloto piloto, Model model) {
        
        try {
            context.getBean(PilotoService.class).atualizar(id, piloto);
            return "redirect:/pilotos";
        }
        catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("piloto", piloto);
            model.addAttribute("id", id);
            adicionarEquipesDisponiveis(model);
            return "piloto/form-editar";
        }
    }

    // Monta a lista de equipes não-bloqueadas, usada nos 4 retornos de formulário acima
    private void adicionarEquipesDisponiveis(Model model) {
        List<Equipe> disponiveis = new ArrayList<>();
        for (Equipe equipe : context.getBean(EquipeService.class).obterTodas()) {
            if (!equipe.isBloqueada()) disponiveis.add(equipe);
        }
        model.addAttribute("equipes", disponiveis);
    }

    
}