package br.ucsal.janelacomum.nucleo;

import br.ucsal.janelacomum.entrada.FonteDeDisponibilidades;
import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;
import br.ucsal.janelacomum.modelo.Janela;
import br.ucsal.janelacomum.modelo.ResultadoDaBusca;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ServicoDeAgendamento {

    private final FonteDeDisponibilidades fonte;
    private final CalculadoraDeJanelas calculadora;
    private final Comparator<Janela> criterio;
    private final int duracaoMinimaEmMinutos;

    public ServicoDeAgendamento(FonteDeDisponibilidades fonte, CalculadoraDeJanelas calculadora,
                                Comparator<Janela> criterio, int duracaoMinimaEmMinutos) {
        CalculadoraDeJanelas.validarDuracaoMinima(duracaoMinimaEmMinutos);
        this.fonte = fonte;
        this.calculadora = calculadora;
        this.criterio = criterio;
        this.duracaoMinimaEmMinutos = duracaoMinimaEmMinutos;
    }

    public ResultadoDaBusca buscar() {
        List<Disponibilidade> disponibilidades = fonte.carregar();

        Set<String> participantes = new TreeSet<>(Disponibilidade.ORDEM_DOS_NOMES);
        disponibilidades.forEach(d -> participantes.add(d.nome()));
        if (participantes.size() < 2) {
            throw new EntradaInvalidaException("informe a disponibilidade de pelo menos 2 pessoas.");
        }

        List<Janela> janelas = new ArrayList<>(calculadora.calcular(disponibilidades, duracaoMinimaEmMinutos));
        janelas.sort(criterio);
        return new ResultadoDaBusca(participantes, duracaoMinimaEmMinutos, janelas);
    }
}
