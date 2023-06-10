package br.unitins.topicos1.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.NotFoundException;

import br.unitins.topicos1.dto.BebidaDTO;
import br.unitins.topicos1.dto.BebidaResponseDTO;
import br.unitins.topicos1.model.Bebida;
import br.unitins.topicos1.model.TipoBebida;
import br.unitins.topicos1.repository.BebidaRepository;
import br.unitins.topicos1.repository.MarcaRepository;

@ApplicationScoped
public class BebidaServiceImpl implements BebidaService {

    @Inject
    BebidaRepository bebidaRepository;

    @Inject
    MarcaRepository marcaRepository;

    @Inject
    Validator validator;

    @Override
    public List<BebidaResponseDTO> getAll() {
        List<Bebida> list = bebidaRepository.listAll();
        return list.stream().map(BebidaResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public BebidaResponseDTO findById(Long id) {
        Bebida bebida = bebidaRepository.findById(id);
        if (bebida == null)
            throw new NotFoundException("Bebida não encontrada.");
        return new BebidaResponseDTO(bebida);
    }

    @Override
    @Transactional
    public BebidaResponseDTO create(BebidaDTO bebidaDTO) throws ConstraintViolationException {
        validar(bebidaDTO);

        Bebida entity = new Bebida();
        entity.setNome(bebidaDTO.nome());
        entity.setMarca(marcaRepository.findById(bebidaDTO.idmarca()));
        entity.setDescricao(bebidaDTO.descricao());
        entity.setValor(bebidaDTO.valor());
        entity.setEstoque(bebidaDTO.estoque());
        entity.setTipoBebida(TipoBebida.valueOf(bebidaDTO.tipoBebida()));

        bebidaRepository.persist(entity);

        return new BebidaResponseDTO(entity);
    }

    @Override
    @Transactional
    public BebidaResponseDTO update(Long id, BebidaDTO bebidaDTO) throws ConstraintViolationException{
         validar(bebidaDTO);
   
        Bebida entity = bebidaRepository.findById(id);
        entity.setTipoBebida(TipoBebida.valueOf(bebidaDTO.tipoBebida()));
        entity.setNome(bebidaDTO.nome());
        entity.setMarca(marcaRepository.findById(bebidaDTO.idmarca()));
        entity.setDescricao(bebidaDTO.descricao());
        entity.setValor(bebidaDTO.valor());
        entity.setEstoque(bebidaDTO.estoque());
        

        return new BebidaResponseDTO(entity);
    }

    private void validar(BebidaDTO bebidaDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<BebidaDTO>> violations = validator.validate(bebidaDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);


    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Número inválido");

        bebidaRepository.deleteById(id);
    }

    @Override
    public List<BebidaResponseDTO> findByNome(String nome) {
        List<Bebida> list = bebidaRepository.findByNome(nome);
        return list.stream().map(BebidaResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return bebidaRepository.count();
    }

    @Override
    public List<BebidaResponseDTO> getByTipoBebida(Integer id) throws IndexOutOfBoundsException, NullPointerException {

        if (id < 1 || id > 2)
            throw new IndexOutOfBoundsException("número fora das opções");

        List<Bebida> list = bebidaRepository.findByTipoBebida(TipoBebida.valueOf(id));

        if (list == null)
            throw new NullPointerException("Nenhuma Bebida encontrada");

        return list.stream()
                    .map(BebidaResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    public List<BebidaResponseDTO> getByMarca(String nome) throws NullPointerException {

        List<Bebida> list = bebidaRepository.findByMarca(marcaRepository.findByNome(nome).get(0));

        if (list == null)
            throw new NullPointerException("Nenhuma marca encontrada");

        return list.stream()
                    .map(BebidaResponseDTO::new)
                    .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BebidaResponseDTO update(Long id, String nomeImagem) {
   
        Bebida entity = bebidaRepository.findById(id);
        entity.setNomeImagem(nomeImagem);

        return new BebidaResponseDTO(entity);
    }

}
