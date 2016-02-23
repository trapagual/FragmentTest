package es.trapasoft.android.fragmenttest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by Administrador on 14/02/2016.
 */
public class CitasCursorAdapter extends CursorAdapter {

    public CitasCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public CitasCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.row, parent, false);

    }

    /**
     * En este metodo es donde cargamos los datos del cursor
     * en los objetos de la vista
     * Rellenar los TextView con los datos del cursor
     * @param view
     * @param context
     * @param cursor
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvPaciente = (TextView)view.findViewById(R.id.tvPaciente);
        TextView tvDiaSem = (TextView)view.findViewById(R.id.tvDiaSem);
        TextView tvDia = (TextView)view.findViewById(R.id.tvDia);
        TextView tvHoras = (TextView)view.findViewById(R.id.tvHoras);
        TextView tvLugar = (TextView)view.findViewById(R.id.tvLugar);
        /* COLUMNAS
                String[] select = new String[] {
                "_id",  //0
                "title",        //1
                "description",  //2
                "dtstart",      //3
                "dtend",        //4
                "eventLocation" //5
        };
         */
        tvDiaSem.setText(getWeekDayFromMilliseconds(cursor.getLong(cursor.getColumnIndex("dtstart"))));
        tvPaciente.setText(cursor.getString(cursor.getColumnIndex("title")));
        tvDia.setText(getDayStringFromMilliseconds(cursor.getLong(cursor.getColumnIndex("dtstart"))));
        tvHoras.setText(getHoursStringFromMilliseconds(cursor.getLong(cursor.getColumnIndex("dtstart")), cursor.getLong(cursor.getColumnIndex("dtend"))));
        tvLugar.setText(cursor.getString(cursor.getColumnIndex("eventLocation")));
    }

    private String getWeekDayFromMilliseconds(Long milis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        int diasem = cal.get(Calendar.DAY_OF_WEEK);
        switch (diasem) {
            case Calendar.MONDAY:
                return "LUN";
            case Calendar.TUESDAY:
                return "MAR";
            case Calendar.WEDNESDAY:
                return "MIE";
            case Calendar.THURSDAY:
                return "JUE";
            case Calendar.FRIDAY:
                return "VIE";
            case Calendar.SATURDAY:
                return "SA";
            case Calendar.SUNDAY:
                return "DO";
            default:
                return "???";
        }
    }
    private String getDayStringFromMilliseconds(Long milis) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        return String.format("%02d/%02d/%04d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)+1, // los meses empiezan en cero
                cal.get(Calendar.YEAR));
    }

    private String getHoursStringFromMilliseconds(Long milisStart, Long milisEnd) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milisStart);
        String desde = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        cal.setTimeInMillis(milisEnd);
        String hasta = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        return desde + " -> " + hasta;
    }

}
