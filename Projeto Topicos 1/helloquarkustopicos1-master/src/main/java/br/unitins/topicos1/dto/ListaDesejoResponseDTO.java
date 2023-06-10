package br.unitins.topicos1.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.unitins.topicos1.model.Produto;
import br.unitins.topicos1.model.Usuario;

public record ListaDesejoResponseDTO(
        Map<String, Object> usuario,
        List<Map<String, Object>> produtos) {

    public ListaDesejoResponseDTO(Usuario usuario) {

        this(encontrarUsuario(usuario.getId(), usuario.getLoginUsu(),
                usuario.getPessoaFisica().getEmail()), vizualizarProdutos(usuario.getBebidas()));
    }

    public static Map<String, Object> encontrarProduto(Long id, String nome) {

        Map<String, Object> produto = new HashMap<>();

        produto.put("id", id);
        produto.put("nome", nome);

        return produto;
    }

    public static Map<String, Object> encontrarUsuario(Long id, String nome, String email) {

        Map<String, Object> usuario = new HashMap<>();

        usuario.put("id", id);
        usuario.put("nome", nome);
        usuario.put("email", email);

        return usuario;
    }

    private static List<Map<String, Object>> vizualizarProdutos(List<Produto> lista) {

        List<Map<String, Object>> listaProdutos = new ArrayList<>();

        for (Produto produtos : lista) {

            Map<String, Object> produto = new HashMap<>();

            produto = encontrarProduto(produtos.getId(), produtos.getNome());

            listaProdutos.add(produto);
        }

        return listaProdutos;
    }
}
