package br.ucsal.janelacomum.modelo;

import java.util.List;
import java.util.Set;

public record ResultadoDaBusca(Set<String> participantes,
                               int duracaoMinimaEmMinutos,
                               List<Janela> janelas) {

    public boolean existeJanelaComTodos() {
        return janelas.stream().anyMatch(Janela::todosPodem);
    }
}
