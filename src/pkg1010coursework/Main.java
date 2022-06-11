/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pkg1010coursework;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Elton John Fernandes
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException, IOException {
        
        ///TextFields///
        
        //Trip Distance TF
        TextField trpDistTF = new TextField();
        trpDistTF.setPromptText("Trip Distance");
        trpDistTF.setTooltip(new Tooltip("Enter the Trip Distance"));
        
        trpDistTF.relocate(10, 15);
        
        //Car Fuel Efficiency TF
        TextField cFuelEffTF = new TextField();
        cFuelEffTF.setPromptText("Car Fuel Efficiency");
        cFuelEffTF.setTooltip(new Tooltip("Enter the Car Fuel Efficiency"));
        
        cFuelEffTF.relocate(210, 15);
        
        
        ///Radio Buttons///
        //Fuel Sel Toggle Group
        ToggleGroup fuelSel = new ToggleGroup();
        
        //Fuel Sel 98 Octane
        RadioButton fuelSel_1 = new RadioButton("98 Octane");
        fuelSel_1.setId("98 Octane");
        fuelSel_1.setToggleGroup(fuelSel);
        fuelSel_1.relocate(90, 60);
        
        //Fuel Sel Diesel
        RadioButton fuelSel_2 = new RadioButton("Diesel");
        fuelSel_2.setId("Diesel");
        fuelSel_2.setToggleGroup(fuelSel);
        fuelSel_2.relocate(210, 60);
        
        
        ///Label///
        
        Label resultLbl = new Label("Results:");
        resultLbl.relocate(10, 200);
        
        ///Buttons///
        
        //Calculate Button
        Button calcBTN = new Button("Calculate", new ImageView(new Image(new FileInputStream("resources/calc_icon.png"))));
        calcBTN.relocate(50, 125);
        
        
        calcBTN.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            @SuppressWarnings("empty-statement")
            public void handle(ActionEvent event) {

                
                try
                {
                    Socket fuelClient = new Socket("localhost", 6789);
                    
                    //Creating an Object Writer
                    ObjectOutputStream toServer = new ObjectOutputStream(fuelClient.getOutputStream());
                                       
                    /*BufferedReader fromServer = new BufferedReader(new InputStreamReader(fuelClient.getInputStream()));  //creates the Reader
                    String[] fuelPrices = fromServer.readLine().split(",");        //reads from File*/
                    //Upper Two lines used for a older ver.
                    
                    
                    RadioButton fuelSelChck = (RadioButton) fuelSel.getSelectedToggle();  //gets the selected Toggle

                    
                    //Creating an object with the respective attributes
                    switch (fuelSelChck.getId()) {
                        case "Diesel":
                                                                           //This creates an object with
                                                                           //Diesel attributes -> 0
                            //Writing the Object to server
                            toServer.writeObject(new FuelData(Double.parseDouble(trpDistTF.getText()),
                    Double.parseDouble(cFuelEffTF.getText()), 0, 0));           //Writing the object
                            break;
                            
                        case "98 Octane":
                                                                           //This creates an object with
                                                                           //98 Octane attributes -> 1
                            //Writing the Object to server
                            toServer.writeObject(new FuelData(Double.parseDouble(trpDistTF.getText()),
                    Double.parseDouble(cFuelEffTF.getText()), 1, 0));           //Writing the object
                            break;
                            
                        default:
                            System.out.println("You shouldn't be here, client");
                            break;
                    }
                                        
                    //Creating an Object Reader
                    ObjectInputStream fromServer = new ObjectInputStream(fuelClient.getInputStream());
                    
                    //Reading from Server
                    FuelData fuelINPckt = (FuelData) fromServer.readObject();
                    
                    //checking fuel type and Setting the result 
                    if(fuelINPckt.getFuelType()==0)
                        resultLbl.setText("Result:\n Diesel");
                    else if(fuelINPckt.getFuelType()==1)
                        resultLbl.setText("Result:\n 98 Octane");
                    
                    resultLbl.setText(resultLbl.getText() +" Fuel Price ="+ fuelINPckt.getFuelCost() +"$/L\n "
                                + "Trip Distance = "+ trpDistTF.getText() +"\n "
                                + "Car Fuel Efficiency = "+ cFuelEffTF.getText() +"\n\n "
                                + "Total Trip Cost = "+ fuelINPckt.getResult() );                   
                
                }catch(NullPointerException Ex)
                {
                    Alert noFuelSel = new Alert(AlertType.WARNING, "Please Select a Fuel Type", ButtonType.OK);
                    noFuelSel.showAndWait();
                }
                catch(NumberFormatException Ex)
                {
                    Alert wrongVal = new Alert(AlertType.WARNING, "Please Enter Postive Numeric Values Only", ButtonType.OK);
                    wrongVal.showAndWait();
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                
            }
        });
        
        //Reset Button
        Button resetBTN = new Button("Reset", new ImageView(new Image(new FileInputStream("resources/reset_icon.png"))));
        resetBTN.relocate(175, 125);
        
        
        resetBTN.setOnAction((ActionEvent event) -> {
            
            trpDistTF.setText(null);
            cFuelEffTF.setText(null);
            resultLbl.setText("Results:");
            
            
        });
        
        //Store Button
        Button storeBTN = new Button("Store", new ImageView(new Image(new FileInputStream("resources/store_icon.png"))));
        storeBTN.relocate(275, 125);
        
        
        storeBTN.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                
                Alert EA = new Alert(AlertType.CONFIRMATION, "In order to use this Function, you need to purchase the 100% DLC season pass for $99999.99 / month,\n\n Would you like to purchase the DLC? ", ButtonType.NO, ButtonType.CLOSE);
                EA.showAndWait();
                
            }
        });
        
        Pane base = new Pane();
        base.getChildren().addAll(trpDistTF, cFuelEffTF, fuelSel_1, fuelSel_2, calcBTN, resetBTN, storeBTN,
                                    resultLbl);
        
        Scene scene = new Scene(base, 410, 400);
        
        primaryStage.setTitle("Fuel Cost Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            
            try {
                Socket fuelClient = new Socket("localhost", 6789);
                
                try (ObjectOutputStream toServer = new ObjectOutputStream(fuelClient.getOutputStream())) {
                    toServer.writeObject(new FuelData(-1, -1, -1, -1));
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }        
        });
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /* Redacted: Used for older ver
    public static String tripCostCalc(String dist, String fuelEff_mpG, String fuelCost) throws NumberFormatException
    {
        double tripCost = 0.0;
        
        double fuelEff_mpL = Double.parseDouble(fuelEff_mpG) * 0.22;
        
        tripCost = Double.parseDouble(dist) * Double.parseDouble(fuelCost)/(fuelEff_mpL); 
        
        
        return Double.toString(Math.round(tripCost * 100.0) / 100.0);
    }
    */
}
