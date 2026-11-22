package br.ucsal.janelacomum.saida;

import br.ucsal.janelacomum.modelo.Janela;
import br.ucsal.janelacomum.modelo.ResultadoDaBusca;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ApresentadorConsoleTest {

    private String apresentar(ResultadoDaBusca resultado) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new ApresentadorConsole(new PrintStream(buffer, true, StandardCharsets.UTF_8)).apresentar(resultado);
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static Janela janela(Set<String> presentes, Set<String> ausentes) {
        return new Janela(DayOfWeek.WEDNESDAY, LocalTime.of(19, 0), LocalTime.of(21, 0), presentes, ausentes);
    }

    @Test
    void mostraHorarioQuandoTodosPodem() {
        String texto = apresentar(new ResultadoDaBusca(Set.of("Ana", "Bruno"), 60,
                List.of(janela(Set.of("Ana", "Bruno"), Set.of()))));

        assertTrue(texto.contains("TODOS podem"));
        assertTrue(texto.contains("Quarta-feira 19:00-21:00 (120 min)"));
    }

    @Test
    void mostraAlternativasEQuemFaltaQuandoNaoHaHorarioComum() {
        String texto = apresentar(new ResultadoDaBusca(Set.of("Ana", "Bruno", "Carla"), 60,
                List.of(janela(Set.of("Ana", "Bruno"), Set.of("Carla")))));

        assertTrue(texto.contains("Não há horário em comum para todos"));
        assertTrue(texto.contains("2 de 3 pessoas; falta: Carla"));
    }

    @Test
    void avisaQuandoNaoHaNenhumaJanela() {
        String texto = apresentar(new ResultadoDaBusca(Set.of("Ana", "Bruno"), 60, List.of()));

        assertTrue(texto.contains("Nenhuma janela encontrada"));
    }
}
