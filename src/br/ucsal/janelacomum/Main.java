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

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Main {

    private static final String ARQUIVO_PADRAO = "demo/disponibilidades-demo.txt";
    private static final int DURACAO_MINIMA_EM_MINUTOS = 60;
    private static final String LETRAS_ACENTUADAS = "áàâãéêíóôõúçÁÀÂÃÉÊÍÓÔÕÚÇ";

    public static void main(String[] args) {
        PrintStream saida = comAcentos(System.out, "stdout.encoding");
        PrintStream erro = comAcentos(System.err, "stderr.encoding");
        String caminho = args.length > 0 ? args[0] : ARQUIVO_PADRAO;

        FonteDeDisponibilidades fonte = new FonteArquivoTexto(Path.of(caminho), new InterpretadorDeLinha());
        ServicoDeAgendamento servico = new ServicoDeAgendamento(
                fonte, new CalculadoraDeJanelas(), Criterios.maisPessoasDepoisMaisLonga(),
                DURACAO_MINIMA_EM_MINUTOS);
        ApresentadorDeResultado apresentador = new ApresentadorConsole(saida);

        try {
            apresentador.apresentar(servico.buscar());
        } catch (EntradaInvalidaException e) {
            erro.println("ERRO: " + e.getMessage());
            System.exit(1);
        }
    }

    private static PrintStream comAcentos(PrintStream original, String propriedade) {
        Charset atual;
        try {
            String nome = System.getProperty(propriedade);
            atual = nome != null ? Charset.forName(nome) : Charset.defaultCharset();
        } catch (RuntimeException e) {
            return new PrintStream(original, true, StandardCharsets.UTF_8);
        }
        return atual.newEncoder().canEncode(LETRAS_ACENTUADAS)
                ? original
                : new PrintStream(original, true, StandardCharsets.UTF_8);
    }
}
