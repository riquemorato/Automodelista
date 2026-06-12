package com.bd.aulabd.controller;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
 
import com.bd.aulabd.model.Cliente;
import com.bd.aulabd.model.ClienteService;
 
@Controller
public class MainController {
 
    @Autowired
    ApplicationContext context;
 
    @GetMapping("/")
    public String index(){
        return "index";
    }
 
    @GetMapping("/cliente")
    public String formCliente(Model model){
        model.addAttribute("cliente",new Cliente());
        return "formcliente";
    }

    @GetMapping("/cliente/{id}/atualizar") 
    {
        public String formupdCliente(Model model, @PathVariable int id) {
            model.addAttribute("id", id);
            ClienteService cs = context.getBean(ClienteService.class);
            Cliente clienteAntigo = cs.obterCliente(id);
            model.addAttribute("cliente", clienteAntigo);
            return "formupdcliente";
        }
    }

    @PostMapping("/cliente/{id}/atualizar") {
        public String atualizarCliente(Model model, @PathVariable int id, @ModelAttribute Cliente cliente) {
            ClienteService cs = context.getBean(ClienteService.class);
            cs.inserirCliente(id, cliente);
            return "redirect:/listar/clientes";
        }
    }
 
    @PostMapping("/cliente")
    public String formCliente(@ModelAttribute Cliente cli, Model model){
        ClienteService cs = context.getBean(ClienteService.class);
		cs.inserirCliente(cli);
		return "sucesso";
    }

    @GetMapping("/listar/clientes")
    public String listarClientes(Model model) {
        ClienteService cs = context.getBean(ClienteService.class);
        List<Cliente> lista = cs.obterTodosClientes();
        //Link do view com o model.
        model.addAttribute("clientes", lista);
                return "formlista";
    }
 
}
