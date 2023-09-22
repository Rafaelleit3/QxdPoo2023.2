
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

class Pass {
    private int age;
    private String name;

    public Pass(String name, int age) {
        setName(name);
        setAge(age);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isPriority() {
        return this.age > 64;
    }

    public String getName() {
        return this.name;
    }

    public int getAge() {
        return this.age;
    }

    public String toString() {
        return getName() + ":" + getAge();
    }

}

class Topic {
    private List<Pass> normalSeats;
    private List<Pass> prioritySeats;

    public Topic() {

    }

    public Topic(int capacity, int qtdPriority) {
        this.normalSeats = new ArrayList<>();
        this.prioritySeats = new ArrayList<>();
        // capacidade normal = capacity - qtdPriority
        int qtdNormal = capacity - qtdPriority;
        for (int i = 0; i < qtdNormal; i++) {
            this.normalSeats.add(null);
        }
        for (int i = 0; i < qtdPriority; i++) {
            this.prioritySeats.add(null);
        }

    }
    // metodos auxiliares------------------------------

    private static int findFree(List<Pass> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private static int findName(List<Pass> list, String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                if (name.equals(list.get(i).getName())) {
                    return i;
                }
            }

        }

        return -1;
    }

    private static boolean insert(List<Pass> list, Pass pass) {
        int indice = findFree(list);
        if (indice != -1) {
            list.set(indice, pass);
            return true;
        }

        return false;
    }

    private static boolean remove(List<Pass> list, String name) {
        int indice = findName(list, name);
        if (indice != -1) {
            list.set(indice, null);
            return true;
        }

        return false;
    }

    // ------------------------------------------------
    public String toString() {
        String saida = "[";

        if (prioritySeats != null) {
            for (int i = 0; i < this.prioritySeats.size(); i++) {
                if (this.prioritySeats.get(i) == null)
                    saida += "@ ";
                else
                    saida += "@" + this.prioritySeats.get(i) + " ";
            }
        }

        if (normalSeats != null) {
            for (int i = 0; i < this.normalSeats.size(); i++) {
                if (this.normalSeats.get(i) == null)
                    saida += "=";
                else
                    saida += "=" + this.normalSeats.get(i);

                if (i != this.normalSeats.size() - 1)
                    saida += " ";
            }
        }

        return saida + "]";
    }

    public boolean insert(Pass pass) {

        // pessoa está na topic?
        if (findName(this.prioritySeats, pass.getName()) != -1) {
            if (findName(this.normalSeats, pass.getName()) != -1) {
                System.out.println("fail: " + pass.getName() + " ja esta na topic");
                return false;
            }
            System.out.println("fail: " + pass.getName() + " ja esta na topic");
            return false;
        }

        if (pass.isPriority()) { // a pessoa é prioridade?
            if (findFree(this.prioritySeats) != -1) { // verifica vaga em prioridade
                insert(this.prioritySeats, pass);
                return true;
            } else if (findFree(this.normalSeats) != -1) { // verifica vaga em normal
                insert(this.normalSeats, pass);
                return true;
            } else {
                System.out.println("fail: topic lotada");
                return false;
            }

        } else { // a pessoa não é prioridade
            if (findFree(this.normalSeats) != -1) {
                insert(this.normalSeats, pass);
                return true;
            } else if (findFree(this.prioritySeats) != -1) {
                insert(this.prioritySeats, pass);
                return true;
            } else {
                System.out.println("fail: topic lotada");
            }
        }

        return false;

    }

    public boolean remove(String name) {

        // verifica se o nome esta na topic
        if (findName(this.prioritySeats, name) == -1) {
            if (findName(this.normalSeats, name) == -1) {
                System.out.println("fail: " + name + " nao esta na topic");
                return false;
            }
        }

        boolean removerPass = remove(this.prioritySeats, name);
        if (!removerPass) {
            removerPass = remove(this.normalSeats, name);
        }
        return removerPass;

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

    public static void main(String[] arg) {
        Topic topic = new Topic();

        while (true) {

            String line = input();
            var args = line.split(" ");
            write('$' + line);

            if ("end".equals(args[0])) {
                break;
            } else if ("init".equals(args[0])) {
                topic = new Topic(number(args[1]), number(args[2]));
            } else if ("show".equals(args[0])) {
                write(topic.toString());
            } else if ("in".equals(args[0])) {
                topic.insert(new Pass(args[1], number(args[2])));
            } else if ("out".equals(args[0])) {
                topic.remove(args[1]);
            } else {
                System.out.println("fail: comando invalido");
            }

        }

    }

}