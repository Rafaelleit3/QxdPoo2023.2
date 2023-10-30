import java.util.*;

abstract class Funcionario{

    protected int bonus;
    protected int diarias;
    protected int maxDiarias;
    protected String nome;
    
    public Funcionario(String nome){
        this.nome = nome;
    }
    
    public void addDiaria() throws Exception{
        
        if(this.diarias + 1 > maxDiarias){
            throw new Exception("fail: limite de diarias atingido");
        }
        
        this.diarias++;
        
    }
    
    public String getNome(){
        return this.nome;
    }
    
    public int getSalario(){
        
        return this.bonus + diarias*100;
    }
    
    public void setBonus(int bonus){
        this.bonus = bonus;
    }
}

class Professor extends Funcionario{
    protected String classe;
    
    public Professor(String nome, String classe){
        super(nome);
        super.maxDiarias = 2;
        this.classe = classe;
    }
    
    public String getClasse(){
        return this.classe;
    }
    
    @Override
    public int getSalario(){
        if(this.classe.equals("A")) return 3000 + super.getSalario();
        else if(this.classe.equals("B")) return 5000 + super.getSalario();
        else if(this.classe.equals("C")) return 7000 + super.getSalario();
        else if(this.classe.equals("D")) return 9000 + super.getSalario();
        else return 11000 + super.getSalario();
        
    }
    
    public String toString(){
        return "prof:" + super.getNome() + ":" + getClasse() + ":" + getSalario();
    }
}

class Sta extends Funcionario{
    protected int nivel;
    
    public Sta(String nome, int nivel){
        super(nome);
        super.maxDiarias = 1;
        this.nivel = nivel;
    }
    
    
    public int getNivel(){
        return this.nivel;
    }
    
    @Override
    public int getSalario(){
        return super.getSalario() + 3000 + (300 * this.nivel);
    }
    
    @Override
    public String toString(){
        return "sta:" + super.getNome() + ":" + this.nivel + ":" + getSalario();
    }
}

class Terceirizado extends Funcionario{
    protected int horas;
    protected boolean isSalubre = false;
    
    public Terceirizado(String nome, int horas, String isSalubre){
        super(nome);
        this.horas = horas;
        if(isSalubre.equals("sim")){
            this.isSalubre = true;
        }
    }
    
    public int getHoras(){
        return this.horas;
    }
    
    @Override
    public void addDiaria() throws Exception{
            throw new Exception("fail: terc nao pode receber diaria");
    }
    
    public boolean getisSalubre(){
        return this.isSalubre;
    }
    
    @Override
    public int getSalario(){
        if(getisSalubre()){
            return this.horas * 4 + 500;
        }
        return this.horas * 4;
        
    }
    
    @Override
    public String toString(){
        String ss = (isSalubre) ? "sim" : "nao";
        return "ter:" + super.getNome() + ":" + getHoras() + ":" + ss + ":" + getSalario();
    }
    
}


class UFC{
   private Map<String, Funcionario> funcionarios;
   
   public UFC(){
       this.funcionarios = new TreeMap<String, Funcionario>();
   }
   
   public void addFuncionario(Funcionario funcionario){
       this.funcionarios.put(funcionario.getNome(), funcionario);
   }
   
   public Funcionario getFuncionario(String nome){
       Funcionario funcionario = this.funcionarios.get(nome);
       if(this.funcionarios.containsKey(nome)){
           return funcionario;
       }
       return null;
   }
   
   public void rmFuncionario(String nome){
       this.funcionarios.remove(nome);
       
   }
   
   public void setBonus(int bonus){
       int bonusDividido = (bonus / funcionarios.size());
       for(Funcionario fun: funcionarios.values()){
           fun.setBonus(bonusDividido);
       }
   }
   
   public String toString(){
       StringBuilder sb = new StringBuilder();
       
       for(Funcionario fun: funcionarios.values()){
           if(fun instanceof Professor){
               sb.append(fun.toString()).append("\n");
           }
           else if(fun instanceof Sta){
                sb.append(fun.toString()).append("\n");
           }
           else if(fun instanceof Terceirizado){
               sb.append(fun.toString()).append("\n");
           }
       }
       sb.setLength(sb.length() - 1); //retirar o \n do final
       
       return sb.toString();
   }
   
}

class Solver{
    
    static Scanner in = new Scanner(System.in);
    
    public static void main(String[] arg){
        UFC ufc = new UFC();
        
        while(true){
         try{
             String line = in.nextLine();
             System.out.println("$" + line);
             String args[] = line.split(" ");
             
             if("end".equals(args[0]))  {break;}
             else if("addProf".equals(args[0])) {ufc.addFuncionario(new Professor(args[1], args[2]));}
             else if("addSta".equals(args[0]))  {ufc.addFuncionario(new Sta(args[1], Integer.parseInt(args[2])));}
             else if("addTer".equals(args[0]))  {ufc.addFuncionario(new Terceirizado(args[1], Integer.parseInt(args[2]), args[3]));}
             else if("showAll".equals(args[0])) {System.out.println(ufc);}
             else if("rm".equals(args[0]))      {ufc.rmFuncionario(args[1]);}
             else if("addDiaria".equals(args[0])) {ufc.getFuncionario(args[1]).addDiaria();}
             else if("setBonus".equals(args[0]))  {ufc.setBonus(Integer.parseInt(args[1]));}
             else if("show".equals(args[0]))    {System.out.println(ufc.getFuncionario(args[1]));}
             else {System.out.println("fail: comando invalido");}
             
             
         }catch(Exception e){
             System.out.println(e.getMessage());
         } 
            
            
        }
        
    }
}
