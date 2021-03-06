package programacion.entregaut6.modelo;

import programacion.entregaut6.io.CalendarioIO;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Set; 
import java.util.Map;
import java.util.Iterator;
/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 * @author (Andrés Guallar Chamorro)
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario;

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        Mes clave = nuevo.getMes();

        if(!calendario.containsKey(clave)){
            ArrayList<Evento> value = new ArrayList<>();
            value.add(0, nuevo);
            calendario.put(clave, value);
        }

        else{
            ArrayList<Evento> value = calendario.get(clave);
            boolean añadido = false;

            for(int i = 0; añadido == false && i < value.size(); i++){
                if(nuevo.antesDe(value.get(i))){
                    añadido = true;
                    calendario.get(clave).add(value.indexOf(value.get(i)), nuevo);

                }
            }

            if(añadido == false){
                calendario.get(clave).add(value.size(), nuevo);
            }
        }
    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        String str = "";
        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        for(Map.Entry<Mes, ArrayList<Evento>> entrada : entradas){
            str += "\n" + entrada.getKey() + "\n";

            for(Evento evento : entrada.getValue()){
                str += evento.toString() + "\n"; 
            }

        }
        return str;
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        if(calendario.containsKey(mes)){
            return calendario.get(mes).size();
        }
        return 0;
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        TreeSet<Mes> conjunto = new TreeSet<>();

        int maxPrimera = -1;
        int maxSegunda = -1;
        Mes mesMaxPrimero = null;
        Mes mesMaxSegundo = null;

        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it =  entradas.iterator();

        while(it.hasNext()){
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            ArrayList<Evento> valor =  entrada.getValue();
            int cantidad = valor.size();
            if(cantidad > maxPrimera){
                maxSegunda = maxPrimera;
                maxPrimera = cantidad;
                mesMaxPrimero = entrada.getKey();
            }
            else if(cantidad > maxSegunda){
                maxSegunda = cantidad;
                mesMaxSegundo = entrada.getKey();
            }
        }

        if(calendario.size() == 1){
            conjunto.add(calendario.firstKey());
            return conjunto;
        }
        conjunto.add(mesMaxPrimero);
        conjunto.add(mesMaxSegundo);
        return conjunto;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        String evMasAlto = "";
        int duracionMasAlta = -1;
        Set<Mes> meses = calendario.keySet();

        for(Mes mes : meses){
            ArrayList<Evento> eventos =  calendario.get(mes);
            for(Evento evento : eventos){
                int dur = evento.getDuracion();
                if(dur > duracionMasAlta){
                    duracionMasAlta = dur;
                    evMasAlto = evento.getNombre();
                }
            }
        }
        return evMasAlto;
    }

    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int cantidadBorrados = 0;

        Set<Map.Entry<Mes, ArrayList<Evento>>> entradas = calendario.entrySet();
        Iterator<Map.Entry<Mes, ArrayList<Evento>>> it =  entradas.iterator();

        while(it.hasNext()){
            Map.Entry<Mes, ArrayList<Evento>> entrada = it.next();
            for(Mes mes : meses){
                if(entrada.getKey().equals(mes)){
                    ArrayList<Evento> eventos =  calendario.get(mes);
                    for(int i = 0; i < eventos.size(); i++){
                        if(eventos.get(i).getDia() == dia){
                            eventos.remove(eventos.get(i));
                            cantidadBorrados ++;
                            i--;
                        }
                    }
                    if(eventos.isEmpty()){
                        it.remove();
                    }
                }
            }

        }
        return cantidadBorrados;
    }

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.FEBRERO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        mes = Mes.MARZO;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
            + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
            + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.FEBRERO, Mes.MARZO, Mes.MAYO, Mes.JUNIO};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
            " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
