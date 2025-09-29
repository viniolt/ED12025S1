
//VINÍCIUS PEREIRA RODRIGUES - 10729470
//SOFIA CAVALCANTI DE OLIVIERA - 10723361
//ALESSANDRO PIMENTA DE MELO - 10723221
//
//SEDGEWICK, R.; WAYNE, K Algorithms, Fourh Edition
//GOODRICH, M. T.; TAMASSIA, R., MOUNT, M.N. Data Structures and Algorithms in C++. 2.ed. New Yok: Wiley, 2011.


/**
 * PILHA - Last In, First Out (LIFO)
 *
 * Neste projeto, utilizamos as pilhas para :
 * - Converter expressões matemáticas (infxa para posfixa)
 * - Calcular o resultado das expressões
 */

public class Pilha<T> {
    private T[] dados;           // Para guardar os elementos da pilha
    private int topo;            // Topo da pilha (índice)
    private int capacidade;      // Para definir a capacidade da pilha

    /**
     * Construtor com capacidade personalizada
     */
    public Pilha(int capacidade) {
        this.capacidade = capacidade;
        this.dados = (T[]) new Object[capacidade];  // array da pilha
        this.topo = -1;  // define a pilha como vazia
    }

    public Pilha() {
        this(100);
    }

    public boolean isEmpty() { //booleano (true ou false) que verifica se a pilha está vazia
        return topo == -1;
    }

    public boolean isFull() { //booleano que verifica se a pilha está cheia
        return topo == capacidade - 1;
    }

    public void push(T elemento) { //adiciona um elemento ao topo da pilha caso não esteja cheia
        if (isFull()) {
            throw new RuntimeException("Pilha cheia! Não consigo adicionar mais elementos.");
        }
        dados[++topo] = elemento;  // Incrementa topo e adiciona elemento
    }

    public T pop() {
        if (isEmpty()) {
            throw new RuntimeException("Pilha vazia! Não tem nada para remover.");
        }
        T elemento = dados[topo];  // seleciona o elemento do topo
        dados[topo--] = null;      // transforma o topo em null, para remover o elemento e evitar problemas com memória
        return elemento;
    }

    public T peek() { // espia o elemento do topo da pilha
        if (isEmpty()) {
            throw new RuntimeException("Pilha vazia! Não tem nada para ver.");
        }
        return dados[topo];
    }
    public int size() { // retorna o tamanho da pilha
        return topo + 1;
    }

    public void clear() { // limpa a pilha, utilizando o pop para remover todos os elementos, basicamente ela é reiniciada.
        while (!isEmpty()) {
            pop();
        }
    }
}
