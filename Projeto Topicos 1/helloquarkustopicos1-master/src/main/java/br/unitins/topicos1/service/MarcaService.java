package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.MarcaDTO;
import br.unitins.topicos1.dto.MarcaResponseDTO;

public interface MarcaService {

    // recursos basicos
    List<MarcaResponseDTO> getAll();

    MarcaResponseDTO findById(Long id);

    MarcaResponseDTO create(MarcaDTO productDTO);

    MarcaResponseDTO update(Long id, MarcaDTO productDTO);

    void delete(Long id);

    // recursos extras

    List<MarcaResponseDTO> findByNome(String nome);

    long count();

}
