/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.automodelista.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Henrique
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired ApplicationContext context;

    @GetMapping
    public String painel() {
        return "admin/painel";
    }

    @PostMapping("/resetar-demo")
    public String resetar(RedirectAttributes redirectAttributes) {
        context.getBean(DadosSeederService.class).resetarDemo();
        redirectAttributes.addFlashAttribute("mensagem", "Demonstração restaurada ao estado inicial.");
        return "redirect:/admin";
    }
}
