package br.ucsal.janelacomum.entrada;

import br.ucsal.janelacomum.modelo.Disponibilidade;
import br.ucsal.janelacomum.modelo.EntradaInvalidaException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FonteArquivoTexto implements FonteDeDisponibilidades {

    private final Path arquivo;
    private final InterpretadorDeLinha interpretador;

    public FonteArquivoTexto(Path arquivo, InterpretadorDeLinha interpretador) {
        this.arquivo = arquivo;
        this.interpretador = interpretador;
    }

    @Override
    public List<Disponibilidade> carregar() {
        List<String> linhas;
        try {
            linhas = Files.readAllLines(arquivo, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new EntradaInvalidaException(
                    "não foi possível ler o arquivo '" + arquivo + "' (ele existe e está em UTF-8?).", e);
        }

        List<Disponibilidade> disponibilidades = new ArrayList<>();
        for (int i = 0; i < linhas.size(); i++) {
            String linha = linhas.get(i).replace("\uFEFF", "").trim();
            if (linha.isEmpty() || linha.startsWith("#")) {
                continue;
            }
            try {
                disponibilidades.add(interpretador.interpretar(linha));
            } catch (EntradaInvalidaException e) {
                throw new EntradaInvalidaException("Linha " + (i + 1) + ": " + e.getMessage(), e);
            }
        }
        return disponibilidades;
    }
}
