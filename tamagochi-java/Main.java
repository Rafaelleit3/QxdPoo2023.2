/*  1. fiz parte, sozinho e com ajuda de amigos e prof.
    2. demorei em média 2 aulas, fiz somente nas aulas
*/

import java.util.Scanner;

class Pet{
    private int energyMax, hungryMax, cleanMax;
    private int energy, hungry, clean;
    private int diamonds;
    private int age;
    private boolean alive;

    public Pet(int energy, int hungry, int clean){
        this.energyMax = energy;
        this.hungryMax = hungry;
        this.cleanMax = clean;

        this.diamonds = 0;
        this.age = 0;
        this.alive = true;

        this.energy = energy;
        this.hungry = hungry;
        this.clean = clean;
        
    }

    void setEnergy(int value){
        if(value <= 0){
            this.energy = 0;
            System.out.println("fail: pet morreu de fraqueza");
            this.alive = false;
            return;
        } 
        if(value > this.energyMax) {
            this.energy = this.energyMax;
            return;
        }
        else{
            this.energy = value;
        }

    }


    public void setHungry(int value){
        if(value <= 0){
            this.hungry = 0;
            System.out.println("fail: pet morreu de fome");
            this.alive = false;
            return;
        } 
        if(value > this.hungryMax) {
            this.hungry = this.hungryMax;
            return;
        }
        else{
            this.hungry = value;
        }
    }
    
    void setClean(int value){

        if(value <= 0){
            this.clean = 0;
            System.out.println("fail: pet morreu de sujeira");
            this.alive = false;
            return;
        } 
        if(value > this.cleanMax) {
            this.clean = this.cleanMax;
            return;
        }
        else{
            this.clean = value;
        }
    }
    

    private boolean testAlive() {
        if (this.alive) {
            return true;
        } else {
            return false;
        }
    }


    public String toString(){
        String ss = "";
        ss +=  "E:" + energy + "/" + energyMax + ", "
            +  "S:" + hungry + "/" + hungryMax + ", "
            +  "L:" + clean + "/" + cleanMax + ", "
            +  "D:" + diamonds + ", " + "I:"  + age;
        return ss;
    }

    public void play() {
        if (!this.testAlive()) {
            return;
        }
        setEnergy(getEnergy() - 2);
        setHungry(getHungry() - 1);
        setClean(getClean() - 3);
        diamonds += 1;
        age += 1;
    }
    
    public void shower(){
        if(!this.testAlive())
            return;
        setEnergy(getEnergy() - 3);
        setHungry(getHungry() - 1);
        setClean(this.cleanMax);
        this.diamonds += 0;
        this.age += 2;
        
    }

    public void eat(){
        if(!this.testAlive())
            return;
        setEnergy(getEnergy() - 1);
        setHungry(getHungry() + 4);
        setClean(getClean() - 2);
        this.diamonds += 0;
        this.age += 1;
        
        
    }

    public void sleep(){
        if(!this.testAlive())
            return;

        if(getEnergy() <= getEnergyMax() - 5){
            this.age += getEnergyMax() - getEnergy();
            setEnergy(this.energyMax);
            setHungry(getHungry() - 1);
        }
        else{
            System.out.println("fail: nao esta com sono");
        }
            
        
    }


    int getClean(){
        return this.clean;
    }
    int getHungry(){
        return this.hungry;
    }
    int getEnergy(){
        return this.energy;
    }
    int getEnergyMax(){
        return this.energyMax;
    }
    int getCleanMax(){
        return this.cleanMax;
    }
    int getHungryMax(){
        return this.hungryMax;
    }
}




public class Main {
    public static void main(String[] a) {
        Pet pet = new Pet(0, 0, 0);
        
        while (true) {
            var line = input();
            write("$" + line);
            var args = line.split(" ");

            if      (args[0].equals("end"))   { break;                                                                           }
            else if (args[0].equals("show"))  { write(pet.toString());                                                           }
            else if (args[0].equals("init"))  { pet = new Pet((int)number(args[1]), (int)number(args[2]), (int)number(args[3])); }
            else if (args[0].equals("play"))  { pet.play();                                                                      }
            else if (args[0].equals("eat"))   { pet.eat();                                                                       }
            else if (args[0].equals("sleep")) { pet.sleep();                                                                     }
            else if (args[0].equals("shower")){ pet.shower();                                                                    }
            else                              { write("fail: comando invalido");                                                 }
        }
    }

    private static Scanner scanner = new Scanner(System.in);
    private static String  input()              { return scanner.nextLine(); }
    private static double  number(String value) { return Double.parseDouble(value); }
    private static void    write(String value)  { System.out.println(value); }
}