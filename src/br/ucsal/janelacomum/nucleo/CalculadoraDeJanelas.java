package br.ucsal.janelacomum.nucleo;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.Janela;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CalculadoraDeJanelas {

    private static final int BLOCO = Disponibilidade.BLOCO_EM_MINUTOS;
    private static final int BLOCOS_NO_DIA = Disponibilidade.BLOCOS_NO_DIA;
    private static final int MINIMO_DE_PESSOAS = 2;

    public static void validarDuracaoMinima(int minutos) {
        if (minutos <= 0 || minutos % BLOCO != 0) {
            throw new IllegalArgumentException(
                    "a duração mínima deve ser positiva e múltipla de " + BLOCO + " minutos.");
        }
    }

    public List<Janela> calcular(List<Disponibilidade> disponibilidades, int duracaoMinimaEmMinutos) {
        validarDuracaoMinima(duracaoMinimaEmMinutos);
        Set<String> todos = new TreeSet<>(Disponibilidade.ORDEM_DOS_NOMES);
        disponibilidades.forEach(d -> todos.add(d.nome()));

        List<Janela> janelas = new ArrayList<>();
        for (DayOfWeek dia : DayOfWeek.values()) {
            List<Set<String>> livresPorBloco = livresPorBloco(disponibilidades, dia);
            janelas.addAll(juntarBlocos(dia, livresPorBloco, todos, duracaoMinimaEmMinutos));
        }
        return janelas;
    }

    private List<Set<String>> livresPorBloco(List<Disponibilidade> disponibilidades, DayOfWeek dia) {
        List<Set<String>> livres = new ArrayList<>();
        for (int i = 0; i < BLOCOS_NO_DIA; i++) {
            livres.add(new TreeSet<>(Disponibilidade.ORDEM_DOS_NOMES));
        }
        for (Disponibilidade d : disponibilidades) {
            if (d.dia() != dia) {
                continue;
            }
            for (int bloco = d.blocoInicial(); bloco < d.blocoFinal(); bloco++) {
                livres.get(bloco).add(d.nome());
            }
        }
        return livres;
    }

    private List<Janela> juntarBlocos(DayOfWeek dia, List<Set<String>> livres,
                                      Set<String> todos, int duracaoMinimaEmMinutos) {
        List<Janela> janelas = new ArrayList<>();
        int inicio = 0;
        while (inicio < BLOCOS_NO_DIA) {
            Set<String> grupo = livres.get(inicio);
            int fim = inicio;
            while (fim < BLOCOS_NO_DIA && livres.get(fim).equals(grupo)) {
                fim++;
            }

            int duracao = (fim - inicio) * BLOCO;
            if (grupo.size() >= MINIMO_DE_PESSOAS && duracao >= duracaoMinimaEmMinutos) {
                Set<String> ausentes = new TreeSet<>(Disponibilidade.ORDEM_DOS_NOMES);
                ausentes.addAll(todos);
                ausentes.removeAll(grupo);
                janelas.add(new Janela(dia, horarioDoBloco(inicio), horarioDoBloco(fim), grupo, ausentes));
            }
            inicio = fim;
        }
        return janelas;
    }

    private LocalTime horarioDoBloco(int bloco) {
        return LocalTime.MIDNIGHT.plusMinutes((long) bloco * BLOCO);
    }
}
