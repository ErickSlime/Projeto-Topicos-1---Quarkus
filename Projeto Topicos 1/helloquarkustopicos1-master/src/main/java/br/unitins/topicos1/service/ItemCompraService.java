package br.unitins.topicos1.service;

import java.util.List;

import br.unitins.topicos1.dto.ItemCompraDTO2;
import br.unitins.topicos1.dto.ItemCompraResponseDTO;

public interface ItemCompraService {
    
    // recursos basicos
    List<ItemCompraResponseDTO> getAll();

    ItemCompraResponseDTO findById(Long id);

    ItemCompraResponseDTO create2(Long idUsuario, ItemCompraDTO2 ItemCompraDTO);

    void delete(Long idUsuario, Long idItemCarrinho);

    long countItemCompra(Long id);

    public long count();

    public List<ItemCompraResponseDTO> getAllCarrinho(Long idUsuario);

}
