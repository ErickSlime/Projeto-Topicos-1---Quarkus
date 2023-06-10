package br.unitins.topicos1.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ItemCompra extends DefaultEntity {
    
    private int quant;
    private double totalItem;
    private boolean idComprado = false;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Bebida produto;

    public int getQuant() {
        return quant;
    }
    public void setQuant(int quant) {
        this.quant = quant;
    }
    public double getTotalItem() {
        return totalItem;
    }
    public void setTotalItem(double totalItem) {
        this.totalItem = totalItem;
    }
    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Bebida getProduto() {
        return produto;
    }
    public void setProduto(Bebida produto) {
        this.produto = produto;
    }
    public boolean isIdComprado() {
        return idComprado;
    }
    public void setIdComprado(boolean idComprado) {
        this.idComprado = idComprado;
    }
    
    

    
}
