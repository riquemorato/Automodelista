
package com.automodelista.model;

import java.util.Map;

/**
 *
 * @author Henrique
 */
public class Equipe {

    private int id;
    private String nome;
    private double orcamento;
    private boolean bloqueada; //Bloqueia a edição de equipes seed

    //Constructors
    public Equipe() {
    }

    public Equipe(String nome, double orcamento) {
        this.nome = nome;
        this.orcamento = orcamento;
    }

    //construtor para Select
    public Equipe(int id, String nome, double orcamento, boolean bloqueada) {
        this.id = id;
        this.nome = nome;
        this.orcamento = orcamento;
        this.bloqueada = bloqueada;
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

    public boolean isBloqueada(){
        return bloqueada;
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

    public void setBloqueada(boolean bloqueada) {
        this.bloqueada = bloqueada;
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

    public static Equipe converterRegistros(Map<String, Object> registros) {
        int idEquipe = (int) registros.get("id");
        String nome = (String) registros.get("nome");
        double orcamento = ((Number) registros.get("orcamento")).doubleValue();
        boolean bloqueada = registros.get("bloqueada") != null && (boolean) registros.get("bloqueada");
        return new Equipe(idEquipe, nome, orcamento, bloqueada);
    }
}