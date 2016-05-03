package Comun;



import java.util.ArrayList;
import org.opencv.core.Rect;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gastr
 */
public class Mergesort {

    private final ArrayList<Rect> v;
    private ArrayList<Rect>  temp;
    
    public Mergesort(ArrayList<Rect>  v) {
        this.v = v;
    }

    public void ordenar() {
        int n = v.size();
        temp = new ArrayList<>(n);
        sort(0, n - 1);
    }

    private void sort(int izq, int der) {
        if (izq < der) {
            int centro = (izq + der) / 2;
            sort(izq, centro);
            sort(centro + 1, der);
            merge(izq, centro, der);
        }
    }

    private void merge(int izq, int centro, int der) {
        for (int i = izq; i <= der; i++) {
            temp.add(i, v.get(i));
        }
        int i = izq, j = centro + 1, k = izq;
        while (i <= centro && j <= der) {
            if (temp.get(i).area() >= temp.get(j).area()) {           
                v.set(k, temp.get(i));
                i++;               
            } else {
                v.set(k, temp.get(j));
                j++;
            }
            k++;
        }
        while (i <= centro) {
            v.set(k, temp.get(i));
            k++;
            i++;
        }
    }

    public ArrayList<Rect> getV() {
        return v;
    }
    
    
}
