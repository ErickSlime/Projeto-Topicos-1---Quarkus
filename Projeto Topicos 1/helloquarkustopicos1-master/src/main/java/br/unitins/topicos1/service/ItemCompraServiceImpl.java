package br.unitins.topicos1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.dto.ItemCompraDTO2;
import br.unitins.topicos1.dto.ItemCompraResponseDTO;
import br.unitins.topicos1.model.Bebida;
import br.unitins.topicos1.model.ItemCompra;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.repository.BebidaRepository;
import br.unitins.topicos1.repository.ItemCompraRepository;
import br.unitins.topicos1.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class ItemCompraServiceImpl implements ItemCompraService{
    
    @Inject
    ItemCompraRepository itemCompraRepository;

    @Inject
    BebidaRepository bebidaRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    Validator validator;

    @Override
    public List<ItemCompraResponseDTO> getAll() {
        List<ItemCompra> list = itemCompraRepository.listAll();
        return list.stream().map(ItemCompraResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<ItemCompraResponseDTO> getAllCarrinho(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        List<ItemCompra> list = itemCompraRepository.findItemCompraByUsuario(usuario);
        return list.stream().map(ItemCompraResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public ItemCompraResponseDTO findById(Long id) {
        ItemCompra ItemCompra = itemCompraRepository.findById(id);
        if (ItemCompra == null)
            throw new NotFoundException("ItemCompra não encontrada.");
        return new ItemCompraResponseDTO(ItemCompra);
    }

    @Override
    @Transactional
    public ItemCompraResponseDTO create2(Long idUsuario, ItemCompraDTO2 itemCompraDTO) throws ConstraintViolationException {

        ItemCompra entity = new ItemCompra();
        Bebida bebida = bebidaRepository.findById(itemCompraDTO.idProduto());
        if(itemCompraDTO.quant() > bebida.getEstoque()){
            throw new NotFoundException("Produto sem Estoque");
        }

        bebida.setEstoque(bebida.getEstoque()-itemCompraDTO.quant());

        entity.setQuant(itemCompraDTO.quant());
        entity.setProduto(bebidaRepository.findById(itemCompraDTO.idProduto()));
        entity.setUsuario(usuarioRepository.findById(idUsuario));
        entity.setTotalItem(entity.getProduto().getValor() * entity.getQuant());

        itemCompraRepository.persist(entity);

        return new ItemCompraResponseDTO(entity);
    }

    @Override
    @Transactional
    public void delete(Long idUsuario, Long idItem) {

        ItemCompra entity = itemCompraRepository.findById(idItem);
        Bebida bebida = bebidaRepository.findById(entity.getProduto().getId());

        if(entity.isIdComprado() == true){
            throw new NotAuthorizedException("Esse Item ja foi comprado");
        }

        if(entity.getUsuario().getId() == idUsuario){
            bebida.setEstoque(bebida.getEstoque() + entity.getQuant());
            itemCompraRepository.deleteById(idItem);
        }
        else
            throw new NotAuthorizedException("Você não pode excluir items de outros usuarios");
    }

    @Override
    public long count() {
        return itemCompraRepository.count();
    }

    @Override
    public long countItemCompra(Long id) throws NullPointerException {
        Usuario usuario = usuarioRepository.findById(id);
        List<ItemCompra> listaItens = new ArrayList<ItemCompra>();
        listaItens = itemCompraRepository.findItemCompraByUsuario(usuario);
        return listaItens.size();
    }
    }

