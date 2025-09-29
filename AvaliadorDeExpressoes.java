
//VINÍCIUS PEREIRA RODRIGUES - 10729470
//SOFIA CAVALCANTI DE OLIVIERA - 10723361
//ALESSANDRO PIMENTA DE MELO - 10723221
//
//SEDGEWICK, R.; WAYNE, K Algorithms, Fourh Edition
//GOODRICH, M. T.; TAMASSIA, R., MOUNT, M.N. Data Structures and Algorithms in C++. 2.ed. New Yok: Wiley, 2011.


/**
 * AVALIADOR DE EXPRESSÕES
 *
 * Esta é a classe mais complexa! Ela faz 3 coisas principais:
 * 1. Valida se a expressão está correta
 * 2. Converte de INFIXA para POSFIXA (algoritmo de Dijkstra)
 * 3. Calcula o resultado da expressão posfixa
 *
 * INFIXA: A + B (operador no meio) - jeito que escrevemos normalmente
 * POSFIXA: A B + (operador no final) - mais fácil para o computador calcular
 */

public class AvaliadorDeExpressoes {
    private GerenciadorDeVariaveis gerenciador;

    public AvaliadorDeExpressoes(GerenciadorDeVariaveis gerenciador) {
        this.gerenciador = gerenciador;
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Verifica se um caractere é operador matemático
     */
    private boolean isOperador(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    /**
     * Verifica se um caractere é uma variável válida (A-Z)
     */
    private boolean isVariavel(char c) {
        char upper = Character.toUpperCase(c);
        return upper >= 'A' && upper <= 'Z';
    }

    /**
     * PRECEDÊNCIA DOS OPERADORES
     *
     * Define a ordem de prioridade:
     * - Exponenciação (^) tem precedência 3 (mais alta)
     * - Multiplicação e Divisão (*,/) tem precedência 2
     * - Adição e Subtração (+,-) tem precedência 1 (mais baixa)
     *
     * Exemplo: 2 + 3 * 4
     * Multiplicação tem precedência maior, então é calculada primeiro
     * Resultado: 2 + 12 = 14 (não 5 * 4 = 20)
     */
    private int precedencia(char operador) {
        switch (operador) {
            case '+':
            case '-':
                return 1;  // Precedência baixa
            case '*':
            case '/':
                return 2;  // Precedência média
            case '^':
                return 3;  // Precedência alta
            default:
                return 0;
        }
    }

    // ==================== VALIDAÇÃO ====================

    /**
     * VALIDA A EXPRESSÃO
     *
     * Verifica se a expressão tem sintaxe correta:
     * - Parênteses balanceados
     * - Operadores e operandos na ordem certa
     * - Apenas caracteres válidos
     *
     * Exemplo válido: (A + B) * C
     * Exemplo inválido: A + + B (dois operadores seguidos)
     */
    public boolean validarExpressao(String expressao) {
        if (expressao == null || expressao.trim().isEmpty()) {
            return false;
        }

        expressao = expressao.replaceAll("\\s+", "");  // Remove espaços

        int parenteses = 0;
        boolean esperandoOperando = true;  // Começa esperando um número/variável

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            if (c == '(') {
                parenteses++;
                esperandoOperando = true;  // Depois de ( espera operando
            }
            else if (c == ')') {
                parenteses--;
                if (parenteses < 0) return false;  // Fecha antes de abrir!
                esperandoOperando = false;
            }
            else if (isVariavel(c)) {
                if (!esperandoOperando) return false;  // Duas variáveis seguidas!
                esperandoOperando = false;
            }
            else if (isOperador(c)) {
                if (esperandoOperando) return false;  // Operador sem operando antes!
                esperandoOperando = true;  // Depois de operador espera operando
            }
            else {
                return false;  // Caractere inválido!
            }
        }

        // No final: parênteses devem estar balanceados e não pode estar esperando operando
        return parenteses == 0 && !esperandoOperando;
    }

    // ==================== CONVERSÃO INFIXA -> POSFIXA ====================

    /**
     * ALGORITMO DE SHUNTING YARD (Dijkstra)
     *
     * Converte expressão INFIXA em POSFIXA
     *
     * Exemplo:
     * Infixa:   A + B * C
     * Posfixa:  A B C * +
     * 
     * 1. Variável -> vai direto para saída
     * 2. Operador -> vai para pilha (respeitando precedência)
     * 3. ( -> vai para pilha
     * 4. ) -> desempilha até achar (
     */
    public String infixaParaPosfixa(String expressao) {
        if (!validarExpressao(expressao)) {
            throw new RuntimeException("Expressão inválida!");
        }

        expressao = expressao.replaceAll("\\s+", "");
        StringBuilder saida = new StringBuilder();
        Pilha<Character> pilha = new Pilha<>();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);

            // CASO 1: É variável -> vai direto pra saída
            if (isVariavel(c)) {
                saida.append(c);
            }
            // CASO 2: É parêntese de abertura -> empilha
            else if (c == '(') {
                pilha.push(c);
            }
            // CASO 3: É parêntese de fechamento -> desempilha até achar (
            else if (c == ')') {
                while (!pilha.isEmpty() && pilha.peek() != '(') {
                    saida.append(pilha.pop());
                }
                pilha.pop();  // Remove o '('
            }
            // CASO 4: É operador
            else if (isOperador(c)) {
                // Desempilha operadores com precedência >= atual
                while (!pilha.isEmpty() && pilha.peek() != '(' &&
                       precedencia(pilha.peek()) >= precedencia(c)) {
                    saida.append(pilha.pop());
                }
                pilha.push(c);  // Empilha o operador atual
            }
        }

        // Desempilha o resto
        while (!pilha.isEmpty()) {
            saida.append(pilha.pop());
        }

        return saida.toString();
    }

    // ==================== VERIFICAÇÃO DE VARIÁVEIS ====================

    /**
     * Verifica se todas as variáveis usadas foram definidas
     *
     * Exemplo: se expressão é "A + B" mas só A foi definida,
     * retorna mensagem de erro sobre B
     */
    public String verificarVariaveisDefinidas(String expressao) {
        StringBuilder erros = new StringBuilder();

        for (int i = 0; i < expressao.length(); i++) {
            char c = expressao.charAt(i);
            if (isVariavel(c)) {
                char upper = Character.toUpperCase(c);
                if (!gerenciador.isDefinida(upper)) {
                    if (erros.length() > 0) {
                        erros.append("\n");
                    }
                    erros.append("Erro: variável ")
                         .append(upper)
                         .append(" não definida.");
                }
            }
        }

        return erros.toString();
    }

    // ==================== CÁLCULO DA EXPRESSÃO POSFIXA ====================
    /**
     * 
     * 1. Se é variável -> empilha o valor
     * 2. Se é operador -> desempilha 2 valores, calcula, empilha resultado
     *
     * Exemplo: A B C * +  (com A=2, B=3, C=4)
     *
     * Passo a passo:
     * - Lê A -> empilha 2 -> pilha: [2]
     * - Lê B -> empilha 3 -> pilha: [2, 3]
     * - Lê C -> empilha 4 -> pilha: [2, 3, 4]
     * - Lê * -> desempilha 4 e 3, calcula 3*4=12, empilha -> pilha: [2, 12]
     * - Lê + -> desempilha 12 e 2, calcula 2+12=14, empilha -> pilha: [14]
     * - Resultado final: 14
     */
    public double avaliarPosfixa(String posfixa) {
        Pilha<Double> pilha = new Pilha<>();

        for (int i = 0; i < posfixa.length(); i++) {
            char c = posfixa.charAt(i);

            // Se é variável: pega valor e empilha
            if (isVariavel(c)) {
                double valor = gerenciador.getVariavel(c);
                pilha.push(valor);
            }
            // Se é operador: desempilha 2, calcula, empilha resultado
            else if (isOperador(c)) {
                if (pilha.size() < 2) {
                    throw new RuntimeException("Expressão inválida!");
                }

                // ATENÇÃO: ordem importa! Desempilha B primeiro, depois A
                double b = pilha.pop();
                double a = pilha.pop();
                double resultado = 0;

                switch (c) {
                    case '+':
                        resultado = a + b;
                        break;
                    case '-':
                        resultado = a - b;
                        break;
                    case '*':
                        resultado = a * b;
                        break;
                    case '/':
                        if (b == 0) {
                            throw new RuntimeException("Divisão por zero!");
                        }
                        resultado = a / b;
                        break;
                    case '^':
                        resultado = Math.pow(a, b);
                        break;
                }

                pilha.push(resultado);
            }
        }

        // No final deve ter exatamente 1 elemento na pilha: o resultado
        if (pilha.size() != 1) {
            throw new RuntimeException("Expressão inválida!");
        }

        return pilha.pop();
    }

    // ==================== MÉTODO PRINCIPAL ====================

    /**
     * AVALIA EXPRESSÃO COMPLETA
     *
     * Este é o método que junta tudo:
     * 1. Verifica se variáveis estão definidas
     * 2. Converte infixa para posfixa
     * 3. Calcula o resultado
     */
    public double avaliarExpressao(String expressaoInfixa) {
        // Verifica variáveis
        String errosVariaveis = verificarVariaveisDefinidas(expressaoInfixa);
        if (!errosVariaveis.isEmpty()) {
            throw new RuntimeException(errosVariaveis);
        }

        // Converte e calcula
        String posfixa = infixaParaPosfixa(expressaoInfixa);
        return avaliarPosfixa(posfixa);
    }
}
