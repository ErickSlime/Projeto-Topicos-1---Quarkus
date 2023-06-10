package br.unitins.topicos1.converterjpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import br.unitins.topicos1.model.TipoBebida;

@Converter(autoApply = true)
public class BebidaConverter implements AttributeConverter<TipoBebida, Integer>{

    @Override
    public Integer convertToDatabaseColumn(TipoBebida bebida) {
        return bebida == null ? null : bebida.getId();
    }

    @Override
    public TipoBebida convertToEntityAttribute(Integer id) {
        return TipoBebida.valueOf(id);
    }
    
}
