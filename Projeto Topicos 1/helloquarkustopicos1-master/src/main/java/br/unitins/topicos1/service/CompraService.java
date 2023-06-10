package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.CartaoCreditoDTO;
import br.unitins.topicos1.dto.CompraResponseDTO;

public interface CompraService {
    // recursos basicos
    List<CompraResponseDTO> getAll();

    CompraResponseDTO findById(Long id);

    CompraResponseDTO comprarItensPix(Long idUsuario);

    CompraResponseDTO comprarItensCartaoCredito(Long idUsuario, CartaoCreditoDTO cartaoCreditoDTO);

    public List<CompraResponseDTO> getAllUsuario(Long idUsuario);

}
