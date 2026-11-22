package br.ucsal.janelacomum;

import br.ucsal.janelacomum.entrada.FonteArquivoTexto;
import br.ucsal.janelacomum.entrada.FonteDeDisponibilidades;
import br.ucsal.janelacomum.entrada.InterpretadorDeLinha;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;
import br.ucsal.janelacomum.nucleo.CalculadoraDeJanelas;
import br.ucsal.janelacomum.nucleo.Criterios;
import br.ucsal.janelacomum.nucleo.ServicoDeAgendamento;
import br.ucsal.janelacomum.saida.ApresentadorConsole;
import br.ucsal.janelacomum.saida.ApresentadorDeResultado;

import java.nio.file.Path;

public class Main {

    private static final String ARQUIVO_PADRAO = "demo/disponibilidades-demo.txt";
    private static final int DURACAO_MINIMA_EM_MINUTOS = 60;

    public static void main(String[] args) {
        String caminho = args.length > 0 ? args[0] : ARQUIVO_PADRAO;

        FonteDeDisponibilidades fonte = new FonteArquivoTexto(Path.of(caminho), new InterpretadorDeLinha());
        ServicoDeAgendamento servico = new ServicoDeAgendamento(
                fonte, new CalculadoraDeJanelas(), Criterios.maisPessoasDepoisMaisLonga(),
                DURACAO_MINIMA_EM_MINUTOS);
        ApresentadorDeResultado apresentador = new ApresentadorConsole(System.out);

        try {
            apresentador.apresentar(servico.buscar());
        } catch (EntradaInvalidaException e) {
            System.err.println("ERRO: " + e.getMessage());
            System.exit(1);
        }
    }
}
