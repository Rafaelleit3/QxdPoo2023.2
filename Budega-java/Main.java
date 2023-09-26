import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Pessoa {
    private String name;

    public Pessoa(String name) {
        setName(name);
    }

    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}

class Mercantil {
    private List<Pessoa> caixas;
    private List<Pessoa> esperando;

    public Mercantil() {

    }

    public Mercantil(int qtdCaixas) {
        this.caixas = new ArrayList<>(qtdCaixas);
        this.esperando = new ArrayList<>();

        for (int i = 0; i < qtdCaixas; i++) {
            this.caixas.add(null);
        }
    }

    private boolean validarIndice(int indice) {
        if (indice >= 0 && indice < this.caixas.size()) {
            return true;
        }
        System.out.println("fail: caixa inexistente");
        return false;
    }

    public String toString() {

        for (int i = 0; i < this.caixas.size(); i++) {
            // System.out.println("entrou no for");
            if (this.caixas.get(i) == null) {
                this.caixas.set(i, new Pessoa("-----"));
                // System.out.println("entrou no if");
            }
        }

        return "Caixas: " + this.caixas.toString() + "\nEspera: " + this.esperando.toString();
    }

    public void chegar(Pessoa pessoa) {
        this.esperando.add(pessoa);
    }

    public boolean chamar(int indice) {
        if (validarIndice(indice) && !this.esperando.isEmpty()) {
            if (this.caixas.get(indice).getName() == "-----") {
                Pessoa cliente = this.esperando.remove(0);
                this.caixas.set(indice, cliente);
                return true;
            } else {
                System.out.println("fail: caixa ocupado");
            }

        } else {
            System.out.println("fail: sem clientes");
        }
        return false;
    }

    public Pessoa finalizar(int indice) {

        if (validarIndice(indice)) {
            Pessoa cliente = this.caixas.get(indice);
            if (cliente.getName() != "-----") {
                this.caixas.set(indice, null);
                return cliente;
            } else {
                System.out.println("fail: caixa vazio");
            }
        }

        return null;
    }

}

class Main {

    static Scanner scanner = new Scanner(System.in);

    public static String input() {
        return scanner.nextLine();
    }

    public static void write(String value) {
        System.out.println(value);
    }

    public static int number(String value) {
        return Integer.parseInt(value);
    }

    public static void main(String[] a) {
        Mercantil mercantil = new Mercantil();

        while (true) {
            String line = input();
            var args = line.split(" ");
            write('$' + line);

            if ("end".equals(args[0])) {
                break;
            } else if ("init".equals(args[0])) {
                mercantil = new Mercantil(number(args[1]));
            } else if ("show".equals(args[0])) {
                write(mercantil.toString());
            } else if ("call".equals(args[0])) {
                mercantil.chamar(number(args[1]));
            } else if ("finish".equals(args[0])) {
                mercantil.finalizar(number(args[1]));
            } else if ("arrive".equals(args[0])) {
                mercantil.chegar(new Pessoa(args[1]));
            } else {
                System.out.println("fail: comando invalido");
            }

        }

    }

}