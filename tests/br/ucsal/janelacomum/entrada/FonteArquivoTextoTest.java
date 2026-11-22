package br.ucsal.janelacomum.entrada;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;
import br.ucsal.janelacomum.modelo.Janela;
import br.ucsal.janelacomum.modelo.ResultadoDaBusca;
import br.ucsal.janelacomum.nucleo.CalculadoraDeJanelas;
import br.ucsal.janelacomum.nucleo.Criterios;
import br.ucsal.janelacomum.nucleo.ServicoDeAgendamento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FonteArquivoTextoTest {

    @TempDir
    Path pasta;

    private FonteArquivoTexto fonteDe(Path arquivo) {
        return new FonteArquivoTexto(arquivo, new InterpretadorDeLinha());
    }

    private Path escrever(String conteudo) throws IOException {
        return Files.writeString(pasta.resolve("dados.txt"), conteudo);
    }

    @Test
    void ignoraComentariosELinhasEmBranco() throws IOException {
        Path arquivo = escrever("# comentário\n\nAna;QUA;19:00-21:00\n   \nBruno;QUA;19:00-21:00\n");

        List<Disponibilidade> lidas = fonteDe(arquivo).carregar();

        assertEquals(2, lidas.size());
    }

    @Test
    void erroInformaONumeroDaLinha() throws IOException {
        Path arquivo = escrever("Ana;QUA;19:00-21:00\n# comentário\nBruno;FERIADO;19:00-21:00\n");

        EntradaInvalidaException e = assertThrows(EntradaInvalidaException.class,
                () -> fonteDe(arquivo).carregar());

        assertTrue(e.getMessage().startsWith("Linha 3:"), e.getMessage());
    }

    @Test
    void arquivoInexistenteGeraErroClaro() {
        EntradaInvalidaException e = assertThrows(EntradaInvalidaException.class,
                () -> fonteDe(pasta.resolve("nao-existe.txt")).carregar());

        assertTrue(e.getMessage().contains("não foi possível ler"));
    }

    private ResultadoDaBusca buscarNoArquivoDeDemo(String nomeDoArquivo) {
        ServicoDeAgendamento servico = new ServicoDeAgendamento(
                fonteDe(Path.of("demo", nomeDoArquivo)), new CalculadoraDeJanelas(),
                Criterios.maisPessoasDepoisMaisLonga(), 60);
        return servico.buscar();
    }

    @Test
    void demoPrincipalTemDoisHorariosParaTodos() {
        ResultadoDaBusca r = buscarNoArquivoDeDemo("disponibilidades-demo.txt");

        assertEquals(6, r.participantes().size());
        assertTrue(r.existeJanelaComTodos());

        List<Janela> comTodos = r.janelas().stream().filter(Janela::todosPodem).toList();
        assertEquals(2, comTodos.size());
        assertEquals(DayOfWeek.WEDNESDAY, comTodos.get(0).dia());
        assertEquals(LocalTime.of(19, 0), comTodos.get(0).inicio());
        assertEquals(LocalTime.of(21, 0), comTodos.get(0).fim());
        assertEquals(DayOfWeek.SATURDAY, comTodos.get(1).dia());
        assertEquals(LocalTime.of(10, 0), comTodos.get(1).inicio());
        assertEquals(LocalTime.of(11, 30), comTodos.get(1).fim());
    }

    @Test
    void demoSemJanelaComumMostraAlternativasSemOFabio() {
        ResultadoDaBusca r = buscarNoArquivoDeDemo("disponibilidades-sem-janela-comum.txt");

        assertFalse(r.existeJanelaComTodos());
        Janela melhor = r.janelas().get(0);
        assertEquals(5, melhor.presentes().size());
        assertTrue(melhor.ausentes().contains("Fabio"));
    }

    @Test
    void demoInvalidaGeraErroNaLinhaDoProblema() {
        EntradaInvalidaException e = assertThrows(EntradaInvalidaException.class,
                () -> buscarNoArquivoDeDemo("disponibilidades-invalida.txt"));

        assertTrue(e.getMessage().startsWith("Linha 4:"), e.getMessage());
    }
}
