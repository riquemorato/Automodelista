
package com.automodelista.model;

import java.util.HashMap;

/**
 *
 * @author Henrique
 */
public class Equipe {

    private int id;
    private String nome;
    private double orcamento;

    //Constructors
    public Equipe() {
    }

    public Equipe(String nome, double orcamento) {
        this.nome = nome;
        this.orcamento = orcamento;
    }
    
    public Equipe(int id, String nome, double orcamento) {
        this.id = id;
        this.nome = nome;
        this.orcamento = orcamento;
    }
    
    //Getters
    public int getId() {
        return id;
    }

    public String getNome(){
        return nome;
    }
    
    public double getOrcamento(){
        return orcamento; 
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public void setOrcamento(double orcamento){
        this.orcamento = orcamento;
    }


    //Cada equipe tem um orçamento. O orçamento ajuda a determinar a performance da equipe/carro/piloto
    //durante as corridas e a temporada
    //O orçamento varia (entrada e saida de dinheiro apos cada corrida)
    public void sacarOrcamento(double valor) {
        
        if (valor > orcamento) {
            throw new IllegalStateException("Saldo disponível insuficiente. Disponível: R$" + orcamento + ". Necessário: R$" + valor);
        }

        this.orcamento -= valor;
    }

    public void depositarOrcamento(double valor) {
        this.orcamento += valor;
    }

    //Conversao de registros
    public static Equipe converterRegistros(HashMap<String, Object> registros) {

        int idEquipe = (int) registros.get("id");
        String nome = (String) registros.get("nome");
        double orcamento = (double) registros.get("orcamento");

        return new Equipe(idEquipe, nome, orcamento); //Tomar cuidado com a ordem.

    }
}