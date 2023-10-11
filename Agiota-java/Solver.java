
import java.lang.ref.Cleaner.Cleanable;
import java.util.*;

enum Label{
    GIVE,
    TAKE,
    PLUS;

    @Override
    public String toString(){
        switch(this.name()){
            case GIVE:
                return "give";
            case TAKE:
                return "take";
            case PLUS:
                return "plus";
            default:
                return null;
        }
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
        return getName() + " " + getLimite();
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

    //XXX public void addOperation(String name, Label label, int value)
    public void addOperation(Operation operation) {
        if(operations == null){
            operations = new ArrayList<>();
        }
        operations.add(operation);
    }
    //quanto esta devendo
    public int getBalance() {
        int divida = 0;
        for(Operation operation: operations){
            if (operation.getLabel().equals("GIVE")) {
                divida += operation.getValue();
            }
            if (operation.getLabel().equals("TAKE")) {
                divida -= operation.getValue();
            }
            if (operation.getLabel().equals("PLUS")) {
                divida += operation.getValue();
            }
        }

        return divida;
    }
}

class Operation {
    private static int nextOpId = 0;
    private int id;
    private String name;
    private Label label;
    private int value;

    public Operation( String name, Label label, int value ) {
        this.id = Operation.nextOpId++;
        this.name = name;
        this.label = label;
        this.value = value;
        //this.id = Operation.nextOpId;
        //Operation.nextOpId++;
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



class Agiota {
    private ArrayList<Client> aliveList;
    private ArrayList<Client> deathList;
    private ArrayList<Operation> aliveOper;
    private ArrayList<Operation> deathOper;

    private int searchClient(String name) {
        for(Client client: aliveList){
            if(client.getName().equals(name)){
                return client.getLimite();
            }
        }
        System.out.println("fail: cliente nao encontrado");
        return -1;
    }
    
    
    private void pushOperation(Client client, Operation operation) {
        client.addOperation(operation);
        aliveOper.add(operation);
    }

    private void sortAliveList() {
        this.aliveList.sort( new Comparator<Client>() {
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

    public Client getClient(String name) {
        for(Client client: aliveList){
            if(client.getName().equals(name)){
                return client;
            }
        }
        System.out.println("fail: cliente nao encontrado");
        return null;
    }

    public void addClient(String name, int limite) {
        this.aliveList.add(new Client(name, limite));
        this.sortAliveList();
    }

    public void give(String name, int value) {
        Client client = getClient(name);

        if(client != null){
            Operation operation = new Operation(name, Label.GIVE, value);
            pushOperation(client, operation);
        }
        
    }

    public void take(String name, int value) {
        Client client = getClient(name);
        
        if(client != null){
            Operation operation = new Operation(name, Label.TAKE, value);
            pushOperation(client, operation);
        }


    }

    public void kill(String name) {
        Client client = getClient(name);

        this.deathList.add(client);
        this.aliveList.remove(client);

    }

    public void plus() {
        for (Client client : aliveList) {
            Operation operation = new Operation(client.getName(),Label.PLUS, (client.getBalance() * 10/100));
            pushOperation(client, operation);
        }
    }

    @Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    
    // Listagem dos clientes vivos
    for (Client client : aliveList) {
        sb.append(") ").append(client.getName()).append(" ").append(client.getLimite()).append("\n");

        // Listagem das transações do cliente
        for (Operation operation : client.getOperations()) {
            sb.append("id:").append(operation.getId()).append(" ").append(operation.getLabel()).append(":")
                .append(client.getName()).append(" ").append(operation.getValue()).append("\n");
        }
    }

    // Listagem dos clientes mortos
    for (Client client : deathList) {
        sb.append("( ").append(client.getName()).append(" ").append(client.getLimite()).append("\n");

        // Listagem das transações do cliente
        for (Operation operation : client.getOperations()) {
            sb.append("id:").append(operation.getId()).append(" ").append(operation.getLabel()).append(":")
                .append(client.getName()).append(" ").append(operation.getValue()).append("\n");
        }
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

            if      (args[0].equals("end"))     { break; }
            else if (args[0].equals("init"))    { agiota = new Agiota(); }
            else if (args[0].equals("show"))    { print(agiota); }
            else if (args[0].equals("showCli")) { print( agiota.getClient( args[1] ) ); }
            else if (args[0].equals("addCli"))  { agiota.addClient( args[1], (int) number(args[2]) ); }
            else if (args[0].equals("give"))    { agiota.give( args[1], (int) number(args[2]) ); }
            else if (args[0].equals("take"))    { agiota.take( args[1], (int) number(args[2]) ); }
            else if (args[0].equals("kill"))    { agiota.kill( args[1] ); }
            else if (args[0].equals("plus"))    { agiota.plus(); }
            else                                { println("fail: comando invalido"); }
        }
    }

    private static Scanner scanner = new Scanner(System.in);
    private static String  input()                { return scanner.nextLine();        }
    private static double  number(String value)   { return Double.parseDouble(value); }
    public  static void    println(Object value)  { System.out.println(value);        }
    public  static void    print(Object value)    { System.out.print(value);          }
}
