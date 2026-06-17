package com.example.paceapp.model;

import java.io.*;
import java.util.ArrayList;

public class ArquivoUsuario implements Persistencia<Usuario>{
    private static final String CAMINHO_ARQUIVO = "usuarios.dat";

    @Override
    public void salvarLista(ArrayList<Usuario> lista){
        try{
            File arq = new File(CAMINHO_ARQUIVO);
            if(!arq.exists()) {
                arq.createNewFile();
            }
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(arq))){
                oos.writeObject(lista);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar a lista de usuários: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Usuario> lerLista() {
        ArrayList<Usuario> lista = new ArrayList<>();
        File arq = new File(CAMINHO_ARQUIVO);
        if (arq.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arq))){
                lista = (ArrayList<Usuario>) ois.readObject();
            } catch (IOException | ClassNotFoundException e){
                System.err.println("Erro ao ler lista de usuários: " + e.getMessage());
            }
        }
        return lista;
    }

    //Create
    @Override
    public void adicionar(Usuario novo) {
        ArrayList<Usuario> lista = lerLista();
        lista.add(novo);
        salvarLista(lista);
    }

    //Update
    @Override
    public void atualizar(String emailAntigo, Usuario atualizado){ //o emailAntigo é como se fosse o id de busca
        ArrayList<Usuario> lista = lerLista();
        for (int i = 0; i < lista.size(); i++){
            if(lista.get(i).getEmail().equalsIgnoreCase(emailAntigo)){
                lista.set(i, atualizado);
                break;
            }
        }
        salvarLista(lista);
    }

    //Delete
    @Override
    public void excluir(String email){
        ArrayList<Usuario> lista = lerLista();
        lista.removeIf(usuario -> usuario.getEmail().equalsIgnoreCase(email));
        salvarLista(lista);
    }


}
