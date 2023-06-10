package br.unitins.topicos1.repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;

import br.unitins.topicos1.model.PessoaFisica;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class PessoaFisicaRepository implements PanacheRepository<PessoaFisica> {

    public List<PessoaFisica> findByNome(String email) {
        if (email == null)
            return null;
        return find("UPPER(email) LIKE ?1 ", "%" + email.toUpperCase() + "%").list();
    }

}
