
//VINÍCIUS PEREIRA RODRIGUES - 10729470
//SOFIA CAVALCANTI DE OLIVIERA - 10723361
//ALESSANDRO PIMENTA DE MELO - 10723221
//
//SEDGEWICK, R.; WAYNE, K Algorithms, Fourh Edition
//GOODRICH, M. T.; TAMASSIA, R., MOUNT, M.N. Data Structures and Algorithms in C++. 2.ed. New Yok: Wiley, 2011.

public class ProcessadorDeComandos {
    private GerenciadorDeVariaveis gerenciador;
    private AvaliadorDeExpressoes avaliador;
    private Fila<String> filaComandos;
    private boolean modoGravacao;


    public ProcessadorDeComandos() {
        this.gerenciador = new GerenciadorDeVariaveis();
        this.avaliador = new AvaliadorDeExpressoes(gerenciador);
        this.filaComandos = new Fila<>(10);  // Máximo 10 comandos gravados
        this.modoGravacao = false;
    }

    public boolean isModoGravacao() {
        return modoGravacao;
    }

    // ==================== PROCESSAMENTO PRINCIPAL ====================

    /**
     * PROCESSA UM COMANDO
     *
     * Este é o método principal que decide o que fazer com cada comando
     */
    public String processarComando(String entrada) {
        // Trata entrada nula ou vazia
        if (entrada == null) {
            entrada = "";
        }
        entrada = entrada.trim();

        if (entrada.isEmpty()) {
            return "";
        }

        String entradaUpper = entrada.toUpperCase();

        // ==================== MODO GRAVAÇÃO ====================

        if (modoGravacao) {
            // STOP precisa ser tratado ANTES de gravar
            if (entradaUpper.equals("STOP")) {
                modoGravacao = false;
                return "Encerrando gravação... (" + formatarStatusRec() + ")";
            }

            // Comandos que NÃO podem ser gravados
            if (entradaUpper.equals("REC") || entradaUpper.equals("PLAY") ||
                entradaUpper.equals("ERASE") || entradaUpper.equals("EXIT")) {
                return "Erro: comando inválido para gravação.";
            }

            // Verifica se atingiu limite
            if (filaComandos.isFull()) {
                modoGravacao = false;
                return "Erro: limite de gravação atingido. Gravação interrompida.";
            }

            // Grava o comando na fila
            filaComandos.enqueue(entrada);
            String resultado = "(" + formatarStatusRec() + ") " + entrada;

            // Se chegou no limite, para gravação
            if (filaComandos.size() == 10) {
                modoGravacao = false;
                resultado += "\nLimite de gravação atingido. Gravação interrompida.";
            }

            return resultado;
        }

        // ==================== MODO NORMAL ====================

        try {
            // COMANDO: EXIT
            if (entradaUpper.equals("EXIT")) {
                return "EXIT";
            }

            // COMANDO: VARS (lista variáveis)
            else if (entradaUpper.equals("VARS")) {
                return gerenciador.listarVariaveis();
            }

            // COMANDO: RESET (reinicia variáveis)
            else if (entradaUpper.equals("RESET")) {
                gerenciador.reset();
                return "Variáveis reiniciadas.";
            }

            // COMANDO: REC (inicia gravação)
            else if (entradaUpper.equals("REC")) {
                modoGravacao = true;
                return "Iniciando gravação... (" + formatarStatusRec() + ")";
            }

            // COMANDO: STOP (para gravação - mas só funciona dentro do modo gravação)
            else if (entradaUpper.equals("STOP")) {
                if (!modoGravacao) {
                    return "Não há gravação em andamento.";
                }
                return "Erro interno.";
            }

            // COMANDO: PLAY (reproduz comandos gravados)
            else if (entradaUpper.equals("PLAY")) {
                return reproduzirComandos();
            }

            // COMANDO: ERASE (apaga comandos gravados)
            else if (entradaUpper.equals("ERASE")) {
                filaComandos.clear();
                return "Gravação apagada.";
            }

            // COMANDO: Atribuição (A = 10)
            else if (isComandoAtribuicao(entrada)) {
                return processarAtribuicao(entrada);
            }

            // COMANDO: Expressão matemática (A + B)
            else {
                return processarExpressao(entrada);
            }
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Formata o status da gravação
     * Exemplo: "REC: 3/10" (3 comandos de 10 gravados)
     */
    private String formatarStatusRec() {
        return "REC: " + filaComandos.size() + "/10";
    }

    /**
     * Verifica se o comando é uma atribuição
     *
     * Padrão: letra = número
     * Exemplos válidos: A = 10, B = -5.5, C = 3.14
     *
     * Usa Regex (expressões regulares):
     * - \\s* = zero ou mais espaços
     * - [A-Za-z] = uma letra
     * - [+-]? = opcional: sinal + ou -
     * - \\d* = zero ou mais dígitos
     * - \\.? = opcional: ponto decimal
     * - \\d+ = um ou mais dígitos
     */
    private boolean isComandoAtribuicao(String entrada) {
        return entrada.matches("^\\s*[A-Za-z]\\s*=\\s*[+-]?\\d*\\.?\\d+\\s*$");
    }

    /**
     * PROCESSA ATRIBUIÇÃO
     *
     * Exemplo: "A = 10"
     * 1. Remove espaços -> "A=10"
     * 2. Divide pelo = -> ["A", "10"]
     * 3. Pega a letra A
     * 4. Converte "10" para número
     * 5. Guarda A = 10
     */
    private String processarAtribuicao(String entrada) {
        entrada = entrada.replaceAll("\\s+", "");  // Remove espaços
        String[] partes = entrada.split("=");      // Divide pelo =

        if (partes.length != 2) {
            return "Erro: comando inválido.";
        }

        char variavel = Character.toUpperCase(partes[0].charAt(0));

        try {
            double valor = Double.parseDouble(partes[1]);
            gerenciador.setVariavel(variavel, valor);
            return variavel + " = " + valor;
        } catch (NumberFormatException e) {
            return "Erro: valor numérico inválido.";
        }
    }

    /**
     * PROCESSA EXPRESSÃO MATEMÁTICA
     *
     * Exemplo: "A + B * C"
     * 1. Valida a expressão
     * 2. Verifica se variáveis estão definidas
     * 3. Calcula o resultado
     * 4. Formata o resultado (inteiro ou decimal)
     */
    private String processarExpressao(String entrada) {
        try {
            // Valida sintaxe
            if (!avaliador.validarExpressao(entrada)) {
                return "Erro: expressão inválida.";
            }

            // Verifica variáveis
            String errosVariaveis = avaliador.verificarVariaveisDefinidas(entrada);
            if (!errosVariaveis.isEmpty()) {
                return errosVariaveis;
            }

            // Calcula
            double resultado = avaliador.avaliarExpressao(entrada);

            // Formata resultado
            // Se for número inteiro (10.0), mostra como inteiro (10)
            // Se for decimal (10.5), mostra com casas decimais
            if (resultado == (long) resultado) {
                return String.valueOf((long) resultado);
            } else {
                return String.valueOf(resultado);
            }
        } catch (Exception e) {
            return "Erro: expressão inválida.";
        }
    }

    // ==================== REPRODUÇÃO DE COMANDOS ====================

    /**
     * REPRODUZ COMANDOS GRAVADOS (comando PLAY)
     *
     * Pega todos os comandos da fila e executa um por um
     *
     * Exemplo:
     * Comandos gravados: ["A = 5", "B = 10", "A + B"]
     * Resultado:
     * Reproduzindo gravação...
     * A = 5
     * A = 5.0
     * B = 10
     * B = 10.0
     * A + B
     * 15
     */
    private String reproduzirComandos() {
        if (filaComandos.isEmpty()) {
            return "Não há gravação para ser reproduzida.";
        }

        StringBuilder resultado = new StringBuilder("Reproduzindo gravação...");
        Object[] comandos = filaComandos.toArray();

        // Executa cada comando gravado
        for (Object obj : comandos) {
            if (obj != null) {
                String comando = obj.toString();
                resultado.append("\n").append(comando);

                // Processa o comando e adiciona a resposta
                String resposta = processarComandoSemGravacao(comando);
                if (!resposta.isEmpty()) {
                    resultado.append("\n").append(resposta);
                }
            }
        }

        return resultado.toString();
    }

    /**
     * Processa comando SEM permitir que entre em modo gravação
     *
     * Usado no PLAY para evitar que REC seja executado dentro do PLAY
     */
    private String processarComandoSemGravacao(String entrada) {
        boolean gravacaoAnterior = modoGravacao;
        modoGravacao = false;
        String resultado = processarComando(entrada);
        modoGravacao = gravacaoAnterior;
        return resultado.equals("EXIT") ? "" : resultado;
    }
}
