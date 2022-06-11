/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1010coursework;

import java.io.Serializable;

/**
 * This is a communication class whose objects are sent back and forth
 * used by the Fuel Client and Fuel Server
 * @author Elton John Fernandes
 */
public class FuelData implements Serializable {
    
    double tripDist;
    double fuelEff;
    int fuelType;  
    double fuelCost;
    double result;

    /* 0 = Diesel
       1 = 98 Octane */

    /**
     * Standard Constructor, however the fuel type has a special meaning based on value
     * Fuel Type: 
     *           0 = run as normal w/ Diesel Price
     *           1 = run as normal w/ 98 Octane Price
     *          -1 = abort normal run and shut down thread
     *          -2 = redacted//store object
     *          -3 = return all previous stored data, no normal run this cycle
     * @param tripDist The Trip Distance 
     * @param fuelEff  The Fuel Efficiency
     * @param fuelType The Fuel Type
     * @param result   The Result
     */ 
    public FuelData(double tripDist, double fuelEff, int fuelType, double result) {
        this.tripDist = tripDist;
        this.fuelEff = fuelEff;
        this.fuelType = fuelType;
        this.result = result;
        
    }

    /**
     * Alternative Constructor that takes no parameters 
     */
    public FuelData() {
        this.tripDist = 0.0;
        this.fuelEff = 0.0;
        this.fuelType = 0;
        this.fuelCost = -1.0;
        this.result = 0.0;
    }

    /**
     * Gets the trip distance
     * @return Trip Distance
     */
    public double getTripDist() {
        return tripDist;
    }

    /**
     * sets the trip distance
     * @param tripDist The Trip Distance 
     */
    public void setTripDist(double tripDist) {
        this.tripDist = tripDist;
    }

    /**
     * gets the Fuel Efficiency
     * @return Fuel Efficiency
     */
    public double getFuelEff() {
        return fuelEff;
    }

    /**
     * sets the Fuel Efficiency 
     * @param fuelEff The Fuel Efficiency 
     */
    public void setFuelEff(double fuelEff) {
        this.fuelEff = fuelEff;
    }

    /**
     * gets the Fuel Type
     * @return Fuel Type
     */
    public int getFuelType() {
        return fuelType;
    }

    /**
     * sets the Fuel Type
     * @param fuelType The Fuel Type
     */
    public void setFuelType(int fuelType) {
        this.fuelType = fuelType;
    }

    /**
     * gets the Fuel Result
     * @return Fuel Result
     */
    public double getResult() {
        return result;
    }

    /**
     * sets the Fuel Result
     * @param result The Result
     */
    public void setResult(double result) {
        this.result = result;
    }

    /**
     * gets the Fuel Cost
     * @return Fuel Cost
     */
    public double getFuelCost() {
        return fuelCost;
    }

    /**
     * sets the Fuel Cost
     * @param fuelCost The Fuel Cost
     */
    public void setFuelCost(double fuelCost) {
        this.fuelCost = fuelCost;
    }
    
    
    

    
    
    
    
    
    
    
}
