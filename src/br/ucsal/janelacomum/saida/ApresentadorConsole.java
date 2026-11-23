package br.ucsal.janelacomum.saida;

import br.ucsal.janelacomum.modelo.Janela;
import br.ucsal.janelacomum.modelo.ResultadoDaBusca;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class ApresentadorConsole implements ApresentadorDeResultado {

    private static final int MAXIMO_DE_ALTERNATIVAS = 3;

    private final PrintStream saida;

    public ApresentadorConsole(PrintStream saida) {
        this.saida = saida;
    }

    @Override
    public void apresentar(ResultadoDaBusca resultado) {
        saida.println("=== JanelaComum - horários em comum ===");
        saida.println("Participantes considerados (" + resultado.participantes().size() + "): "
                + String.join(", ", resultado.participantes()));
        saida.println("Duração mínima da janela: " + resultado.duracaoMinimaEmMinutos() + " minutos");
        saida.println();

        if (resultado.janelas().isEmpty()) {
            saida.println("Nenhuma janela encontrada: não há dois ou mais participantes livres "
                    + "ao mesmo tempo pelo tempo mínimo.");
            return;
        }

        if (resultado.existeJanelaComTodos()) {
            saida.println("Horários em que TODOS podem:");
            List<Janela> comTodos = resultado.janelas().stream().filter(Janela::todosPodem).toList();
            imprimir(comTodos, false);
        } else {
            saida.println("Não há horário em comum para todos. Melhores alternativas:");
            List<Janela> melhores = resultado.janelas().stream().limit(MAXIMO_DE_ALTERNATIVAS).toList();
            imprimir(melhores, true);
        }
    }

    private void imprimir(List<Janela> janelas, boolean mostrarAusentes) {
        int numero = 1;
        for (Janela j : janelas) {
            String linha = " " + numero + ") " + nomeDoDia(j.dia()) + " " + j.inicio() + "-" + horaFinal(j.fim())
                    + " (" + j.duracaoEmMinutos() + " min)";
            if (mostrarAusentes) {
                int total = j.presentes().size() + j.ausentes().size();
                linha += " - " + j.presentes().size() + " de " + total + " pessoas; "
                        + (j.ausentes().size() == 1 ? "falta: " : "faltam: ")
                        + String.join(", ", j.ausentes());
            }
            saida.println(linha);
            numero++;
        }
    }

    private String horaFinal(LocalTime fim) {
        return fim.equals(LocalTime.MIDNIGHT) ? "24:00" : fim.toString();
    }

    private String nomeDoDia(DayOfWeek dia) {
        return switch (dia) {
            case MONDAY -> "Segunda-feira";
            case TUESDAY -> "Terça-feira";
            case WEDNESDAY -> "Quarta-feira";
            case THURSDAY -> "Quinta-feira";
            case FRIDAY -> "Sexta-feira";
            case SATURDAY -> "Sábado";
            case SUNDAY -> "Domingo";
        };
    }
}
