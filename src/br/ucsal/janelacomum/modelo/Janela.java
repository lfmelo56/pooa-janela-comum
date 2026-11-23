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
        presentes = Collections.unmodifiableSet(ordenado(presentes));
        ausentes = Collections.unmodifiableSet(ordenado(ausentes));
    }

    public int duracaoEmMinutos() {
        int minutos = (int) Duration.between(inicio, fim).toMinutes();
        return minutos > 0 ? minutos : minutos + 24 * 60;
    }

    public boolean todosPodem() {
        return ausentes.isEmpty();
    }

    private static Set<String> ordenado(Set<String> nomes) {
        Set<String> ordenado = new TreeSet<>(Disponibilidade.ORDEM_DOS_NOMES);
        ordenado.addAll(nomes);
        return ordenado;
    }
}
