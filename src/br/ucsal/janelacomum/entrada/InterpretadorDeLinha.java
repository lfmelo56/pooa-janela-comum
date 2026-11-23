package br.ucsal.janelacomum.entrada;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class InterpretadorDeLinha {

    public Disponibilidade interpretar(String linha) {
        String[] partes = linha.split(";", -1);
        if (partes.length != 3) {
            throw new EntradaInvalidaException(
                    "use o formato Nome;DIA;HH:mm-HH:mm (ex.: Ana;QUA;19:00-21:00).");
        }

        String[] horas = partes[2].replace('\u2013', '-').replace('\u2014', '-').split("-", -1);
        if (horas.length != 2) {
            throw new EntradaInvalidaException(
                    "o intervalo deve ser HH:mm-HH:mm (ex.: 19:00-21:00).");
        }

        return new Disponibilidade(
                partes[0],
                interpretarDia(partes[1]),
                interpretarHora(horas[0], false),
                interpretarHora(horas[1], true));
    }

    private DayOfWeek interpretarDia(String texto) {
        return switch (texto.trim().toUpperCase()) {
            case "SEG" -> DayOfWeek.MONDAY;
            case "TER" -> DayOfWeek.TUESDAY;
            case "QUA" -> DayOfWeek.WEDNESDAY;
            case "QUI" -> DayOfWeek.THURSDAY;
            case "SEX" -> DayOfWeek.FRIDAY;
            case "SAB", "SÁB" -> DayOfWeek.SATURDAY;
            case "DOM" -> DayOfWeek.SUNDAY;
            default -> throw new EntradaInvalidaException(
                    "dia '" + texto.trim() + "' inválido. Use SEG, TER, QUA, QUI, SEX, SAB ou DOM.");
        };
    }

    private LocalTime interpretarHora(String texto, boolean ehHorarioFinal) {
        String horario = texto.trim();
        if (ehHorarioFinal && horario.equals("24:00")) {
            return LocalTime.MIDNIGHT;
        }
        try {
            return LocalTime.parse(horario);
        } catch (DateTimeParseException e) {
            throw new EntradaInvalidaException(
                    "horário '" + horario + "' inválido. Use HH:mm, ex.: 08:30.");
        }
    }
}
