package br.unitins.topicos1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Endereco extends DefaultEntity {

    @Column(nullable = false, length = 120)
    private String logradouro;

    @Column(nullable = false, length = 40)
    private String bairro;

    @Column(nullable = false, length = 15)
    private String cep;

    @Column(nullable = false, length = 10)
    private String numero;

    @Column(nullable = false, length = 40)
    private String complemento;

    @ManyToOne
    @JoinColumn(name = "id_municipio")
    private Municipio municipio;

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    

}
