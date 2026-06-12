/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bd.aulabd.model.abstracts;

/**
 *
 * @author Henrique
 */
public abstract class PessoaAbstract {
    
    //Precisam ser protected para ser acessados nas classes herdeiras (piloto)
    protected int id;
    protected String nome;
    protected String nacionalidade;
    protected int idade;


    //Constructor - precisa ser protected e não private, para conseguir acessar nas classes herdeiras
    protected PessoaAbstract(){
    }

    protected PessoaAbstract(String nome, String nacionadade, int idade){
        this.nome = nome;
        this.nacionalidade = nacionadade;
        this.idade = idade;
    }

    protected PessoaAbstract(int id, String nome, String nacionadade, int idade){
        this.id = id;
        this.nome = nome;
        this.nacionalidade = nacionadade;
        this.idade = idade;
    }

    //Getters
    public int getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public String getNacionalidade(){
        return nacionalidade;
    }

    public int getIdade(){
        return idade;
    }

    //Setters
    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setNacionalidade(String nacionalidade){
        this.nacionalidade = nacionalidade;
    }

    public void setIdade(int idade){
        this.idade = idade;
    }

    //MÉTODOS
    //Identificador do tipo concreto da Pessoa --> piloto, engenheiro, staff, etc
    public abstract String getTipo();

    @Override
    public String toString(){
        return getTipo() + " [" + id + "] " + nome; 
    }




}
