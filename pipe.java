/**
 * Aggiungi qui una descrizione della classe Pipe
 * 
 * @author (il tuo nome) 
 * @version (un numero di versione o una data)
 */

import java.awt.*;

public class Pipe implements Buffer
{
    public static int DIM = 20;
    private int buffer[] = new int [DIM]; 
    private int Ndati = 0;//indica quanti dati ci sono nel buffer
    private int writePos=0;//indica la prima pos libera x scrivere
    private int  readPos=0;//indica la pos del primo dato da leggere
    public synchronized void set(int value)
        {
            String name = Thread.currentThread().getName();
            while (Ndati >=DIM)
                {try
                      {displayState ("Buffer full. " + name + " waits.");
                       wait(); //deve essere all'interno di una gestione di eccezione
                      }
                 catch (InterruptedException chisenefrega){} //
                }// fine while
            buffer[writePos]= value;
            displayState( name + " writes " + value + " in pos " + writePos);
            Interfaccia.myBufGlass[writePos].setForeground(Color.RED);
            if (writePos==DIM-1)
               {writePos= 0;}
            else 
                {writePos++;}
            Ndati++;
            notify(); //comunica di passare a ready
            displayBuffer();
        }
    public synchronized int get()
        {  int tmp;
           String name = Thread.currentThread().getName();
           while (Ndati==0 && !Interfaccia.FINITO)
               {try
                    {displayState ("Buffer empty. " + name + " waits.");
                     wait(); //deve essere all'interno di una gestione di eccezione
                    }
                 catch (InterruptedException chisenefrega){} //
               }// fine while
           if  (Ndati>0 )//
               {tmp=buffer[readPos];
                buffer[readPos]=0; //resetta il campo del buffer   
                displayState( name + " reads " + tmp + " in pos " + readPos);
                Interfaccia.myBufGlass[readPos].setForeground (Color.BLACK);
                if (readPos==DIM-1)
                   {readPos= 0;}
                else 
                    {readPos++;}
                Ndati --;
                notify(); //comunica di passare a ready
                displayBuffer();
                return tmp;
                }   
            else
                {Interfaccia.TheEnd.setText("THREADS FERMI");
                 Interfaccia.TheEnd.setForeground(Color.RED);
                 return -1;}
        }
    public void displayBuffer()
        {
            for (int i=0; i < DIM; i++)
                {Interfaccia.myBufGlass[i].setText(Integer.toString(buffer[i]));
                }
        }
        
    public void displayState (String op)
        {
            StringBuffer str = new StringBuffer (op);
            str.setLength(40);
            System.out.println(str);
        }
    public int capacity ()
        {
            return DIM;
        }
    }
