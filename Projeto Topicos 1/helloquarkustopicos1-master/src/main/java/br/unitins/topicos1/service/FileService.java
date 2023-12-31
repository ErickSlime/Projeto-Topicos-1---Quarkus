package br.unitins.topicos1.service;

import java.io.File;
import java.io.IOException;

public interface FileService {
    String salvarImagemUsuario(byte[] imagem, String nomeImagem) throws IOException;
    
    File download(String nomeArquivo); 

    public String salvarImagemBebida(byte[] imagem, String nomeImagem) throws IOException;

    public File downloadBebida(String nomeArquivo);
}
