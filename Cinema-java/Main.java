import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

class Client {
    private String telefone;
    private String id;

    public Client(String id, String fone) {
        setId(id);
        setTelefone(fone);
    }

    public String toString() {
        return getId() + ":" + getTelefone();
    }

    public void setTelefone(String fone) {
        this.telefone = fone;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public String getId() {
        return this.id;
    }

}

class Sala {
    private List<Client> cadeiras;

    public Sala() {
        cadeiras = new ArrayList<>();
    }

    public Sala(int capacidade) {
        cadeiras = new ArrayList<>();
        for (int i = 0; i < capacidade; i++) {
            this.cadeiras.add(null);
        }
    }

    private boolean verificarIndice(int indice) {
        if (indice >= 0 && indice < this.cadeiras.size()) {
            return true;
        }

        return false;
    }

    private int procurar(String nome) {
        for (int i = 0; i < this.cadeiras.size(); i++) {
            if (this.cadeiras.get(i) != null) {
                if (this.cadeiras.get(i).getId().equals(nome)) {
                    return i;
                }
            }

        }
        return -1;
    }

    public boolean reservar(String id, String telefone, int ind) {

        if (verificarIndice(ind)) {
            if (this.cadeiras.get(ind) == null) {
                if (procurar(id) == -1) {
                    this.cadeiras.set(ind, new Client(id, telefone));
                    return true;
                } else {
                    System.out.println("fail: cliente ja esta no cinema");
                    return false;
                }
            } else {
                System.out.println("fail: cadeira ja esta ocupada");
                return false;
            }

        } else {
            System.out.println("fail: cadeira nao existe");
        }
        return false;
    }

    public void cancelar(String id) {
        if (procurar(id) != -1) {
            this.cadeiras.set(procurar(id), null);
        } else {
            System.out.println("fail: cliente nao esta no cinema");
        }

    }

    // public Client getCadeiras(){

    // }

    public String toString() {
        String saida = "[";
        for (int i = 0; i < this.cadeiras.size(); i++) {
            if (this.cadeiras.get(i) == null) {
                saida += "-";
            } else {
                saida += this.cadeiras.get(i);
            }
            if (i != this.cadeiras.size() - 1) {
                saida += " ";
            }
        }

        return saida + "]";
    }

}

public class Main {

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

        Sala sala = new Sala();

        while (true) {

            String line = input();
            var args = line.split(" ");
            write('$' + line);

            if ("end".equals(args[0])) {
                break;
            } else if ("init".equals(args[0])) {
                sala = new Sala(number(args[1]));
            } else if ("reservar".equals(args[0])) {
                sala.reservar(args[1], args[2], number(args[3]));
            } else if ("cancelar".equals(args[0])) {
                sala.cancelar(args[1]);
            } else if ("show".equals(args[0])) {
                write(sala.toString());
            } else {
                System.out.println("fail: comando invalido");
            }

        }

    }

}