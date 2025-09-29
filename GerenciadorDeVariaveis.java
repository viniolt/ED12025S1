
//VINÍCIUS PEREIRA RODRIGUES - 10729470
//SOFIA CAVALCANTI DE OLIVIERA - 10723361
//ALESSANDRO PIMENTA DE MELO - 10723221
//
//SEDGEWICK, R.; WAYNE, K Algorithms, Fourh Edition
//GOODRICH, M. T.; TAMASSIA, R., MOUNT, M.N. Data Structures and Algorithms in C++. 2.ed. New Yok: Wiley, 2011.


//Gerencia as variáveis de A até Z, que o usuário pode utilizar.
public class GerenciadorDeVariaveis {
    private double[] valores;      // Guarda o valor de cada variável
    private boolean[] definidas;   // Guarda se a variável foi definida

    public GerenciadorDeVariaveis() { // Inicializa os arrays das variáveis, marcando o valor de cada letra e se estão definidas ou nao
        valores = new double[26];
        definidas = new boolean[26];
        reset();  // Inicializa tudo como não definido
    }

    public void setVariavel(char variavel, double valor) { //definir valor de alguma variável
        // Converte letra para índice: ex: a = 0
        int index = Character.toUpperCase(variavel) - 'A'; //Exemplo - A = 65, então A - 'A' = 0, vira indice 0 e por aí vai

        // Validação: verifica se é letra válida
        if (index < 0 || index >= 26) {
            throw new IllegalArgumentException("Variável deve estar entre A e Z!");
        }

        valores[index] = valor;
        definidas[index] = true;  // Marca como definida
    }

    public double getVariavel(char variavel) {
        int index = Character.toUpperCase(variavel) - 'A';

        if (index < 0 || index >= 26) {
            throw new IllegalArgumentException("Variável deve estar entre A e Z!");
        }

        // Erro se tentar usar variável não definida
        if (!definidas[index]) {
            throw new RuntimeException("Variável " + Character.toUpperCase(variavel) + " não foi definida ainda!");
        }

        return valores[index];
    }

    public boolean isDefinida(char variavel) { //verifica se a variável está ou não definida
        int index = Character.toUpperCase(variavel) - 'A';

        if (index < 0 || index >= 26) {
            return false;
        }

        return definidas[index];
    }


    public void reset() { //comandor RESET para resetar as variáveis
        for (int i = 0; i < 26; i++) {
            valores[i] = 0.0;
            definidas[i] = false;
        }
    }

    public String listarVariaveis() { //lista todas as variáveis
        StringBuilder sb = new StringBuilder();
        boolean temVariavel = false;

        for (int i = 0; i < 26; i++) { //as percorre de A até Z
            if (definidas[i]) {
                // Converte índice de volta para letra: 0->A, 1->B, etc
                char var = (char) ('A' + i);

                if (temVariavel) {
                    sb.append("\n");
                }

                sb.append(var).append(" = ").append(valores[i]);
                temVariavel = true;
            }
        }

        // Se nenhuma variável foi definida
        return temVariavel ? sb.toString() : "Nenhuma variável definida."; // se tem variável, passa a variável para string, caso contrário, nenhuma variável definida
    }
    
    public boolean temVariaveisDefinidas() { //verifica se há ao menos 01 variável definida
        for (boolean def : definidas) {
            if (def) return true;
        }
        return false;
    }
}
