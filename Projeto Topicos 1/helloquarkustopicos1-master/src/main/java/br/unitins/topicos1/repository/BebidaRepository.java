package br.unitins.topicos1.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import br.unitins.topicos1.model.Bebida;
import br.unitins.topicos1.model.Marca;
import br.unitins.topicos1.model.TipoBebida;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class BebidaRepository implements PanacheRepository<Bebida> {
    
    public List<Bebida> findByNome(String nome){
        if (nome == null)
            return null;
        return find("UPPER(nome) LIKE ?1 ", "%"+nome.toUpperCase()+"%").list();
    }

    public List<Bebida> findByTipoBebida (TipoBebida tipoBebida) {

        if (tipoBebida == null)
            return null;

        return find("FROM Bebida WHERE tipoBebida = ?1", tipoBebida).list();
    }

    public List<Bebida> findByMarca (Marca marca) {

        if (marca == null)
            return null;

        return find("FROM Bebida WHERE marca = ?1", marca).list();
    }
}
