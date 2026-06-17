package com.example.paceapp.model;

public class Usuario extends Pessoa{
    private static final long serialVersionUID = 1L;

    private String email;
    private String senha;
    private String telefone;

    public Usuario(String nome, String email, String senha, String telefone){
        super(nome);
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getTelefone(){
        return this.telefone;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
}
