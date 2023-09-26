
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
//import java.text.DecimalFormat;

class Slot{
    private String name;
    private float price;
    private int quantity;
    
    public Slot(String name, int quantity, float price){
        setName(name);
        setPrice(price);
        setQuantity(quantity);
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setPrice(float price){
        this.price = price;
    }
    
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    
    public String toString(){
        return getName() + " : " + getQuantity() + " U : " + String.format("%.2f", getPrice()) + " RS" ;
    }
    
    public String getName(){
        return this.name;
    }
    
    public int getQuantity(){
        return this.quantity;
    }
    
    public float getPrice(){
        return this.price;
    }
    
}


class VendingMachine{
    private List<Slot> slots;
    private float profit;
    private float cash;
    
    public VendingMachine(){
        
    }
    
    public VendingMachine(int capacity){
        this.slots = new ArrayList<>();
        
        for(int i = 0; i < capacity; i++){
            this.slots.add(new Slot("empty", 0, 0f));
        }
        
    }
    
    public Slot getSlot(int index){
        if(slots != null){
            if(index >= 0 && index < this.slots.size()){
                return this.slots.get(index);
            }
        }
        return null;
    }
    
    public void setSlot(int index, Slot slot){
        if(slots != null){
          if(getSlot(index) != null){
              this.slots.set(index, slot);
          }
          else System.out.println("fail: indice nao existe");
        }
        else System.out.println("fail: espiral sem produtos");
    }
    
    public void clearSlot(int index){
        if(slots != null){
            if(getSlot(index) != null){
                this.slots.set(index, new Slot("empty", 0, 0f));
            }
        }
    }
    
    public void insertCash(float cash){
        
        this.cash += cash;
    }
    
    public float withdrawCash(){
        System.out.println("voce recebeu " + String.format("%.2f", getCash()) + " RS");
        return this.cash = 0f;
    }
    
    public float getCash(){
        return this.cash;
    }
    
    public float getProfit(){
        return this.profit;
    }
    
    public void buyItem(int index){
        if(getSlot(index) != null){
            Slot produto = this.slots.get(index);
            if(getCash() >= produto.getPrice()){
                if(produto.getQuantity() > 0){
                   produto.setQuantity(produto.getQuantity() - 1);
                   this.cash = this.cash - produto.getPrice();
                   System.out.println("voce comprou um " + produto.getName());
                }
                else System.out.println("fail: espiral sem produtos");
            }
            else System.out.println("fail: saldo insuficiente");
        }
        else System.out.println("fail: indice nao existe");
    }
    
    public String toString(){
        String saida = "saldo: " + String.format("%.2f", getCash()) + "\n";
        if(slots != null){
            for(int i = 0; i < this.slots.size(); i++){
                if("empty".equals(this.slots.get(i).getName())){
                    saida += i + " [   "+ this.slots.get(i);
                }
                else{
                    saida += i + " [ " + this.slots.get(i);
                }
                
                if(i != this.slots.size()-1){
                    saida += "]\n";
                }
                else saida += "]";
            }
        }
        
        return saida;
    }
    
    
}

class JunkFood{
    
    static Scanner scanner = new Scanner(System.in);
    public static String input() { return scanner.nextLine();}
    public static void write(String value) { System.out.println(value);}
    public static float number(String value) {return Float.parseFloat(value);}
    
    public static void main(String[] arg){
        VendingMachine machine = new VendingMachine();
        
        while(true){
            String line = input();
            var args = line.split(" ");
            write('$' + line);
            
            if ("end".equals(args[0])) {break;}
            else if ("init".equals(args[0])) { machine = new VendingMachine((int)number(args[1]));}
            else if ("show".equals(args[0])) { write(machine.toString());}
            else if ("set".equals(args[0])) { machine.setSlot((int)number(args[1]), new Slot(args[2], (int) number(args[3]), number(args[4])));}
            else if ("dinheiro".equals(args[0])) { machine.insertCash((int) number(args[1]));}
            else if ("comprar".equals(args[0])) { machine.buyItem((int) number(args[1]));}
            else if ("troco".equals(args[0])) { machine.withdrawCash();}
            else if ("limpar".equals(args[0])) { machine.clearSlot((int)number(args[1]));}
            else { System.out.println("fail: comando invalido");}
            
            
        }
        
        
    }
    
    
}