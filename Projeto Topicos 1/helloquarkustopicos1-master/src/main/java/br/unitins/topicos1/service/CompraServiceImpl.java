package br.unitins.topicos1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.unitins.topicos1.dto.CartaoCreditoDTO;
import br.unitins.topicos1.dto.CompraResponseDTO;
import br.unitins.topicos1.model.BandeiraCartao;
import br.unitins.topicos1.model.CartaoCredito;
import br.unitins.topicos1.model.Compra;
import br.unitins.topicos1.model.ItemCompra;
import br.unitins.topicos1.model.Pix;
import br.unitins.topicos1.model.Usuario;
import br.unitins.topicos1.repository.BebidaRepository;
import br.unitins.topicos1.repository.CartaoCreditoRepository;
import br.unitins.topicos1.repository.CompraRepository;
import br.unitins.topicos1.repository.ItemCompraRepository;
import br.unitins.topicos1.repository.PixRepository;
import br.unitins.topicos1.repository.UsuarioRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

@ApplicationScoped
public class CompraServiceImpl implements CompraService {

    @Inject
    ItemCompraRepository itemCompraRepository;

    @Inject
    ItemCompraService itemCompraService;

    @Inject
    CartaoCreditoRepository cartaoCreditoRepository;

    @Inject
    CompraRepository compraRepository;

    @Inject
    PixRepository pixRepository;

    @Inject
    BebidaRepository bebidaRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    Validator validator;

    @Override
    public List<CompraResponseDTO> getAll() {
        List<Compra> list = compraRepository.listAll();
        return list.stream().map(CompraResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public List<CompraResponseDTO> getAllUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        List<Compra> list = compraRepository.findComprasByIdUsuario(usuario);
        return list.stream().map(CompraResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public CompraResponseDTO findById(Long id) {
        Compra compra = compraRepository.findById(id);
        if (compra == null)
            throw new NotFoundException("compra não encontrada.");
        return new CompraResponseDTO(compra);
    }

    @Override
    @Transactional
    public CompraResponseDTO comprarItensPix(Long idUsuario) {
        double total = 0;
        Compra entity = new Compra();
        Usuario usuario = usuarioRepository.findById(idUsuario);
        List<ItemCompra> listaItens = new ArrayList<ItemCompra>();
        listaItens = itemCompraRepository.findItemCompraByUsuario(usuario);

        if(listaItens.isEmpty() == true) throw new NotFoundException("sem itens");

        if(usuario.getEndereco() == null || usuario.getTelefone() == null || usuario.getPessoaFisica() == null) throw new NotFoundException("Complete seu cadastro");

        for(int i=0; i<listaItens.size();i++){
            total = total + listaItens.get(i).getTotalItem();
            listaItens.get(i).setIdComprado(true);
        }

        entity.setListaDeItens(listaItens);
        entity.setTotalCompra(total);
        entity.setUsuario(usuarioRepository.findById(idUsuario));

        Pix pagamento = new Pix(entity.getTotalCompra(), entity.getUsuario().getPessoaFisica().getNome(), entity.getUsuario().getPessoaFisica().getCpf());
        pixRepository.persist(pagamento);

        entity.setPagamento(pagamento);
        compraRepository.persist(entity);

        return new CompraResponseDTO(entity);
    }

    @Override
    @Transactional
    public CompraResponseDTO comprarItensCartaoCredito(Long idUsuario, CartaoCreditoDTO cartaoCreditoDTO) {
        double total = 0;
        Compra entity = new Compra();
        Usuario usuario = usuarioRepository.findById(idUsuario);
        List<ItemCompra> listaItens = new ArrayList<ItemCompra>();
        listaItens = itemCompraRepository.findItemCompraByUsuario(usuario);

        if(listaItens.isEmpty() == true) throw new NotFoundException("sem itens");

        for(int i=0; i<listaItens.size();i++){
            total = total + listaItens.get(i).getTotalItem();
            listaItens.get(i).setIdComprado(true);
        }

        entity.setListaDeItens(listaItens);
        entity.setTotalCompra(total);
        entity.setUsuario(usuarioRepository.findById(idUsuario));

        CartaoCredito pagamento = new CartaoCredito(entity.getTotalCompra(),
                                            cartaoCreditoDTO.numeroCartao(),
                                            cartaoCreditoDTO.nomeImpressoCartao(),
                                            usuario.getPessoaFisica().getCpf(),
                                            BandeiraCartao.valueOf(cartaoCreditoDTO.bandeiraCartao()));
        
        cartaoCreditoRepository.persist(pagamento);

        entity.setPagamento(pagamento);
        compraRepository.persist(entity);

        return new CompraResponseDTO(entity);
    }
    
}
