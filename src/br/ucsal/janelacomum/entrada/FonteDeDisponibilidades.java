package br.ucsal.janelacomum.entrada;

import br.ucsal.janelacomum.modelo.Disponibilidade;

import java.util.List;

public interface FonteDeDisponibilidades {

    List<Disponibilidade> carregar();
}
