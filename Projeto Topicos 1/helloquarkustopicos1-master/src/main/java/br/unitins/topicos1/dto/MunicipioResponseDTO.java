package br.unitins.topicos1.dto;

import java.util.HashMap;
import java.util.Map;

import br.unitins.topicos1.model.Municipio;

public record MunicipioResponseDTO (
    Long id,
    String nome,
    Map<String, Object> estado

) {
    public MunicipioResponseDTO(Municipio municipio) {
        this(municipio.getId(),municipio.getNome(),verEstado(municipio.getEstado().getNome())); 
    }

    public static Map<String, Object> verEstado(String nome) {

        Map<String, Object> estado = new HashMap<>();

        estado.put("nome", nome);

        return estado;
    }

}