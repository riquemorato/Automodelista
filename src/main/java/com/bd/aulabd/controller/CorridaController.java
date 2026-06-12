
package com.bd.aulabd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bd.aulabd.services.PilotoService;

@Controller
@RequestMapping("/sessoes")
public class CorridaController {
    @Autowired ApplicationContext context;

    public String formInscrever(@PathVariable int sessaoId, Model model) {
        model.addAttribute("sessaoId", sessaoId);
        model.addAttribute("pilotos", context.getBean(PilotoService.class).obterTodosPilotos());
        return "sessao/form-inscrever";
    }
}
