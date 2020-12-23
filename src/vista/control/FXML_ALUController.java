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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class FXML_ALUController implements Initializable {

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
    private ComboBox<?> cmbEntrada_base;
    @FXML
    private Button btn_operar;
    @FXML
    private TextField txtEntrada_sig1;
    @FXML
    private TextField txtEntrada_sig2;

    /**
     * Variables Globales
     */
    private String baseActual = "dec";
    private String opActual = "+";
    private int longRegistro = 32;
    private boolean desbordamiento = false;

    //Valores al sistema Decimal
    int valorEntrada1 = 0;
    int valorEntrada2 = 0;

    @FXML
    private Button btn_irFPU;
    @FXML
    private VBox vbox_contenedor;
    @FXML
    private ToggleButton btn_convABin;
    @FXML
    private ToggleButton btn_convADec;
    @FXML
    private ToggleButton btn_convAOct;
    @FXML
    private ToggleButton btn_convAHex;

    /**
     * Inicializar
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

        // Llenar bases
        ToggleGroup toggleGroup = new ToggleGroup();

        btn_convABin.setToggleGroup(toggleGroup);
        btn_convADec.setToggleGroup(toggleGroup);
        btn_convAOct.setToggleGroup(toggleGroup);
        btn_convAHex.setToggleGroup(toggleGroup);

        btn_convADec.setSelected(true);

        // Configuracion de inicio
        txtEntrada_sig1.setText("+");
        txtEntrada_sig2.setText("+");

        // Listener conversion
        /*   cmbEntrada_base.addActionListener (new ActionListener () {
    public void actionPerformed(ActionEvent e) {
       // doSomething();
    }
});*/
    }

    @FXML
    private void operar(ActionEvent event) {
        String aluNumero1 = "";
        String aluNumero2 = "";

        // Especificar la operacion
        switch (cmbEntrada_operacion.getSelectionModel().getSelectedIndex()) {
            case 0:
                opActual = "+";
                break;
            case 1:
                opActual = "-";
                break;
            case 2:
                opActual = "*";
                break;
            case 3:
                opActual = "/";
                break;
            default:
                break;
        }

        // Obtener signos
        String sig1 = getSigno(txtEntrada_sig1.getText());
        String sig2 = getSigno(txtEntrada_sig2.getText());

        // Conversion al sistema binario
        if (baseActual.equals("dec")) {
            aluNumero1 = Integer.toBinaryString((int) Integer.parseInt(txtEntrada_n1.getText()));
            aluNumero2 = Integer.toBinaryString((int) Integer.parseInt(txtEntrada_n2.getText()));

            valorEntrada1 = Integer.parseInt(txtEntrada_n1.getText());
            valorEntrada2 = Integer.parseInt(txtEntrada_n2.getText());

        } else if (baseActual.equals("bin")) {
            int temp1 = Integer.parseInt(txtEntrada_n1.getText(), 2);
            int temp2 = Integer.parseInt(txtEntrada_n2.getText(), 2);

            aluNumero1 = Integer.toBinaryString(temp1);
            aluNumero2 = Integer.toBinaryString(temp2);

            valorEntrada1 = Integer.parseInt(txtEntrada_n1.getText(), 2);
            valorEntrada2 = Integer.parseInt(txtEntrada_n2.getText(), 2);

        } else if (baseActual.equals("oct")) {
            int temp1 = Integer.parseInt(txtEntrada_n1.getText());
            int temp2 = Integer.parseInt(txtEntrada_n2.getText());

            aluNumero1 = Integer.toBinaryString(Octal_a_Decimal(temp1));
            aluNumero2 = Integer.toBinaryString(Octal_a_Decimal(temp2));

            valorEntrada1 = Integer.parseInt(txtEntrada_n1.getText(), 8);
            valorEntrada2 = Integer.parseInt(txtEntrada_n2.getText(), 8);

        } else if (baseActual.equals("hex")) {
            aluNumero1 = Integer.toBinaryString(Hexa_a_Decimal(txtEntrada_n1.getText()));
            aluNumero2 = Integer.toBinaryString(Hexa_a_Decimal(txtEntrada_n2.getText()));

            valorEntrada1 = Integer.parseInt(txtEntrada_n1.getText(), 16);
            valorEntrada2 = Integer.parseInt(txtEntrada_n2.getText(), 16);

        }

        // Completar ceros (Formato)
        aluNumero1 = completarCeros(aluNumero1, longRegistro);
        aluNumero2 = completarCeros(aluNumero2, longRegistro);

        // Operaciones
        String resultadoString = "";
        switch (opActual) {
            case "+":
                resultadoString = sumar(sig1, aluNumero1, sig2, aluNumero2);
                break;
            case "-":
                resultadoString = sumar(sig1, aluNumero1, sig2, aluNumero2);
                break;
            case "*":
                resultadoString = multiplicar(sig1, aluNumero1, sig2, aluNumero2);
                /* int resultadoMul = valorEntrada1 * valorEntrada2;
                resultadoString = Integer.toBinaryString(resultadoMul);
                txtAlu_r.setText(resultadoString);*/
                break;
            case "/":
                resultadoString = dividir(sig1, aluNumero1, sig2, aluNumero2);
                break;
            default:
                break;
        }

        // Decodificacion del resultado
        char signoResultado = resultadoString.charAt(0);
        int resultado = 0;

        if (signoResultado == '0') { // positivo
            resultado = Integer.parseInt(resultadoString.substring(1), 2);
        } else if (signoResultado == '1') { // negativo
            resultado = Integer.parseInt(complementoDe2(resultadoString.substring(1)), 2);
            resultado = resultado * (-1);
        }

        // Reconversion del resultado
        String salidaString = "";
        if (baseActual.equals("dec")) {
            //salidaString += (signoResultado == 0)? "+": "-";
            salidaString += Integer.toString(resultado);
        } else if (baseActual.equals("bin")) {
            salidaString = Integer.toBinaryString(resultado);
        } else if (baseActual.equals("oct")) {
            salidaString = Integer.toOctalString(resultado);
        } else if (baseActual.equals("hex")) {
            salidaString = Integer.toHexString(resultado);
        }
        System.out.println(baseActual);
        txtSalida_r.setText(salidaString);

    }

    private String dividir(String sig1, String dividendo, String sig2, String divisor) {
        String resultado = "";

        // Colocar numeros en alu
        txtAlu_n1.setText(dividendo.charAt(0) + " " + dividendo.substring(1));
        txtAlu_n2.setText(divisor.charAt(0) + " " + divisor.substring(1));

        // DIvision
        String cociente = "0";
        String resto = "0";

        resto = dividendo;

        while (resto.charAt(0) == '0') {
            //if (!esCero(resto)) {
            // Restar el divisor del dividendo y almacenarlo en el resto
            resto = sumaBinaria(resto, complementoDe2(divisor), 0, false);
            cociente += sumaBinaria(cociente, "1", 0, false);

            // Verificar si el resto es positivo
            resto = forzar_a_NBits(resto, longRegistro);
            //System.out.println("resto: " + resto);
            cociente = forzar_a_NBits(cociente, longRegistro);
            System.out.println(cociente);
            //}
        }
        cociente = sumaBinaria(cociente, complementoDe2(forzar_a_NBits("1", longRegistro)), 0, false); // restarle 1

        if (multiplicarSignos(sig1, sig2).equals("-")) {
            cociente = complementoDe2(cociente);
        }

        cociente = forzar_a_NBits(cociente, longRegistro);

        txtAlu_signo.setText("/");
        txtAlu_r.setText(cociente.charAt(0) + " " + cociente.substring(1));
        return cociente;

    }

    private boolean esCero(String binario) {
        boolean esCero = true;

        for (int i = 0; i < binario.length(); i++) {
            if (binario.charAt(i) != '0') {
                esCero = false;
                break;
            }
        }

        return esCero;
    }

    /*
    private int[] clonarBinario(int a[]){
        int[] clon = new int[31];
        System.arraycopy(a, 0, clon, 0, 31);
        return clon;
    }
     */
    private String multiplicar(String sig1, String binario1, String sig2, String binario2) {
        String resultado = "";

        // Colocar numeros en alu
        txtAlu_n1.setText(binario1.charAt(0) + " " + binario1.substring(1));
        txtAlu_n2.setText(binario2.charAt(0) + " " + binario2.substring(1));

        int numeroIteraciones = Integer.parseInt(binario1.substring(1), 2);
        System.out.println(numeroIteraciones);
        resultado = binario2;
        int resultadoLong;

        for (int i = 0; i < numeroIteraciones - 1; i++) {
            resultadoLong = resultado.length();
            resultado = sumaBinaria(resultado, completarCeros(binario2, resultadoLong), 0, true);
            System.out.println("Iter " + i + " res = " + resultado);
        }

        resultado = completarCeros(resultado, longRegistro);

        System.out.println(resultado);
        if (multiplicarSignos(sig1, sig2).equals("-")) { // Si es negativo se hace complemento a 2
            resultado = complementoDe2(resultado);
        }

        // Recortar a la long de el registro
        if (resultado.length() > longRegistro) {
            resultado = resultado.substring(resultado.length() - longRegistro);
        }

        txtAlu_signo.setText("*");
        txtAlu_r.setText(resultado.charAt(0) + " " + resultado.substring(1));

        return resultado;
    }

    private String sumar(String sig1, String binario1, String sig2, String binario2) {

        // Colocar numeros en alu
        // Se coloca el primer numero
        if (sig1.equals("+")) {
            txtAlu_n1.setText(binario1.charAt(0) + " " + binario1.substring(1));
            System.out.println("No Complemento n1");
        } else if (sig1.equals("-")) {
            binario1 = complementoDe2(binario1);
            txtAlu_n1.setText(binario1.charAt(0) + " " + binario1.substring(1));
            System.out.println("Complemento n1");
        }

        // Si la operacion es resta
        if (cmbEntrada_operacion.getSelectionModel().getSelectedIndex() == 1) {
            if (sig2.equals("+")) {
                binario2 = complementoDe2(binario2);
                txtAlu_n2.setText(binario2.charAt(0) + " " + binario2.substring(1));
            } else {
                txtAlu_n2.setText(binario2.charAt(0) + " " + binario2.substring(1));
            }
        } else {
            if (sig2.equals("+")) {
                txtAlu_n2.setText(binario2.charAt(0) + " " + binario2.substring(1));
            } else {
                binario2 = complementoDe2(binario2);
                txtAlu_n2.setText(binario2.charAt(0) + " " + binario2.substring(1));
            }
        }

        String resultado = sumaBinaria(binario1, binario2, 0, false);
        if (resultado.length() > longRegistro) {
            resultado = resultado.substring(resultado.length() - longRegistro);
        }

        txtAlu_signo.setText("+");
        txtAlu_r.setText(resultado.charAt(0) + " " + resultado.substring(1));

        return resultado;
    }

    private String complementoDe2(String binario) {
        int n = binario.length();
        int i;

        String ones = "", twos = "";
        ones = twos = "";

        // for ones complement flip every bit 
        for (i = 0; i < n; i++) {
            ones += flip(binario.charAt(i));
        }

        // for two's complement go from right to left in ones complement and if we get 1 make, we make 
        // them 0 and keep going left when we get first 0, make that 1 and go out of loop 
        twos = ones;
        for (i = n - 1; i >= 0; i--) {
            if (ones.charAt(i) == '1') {
                twos = twos.substring(0, i) + '0' + twos.substring(i + 1);
            } else {
                twos = twos.substring(0, i) + '1' + twos.substring(i + 1);
                break;
            }
        }

        // If No break : all are 1 as in 111 or 11111; 
        // in such case, add extra 1 at beginning 
        if (i == -1) {
            twos = '1' + twos;
        }

        return twos;
    }

    private char flip(char c) {
        return (c == '0') ? '1' : '0';
    }

    // Funciones utilitarias
    private String decimal_a_binario(int decimal1) {

        // Algoritmo
        String binario = "", binario1 = "";
        while (decimal1 > 0) {
            binario = decimal1 % 2 + binario;
            decimal1 /= 2;

        }
        return binario;
    }

    private static int Binario_a_Decimal(int binario) {
        int decimal = 0, exponente = 0;
        int digito;
        while (binario > 0) {
            digito = binario % 10;
            decimal = decimal + digito * (int) Math.pow(2, exponente);
            binario /= 10;
            exponente++;
        }
        return decimal;
    }

    private static int Octal_a_Decimal(int octal) {
        int decimal = 0, exponente = 0;
        int digito;
        while (octal > 0) {
            digito = octal % 10;
            decimal = decimal + digito * (int) Math.pow(8, exponente);
            octal /= 10;
            exponente++;
        }
        return decimal;
    }

    public void imprimirBinario(int bin[]) {
        for (int i = 0; i < bin.length; i++) {
            System.out.println(bin[i] + ',');
        }
        System.out.println(" Longitud: " + bin.length);
    }

    public static int Hexa_a_Decimal(String hex) {
        String digitos = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digitos.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    // Aritmetica
    private String sumaBinaria(String binario1, String binario2, int acarreo, boolean quitarCeros) {
        int longitud;
        String resultado = "";

        if (binario1.length() > binario2.length()) {
            binario2 = completarCeros(binario2, binario1.length());
            longitud = binario1.length();
        } else if (binario2.length() > binario1.length()) {
            binario1 = completarCeros(binario1, binario2.length());
            longitud = binario2.length();
        } else {
            longitud = binario1.length();
        }

        binario1 = invertirString(binario1);
        binario2 = invertirString(binario2);

        for (int i = 0; i < longitud; i++) {
            if ((binario1.charAt(i) == '0') && (binario2.charAt(i) == '0')) {        // Caso 0 + 0; solo se suma el acarreo
                resultado += (acarreo == 1) ? "1" : "0";
                acarreo = 0;
            } else if ((binario1.charAt(i) == '1') && (binario2.charAt(i) == '1')) { // Caso 1 + 1
                resultado += (acarreo == 1) ? "1" : "0";
                acarreo = 1;
            } else {                                                         // Caso (1 + 0) o viceversa                                         
                resultado += (acarreo == 1) ? "0" : "1";
                acarreo = (acarreo == 1) ? 1 : 0;
            }
        }

        resultado += (acarreo == 1) ? "1" : "0";
        resultado = invertirString(resultado);

        // Quitar ceros a la izquierda inservibles
        if (quitarCeros) {
            int aux = resultado.length();
            if (aux > 1) {
                for (int i = 0; i < aux - 1; i++) {
                    if (resultado.charAt(0) == '0') {
                        resultado = resultado.substring(1);
                    }
                }
            }
        }

        return resultado;
    }

    private String completarCeros(String binario, int longitudFinal) {
        // Completar ceros
        String completarCeros = "";

        if (binario.length() < longitudFinal) {
            for (int i = 0; i < longitudFinal - binario.length(); i++) {
                completarCeros += '0';
            }
            binario = completarCeros + binario;
        }
        return binario;
    }

    private String forzar_a_NBits(String binario, int longitudFinal) {
        // Completar ceros
        String completarCeros = "";

        if (binario.length() < longitudFinal) {
            for (int i = 0; i < longitudFinal - binario.length(); i++) {
                completarCeros += '0';
            }
            binario = completarCeros + binario;
        } else if (binario.length() > longitudFinal) {
            binario = binario.substring(binario.length() - longitudFinal);
        }

        return binario;
    }

    // Util
    private String getSigno(String signo) {
        if (signo.equals("+") || signo.equals("")) {
            signo = "+";
        } else {
            signo = "-";
        }
        return signo;
    }

    private String invertirString(String string) {
        int longitud = string.length();

        if (longitud >= 2) {
            for (int i = 0; i < longitud - 1; i++) {
                string += string.charAt(longitud - (i + 2));
            }
            string = string.substring(longitud - 1);
        }

        return string;
    }

    private String quitarCerosIzquierda(String binario) {
        int aux = binario.length();

        if (aux > 1) {
            for (int i = 0; i < aux - 1; i++) {
                if (binario.charAt(0) == '0') {
                    binario = binario.substring(1);
                }
            }
        }
        return binario;
    }

    private String multiplicarSignos(String sig1, String sig2) {
        if ("+".equals(sig1) && "+".equals(sig2)) {
            return "+";
        } else if ("-".equals(sig1) && "-".equals(sig2)) {
            return "+";
        } else {
            return "-";
        }
    }

    @FXML
    private void irFPU(ActionEvent event) throws IOException {
        Stage stageActual = (Stage) btn_irFPU.getScene().getWindow();
        stageActual.close();

        Parent root = FXMLLoader.load(getClass().getResource("/vista/FXML_FPU.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void convertir_a_binario(ActionEvent event) {
        String baseAnterior = baseActual;
        baseActual = "bin";

        txtEntrada_n1.setText(Integer.toBinaryString(valorEntrada1));
        txtEntrada_n2.setText(Integer.toBinaryString(valorEntrada2));
    }

    @FXML
    private void convertir_a_decimal(ActionEvent event) {
        String baseAnterior = baseActual;
        baseActual = "dec";

        txtEntrada_n1.setText(Integer.toString(valorEntrada1, 10));
        txtEntrada_n2.setText(Integer.toString(valorEntrada2, 10));
    }

    @FXML
    private void convertir_a_octal(ActionEvent event) {
        String baseAnterior = baseActual;
        baseActual = "oct";

        txtEntrada_n1.setText(Integer.toOctalString(valorEntrada1));
        txtEntrada_n2.setText(Integer.toOctalString(valorEntrada2));
    }

    @FXML
    private void convertir_a_hex(ActionEvent event) {
        String baseAnterior = baseActual;
        baseActual = "hex";

        txtEntrada_n1.setText(Integer.toHexString(valorEntrada1));
        txtEntrada_n2.setText(Integer.toHexString(valorEntrada2));
    }

}
