
package com.automodelista.model.abstracts;

/**
 *
 * @author Henrique
 */
public abstract class PessoaAbstract {

    protected int id;
    protected String nome;
    protected String nacionalidade;
    protected int idade;
    
    //Constructors
    protected PessoaAbstract() {
    }

    protected PessoaAbstract(String nome, String nacionalidade, int idade) {
        this.nome = nome;
        this.nacionalidade = nacionalidade;
        this.idade = idade;
    }

    protected PessoaAbstract(int id, String nome, String nacionalidade, int idade) {
        this.id = id;
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
