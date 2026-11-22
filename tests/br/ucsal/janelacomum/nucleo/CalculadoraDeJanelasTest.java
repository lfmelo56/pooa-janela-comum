package br.ucsal.janelacomum.nucleo;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.Janela;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculadoraDeJanelasTest {

    private final CalculadoraDeJanelas calculadora = new CalculadoraDeJanelas();

    private static Disponibilidade livre(String nome, DayOfWeek dia, String inicio, String fim) {
        return new Disponibilidade(nome, dia, LocalTime.parse(inicio), LocalTime.parse(fim));
    }

    @Test
    void encontraJanelaEmQueTodosPodem() {
        List<Janela> janelas = calculadora.calcular(List.of(
                livre("Ana", DayOfWeek.WEDNESDAY, "18:00", "21:00"),
                livre("Bruno", DayOfWeek.WEDNESDAY, "19:00", "22:00")), 60);

        assertEquals(1, janelas.size());
        Janela j = janelas.get(0);
        assertEquals(LocalTime.of(19, 0), j.inicio());
        assertEquals(LocalTime.of(21, 0), j.fim());
        assertEquals(120, j.duracaoEmMinutos());
        assertTrue(j.todosPodem());
    }

    @Test
    void descartaJanelaMaisCurtaQueOMinimo() {
        List<Janela> janelas = calculadora.calcular(List.of(
                livre("Ana", DayOfWeek.WEDNESDAY, "19:00", "19:30"),
                livre("Bruno", DayOfWeek.WEDNESDAY, "19:00", "19:30")), 60);

        assertTrue(janelas.isEmpty());
    }

    @Test
    void naoMisturaDiasDiferentes() {
        List<Janela> janelas = calculadora.calcular(List.of(
                livre("Ana", DayOfWeek.WEDNESDAY, "19:00", "21:00"),
                livre("Bruno", DayOfWeek.THURSDAY, "19:00", "21:00")), 60);

        assertTrue(janelas.isEmpty());
    }

    @Test
    void informaQuemEstaAusente() {
        List<Janela> janelas = calculadora.calcular(List.of(
                livre("Ana", DayOfWeek.WEDNESDAY, "19:00", "21:00"),
                livre("Bruno", DayOfWeek.WEDNESDAY, "19:00", "21:00"),
                livre("Carla", DayOfWeek.FRIDAY, "19:00", "21:00")), 60);

        assertEquals(1, janelas.size());
        assertEquals(Set.of("Ana", "Bruno"), janelas.get(0).presentes());
        assertEquals(Set.of("Carla"), janelas.get(0).ausentes());
    }

    @Test
    void separaJanelasQuandoOGrupoMuda() {

        List<Janela> janelas = calculadora.calcular(List.of(
                livre("Ana", DayOfWeek.MONDAY, "18:00", "22:00"),
                livre("Bruno", DayOfWeek.MONDAY, "18:00", "20:00"),
                livre("Carla", DayOfWeek.MONDAY, "20:00", "22:00")), 60);

        assertEquals(2, janelas.size());
        assertEquals(Set.of("Ana", "Bruno"), janelas.get(0).presentes());
        assertEquals(Set.of("Ana", "Carla"), janelas.get(1).presentes());
    }

    @Test
    void intervalosSobrepostosDaMesmaPessoaNaoDuplicam() {
        List<Janela> janelas = calculadora.calcular(List.of(
                livre("Ana", DayOfWeek.MONDAY, "18:00", "20:00"),
                livre("Ana", DayOfWeek.MONDAY, "19:00", "21:00"),
                livre("Bruno", DayOfWeek.MONDAY, "18:00", "21:00")), 60);

        assertEquals(1, janelas.size());
        assertEquals(180, janelas.get(0).duracaoEmMinutos());
    }
}
