package br.unitins.topicos1.repository;

import java.util.List;


// import java.util.List;

// import br.unitins.topicos1.model.Bebida;
import br.unitins.topicos1.model.Compra;
import br.unitins.topicos1.model.Usuario;
// import br.unitins.topicos1.model.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CompraRepository implements PanacheRepository<Compra> {

    public List<Compra> findComprasByIdUsuario(Usuario usuario){
        if (usuario == null)
            return null;
        return find("FROM Compra WHERE usuario = ?1",usuario).list();
}
}