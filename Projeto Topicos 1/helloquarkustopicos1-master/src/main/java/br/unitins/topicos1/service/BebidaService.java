package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.BebidaDTO;
import br.unitins.topicos1.dto.BebidaResponseDTO;

public interface BebidaService {

    // recursos basicos
    List<BebidaResponseDTO> getAll();

    BebidaResponseDTO findById(Long id);

    BebidaResponseDTO create(BebidaDTO bebidaDTO);

    BebidaResponseDTO update(Long id, BebidaDTO bebidaDTO);

    void delete(Long id);

    // recursos extras

    public BebidaResponseDTO update(Long id, String nomeImagem);

    List<BebidaResponseDTO> findByNome(String nome);

    List<BebidaResponseDTO> getByTipoBebida(Integer id);

    List<BebidaResponseDTO> getByMarca(String nome);

    long count();

}
