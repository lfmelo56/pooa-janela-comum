package br.ucsal.janelacomum.nucleo;

import br.ucsal.janelacomum.entrada.FonteDeDisponibilidades;
import br.ucsal.janelacomum.modelo.Disponibilidade;

import java.util.List;

class FonteEmMemoria implements FonteDeDisponibilidades {

    private final List<Disponibilidade> disponibilidades;

    FonteEmMemoria(List<Disponibilidade> disponibilidades) {
        this.disponibilidades = disponibilidades;
    }

    @Override
    public List<Disponibilidade> carregar() {
        return disponibilidades;
    }
}
