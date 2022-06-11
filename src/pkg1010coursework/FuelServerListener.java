/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1010coursework;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import static pkg1010coursework.Fuel_Server.tripCostCalc;

/**
 * This is a thread created by a server to handle a client
 * constructor(Socket client, int cID)
 * @since 6.0
 * @author Elton John Fernandes
 */
public class FuelServerListener implements Runnable {
    
    private boolean open;
    private Socket client;
    private int cID;

    /**
     * Standard Constructor 
     * @param client The Client Socket
     * @param cID The Id of the client Socket
     */
    public FuelServerListener(Socket client, int cID) {
        this.open = true;
        this.client = client;
        this.cID = cID;
    }
    
    

    @Override
    public void run() {
        
        String rFileDest = "resources/Trip_History"+cID+".dat";
        
        //Creating a printwriter to write to history file
        try(ObjectOutputStream toRFile = 
                    new ObjectOutputStream(new FileOutputStream(rFileDest)))
        {
            //Creating a Write for Client dat
            ObjectOutputStream toClient = new ObjectOutputStream(client.getOutputStream());
            //Creating a Reader for Client Data
            ObjectInputStream fromClient = new ObjectInputStream(client.getInputStream());
            
            while(open)
            {            
                //Reading from Client
                FuelData fuelPcktIN = (FuelData) fromClient.readObject();

                if(fuelPcktIN.getFuelType()==-1)   //Checks for a specific Flag Packet
                {
                    open = false;                  //Shuts down the server
                    break;
                }
                else if(fuelPcktIN.getFuelType()==-2) //Checks for a specific Flag Packet that says store obj
                {
                   //This function is redacted 
                    
                   /* if(FuelSTORAGE.lastObj.fuelCost != -1.0)
                    {  
                        //FuelSTORAGE.storeObj(FuelSTORAGE.lastObj);                   
                    }
                    continue;*/
                }
                else if(fuelPcktIN.getFuelType()==-3) //Checks for a specific Flag Packet that says disp all obj
                {
                    FuelSTORAGE fSto = new FuelSTORAGE();

                    ObjectInputStream fromRFile = new ObjectInputStream(new FileInputStream(rFileDest));

                    boolean rFileOpen = true;

                    while(rFileOpen)
                    {
                        try{
                            fSto.genArrList((FuelData) fromRFile.readObject());
                            System.out.println("reading from " + rFileDest);
                        }
                        catch(EOFException Ex)
                        {
                            rFileOpen = false;
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(FuelServerListener.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }                    


                    toClient.writeObject(fSto);

                    continue;
                }

                //Creating a Reader for Fuel Prices and Reading
                BufferedReader fromFile = new BufferedReader(new FileReader("resources/Fuel_Price.csv"));
                String[] fuelPrices = fromFile.readLine().split(",");


                //Checking for fuel type and then calling the function to set the result value
                switch(fuelPcktIN.getFuelType())
                {
                    case 0:
                        fuelPcktIN.setFuelCost(Double.parseDouble(fuelPrices[0])); //sets the fuel cost

                        fuelPcktIN.setResult(tripCostCalc(fuelPcktIN.getTripDist()  //calculates and sets result
                                                , fuelPcktIN.getFuelEff(), fuelPrices[0]));
                        break;

                    case 1:
                        fuelPcktIN.setFuelCost(Double.parseDouble(fuelPrices[1])); //sets the fuel cost

                        fuelPcktIN.setResult(tripCostCalc(fuelPcktIN.getTripDist()  //calculates and sets result
                                                , fuelPcktIN.getFuelEff(), fuelPrices[1]));
                        break;

                    default:
                        System.out.println("You shouldn't be here, Server");
                        break;
                }

                FuelSTORAGE.lastObj = fuelPcktIN;

                //redacted used for older version
                //ObjectOutputStream toClient = new ObjectOutputStream(client.getOutputStream());
                
                toClient.writeObject(fuelPcktIN);

                //Printing the result to File
                toRFile.writeObject(fuelPcktIN);
                System.out.println("Wrting to " + rFileDest);

                toRFile.flush(); //Flushes the stream so t1000hat an output is obtained

                /*PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
                toClient.println(fuelPrices);*///Used for older ver.
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FuelServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FuelServerListener.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FuelServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }
    
}
