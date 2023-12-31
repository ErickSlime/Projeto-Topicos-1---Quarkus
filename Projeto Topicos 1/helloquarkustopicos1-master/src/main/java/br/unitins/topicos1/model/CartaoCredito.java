package br.unitins.topicos1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CartaoCredito extends FormaPagamento {

    @Column(nullable = false)
    private String numeroCartao;

    @Column(nullable = false)
    private String nomeImpressoCartao;

    @Column(nullable = false)
    private String cpfTitular;

    private BandeiraCartao bandeiraCartao;

    public CartaoCredito(Double valor, String numeroCartao, String nomeImpressoCartao,
            String cpfTitular, BandeiraCartao bandeiraCartao) {

        super(valor);

        this.numeroCartao = numeroCartao;
        this.nomeImpressoCartao = nomeImpressoCartao;
        this.cpfTitular = cpfTitular;
        this.bandeiraCartao = bandeiraCartao;
    }

    public CartaoCredito() {

    }

    public String getNumeroDoCartao() {
        return numeroCartao;
    }

    public void setNumeroDoCartao(String numeroDoCartao) {
        this.numeroCartao = numeroDoCartao;
    }

    public String getNomeImpressoCartao() {
        return nomeImpressoCartao;
    }

    public void setNomeImpressoCartao(String nomeImpressoCartao) {
        this.nomeImpressoCartao = nomeImpressoCartao;
    }

    public String getCpfDoTitular() {
        return cpfTitular;
    }

    public void setCpfDoTitular(String cpfDoTitular) {
        this.cpfTitular = cpfDoTitular;
    }

    public BandeiraCartao getBandeiraCartao() {
        return bandeiraCartao;
    }

    public void setBandeiraCartao(BandeiraCartao bandeiraCartao) {
        this.bandeiraCartao = bandeiraCartao;
    }

}
