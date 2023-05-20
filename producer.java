/**
 * Aggiungi qui una descrizione della classe Producer
 * 
 * @author (il tuo nome) 
 * @version (un numero di versione o una data)
 */

public class Producer extends Thread
{   private Buffer myBuf;
    public static int ProducerN=0; //serve a distinguere le varie istanze di Producer
    public Producer (Buffer buf)
        {super ("Producer_" + ++ProducerN);
         myBuf = buf;
        }
    public void run()
    {    int sum=0;
         int tmp=0;
         final Thread curr = Thread.currentThread();
         final int index = Interfaccia.getIndex(curr);
         while (Interfaccia.myThr[index]==curr && !Interfaccia.FINITO)
             {try
                 {synchronized (this)
                     {while (Interfaccia.mySusp[index] && Interfaccia.myThr[index]==curr)
                         {sleep(500);}}}//ferma il thread}  
             catch(InterruptedException e)
                 {e.printStackTrace();}    
             try
                 {Thread.sleep((int) (Math.random()*1001*100/Interfaccia.mySpeed[index].getValue()));// sleep fno a 1 sec
                  tmp= (int)(Math.random()*10);
                  Interfaccia.myLab[index].setText("Producer_" + (index +1) + " writes " + tmp);
                  sum=sum+tmp;
                  myBuf.set(tmp);}
             catch (InterruptedException e)
                 {e.printStackTrace();}
         }//fine while
         System.out.println("**** " + getName()+ " wrote value for " + sum + " ****");
         System.out.println("Terminating " + getName());
         ProducerN--;
        } //fine run
}//fine Producer
