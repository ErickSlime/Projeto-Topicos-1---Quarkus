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

import br.unitins.topicos1.dto.MarcaDTO;
import br.unitins.topicos1.dto.MarcaResponseDTO;
import br.unitins.topicos1.model.Marca;
import br.unitins.topicos1.repository.MarcaRepository;

@ApplicationScoped
public class MarcaServiceImpl implements MarcaService {

    @Inject
    MarcaRepository marcaRepository;

    @Inject
    Validator validator;

    @Override
    public List<MarcaResponseDTO> getAll() {
        List<Marca> list = marcaRepository.listAll();
        return list.stream().map(MarcaResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public MarcaResponseDTO findById(Long id) {
        Marca marca = marcaRepository.findById(id);
        if (marca == null)
            throw new NotFoundException("Marca não encontrado.");
        return new MarcaResponseDTO(marca);
    }

    @Override
    @Transactional
    public MarcaResponseDTO create(MarcaDTO marcaDTO) throws ConstraintViolationException {
        validar(marcaDTO);

        Marca entity = new Marca();
        entity.setNome(marcaDTO.nome());
        entity.setCnpj(marcaDTO.cnpj());

        marcaRepository.persist(entity);

        return new MarcaResponseDTO(entity);
    }

    @Override
    @Transactional
    public MarcaResponseDTO update(Long id, MarcaDTO marcaDTO) throws ConstraintViolationException{
        validar(marcaDTO);
   
        Marca entity = marcaRepository.findById(id);

        entity.setNome(marcaDTO.nome());
        entity.setCnpj(marcaDTO.cnpj());

        return new MarcaResponseDTO(entity);
    }

    private void validar(MarcaDTO marcaDTO) throws ConstraintViolationException {
        Set<ConstraintViolation<MarcaDTO>> violations = validator.validate(marcaDTO);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);


    }

    @Override
    @Transactional
    public void delete(Long id) {
        marcaRepository.deleteById(id);
    }

    @Override
    public List<MarcaResponseDTO> findByNome(String nome) {
        List<Marca> list = marcaRepository.findByNome(nome);
        return list.stream().map(MarcaResponseDTO::new).collect(Collectors.toList());
    }

    @Override
    public long count() {
        return marcaRepository.count();
    }

}
