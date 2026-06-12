
package com.automodelista.abstracts;

/**
 *
 * @author Henrique
 */
public class Pessoa {
    protected int id;
    protected String nome;
    protected String nacionalidade;
    protected int idade;
    
    //Constructors
    protected Pessoa() {
    }

    protected Pessoa(String nome, String nacionalidade, int idade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
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

    //Método abstrato: getTipo() de pessoa
    public abstract String getTipo();
}
