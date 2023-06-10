package br.unitins.topicos1.model;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoBebida {
    ALCOOLICA(1, "Alcoolica"),
    SEMALCOOL(2, "Sem Alcool");

    private int id;
    private String label;

    TipoBebida(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public static TipoBebida valueOf(Integer id) throws IllegalArgumentException {
        if (id == null)
            return null;
        for (TipoBebida tipoBebida : TipoBebida.values()) {
            if (id.equals(tipoBebida.getId()))
                return tipoBebida;
        }
        throw new IllegalArgumentException("Id inválido:" + id);
    }

}
