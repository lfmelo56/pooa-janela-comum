package br.ucsal.janelacomum.nucleo;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;
import br.ucsal.janelacomum.modelo.ResultadoDaBusca;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServicoDeAgendamentoTest {

    private static Disponibilidade livre(String nome, DayOfWeek dia, String inicio, String fim) {
        return new Disponibilidade(nome, dia, LocalTime.parse(inicio), LocalTime.parse(fim));
    }

    private ServicoDeAgendamento servicoCom(List<Disponibilidade> dados) {
        return new ServicoDeAgendamento(new FonteEmMemoria(dados), new CalculadoraDeJanelas(),
                Criterios.maisPessoasDepoisMaisLonga(), 60);
    }

    @Test
    void janelaComTodosVemAntesDeJanelaMaisLongaComMenosGente() {

        ResultadoDaBusca r = servicoCom(List.of(
                livre("Ana", DayOfWeek.MONDAY, "18:00", "21:00"),
                livre("Bruno", DayOfWeek.MONDAY, "18:00", "21:00"),
                livre("Ana", DayOfWeek.WEDNESDAY, "19:00", "20:00"),
                livre("Bruno", DayOfWeek.WEDNESDAY, "19:00", "20:00"),
                livre("Carla", DayOfWeek.WEDNESDAY, "19:00", "20:00"))).buscar();

        assertTrue(r.existeJanelaComTodos());
        assertEquals(DayOfWeek.WEDNESDAY, r.janelas().get(0).dia());
        assertTrue(r.janelas().get(0).todosPodem());
    }

    @Test
    void semHorarioComumOMelhorAindaTemMaisGente() {
        ResultadoDaBusca r = servicoCom(List.of(
                livre("Ana", DayOfWeek.MONDAY, "18:00", "20:00"),
                livre("Bruno", DayOfWeek.MONDAY, "18:00", "20:00"),
                livre("Carla", DayOfWeek.TUESDAY, "18:00", "20:00"))).buscar();

        assertFalse(r.existeJanelaComTodos());
        assertEquals(Set.of("Ana", "Bruno"), r.janelas().get(0).presentes());
    }

    @Test
    void resultadoListaOsParticipantes() {
        ResultadoDaBusca r = servicoCom(List.of(
                livre("Ana", DayOfWeek.MONDAY, "18:00", "20:00"),
                livre("Bruno", DayOfWeek.MONDAY, "18:00", "20:00"))).buscar();

        assertEquals(Set.of("Ana", "Bruno"), r.participantes());
        assertEquals(60, r.duracaoMinimaEmMinutos());
    }

    @Test
    void exigeAoMenosDuasPessoas() {
        ServicoDeAgendamento servico = servicoCom(List.of(
                livre("Ana", DayOfWeek.MONDAY, "18:00", "20:00")));

        assertThrows(EntradaInvalidaException.class, servico::buscar);
    }
}
