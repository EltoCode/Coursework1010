/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1010coursework;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Elton John Fernandes
 */
public class Fuel_Server {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // TODO code application logic here
        
        boolean open = true;
        
        ServerSocket Server = new ServerSocket(6789);
        //String fuelPrices = new String("1.05,1.03\n");
        
        //Creating a printwriter to write to history file
        PrintWriter toRFile = new PrintWriter(new FileWriter("resources/Trip_History.csv"));
        toRFile.println("Fuel Price, Distance, Fuel Efficiency, Trip Cost");
        
        while(open)
        {
            Socket client = Server.accept(); //accepting a connection
            
            //Creating a Reader for Client Data and Reading
            ObjectInputStream fromClient = new ObjectInputStream(client.getInputStream());
            FuelData fuelPcktIN = (FuelData) fromClient.readObject();
            
            if(fuelPcktIN.getFuelType()==-1)
            {
                open = false;
                break;
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
            
            
            
            ObjectOutputStream toClient = new ObjectOutputStream(client.getOutputStream());
            toClient.writeObject(fuelPcktIN);
            
            //Printing the result to File
                    toRFile.println(fuelPcktIN.getFuelCost() +" ,"+ fuelPcktIN.getTripDist() +" ,"
                                                  + fuelPcktIN.getFuelEff() +" ,"+ fuelPcktIN.getResult());
                    
                    toRFile.flush(); //Flushes the stream so that an output is obtained
            
            /*PrintWriter toClient = new PrintWriter(client.getOutputStream(), true);
            toClient.println(fuelPrices);*///Used for older ver.
        }
        
        toRFile.close();
    }
    
    /**
     * Takes the required parameters and returns the calculated trip cost
     * @param dist
     * @param fuelEff_mpG
     * @param fuelCost
     * @return The calculated cost
     * @throws NumberFormatException 
     */
    public static double tripCostCalc(double dist, double fuelEff_mpG, String fuelCost) throws NumberFormatException
    {
        double tripCost = 0.0;
        
        double fuelEff_mpL = fuelEff_mpG * 0.22;
        
        tripCost = dist * Double.parseDouble(fuelCost)/(fuelEff_mpL); 
        
        
        return Math.round(tripCost * 100.0) / 100.0;
    }
    
}
