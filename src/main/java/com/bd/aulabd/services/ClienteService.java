
package com.bd.aulabd.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bd.aulabd.model.Cliente;
import com.bd.aulabd.model.ClienteDAO;

@Service
public class ClienteService {

    @Autowired
    ClienteDAO cdao;

    public void inserirCliente(Cliente cli) {
        cdao.inserirCliente(cli);
    }

    public Cliente obterCliente(int id) {
        return cdao.obterCliente(id);
    }

    public List<Cliente> obterTodosClientes() {
        return cdao.obterTodosClientes();
    }

    public void atualizarCliente(int id, Cliente novo){
        cdao.atualizarCliente(id, novo);
    }
}
