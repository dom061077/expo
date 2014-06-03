package com.rural.utils;

/**
 * Created by IntelliJ IDEA.
 * User: danielmedina
 * Date: 3/06/14
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class Util {
    public static double redondear(double numero,int numeroDecimales) {
        long mult=(long)Math.pow(10,numeroDecimales);
        double resultado=(Math.round(numero*mult))/(double)mult;
        return resultado;
    }
}
