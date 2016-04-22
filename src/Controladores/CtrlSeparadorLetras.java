/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Comun.Imagen;
import Interfaces.IntSeparadorLetras;
import java.io.File;
import javax.swing.ImageIcon;
import org.opencv.core.Mat;
import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.opencv.imgcodecs.Imgcodecs.imread;
/**
 *
 * @author gastr
 */
public class CtrlSeparadorLetras {

    private IntSeparadorLetras intSeparadorLetras;
    private Mat img;
    private ImageIcon imgMostrar;
    private Imagen Imagen;

    public void setInterfaz(IntSeparadorLetras intSeparadorLetras) {
        this.intSeparadorLetras = intSeparadorLetras;
    }

    /**
     * Recibe un archivo de la imagen a separar
     *
     * @param archivoImagen
     */
    public void setImagen(File archivoImagen) {
        this.img = imread(archivoImagen.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
        this.imgMostrar = new ImageIcon(archivoImagen.toString());
        this.intSeparadorLetras.setLabelImagenMostrar(imgMostrar);
    }

    public void dividirImagen(String nombreCarpeta) {
        if (img != null) {
            Imagen = new Imagen(img);

            if (nombreCarpeta.isEmpty()) {
                nombreCarpeta="NombreCarpeta";
                this.intSeparadorLetras.setTextNombreCarpeta(nombreCarpeta);
            }
            this.intSeparadorLetras.setInfoImagenes(Imagen.dividirImagen(nombreCarpeta));
            this.intSeparadorLetras.setImagenDetecciones(Imagen.getImagen());
        } else {
            this.intSeparadorLetras.setLabelValidacion("No hay ninguna imagen seleccionada");
        }
    }

}
