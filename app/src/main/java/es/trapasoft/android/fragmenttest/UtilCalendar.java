package es.trapasoft.android.fragmenttest;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Clase para los metodos de utilidad del calendario
 * Created by Administrador on 12/02/2016.
 */
public class UtilCalendar {

    private static final String THISTAG = "FragAgenda" ;

    /**
     * Lee TODOS los eventos del calendario id_calendario
     *
     * Falta filtrar fechadesde, fechahasta
     *
     * @param context
     * @param id_calendario
     * @return un ArrayList de objetos Cita
     */
    public static ArrayList<Cita> leerEventosCalendario(Context context, long id_calendario, long inicio, long fin) {


        /* para hacerlo con filtros de fechas
        Uri content = Uri.parse("content://com.android.calendar/events");
        String[] vec = new String[] { "calendar_id", "title", "description", "dtstart", "dtend", "allDay", "eventLocation" };
        String selectionClause = "(dtstart >= ? AND dtend <= ?) OR (dtstart >= ? AND allDay = ?)";
        String[] selectionsArgs = new String[]{"" + dtstart, "" + dtend, "" + dtstart, "1"};

        ContentResolver contentResolver = context.getContentResolver();
           Cursor cursor = contentResolver.query(content, vec, selectionClause, selectionsArgs, null);
         */
        ArrayList<Cita> listaCitas = new ArrayList<>();

        String[] select = new String[] {
                "_id",  //0
                "title",        //1
                "description",  //2
                "dtstart",      //3
                "dtend",        //4
                "eventLocation" //5
        };
        String where = "calendar_id = ? AND ((dtstart >= ? AND dtend <= ?) OR (dtstart >= ? AND allDay = ?))";
        String[] selectionsArgs = new String[]{""+ id_calendario, "" + inicio, "" + fin, "" + inicio, "1"};
        String orderBy = "" + inicio + " ASC";
        Cursor cursor = context.getContentResolver()
                .query(
                        CalendarContract.Events.CONTENT_URI,
                        select,
                        where,
                        selectionsArgs,
                        null);


        if (cursor.moveToFirst()) {
            do {
                Cita cita = new Cita();
                cita.setIdCalendario(cursor.getLong(0));
                cita.setNombrePaciente(cursor.getString(1));
                cita.setObservaciones(cursor.getString(2));
                cita.setFhInicio(cursor.getLong(3));
                cita.setFhFin(cursor.getLong(4));
                cita.setNombreDespacho(cursor.getString(5));
                listaCitas.add(cita);
            } while (cursor.moveToNext());

            return listaCitas;
        } else {
            Log.e(THISTAG +" .leerEventos", "No hay eventos en el calendario");
            return null;
        }
    }


    /**
     * Lee un evento identificado por su id_calendario + id_evento y devuelve
     * un objeto Cita relleno
     * @param context
     * @param id_calendario
     * @param id_evento
     * @return objeto Cita
     */
    public static Cita leerDetalleEventoCalendario(Context context, long id_calendario, long id_evento) {
        Cita cita = null;
        String[] select = new String[] {
                //"calendar_id",  //0
                "_id",  //0
                "title",        //1
                "description",  //2
                "dtstart",      //3
                "dtend",        //4
                "eventLocation" //5
        };
        // aqui solo quiero un evento y tengo el id
        String where = "calendar_id = ? AND _id = ?";
        String[] selectionsArgs = new String[]{String.valueOf(id_calendario), String.valueOf(id_evento)};
        String orderBy = null;
        Cursor cursor = context.getContentResolver()
                .query(
                        CalendarContract.Events.CONTENT_URI,
                        select,
                        where,
                        selectionsArgs,
                        null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            cita = new Cita();
            cita.setIdCalendario(id_calendario);
            cita.setIdEvento(cursor.getLong(0));
            cita.setNombrePaciente(cursor.getString(1));
            cita.setObservaciones(cursor.getString(2));
            cita.setFhInicio(cursor.getLong(3));
            cita.setFhFin(cursor.getLong(4));
            cita.setNombreDespacho(cursor.getString(5));
        }
            return cita;
    }
    public static Cursor leerEventosCalendarioCursor(Context context, long id_calendario, long inicio, long fin) {
        String[] select = new String[] {
                //"calendar_id",  //0
                "_id",  //0
                "title",        //1
                "description",  //2
                "dtstart",      //3
                "dtend",        //4
                "eventLocation" //5
        };
        String where = "calendar_id = ? AND ((dtstart >= ? AND dtend <= ?) OR (dtstart >= ? AND allDay = ?))";
        String[] selectionsArgs = new String[]{""+ id_calendario, "" + inicio, "" + fin, "" + inicio, "0"};
        String orderBy = "" + inicio + " ASC";
        Cursor cursor = context.getContentResolver()
                .query(
                        CalendarContract.Events.CONTENT_URI,
                        select,
                        where,
                        selectionsArgs,
                        orderBy);
        if (cursor.getCount() > 0) return cursor;
        else return null;
    }

    public static Date getDateAsDate(String segundos) {
        if (segundos != null && !segundos.isEmpty()) {
            Long milliSeconds = Long.parseLong(segundos);
            return new Date(milliSeconds);
        } else {
            return null;
        }
    }
    public static Date getDateAsDate(long milliSeconds) {
        return new Date(milliSeconds);
    }

    public static String getDateAsString(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
