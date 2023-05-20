/**
 * Aggiungi qui una descrizione della classe Interfaccia
 * 
 * @author (il tuo nome) 
 * @version (un numero di versione o una data)
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*; //per HashTable

public class Interfaccia extends JFrame //implements ActionListener
{
    public  static final int NProd=2;//serve a Consumer per il progressivo di Thread
    private static final int NCons=2;
    private static final int NThread = NProd+NCons;
    Buffer myBuf = new Pipe();
    private static int myBufDim;
    public static JLabel myBufGlass[];
    public static JLabel myLab[];
    public static JCheckBox myBox[];
    public static Thread myThr[];
    public static boolean mySusp[];
    public static boolean FINITO = false;//comunica lo stop a Trhead
    public static JSlider mySpeed[];
    final int min=10; //valore minimo
    final int max=100;//valore massimo
    final int def=50; //valore di default
    private JButton myStart;
    private JButton myStop;
    public static JLabel TheEnd;
    public void init()
    {   Container ExtCont;
        JPanel IntCont;
        myLab = new JLabel [NThread];
        myBox = new JCheckBox [NThread];
        myThr = new Thread [NThread];
        mySusp = new boolean [NThread];
        mySpeed = new JSlider[NThread];
        myBufDim= myBuf.capacity();
        myBufGlass= new JLabel[myBufDim];
        ExtCont= getContentPane();
        ExtCont.setLayout(new BorderLayout(30,30));
        IntCont = new JPanel(new GridLayout(NThread+1,3,15,5)); //3 colonne
        
        for (int i=0; i<NThread;i++)
        {
            myLab[i]=new JLabel();
            myLab[i].setFont(new Font("Verdana",1,12));
            myLab[i].setBackground (Color.GREEN);
            myLab[i].setOpaque(true);
            IntCont.add (myLab[i]);
            IntCont.add (addSlider(i));
            myBox[i]=new JCheckBox ("Sospeso");
            myBox[i].setFont(new Font("Verdana",1,12));
            myBox[i].setToolTipText("Consente di sospendere/riattivare il Thread");
            myBox[i].addActionListener(
                new ActionListener()
                {public void actionPerformed(ActionEvent e)
                    {changeBox(e);}
                });//   
            IntCont.add (myBox[i]);
            setVisible(true);
        }
        ///// Aggiunge il bottone START
        myStart = new JButton ("START");
        myStart.setToolTipText("Fa partire i Threads");
        myStart.addActionListener(
            new ActionListener()
            {public void actionPerformed(ActionEvent e)
                {start();}
            });//
        IntCont.add (myStart);
        ///// Aggiunge il bottone STOP
        myStop = new JButton ("STOP");
        myStop.setToolTipText("Fa fermare i Threads");
        myStop.addActionListener(
            new ActionListener()
            {public void actionPerformed(ActionEvent e)
                {stop();}
            });//
        IntCont.add (myStop);
        
        TheEnd=new JLabel();
        TheEnd.setText("THREADS FERMI");
        IntCont.add (TheEnd);
        
        ExtCont.add (IntCont,BorderLayout.CENTER);
        ///// Aggiunge la visualizzazione del buffer
        FlowLayout Flow = new FlowLayout();
        Flow.setAlignment(FlowLayout.LEFT); 
        JPanel c = new JPanel(Flow);
        
        for (int i=0; i<myBufDim; i++)
            {myBufGlass[i]=new JLabel();
             myBufGlass[i].setText(""+0);
             c.add(myBufGlass[i]);
            }
        ExtCont.add(c, BorderLayout.SOUTH);

        setSize(650,80*NThread+200);
        setVisible(true);
        
    }// fine init

    public void start()
    {   for (int i =0; i < NThread;i++)
        {   FINITO = false; //per eventuale riavvio    
            if (i<NProd)
                {myThr[i]=new Producer(myBuf);
                 myThr[i].start();}
            else
                {myThr[i]=new Consumer(myBuf);
                 myThr[i].start();}   
        }
        TheEnd.setText("THREADS AVVIATI");
        TheEnd.setForeground(new Color(0,153,0));
        return;
    }// fine start
    
    public void stop()
    {   FINITO = true;
        TheEnd.setText("TERMINO I THREADS");
        TheEnd.setForeground(Color.BLUE);
    }
    
    public static void main(String[] args)
    {
       Interfaccia myApp = new Interfaccia();
       myApp.init();
       myApp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
    }
    
    public synchronized void changeBox(ActionEvent myEv)
    {   for (int i =0; i < NThread;i++)
        {
            if (myBox[i]==myEv.getSource())
                {mySusp[i]= !mySusp[i];
                 myLab[i].setBackground(mySusp[i]?Color.RED:Color.GREEN);
                 if (!mySusp[i])
                     {notifyAll();} //se riavviato, si assicura che parta     
                 return;
                }    
        }  
    }
   
    public static int getIndex(Thread curr)
    {   for (int i =0; i < NThread;i++)
        {
            if (myThr[i]==curr)
                {
                    return i;
                }
        }//fine for
        return -1;
    }
    public JPanel addSlider(int i)
        {   
         JPanel p= new JPanel();
         JLabel l= new JLabel();
         l.setText("Velocità Thread");
         mySpeed[i]= new JSlider(JSlider.HORIZONTAL,min,max,def);  
         mySpeed[i].setPaintTicks(true); //voglio vedere le tacche sul cursore
         mySpeed[i].setPaintLabels(true); //voglio vedere i valori sul cursore
         mySpeed[i].setMinorTickSpacing(10);// una tacca ogni 10
         mySpeed[i].setLabelTable(creaEtichette());
         mySpeed[i].addChangeListener(
            new ChangeListener()
            {public void stateChanged (ChangeEvent e)
                {changeSpeed(e);}
            });
         p.add(l);
         //p.setPreferredSize(new Dimension(180, 70));
         p.add(mySpeed[i]);
         return (p);
        }
    protected Hashtable<Integer,String> creaEtichette()
        {
        /*class Etichetta extends JLabel
            {public Etichetta (String lab)
                 {super(lab);
                  setForeground(Color.BLACK);
                  setFont(new Font("Verdana",1,11));                             
                 }

            }*/
        /*String minimo="min";
        Etichetta massimo=new Etichetta("min");
        Etichetta normale=new Etichetta("normal");*/
        Hashtable<Integer,String> eti = new Hashtable<Integer,String>();
        eti.put(Integer.valueOf(10) , "min");
        eti.put(Integer.valueOf(100) , "max");
        eti.put(Integer.valueOf(50) , "normal");
        return(eti);
        }
    public synchronized void changeSpeed(ChangeEvent e)
        {String str="";
         String name="";   
         JSlider source = (JSlider)e.getSource();
         if(!source.getValueIsAdjusting()) //aspetto che sia arrivato all'ultimo valore
             {for (int i=0; i<NThread;i++)
                 {if (e.getSource()==mySpeed[i])
                      {str="nuova velocità thread = " + mySpeed[i].getValue();
                      name= myThr[i].getName();
                      }
                 }//fine for
              JOptionPane.showMessageDialog(null, str,name,JOptionPane.INFORMATION_MESSAGE); 
            }  
        }
   }//fine classe Interfaccia
