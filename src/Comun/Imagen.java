package Comun;

import java.awt.Image;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import org.opencv.imgproc.Imgproc;
import static org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.RETR_TREE;
import static org.opencv.imgproc.Imgproc.THRESH_BINARY;
import static org.opencv.imgproc.Imgproc.THRESH_OTSU;
import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.findContours;
import static org.opencv.imgproc.Imgproc.rectangle;
import static org.opencv.imgproc.Imgproc.threshold;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author gastr
 */
public class Imagen {

    private final Mat imagen;
    private String nombreCarpeta;

    public Imagen(Mat imagen) {
        this.imagen = imagen;
    }

    public String dividirImagen(String nombreCarpeta) {
        StringBuilder SB = new StringBuilder();

        //Crea la carpeta donde alojar las imagenes
        this.nombreCarpeta = nombreCarpeta;
        File carpeta = new File(nombreCarpeta);
        if (carpeta.mkdir()) {
            Mat imgAux = new Mat();
            //Convertir la imagen en escala de grises a binario
            threshold(imagen, imgAux, 128, 255, THRESH_BINARY | THRESH_OTSU);

            //Dibuja los bordes
            double umbral = 100;
            Canny(imgAux, imgAux, umbral, umbral * 2);

            //Lista que guarda los puntos de los contornos
            LinkedList<MatOfPoint> contornos = new LinkedList<>();
            Mat jerarquia = new Mat();

            //Detecto los contornos
            findContours(imgAux, contornos, jerarquia, RETR_TREE, CHAIN_APPROX_SIMPLE);

            Rect contorno;

            if (!contornos.isEmpty()) {
                SB.append("Se han encontrado ").append(contornos.size()).append(" contornos -");

                double areaProm =0;
                for (int i = 0; i < contornos.size(); i++) {
                    contorno = boundingRect(contornos.get(i));
                    areaProm+=contorno.area();
                }

                //Asigna que para ser un contorno valido debe ser mayor al 10% del segundo mayor
                areaProm = (areaProm/contornos.size())*0.25;
                ArrayList<Rect> contornosFiltrados = new ArrayList<>();

                //Guarda las letras detectadas
                for (int i = 0; i < contornos.size(); i++) {

                    contorno = boundingRect(contornos.get(i));
                    //Verifica que supere el limite
                    if (contorno.area() > areaProm) {
                        contornosFiltrados.add(contorno);
                    }
                }
                //Ordeno los contornos de mayor a menor
                Mergesort merge = new Mergesort(contornosFiltrados);
                merge.ordenar();
                contornosFiltrados = merge.getV();

                //Filtro solo los contornos que no se superponen con otro
                ArrayList<Rect> contornosFinal = new ArrayList<>();
                boolean bandera = false;
                for (Rect recta : contornosFiltrados) {
                    for (Rect c : contornosFinal) {
                        if (c.contains(recta.tl()) || c.contains(recta.br())) {
                            bandera = true;
                            break;
                        }
                    }
                    if (!bandera) {
                        //Agrego el contorno a la lista final de contornos
                        contornosFinal.add(recta);
                        //Guardo la imagen en la carpeta
                        this.guardarImagen(new Mat(imagen, recta), contornosFinal.size());

                        //La marca en la imagen principal
                        rectangle(imagen, new Point(recta.x, recta.y), new Point(recta.x + recta.width,
                                recta.y + recta.height), new Scalar(0, 255, 0));
                    }
                    bandera = false;
                }

                SB.append("Contornos Validos: ").append(contornosFinal.size());

            } else {
                SB.append("No se ha encontrado ningun contorno");
            }
        } else {
            SB.append("No se pudo crear la carpeta");
        }
        this.guardarImagen(imagen, 0);
        return SB.toString();
    }

    private void guardarImagen(Mat imagen, int i) {
        String nombreImagen = nombreCarpeta + "//imagen" + i + ".png";
        imwrite(nombreImagen, imagen);
    }

    /*
        Convierte una imagen Mat(formato de opencv) a Image(formato de java)
     */
    private Image convertir(Mat imagen) {
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", imagen, matOfByte);

        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;

        try {

            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            System.out.println(e);
        }
        return (Image) bufImage;
    }

    public Image getImagen() {
        return this.convertir(imagen);
    }

}
