package br.ucsal.janelacomum.nucleo;

import br.ucsal.janelacomum.modelo.Janela;

import java.util.Comparator;

public final class Criterios {

    private Criterios() {
    }

    public static Comparator<Janela> maisPessoasDepoisMaisLonga() {
        return Comparator.comparingInt((Janela j) -> j.presentes().size()).reversed()
                .thenComparing(Comparator.comparingInt(Janela::duracaoEmMinutos).reversed())
                .thenComparing(Janela::dia)
                .thenComparing(Janela::inicio);
    }
}
