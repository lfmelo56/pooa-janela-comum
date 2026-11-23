package br.ucsal.janelacomum.modelo;

import java.text.Collator;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.Locale;

public record Disponibilidade(String nome, DayOfWeek dia, LocalTime inicio, LocalTime fim) {

    public static final int BLOCO_EM_MINUTOS = 30;
    public static final int BLOCOS_NO_DIA = 24 * 60 / BLOCO_EM_MINUTOS;
    public static final Comparator<String> ORDEM_DOS_NOMES = criarOrdemDosNomes();

    public Disponibilidade {
        if (nome == null || nome.isBlank()) {
            throw new EntradaInvalidaException("o nome da pessoa não pode ficar vazio.");
        }
        if (dia == null) {
            throw new EntradaInvalidaException("o dia da semana não pode ficar vazio.");
        }
        if (inicio == null || fim == null) {
            throw new EntradaInvalidaException("os horários não podem ficar vazios.");
        }
        nome = nome.trim();

        if (!alinhadoAoBloco(inicio) || !alinhadoAoBloco(fim)) {
            throw new EntradaInvalidaException(
                    "os horários devem ser múltiplos de 30 minutos (ex.: 18:00 ou 18:30).");
        }
        if (!inicio.isBefore(fim) && !fim.equals(LocalTime.MIDNIGHT)) {
            throw new EntradaInvalidaException("o horário final deve ser depois do inicial.");
        }
    }

    public int blocoInicial() {
        return inicio.toSecondOfDay() / 60 / BLOCO_EM_MINUTOS;
    }

    public int blocoFinal() {
        return fim.equals(LocalTime.MIDNIGHT) ? BLOCOS_NO_DIA : fim.toSecondOfDay() / 60 / BLOCO_EM_MINUTOS;
    }

    private static Comparator<String> criarOrdemDosNomes() {
        Collator collator = Collator.getInstance(Locale.forLanguageTag("pt-BR"));
        collator.setStrength(Collator.TERTIARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
        Comparator<String> porLetras = collator::compare;
        return porLetras.thenComparing(Comparator.naturalOrder());
    }

    private static boolean alinhadoAoBloco(LocalTime horario) {
        return horario.getMinute() % BLOCO_EM_MINUTOS == 0
                && horario.getSecond() == 0
                && horario.getNano() == 0;
    }
}
