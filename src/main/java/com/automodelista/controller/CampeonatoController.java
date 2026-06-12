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
import org.springframework.web.bind.annotation.RequestParam;

import com.automodelista.model.Campeonato;
import com.automodelista.service.CampeonatoService;
import com.automodelista.service.PilotoService;

@Controller
@RequestMapping("/campeonatos")
public class CampeonatoController {
    @Autowired ApplicationContext context;

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

    @PostMapping("/novo")
    public String salvar(@ModelAttribute Campeonato campeonato) {
        context.getBean(CampeonatoService.class).inserir(campeonato);
        return "redirect:/campeonatos";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable int id, Model model) {
        CampeonatoService cs = context.getBean(CampeonatoService.class);
        model.addAttribute("campeonato", cs.obterPorId(id));
        model.addAttribute("corridas",   cs.obterCorridas(id));
        model.addAttribute("standings",  context.getBean(PilotoService.class).gapParaLider());
        return "campeonato/detalhe";
    }

    @PostMapping("/{id}/corridas/nova")
    public String criarCorrida(@PathVariable int id,
                               @RequestParam String nome,
                               @RequestParam String circuito,
                               @RequestParam int rodada) {
        context.getBean(CampeonatoService.class).criarCorrida(nome, circuito, rodada, id);
        return "redirect:/campeonatos/" + id;
    }
}
