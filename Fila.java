//
//VINÍCIUS PEREIRA RODRIGUES - 10729470
//SOFIA CAVALCANTI DE OLIVIERA - 10723361
//ALESSANDRO PIMENTA DE MELO - 10723221
//
//SEDGEWICK, R.; WAYNE, K Algorithms, Fourh Edition
//GOODRICH, M. T.; TAMASSIA, R., MOUNT, M.N. Data Structures and Algorithms in C++. 2.ed. New Yok: Wiley, 2011.


/**
 * Usamos a fila para:
 * - Gravar comandos no modo REC
 * - Reproduzir comandos no modo PLAY
 */

public class Fila<T> {
    private T[] dados;          // array para guardar os elementos - atua de forma circular
    private int inicio;         // índice do primeiro elemento
    private int fim;            // índice do último elemento
    private int tamanho;        // quantidade de elementos na fila
    private int capacidade;     // capacidade máxima da fila

//    @SuppressWarnings("unchecked")
    public Fila(int capacidade) { //cria a fila com o parametro de capacidade.
        this.capacidade = capacidade;
        this.dados = (T[]) new Object[capacidade]; // cria o array generico que foi declarado acima
        this.inicio = 0;
        this.fim = -1;
        this.tamanho = 0;
    }

    public boolean isEmpty() { // verifica se fila está vazia
        return tamanho == 0;
    }



    public boolean isFull() { // verifica se fila está cheia
        return tamanho == capacidade;
    }

    public void enqueue(T elemento) { // Adicionar elemento ao final da fila -
        if (isFull()) {
            throw new RuntimeException("Fila cheia! Não é possível adicionar mais elementos.");
        }
        fim = (fim + 1) % capacidade;  // Próxima posição (circular)
        dados[fim] = elemento; // adiciona o elemento
        tamanho++; // aumenta o tamanho
    }

    public T dequeue() { //remove quem está há mais tempo na fila
        if (isEmpty()) {
            throw new RuntimeException("Fila vazia! Não há nada para remover.");
        }
        T elemento = dados[inicio];
        dados[inicio] = null;  // Limpa a posição
        inicio = (inicio + 1) % capacidade;  // Próxima posição (circular)
        tamanho--;
        return elemento;
    }

    public T front() { // checa o primeiro elemento da fila
        if (isEmpty()) {
            throw new RuntimeException("Fila vazia");
        }
        return dados[inicio];
    }

    public int size() {
        return tamanho;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }

    public Object[] toArray() { //converte a fila num array para não enviarmos objetos null.
        Object[] array = new Object[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = dados[(inicio + i) % capacidade];
        }
        return array;
    }
}
