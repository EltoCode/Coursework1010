/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1010coursework;

import java.util.ArrayList;

/**
 * This is a sub class of FuelData, also used as a communication class
 * however, it is only used when the client requests all previous data
 * @author Elton John Fernandes
 */
public class FuelSTORAGE extends FuelData{
    
    ArrayList<FuelData> TripHistory = new ArrayList<>();
    
    static FuelData lastObj = new FuelData();
    
    /**
     * Default Constructor with Fuel Type set to -1 in case of emergency 
     */
    public FuelSTORAGE() {
        super(0.0, 0.0, -1, 0.0);
    }

    /**
     * 
     * @param tripDist The Trip Distance 
     * @param fuelEff  The Fuel Efficiency
     * @param fuelType The Fuel Type
     * @param result   The Result 
     */
    public FuelSTORAGE(double tripDist, double fuelEff, int fuelType, double result) {
        super(tripDist, fuelEff, fuelType, result);
    }

    /**
     * gets the Trip History
     * @return TripHistory as an ArrayList
     */
    public ArrayList<FuelData> getTripHistory() {
        return TripHistory;
    }

    /**
     * sets the Trip History
     * @param TripHistory The ArrayList that holds the Trip History 
     */
    public void setTripHistory(ArrayList<FuelData> TripHistory) {
        this.TripHistory = TripHistory;
    }
    
    /**
     * Stores an object into the array list 
     * @deprecated since 3.0
     * @param fD The Fuel OBJ
     */
    public void storeObj(Object fD)
    {
        TripHistory.add((FuelData) fD);
    }
    
    /**
     * Stores an object of FuelData into the array list 
     * New method, works better
     * @param dat An Object of Fuel Data that needs to be stored 
     */
    public void genArrList(FuelData dat)
    {
        TripHistory.add(dat);
    }

    
    
    
    
    
}
