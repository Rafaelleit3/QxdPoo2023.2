package Porquinho;
import java.util.*;
import java.util.stream.Collectors;
import java.text.DecimalFormat;

enum Coin{
    
    C10(0.10, 1, "C10"),
    C25(0.25, 2, "C25"),
    C50(0.50, 3, "C50"),
    C100(1.00, 4, "C100");
    
    private double value;
    private int volume;
    private String label;
    
    
    
    Coin (double value, int volume, String label){
        this.value = value;
        this.volume = volume;
        this.label = label;
    }
    
    public double getValue(){
        return this.value;
    }
    
    public int getVolume(){
        return this.volume;
    }
    
    public String getLabel(){
        return this.label;
    }
    
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(getValue()) + ":" + getVolume();
    }
    
    
}

class Item {

    private String label;
    private int volume;

    public Item(String label, int volume) {
        this.label = label;
        this.volume = volume;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return getLabel() + ":" + getVolume();
    }
}

class Pig {

    private boolean broken;
    private List<Coin> coins;
    private List<Item> items;
    private int volumeMax;

    public Pig(int volumeMax) {
        this.volumeMax = volumeMax;
        this.broken = false;
        this.coins = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public Coin createCoin(String valor) {
        switch (valor) {
            case "10":
                return Coin.C10;
            case "25":
                return Coin.C25;
            case "50":
                return Coin.C50;
            case "100":
                return Coin.C100;
            default:
                return null;
        }
    }

    public boolean addCoin(Coin coin) throws Exception {
        if(isBroken()){
            throw new Exception("fail: the pig is broken");
        }
        
        if(getVolume() + coin.getVolume() > getVolumeMax()){
            throw new Exception("fail: the pig is full");
        }
        
        coins.add(coin);
        return true;
        
    }

    public boolean addItem(Item item) throws Exception {
        if(isBroken()){
            throw new Exception("fail: the pig is broken");
        }
        
        if(getVolume() + item.getVolume() > getVolumeMax()){
            throw new Exception("fail: the pig is full");
        }
        
        items.add(item);
        return true;
    }

    public boolean breakPig() {
        if(isBroken()){
            return false;
        }
        return this.broken = true;
    }

    public ArrayList<String> extractCoins() throws Exception {
        if(!isBroken()){
            System.out.println("fail: you must break the pig first");
            return new ArrayList<>(); //não está quebrado;
        }
        
        List<Coin> extractCoins = new ArrayList<>(coins);
        coins.clear(); //pegar as moedas, limpar o vetor de moedas
        
        ArrayList<String> extractCoinString = new ArrayList<>(); //array de strings
        
        for(Coin coin: extractCoins){
           extractCoinString.add(coin.toString()); //adiconando as moedas
        }
        
        return extractCoinString; //retorna vetor de strings de moedas
    }

    public ArrayList<String> extractItems() throws Exception {
        if(!isBroken()){
            System.out.println("fail: you must break the pig first");
            return new ArrayList<>();
        }
        
        List<Item> extractItems = new ArrayList<>(items);
        items.clear();
        
       ArrayList<String> extractItemString = new ArrayList<>();
        
        for(Item item: extractItems){
           extractItemString.add(item.toString());
        }
        return extractItemString; //retorna vetor de Strings de itens
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        double value = getValue();
        texto.append("state=");
        
        try{
            if(isBroken()){
                texto.append("broken : ");
            } 
            else{
                texto.append("intact : ");     
            } 
            //state=intact : coins=[0.10:1, 0.50:3] : items=[] : value=0.60 : volume=4/20
            texto.append("coins=[");
            for(Coin coin: coins){
                texto.append(coin.toString()).append(", ");
            }
            if(!coins.isEmpty()){
                texto.setLength(texto.length() - 2); //remover virgula e espaço do final
            }
            texto.append("] ").append(":").append(" "); 
            
            texto.append("items=[");
            for(Item item: items){
                texto.append(item.toString()).append(", ");
            }
            if(!items.isEmpty()){
                texto.setLength(texto.length() - 2); //remover virgula e espaço do final
            }
            texto.append("] ").append(":").append(" ");
            
            DecimalFormat df = new DecimalFormat("0.00");
            texto.append("value=").append(df.format(value)).append(" ").append(":").append(" ")
                .append("volume=").append(getVolume()).append("/").append(getVolumeMax());
                
        } catch (Exception e){
            texto.append("Error: ").append(e.getMessage());
        }
        
        return texto.toString();
    }

    public int getVolume() {
        int volume = 0;
        if(isBroken()){
            volume = 0;
        }
        else{
            for(Coin coin : coins){
                volume += coin.getVolume();
            }
            
            for(Item item : items){
                volume += item.getVolume();
            }
        }
        
        return volume;
    }

    public double getValue() {
        double value = 0;
        for(Coin coin: coins){
            value += coin.getValue();
        }
        return value;
    }

    public int getVolumeMax() {
        return this.volumeMax;
    }

    public int getVolumeRestante() {
        return getVolumeMax() - getVolume();
    }

    public boolean isBroken() {
        return this.broken;
    }
}


public class Solver {
    public static void main(String[] arg) {
        Pig pig = new Pig(5);

        while (true) {
            String line = input();
            println("$" + line);
            String[] args = line.split(" ");
            try{
                if      (args[0].equals("end"))          { break; }
                else if (args[0].equals("init"))         { pig = new Pig( (int) number(args[1]) ); }
                else if (args[0].equals("show"))         { println(pig); }
                else if (args[0].equals("addCoin"))      { pig.addCoin( pig.createCoin( args[1] ) ); }
                else if (args[0].equals("addItem"))      { pig.addItem( new Item( args[1], (int) number(args[2]) ) ); }
                else if (args[0].equals("break"))        { pig.breakPig(); }
                else if (args[0].equals("extractCoins")) { println("[" + String.join(", ", pig.extractCoins()) + "]"); }
                else if (args[0].equals("extractItems")) { println("[" + String.join(", ", pig.extractItems()) + "]"); }
                else                                     { println("fail: comando invalido"); }
        
                } catch (Exception e){
                    println(e.getMessage());
                }
            }
    }

    private static Scanner scanner = new Scanner(System.in);
    private static String  input()                { return scanner.nextLine();        }
    private static double  number(String value)   { return Double.parseDouble(value); }
    public  static void    println(Object value)  { System.out.println(value);        }
    public  static void    print(Object value)    { System.out.print(value);          }
}
