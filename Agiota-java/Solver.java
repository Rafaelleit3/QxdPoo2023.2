import java.util.*;

enum Label {
    GIVE,
    TAKE,
    PLUS;

    @Override
    public String toString() {
        String labelString;
        switch (this) {
            case GIVE:
                labelString = "give";
                break;
            case TAKE:
                labelString = "take";
                break;
            case PLUS:
                labelString = "plus";
                break;
            default:
                labelString = null;
                break;
        }
        return labelString;
    }
}

class ClienteException extends Exception {
    private boolean existe;

    public ClienteException(boolean existe) {
        this.existe = existe;
    }

    @Override
    public String getMessage() {
        if (!existe) {
            return "fail: cliente nao existe";
        } else {
            return "fail: cliente ja existe";
        }
    }
}

class Operation {
    private static int nextOpId = 0;
    private int id;
    private String name;
    private Label label;
    private int value;

    public Operation(String name, Label label, int value) {
        this.id = Operation.nextOpId++;
        this.name = name;
        this.label = label;
        this.value = value;
        // this.id = Operation.nextOpId;
        // Operation.nextOpId++;
    }

    @Override
    public String toString() {
        return String.format("%s", this.id);
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Label getLabel() {
        return this.label;
    }

    public int getValue() {
        return this.value;
    }
}

class Client {
    private String name;
    private int limite;
    ArrayList<Operation> operations;

    public Client(String name, int limite) {
        this.name = name;
        this.limite = limite;

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName()).append(" ").append(getBalance()).append("/").append(getLimite()).append("\n");

        for (Operation operation : operations) {
            sb.append("id:").append(operation.getId()).append(" ").append(operation.getLabel()).append(":")
                    .append(operation.getName()).append(" ").append(operation.getValue()).append("\n");
        }
        return sb.toString();

    }

    public String getName() {
        return this.name;
    }

    public int getLimite() {
        return this.limite;
    }

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    // XXX public void addOperation(String name, Label label, int value)
    public void addOperation(Operation operation) {
        if (operations == null) {
            this.operations = new ArrayList<>();
        }
        operations.add(operation);
    }

    // quanto esta devendo
    public int getBalance() {
        int divida = 0;
        if (operations != null) {
            for (Operation operation : operations) {
                Label label = operation.getLabel(); // pegar a etiqueta
                if (label == Label.TAKE) {
                    divida -= operation.getValue();
                } else if (label == Label.GIVE || label == Label.PLUS) {
                    divida += operation.getValue();
                }
            }
        }

        return divida;
    }
}

class Agiota {
    private ArrayList<Client> aliveList;
    private ArrayList<Client> deathList;
    private ArrayList<Operation> aliveOper;
    private ArrayList<Operation> deathOper;

    private int searchClient(String name) { // buscar na operação
        for (int i = 0; i < aliveList.size(); i++) {
            if (aliveList.get(i).getName().equals(name)) {
                return i;
            }
        }
        // System.out.println("fail: cliente nao encontrado");
        return -1;
    }

    private void pushOperation(Client client, Operation operation) {
        client.addOperation(operation);
        aliveOper.add(operation);
    }

    private void sortAliveList() {
        this.aliveList.sort(new Comparator<Client>() {
            public int compare(Client c1, Client c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
    }

    public Agiota() {
        this.aliveList = new ArrayList<>();
        this.deathList = new ArrayList<>();
        this.aliveOper = new ArrayList<>();
        this.deathOper = new ArrayList<>();

    }

    public Client getClient(String name) { // buscar na lista
        for (Client client : aliveList) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }

    public void addClient(String name, int limite) throws Exception {
        if (searchClient(name) != -1) {
            // System.out.println("fail: cliente ja existe");
            throw new ClienteException(true);
        }

        this.aliveList.add(new Client(name, limite));
        this.sortAliveList();
        // // if(this.aliveList != null){
        // if(searchClient(name) == -1){
        // this.aliveList.add(new Client(name, limite));
        // this.sortAliveList();
        // }
        // else{
        // // System.out.println("fail: cliente ja existe");
        // throw new Exception("fail: cliente ja existe");
        // }
        // // }
    }

    public void give(String name, int value) throws Exception {
        Client client = getClient(name);

        if (client == null) {
            // throw new Exception("fail: cliente nao existe");
            throw new ClienteException(false);
        }

        if (value + client.getBalance() > client.getLimite()) {
            throw new Exception("fail: limite excedido");
        }

        Operation operation = new Operation(name, Label.GIVE, value);
        pushOperation(client, operation);

        // if(client != null){
        // if(value + client.getBalance() > client.getLimite()){
        // System.out.println("fail: limite excedido");
        // }
        // else{
        // Operation operation = new Operation(name, Label.GIVE, value);
        // pushOperation(client, operation);
        // }
        // } else {
        // System.out.println("fail: cliente nao existe");
        // }
    }

    public void take(String name, int value) throws Exception {
        Client client = getClient(name);

        if (client == null) {
            // throw new Exception("fail: cliente nao existe");
            throw new ClienteException(false);
        }

        Operation operation = new Operation(name, Label.TAKE, value);
        pushOperation(client, operation);
    }

    public void kill(String name) {
        Client client = getClient(name);

        this.deathList.add(client);
        this.aliveList.remove(client);

        for (int i = 0; i < aliveOper.size(); i++) { // mover operações para os mortos
            if (aliveOper.get(i).getName().equals(name)) {
                deathOper.add(aliveOper.get(i));
                aliveOper.remove(i);
                i--;
            }
        }

    }

    public void plus() {
        ArrayList<Client> paraMatar = new ArrayList<>();

        for (Client client : aliveList) {
            int value = (int) Math.ceil((client.getBalance() * 0.1));
            Operation operation = new Operation(client.getName(), Label.PLUS, value);
            pushOperation(client, operation);

            if (client.getBalance() > client.getLimite()) {
                paraMatar.add(client);
            }
        }

        for (Client client : paraMatar) {
            kill(client.getName());
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Listagem dos clientes vivos
        for (Client client : aliveList) {
            sb.append(":) ").append(client.getName()).append(" ").append(client.getBalance()).append("/")
                    .append(client.getLimite()).append("\n");
        }

        // Listagem de todas as transações dos vivos
        for (Operation operation : aliveOper) {
            sb.append("+ id:").append(operation.getId()).append(" ").append(operation.getLabel()).append(":")
                    .append(operation.getName()).append(" ").append(operation.getValue()).append("\n");
        }
        // Listagem dos clientes mortos
        for (Client client : deathList) {
            sb.append(":( ").append(client.getName()).append(" ").append(client.getBalance()).append("/")
                    .append(client.getLimite()).append("\n");
        }

        // Listagem de todas as transações dos clientes mortos
        for (Operation operation : deathOper) {
            sb.append("- id:").append(operation.getId()).append(" ").append(operation.getLabel()).append(":")
                    .append(operation.getName()).append(" ").append(operation.getValue()).append("\n");
        }

        return sb.toString();
    }

}

public class Solver {
    public static void main(String[] arg) {
        Agiota agiota = new Agiota();

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");
            try {
                if (args[0].equals("end")) {
                    break;
                } else if (args[0].equals("init")) {
                    agiota = new Agiota();
                } else if (args[0].equals("show")) {
                    print(agiota);
                } else if (args[0].equals("showCli")) {
                    print(agiota.getClient(args[1]));
                } else if (args[0].equals("addCli")) {
                    agiota.addClient(args[1], (int) number(args[2]));
                } else if (args[0].equals("give")) {
                    agiota.give(args[1], (int) number(args[2]));
                } else if (args[0].equals("take")) {
                    agiota.take(args[1], (int) number(args[2]));
                } else if (args[0].equals("kill")) {
                    agiota.kill(args[1]);
                } else if (args[0].equals("plus")) {
                    agiota.plus();
                } else {
                    println("fail: comando invalido");
                }
            } catch (Exception e) {
                println(e.getMessage());
            }
        }
    }

    private static Scanner scanner = new Scanner(System.in);

    private static String input() {
        return scanner.nextLine();
    }

    private static double number(String value) {
        return Double.parseDouble(value);
    }

    public static void println(Object value) {
        System.out.println(value);
    }

    public static void print(Object value) {
        System.out.print(value);
    }
}
