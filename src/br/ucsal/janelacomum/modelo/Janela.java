package br.ucsal.janelacomum.modelo;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public record Janela(DayOfWeek dia, LocalTime inicio, LocalTime fim,
                     Set<String> presentes, Set<String> ausentes) {

    public Janela {
        presentes = Collections.unmodifiableSet(new TreeSet<>(presentes));
        ausentes = Collections.unmodifiableSet(new TreeSet<>(ausentes));
    }

    public int duracaoEmMinutos() {
        return (int) Duration.between(inicio, fim).toMinutes();
    }

    public boolean todosPodem() {
        return ausentes.isEmpty();
    }
}
