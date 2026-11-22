package br.ucsal.janelacomum.entrada;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InterpretadorDeLinhaTest {

    private final InterpretadorDeLinha interpretador = new InterpretadorDeLinha();

    @Test
    void interpretaLinhaValida() {
        Disponibilidade d = interpretador.interpretar("Ana;QUA;19:00-21:00");

        assertEquals("Ana", d.nome());
        assertEquals(DayOfWeek.WEDNESDAY, d.dia());
        assertEquals(LocalTime.of(19, 0), d.inicio());
        assertEquals(LocalTime.of(21, 0), d.fim());
    }

    @Test
    void ignoraEspacosEAceitaDiaEmMinusculo() {
        Disponibilidade d = interpretador.interpretar(" Bruno ; qua ; 19:30-21:00 ");

        assertEquals("Bruno", d.nome());
        assertEquals(DayOfWeek.WEDNESDAY, d.dia());
        assertEquals(LocalTime.of(19, 30), d.inicio());
    }

    @Test
    void rejeitaDiaInvalido() {
        EntradaInvalidaException e = assertThrows(EntradaInvalidaException.class,
                () -> interpretador.interpretar("Ana;FERIADO;19:00-21:00"));
        assertTrue(e.getMessage().contains("FERIADO"));
    }

    @Test
    void rejeitaHorarioMalFormatado() {
        EntradaInvalidaException e = assertThrows(EntradaInvalidaException.class,
                () -> interpretador.interpretar("Ana;QUA;7:00-9:00"));
        assertTrue(e.getMessage().contains("7:00"));
    }

    @Test
    void rejeitaFimAntesDoInicio() {
        assertThrows(EntradaInvalidaException.class,
                () -> interpretador.interpretar("Ana;QUA;21:00-19:00"));
    }

    @Test
    void rejeitaHorarioForaDoBlocoDe30Minutos() {
        assertThrows(EntradaInvalidaException.class,
                () -> interpretador.interpretar("Ana;QUA;19:10-21:00"));
    }

    @Test
    void rejeitaLinhaIncompleta() {
        assertThrows(EntradaInvalidaException.class, () -> interpretador.interpretar("Ana;QUA"));
    }

    @Test
    void rejeitaNomeVazio() {
        assertThrows(EntradaInvalidaException.class,
                () -> interpretador.interpretar(" ;QUA;19:00-21:00"));
    }
}
