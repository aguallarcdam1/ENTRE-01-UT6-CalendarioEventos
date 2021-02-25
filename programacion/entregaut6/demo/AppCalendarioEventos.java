package programacion.entregaut6.demo;

import programacion.entregaut6.modelo.CalendarioEventos;
import programacion.entregaut6.interfaz.IUConsola;
/**
 * Punto de entrad a la aplicación
 * 
 * @author (Andrés Guallar Chamorro)
 */
public class AppCalendarioEventos {

    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        IUConsola interfaz = new IUConsola(calendario);
        interfaz.iniciar();
        //Apartado f:
        //C:\Users\Andrés_Guallar\Desktop\Programación\ENTRE-01-UT6-CalendarioEventos>java programacion.entregaut6.demo.AppCalendarioEventos
        //Apartado h:
        // C:\Users\Andrés_Guallar\Desktop\Programación\ENTRE-01-UT6-CalendarioEventos>java -jar entregaut6.jar
    }

}
