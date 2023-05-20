
/**
 * Aggiungi qui una descrizione della classe Consumer
 * 
 * @author (il tuo nome) 
 * @version (un numero di versione o una data)
 */
public class Consumer extends Thread
{
    private Buffer myBuf;
    private static int ConsumerN=0; //serve a distinguere le varie istanze di Producer
    public Consumer (Buffer buf)
        {   super("Consumer_" + ++ConsumerN);
            myBuf = buf;
        }
    public void run ()
        {int sum=0;
         int tmp=0;
         final Thread curr = Thread.currentThread();
         final int index = Interfaccia.getIndex(curr);
         boolean Svuotato = false;
         while (Interfaccia.myThr[index]==curr && ! Interfaccia.FINITO)
         {    try
                 {synchronized (this)
                     {while (Interfaccia.mySusp[index] && Interfaccia.myThr[index]==curr)
                         {sleep(500);}}}//ferma il thread, non serve wait... c'è il while}  
             catch(InterruptedException e)
                 {e.printStackTrace();}  
             try
                 {Thread.sleep ((int) (Math.random()*1001)*100/Interfaccia.mySpeed[index].getValue());
                  tmp=myBuf.get();
                  if (tmp!=-1) 
                     {Interfaccia.myLab[index].setText("Consumer_" + (index +1 - Interfaccia.NProd) + " reads " + tmp);
                      sum=sum+tmp;}
                  }
             catch(InterruptedException e)
                {e.printStackTrace();}
         }//fine while
         
         while (!Svuotato) //svuoto il buffer
         {    try
                 {Thread.sleep ((int) (Math.random()*1001));
                  tmp=myBuf.get();
                  if (tmp==-1) 
                      {Svuotato=true;}
                  else
                     {Interfaccia.myLab[index].setText("Consumer_" + (index +1 - Interfaccia.NProd) + " reads " + tmp);
                      sum=sum+tmp;}
                 }
             catch(InterruptedException e)
                {e.printStackTrace();}
         }//fine while
                 
         System.out.println("**** " + getName()+ " read value for " + sum + " ****");
         System.out.println("Terminating " + getName());
         ConsumerN--;
        }//fine run
}// fine Consumer
