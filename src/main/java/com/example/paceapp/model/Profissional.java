package com.example.paceapp.model;

import java.io.Serializable; // vai permitir que a classe seja gravada em disc cm objeto bin

public class Profissional implements Serializable{
    private static final long serialVersionUID = 1L;

    // aqui é somente p criar os atributos. a validação dos seus tipos é feito na view.
    private String nome;
    private String cpf;
    private String email;
    private String crm;

    //constutor n pdr
    public Profissional(String nome, String cpf, String email, String crm){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.crm = crm;
    }

    //getters e setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }
}
