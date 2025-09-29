import java.util.Scanner;

//VINÍCIUS PEREIRA RODRIGUES - 10729470
//SOFIA CAVALCANTI DE OLIVIERA - 10723361
//ALESSANDRO PIMENTA DE MELO - 10723221
//
//SEDGEWICK, R.; WAYNE, K Algorithms, Fourh Edition
//GOODRICH, M. T.; TAMASSIA, R., MOUNT, M.N. Data Structures and Algorithms in C++. 2.ed. New Yok: Wiley, 2011.

/**
 * CLASSE PRINCIPAL
 *
 * Esta é a classe que inicia o programa!
 * Cria um loop infinito (REPL) que:
 * 1. Lê comando do usuário
 * 2. Processa o comando
 * 3. Exibe o resultado
 * 4. Repete até digitar EXIT
 *
 * REPL = Read-Eval-Print Loop (Ler-Avaliar-Imprimir-Loop)
 * É o mesmo padrão usado em Python, calculadoras, etc.
 */

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProcessadorDeComandos processador = new ProcessadorDeComandos();

        // Mensagem de boas-vindas
        System.out.println("=== Avaliador de Expressões Matemáticas ===");
        System.out.println("Digite 'EXIT' para sair.");
        System.out.println();

        // Loop principal do programa
        while (true) {
            // Exibe o prompt (o ">" que aparece antes de digitar)
            System.out.print("> ");

            // Lê o que o usuário digitou
            String entrada = scanner.nextLine();

            // Processa o comando
            String resultado = processador.processarComando(entrada);

            // Se resultado for "EXIT", sai do loop
            if ("EXIT".equals(resultado)) {
                break;
            }

            // Exibe o resultado (se não for vazio)
            if (!resultado.isEmpty()) {
                System.out.println(resultado);
            }
        }

        // Fecha o scanner e finaliza
        scanner.close();
        System.out.println("Programa encerrado.");
    }
}
