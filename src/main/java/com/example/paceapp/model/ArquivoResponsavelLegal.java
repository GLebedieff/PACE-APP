package com.example.paceapp.model;

import java.io.*; // Todas as classes de io
import java.util.ArrayList;

public class ArquivoResponsavelLegal implements Persistencia<ResponsavelLegal> {
    private static final String CAMINHO_ARQUIVO = "responsavel.dat";

    @Override
    public void salvarLista(ArrayList<ResponsavelLegal> lista) {
        try {
            File arq = new File(CAMINHO_ARQUIVO);
            if (!arq.exists()) {
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))) {
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de responsáveis legais: " + e.getMessage());
        }
    }
    @Override
    public ArrayList<ResponsavelLegal> lerLista() {
        ArrayList<ResponsavelLegal> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))) {
                lista = (ArrayList<ResponsavelLegal>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao ler lista de responsáveis legais: " + e.getMessage());
            }
        }
        return lista;
    }
    @Override
    public void adicionar(ResponsavelLegal novo) {
        ArrayList<ResponsavelLegal> lista = lerLista();
        lista.add(novo);
        salvarLista(lista);
    }
    @Override
    public void atualizar(String cpfAntigo, ResponsavelLegal atualizado){
        ArrayList<ResponsavelLegal> lista = new ArrayList<>();
        for( int i = 0; i < lista.size(); i++){
            if(lista.get(i).getCpf().equals(cpfAntigo)){
                lista.set(i, atualizado);
                break;
            }
        }
    }
    @Override
    public void excluir(String cpf){
        ArrayList<ResponsavelLegal> lista = lerLista();
        lista.removeIf(resp -> resp.getCpf().equals(cpf));
        salvarLista(lista);
    }
}