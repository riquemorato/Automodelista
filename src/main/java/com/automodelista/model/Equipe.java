
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
    
    //construtor para Select
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
    
    //MELHORIA: No DB, o orçamento utiliza NUMERIC/DECIMAL. É melhor fazer um cast utilizando (NUMBER) para precisão de valor.
        double orcamento = ((Number) registros.get("orcamento")).doubleValue();
        return new Equipe(idEquipe, nome, orcamento);
    }
}