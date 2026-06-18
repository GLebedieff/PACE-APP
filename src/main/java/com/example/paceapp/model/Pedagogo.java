package com.example.paceapp.model;

import java.io.Serializable;

public class Pedagogo extends Pessoa{
    private static final long serialVersionUID = 1L;

    // nome é herdado de Pessoa
    private String cpf; // identificador unico
    private String especializacao;
    private Instituicao instituicao; // Associação com Instituicao

    public Pedagogo(String nome, String cpf, String especializacao, Instituicao instituicao) {
        super(nome);
        this.cpf = cpf;
        this.especializacao = especializacao;
        this.instituicao = instituicao;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEspecializacao() {
        return especializacao;
    }

    public void setEspecializacao(String especializacao) {
        this.especializacao = especializacao;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }
}
