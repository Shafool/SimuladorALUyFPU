/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXML_FPUController implements Initializable {

    @FXML
    private TextField txtEntrada_n1;
    @FXML
    private TextField txtEntrada_n2;
    @FXML
    private ComboBox<String> cmbEntrada_operacion;
    @FXML
    private TextField txtAlu_n1;
    @FXML
    private TextField txtAlu_n2;
    @FXML
    private TextField txtSalida_r;
    @FXML
    private TextField txtAlu_r;
    @FXML
    private TextField txtAlu_signo;
    @FXML
    private ComboBox<String> cmbEntrada_base;
    @FXML
    private Button btn_operar;
    @FXML
    private TextField txtEntrada_sig1;
    @FXML
    private TextField txtEntrada_sig2;
    @FXML
    private Button btn_irALU;
    
    /**
     * Variables globales
     */
    float floatEntrada_1;
    float floatEntrada_2;
    
    private String baseActual = "dec";
    private String opActual = "+";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       // Llenar combo operaciones
        ObservableList obListOperaciones = FXCollections.observableArrayList();
        obListOperaciones.add("Suma");
        obListOperaciones.add("Resta");
        obListOperaciones.add("Multiplicación");
        obListOperaciones.add("División");
        cmbEntrada_operacion.setItems(obListOperaciones);

        // Llenar combo operaciones
        ObservableList obListBases = FXCollections.observableArrayList();
        obListBases.add("Decimal");
        obListBases.add("Binario");
        obListBases.add("Octal");
        obListBases.add("Hexadecimal");
        cmbEntrada_base.setItems(obListBases);
        
        // Configuracion de inicio
        txtEntrada_sig1.setText("+");
        txtEntrada_sig2.setText("+");
    }    

    @FXML
    private void operar(ActionEvent event) {
        // Especificar el sistema de numeración
        switch (cmbEntrada_base.getSelectionModel().getSelectedIndex()) {
            case 0: baseActual = "dec"; break;
            case 1: baseActual = "bin"; break;
            case 2: baseActual = "oct"; break;
            case 3: baseActual = "hex"; break;
            default: break;
        }
        
        // Especificar la operacion
        switch (cmbEntrada_operacion.getSelectionModel().getSelectedIndex()) {
            case 0: opActual = "+"; break;
            case 1: opActual = "-"; break;
            case 2: opActual = "*"; break;
            case 3: opActual = "/"; break;
            default: break;
        }
        
        floatEntrada_1 = Float.parseFloat(txtEntrada_n1.getText());
        floatEntrada_2 = Float.parseFloat(txtEntrada_n2.getText());
        
        String strEntrada_1 = GetBinary32(floatEntrada_1);
        String strEntrada_2 = GetBinary32(floatEntrada_2);
        
        // Completar ceros
        strEntrada_1 = forzar_a_NBits(strEntrada_1, 32);
        strEntrada_2 = forzar_a_NBits(strEntrada_2, 32);
        
        // Añadir signos
        if (txtEntrada_sig1.getText().equals("+") || txtEntrada_sig1.getText().equals("") || txtEntrada_sig1.getText().equals(" ")) {
            strEntrada_1 = "0" + strEntrada_1.substring(1);
        } else if (txtEntrada_sig1.getText().equals("-")) {
            strEntrada_1 = "1" + strEntrada_1.substring(1);
            floatEntrada_1 = floatEntrada_1 * (-1);
        }
        
        if (txtEntrada_sig2.getText().equals("+") || txtEntrada_sig2.getText().equals("") || txtEntrada_sig2.getText().equals(" ")) {
            strEntrada_2 = "0" + strEntrada_2.substring(1);
        } else if (txtEntrada_sig2.getText().equals("-")) {
            strEntrada_2 = "1" + strEntrada_2.substring(1);
            floatEntrada_2 = floatEntrada_2 * (-1);
        }
        
        // Separar signo, exponente y mantisa
        for (int i = 0; i < 32; i++) {
            if (i == 1 || i == 9) {
                strEntrada_1 += " ";
                strEntrada_2 += " ";
            }
            strEntrada_1 += strEntrada_1.charAt(i);
            strEntrada_2 += strEntrada_2.charAt(i);
        }
        strEntrada_1 = strEntrada_1.substring(32);
        strEntrada_2 = strEntrada_2.substring(32);
        
        txtAlu_n1.setText(strEntrada_1);
        txtAlu_n2.setText(strEntrada_2);
        
        // Operaciones
        String resultadoString = "";
        float resultadoFloat = 0;
        switch (opActual) {
            case "+":
                txtAlu_signo.setText("+");
                resultadoFloat = floatEntrada_1 + floatEntrada_2;
                break;
            case "-":
                txtAlu_signo.setText("+");
                resultadoFloat = floatEntrada_1 - floatEntrada_2;
                break;
            case "*":
                txtAlu_signo.setText("*");
                resultadoFloat = floatEntrada_1 * floatEntrada_2;
                break;
            case "/":
                txtAlu_signo.setText("/");
                resultadoFloat = floatEntrada_1 / floatEntrada_2;
                break;
            default:
                break;
        }
        
        // Colocar resultado
        String strResultado = "";
        strResultado = GetBinary32(resultadoFloat);
        strResultado = forzar_a_NBits(strResultado, 32);
        
        // validar signo de resultado
        if (resultadoFloat < 0) { // nega
            strResultado = "1" + strResultado.substring(1);
        } else if (resultadoFloat > 0) {
            strResultado = "0" + strResultado.substring(1);
        } else if (resultadoFloat == 0) {
            strResultado = "00000000000000000000000000000000";
        }
        
        // Separar signo, exponente y mantisa
        for (int i = 0; i < 32; i++) {
            if (i == 1 || i == 9) {
                strResultado += " ";
            }
            strResultado += strResultado.charAt(i);
        }
        strResultado = strResultado.substring(32);
        
        txtAlu_r.setText(strResultado);
        txtSalida_r.setText(Float.toString(resultadoFloat));

    }
    
    private String signoA_numero(String signo){
        if (signo.equals("+") || signo.equals("") || signo.equals(" ")) {
            return "0";
        } else {
            return "1";
        }
    }

    @FXML
    private void irALU(ActionEvent event) throws IOException{
        Stage stageActual = (Stage)btn_irALU.getScene().getWindow();
        stageActual.close();
        
        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXML_ALU.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
    
    // Convert the 32-bit binary into the decimal  
    private float GetFloat32( String Binary )  
    {  
        int intBits = Integer.parseInt(Binary, 2);
        float myFloat = Float.intBitsToFloat(intBits);
        return myFloat;  
    } 
     
    // Get 32-bit IEEE 754 format of the decimal value  
    private String GetBinary32( float value )  
    {  
        int intBits = Float.floatToIntBits(value); 
        String binary = Integer.toBinaryString(intBits);
        return binary;
    }
    
    private String forzar_a_NBits(String binario, int longitudFinal){
        // Completar ceros
        String completarCeros = "";
        
        if (binario.length() < longitudFinal) {
            for (int i = 0; i < longitudFinal - binario.length(); i++) {
                completarCeros += '0';
            }
            binario = completarCeros + binario;
        } else if (binario.length() > longitudFinal){
            binario = binario.substring(binario.length() - longitudFinal);
        } 
        
        return binario;
    } 
    
    // Convert 19.5 into IEEE 754 binary format..  
        String str = GetBinary32( (float) 19.5 );  

        // .. and back again  
        float f = GetFloat32( str );  
            
}
