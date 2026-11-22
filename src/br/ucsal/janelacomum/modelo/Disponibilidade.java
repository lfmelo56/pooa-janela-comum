package br.ucsal.janelacomum.modelo;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Objects;

public record Disponibilidade(String nome, DayOfWeek dia, LocalTime inicio, LocalTime fim) {

    public static final int BLOCO_EM_MINUTOS = 30;

    public Disponibilidade {
        if (nome == null || nome.isBlank()) {
            throw new EntradaInvalidaException("o nome da pessoa não pode ficar vazio.");
        }
        Objects.requireNonNull(dia);
        Objects.requireNonNull(inicio);
        Objects.requireNonNull(fim);
        nome = nome.trim();

        if (!alinhadoAoBloco(inicio) || !alinhadoAoBloco(fim)) {
            throw new EntradaInvalidaException(
                    "os horários devem ser múltiplos de 30 minutos (ex.: 18:00 ou 18:30).");
        }
        if (!inicio.isBefore(fim)) {
            throw new EntradaInvalidaException("o horário final deve ser depois do inicial.");
        }
    }

    private static boolean alinhadoAoBloco(LocalTime horario) {
        return horario.getMinute() % BLOCO_EM_MINUTOS == 0
                && horario.getSecond() == 0
                && horario.getNano() == 0;
    }
}
