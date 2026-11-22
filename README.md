# JanelaComum

**Descobre em quais horários um grupo inteiro consegue se reunir.** Cada pessoa informa quando está livre e o programa mostra as janelas em comum; se não existir horário para todos, mostra as melhores alternativas e quem ficaria de fora.

Projeto do **PooaLAB 2026.2**: Programação Orientada a Objetos Avançada, UCSAL (professor Mario Jorge Pereira).

Repositório: https://github.com/lfmelo56/pooa-janela-comum

## Integrantes

| Nome completo | Turma |
|---|---|
| Luís Felipe Brito de Melo | ESW-NOT-PIT-4S-T1 |
| Davi Brito Araujo Amaral dos Santos | ESW-NOT-PIT-4S-T1 |
| Elder Galvão Quirino Ribeiro | ESW-NOT-PIT-4S-T1 |
| Andre Santos de Sao Pedro | ESW-NOT-PIT-4S-T1 |
| João Victor dos Santos Lima | ESW-NOT-PIT-4S-T1 |
| Daiana Cristina Boudoux Pimentel Gomes | ESW-NOT-PIT-4S-T1 |

## Problema, público e proposta de valor

- **Público:** público geral que faz algum trabalho em grupo: nas disciplinas acadêmicas, em empregos e/ou em atividades pessoais. O foco é o contexto acadêmico (trabalhos em equipe com entrega e apresentação).
- **Problema:** combinar um horário de reunião em que todos possam participar.
- **Evidência (7 entrevistas, 22/11/2026):** todos apontaram dificuldade de conciliar agendas ou de fazer o grupo responder. Entre os 6 que chegaram a marcar horário, o tempo variou de cerca de 10 minutos a 3–4 dias (4 deles levaram 30 minutos ou mais). Detalhes em `docs/problema-validacao.docx`.
- **Como resolvem hoje:** conversa em sala e grupos de WhatsApp.
- **Proposta de valor:** em vez de trocar mensagens até todos baterem, cada um informa sua disponibilidade uma vez e o programa calcula o resultado em segundos.

## Tecnologias e justificativas

| Tecnologia | Por quê |
|---|---|
| Java 17+ | Linguagem da disciplina; `record`, `switch` com expressão e `Stream` deixam o código curto e legível |
| Maven | Padroniza compilação e testes; o IntelliJ importa o `pom.xml` direto |
| JUnit 5 | Testes automatizados do fluxo principal (única dependência, só em testes) |
| Console + arquivo de texto | Mantém o MVP simples e totalmente reproduzível, sem internet nem cadastro |

## Pré-requisitos

- **JDK 17 ou superior** (testado com JDK 21)
- **IntelliJ IDEA** (Community serve) ou **Maven 3.9+** no terminal

## Como abrir e executar

### No IntelliJ
1. Descompacte o projeto.
2. `File > Open...` e selecione o arquivo **`pom.xml`** (escolha *Open as Project*). Aguarde o Maven baixar o JUnit.
3. Confira o JDK em `File > Project Structure > Project` (17 ou superior).
4. Abra `src/br/ucsal/janelacomum/Main.java` e clique no triângulo verde ao lado de `main` > *Run 'Main.main()'*.

O programa usa, por padrão, `demo/disponibilidades-demo.txt` (o diretório de trabalho é a raiz do projeto).

### No terminal (sem Maven)
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src -name "*.java")      # Linux/macOS
java -cp out br.ucsal.janelacomum.Main
java -cp out br.ucsal.janelacomum.Main demo/disponibilidades-sem-janela-comum.txt
```
No Windows (PowerShell), troque a compilação por:
`javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src -Filter *.java).FullName`

## Como executar os testes

- **IntelliJ:** clique com o botão direito na pasta `tests` > *Run 'All Tests'*.
- **Terminal:** `mvn test`

São 27 testes: validação das linhas, cálculo das janelas, ordenação, leitura de arquivo, mensagens do console e os três arquivos de demonstração.

## Formato do arquivo de entrada

Uma linha por intervalo livre: `Nome;DIA;HH:mm-HH:mm`

```
Ana;QUA;19:00-21:00
Bruno;QUA;19:30-22:00
```

`DIA` = SEG, TER, QUA, QUI, SEX, SAB ou DOM. Horários em múltiplos de 30 minutos. Linhas em branco e linhas iniciadas por `#` são ignoradas.

## Dados de demonstração (todos fictícios)

| Arquivo | O que demonstra |
|---|---|
| `demo/disponibilidades-demo.txt` | 6 pessoas **com** horário em comum |
| `demo/disponibilidades-sem-janela-comum.txt` | 6 pessoas **sem** horário em comum: mostra as melhores alternativas |
| `demo/disponibilidades-invalida.txt` | Erro de propósito: o programa indica a linha com problema |

## Roteiro para reproduzir o fluxo principal

1. Execute `Main` sem argumentos. Resultado esperado:
   ```
   Horários em que TODOS podem:
    1) Quarta-feira 19:00-21:00 (120 min)
    2) Sábado 10:00-11:30 (90 min)
   ```
2. Execute com `demo/disponibilidades-sem-janela-comum.txt`. Resultado esperado: *"Não há horário em comum para todos"* e alternativas com 5 de 6 pessoas (falta: Fabio).
3. Execute com `demo/disponibilidades-invalida.txt`. Resultado esperado: `ERRO: Linha 4: dia 'FERIADO' inválido...`

## Vídeo de contingência

Gravação curta da demonstração do fluxo principal, usada como apoio caso a execução ao vivo falhe. Ela não substitui o programa executável.

- Vídeo: https://drive.google.com/file/d/1OEEFZaBheqjKIyohet15_pdklGcK9KaG/view?usp=sharing

## Estrutura do projeto

```text
README.md
pom.xml
src/br/ucsal/janelacomum/
  Main.java                       liga as peças e executa
  modelo/                         dados: Disponibilidade, Janela, ResultadoDaBusca, EntradaInvalidaException
  entrada/                        FonteDeDisponibilidades (interface), FonteArquivoTexto, InterpretadorDeLinha
  nucleo/                         CalculadoraDeJanelas, Criterios, ServicoDeAgendamento
  saida/                          ApresentadorDeResultado (interface), ApresentadorConsole
tests/                            testes JUnit (mesma estrutura de pacotes)
docs/                             documentação da entrega
demo/                             dados de demonstração
```

## SOLID: como foi aplicado e por que importa em projetos reais

| Princípio | Onde aparece neste projeto |
|---|---|
| **S**: Responsabilidade única | `InterpretadorDeLinha` só entende uma linha; `FonteArquivoTexto` só percorre o arquivo; `CalculadoraDeJanelas` só calcula; `ApresentadorConsole` só imprime; `ServicoDeAgendamento` só coordena |
| **O**: Aberto/fechado | A regra de ordenação é um `Comparator<Janela>` recebido pelo serviço. Para preferir horários noturnos, cria-se um novo critério em `Criterios` sem editar o serviço nem a calculadora |
| **L**: Substituição de Liskov | Qualquer `FonteDeDisponibilidades` pode entrar no lugar de outra. Os testes usam `FonteEmMemoria` no lugar de `FonteArquivoTexto` e o serviço funciona igual |
| **I**: Segregação de interfaces | Duas interfaces pequenas, de um método cada (`carregar()` e `apresentar()`). Nenhuma classe é obrigada a implementar o que não usa |
| **D**: Inversão de dependência | `ServicoDeAgendamento` depende da interface `FonteDeDisponibilidades`, não do arquivo. Só o `Main` conhece as classes concretas e as monta |

**Por que isso importa fora da sala de aula:**

- **Manutenção custa mais que a criação.** A maior parte da vida de um software é alterar o que já existe. Com responsabilidades separadas (S), um erro de leitura se corrige na classe de leitura, sem risco de quebrar o cálculo.
- **Requisitos mudam.** Hoje os dados vêm de um arquivo; amanhã podem vir de um formulário ou de um calendário. Com D e L, troca-se a fonte sem tocar na lógica já testada. Com O, acrescentam-se regras novas sem reescrever as antigas.
- **Testabilidade.** Código que depende de abstrações é testado sem arquivos, rede ou banco (como `FonteEmMemoria`). Testes rápidos e confiáveis permitem alterar o sistema com segurança.
- **Trabalho em equipe.** Peças pequenas e independentes permitem que várias pessoas trabalhem em paralelo com menos conflitos e que um recém-chegado entenda uma classe sem ler o sistema inteiro.
- **Custo de mudança.** Sem esses princípios, cada alteração tende a se espalhar por vários arquivos, e o custo de cada nova funcionalidade cresce com o tempo.

Foi evitado o excesso: só há interface onde existe variação real (fonte de dados e apresentação).

## Limitações conhecidas e próximos passos

**Limitações**

1. Considera só o dia da semana, sem datas específicas.
2. Horários em blocos de 30 minutos; não aceita intervalos que passam da meia-noite; não trata fusos horários.
3. Só mostra janelas de pelo menos 60 minutos e com 2 ou mais pessoas (valores fixos no código) e exibe no máximo 3 alternativas.
4. A entrada é só por arquivo de texto no formato exato e a saída só no console, sem tela nem integração com calendários ou WhatsApp.
5. Quem não aparece no arquivo não é considerado: o programa pode indicar um horário "para todos" sem essa pessoa. Os nomes diferenciam maiúsculas de minúsculas ("Ana" e "ana" viram duas pessoas).
6. A validação do problema foi feita com 7 pessoas, 3 com vínculo pessoal com a equipe, em uma única data (22/11/2026). A solução ainda não foi testada com usuários.

**Próximos passos**

- Coletar os horários por formulário online, no lugar do arquivo de texto.
- Interface gráfica para ver o resultado.
- Aceitar datas específicas e tratar nomes sem diferenciar maiúsculas de minúsculas.
- Testar a solução com usuários e registrar o retorno.

## Fontes, licenças, componentes de terceiros e uso de IA

- **JUnit 5** (Eclipse Public License 2.0), usado apenas nos testes. **Maven** e **JDK/OpenJDK** são ferramentas de build e execução.
- Nenhuma outra biblioteca ou código de terceiros. Todo o código-fonte foi escrito durante o PooaLAB.
- **Uso de IA:** o código, os testes e este README foram gerados com apoio do **Claude (Anthropic)** e revisados e testados pela equipe. Os dados de validação vêm de conversas reais feitas pela equipe; a IA não foi usada como fonte de evidência. Detalhes em `docs/declaracao-ia-terceiros.docx` (Anexo E).
