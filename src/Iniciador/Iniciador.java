/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Iniciador;

import Controladores.CtrlSeparadorLetras;
import Interfaces.IntSeparadorLetras;
import UpperEssential.UpperEssentialLookAndFeel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.opencv.core.Core;

/**
 *
 * @author gastr
 */
public class Iniciador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new UpperEssentialLookAndFeel("temas//Moderno.Theme"));
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Iniciador.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Carga la libreria Opencv
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        IntSeparadorLetras intSeparadorLetras = new IntSeparadorLetras();
        CtrlSeparadorLetras ctrlSeparadorLetras = new CtrlSeparadorLetras();

        intSeparadorLetras.setControlador(ctrlSeparadorLetras);
        ctrlSeparadorLetras.setInterfaz(intSeparadorLetras);

        intSeparadorLetras.setVisible(true);
    }

}
