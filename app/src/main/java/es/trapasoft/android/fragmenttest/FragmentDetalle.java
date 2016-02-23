package es.trapasoft.android.fragmenttest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Administrador on 21/02/2016.
 */
public class FragmentDetalle extends Fragment {

    private static long ID_EVENTO;
    private static long ID_CALENDARIO;
    private static Cita cita = null;
    private TextView tvPacienteDetalle;
    private TextView tvDiaSemDetalle;
    private TextView tvDiaDetalle;
    private  TextView tvHDesde;
    private TextView tvHHasta;
    private EditText etLugar;
    private  EditText etObservaciones;
    private DatePickerDialog dlgFecha;
    private TimePickerDialog dlgHora;

    public void FragmentDetalle() {

    }

    // METODO NEWINSTANCE PARA PODER PASARLE ARGUMENTOS AL FRAGMENT
    // DESDE LA ACTIVITY
    public static FragmentDetalle newInstance(long calID, long eventID) {
        FragmentDetalle f = new FragmentDetalle();
        Bundle args = new Bundle();
        args.putLong("id_evento", eventID);
        args.putLong("id_calendario", calID);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ID_EVENTO = getArguments().getLong("id_evento");
        ID_CALENDARIO = getArguments().getLong("id_calendario");

        // aqui hay que ir a buscar el objeto Cita que corresponde a ese ID
        cita = UtilCalendar.leerDetalleEventoCalendario(getActivity(), ID_CALENDARIO, ID_EVENTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        // aqui van las vistas: textos, botones...

        if (cita != null) {

            findViewsById(view);

            loadDataInViews(cita);

            setOnClickListeners();

            // ahora falta llamar al calendario para actualizar el evento con el objeto cita
            // que he rellenado

        }
        return view;
    }

    private void setOnClickListeners() {

        tvDiaDetalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sacar dia, mes, año de la fecha cita
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cita.getFhInicio());
                // declaramos el dialogo
                dlgFecha = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        // cambiar el texto del dialogo
                        tvDiaDetalle.setText(sdf.format(cal.getTime()));
                        // recalcular el dia del mes
                        tvDiaSemDetalle.setText(cita.getLongWeekDayFromMilliseconds(cal.getTimeInMillis()));
                        // en la cita no se guarda el dia solo, sino con la fecha inicio
                        // y la fecha fin
                        String sfInicio = tvDiaDetalle.getText() + " " + tvHDesde.getText();
                        String sfFin = tvDiaDetalle.getText() + " " + tvHHasta.getText();
                        SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        try {
                            cita.setFhInicio(sdfh.parse(sfInicio).getTime());
                            cita.setFhFin(sdfh.parse(sfFin).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                dlgFecha.show();
                //Toast.makeText(getActivity(), "PULSADO DIA: MOSTRAR DATE PICKER", Toast.LENGTH_SHORT).show();
            }
        });
        // poner aqui los demas listeners
        tvHDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sacar dia, mes, año de la fecha cita
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cita.getFhInicio());
                // declaramos el dialogo
                dlgHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // cambio el texto
                        tvHDesde.setText(hourOfDay + ":" + minute);
                        // para el objeto cita necesito fecha+hora. La fecha la saco del tvDiaDetalle
                        String sfh = tvDiaDetalle.getText() + " " + String.format("%02d:%02d",hourOfDay) + ":" + String.format("%02d:%02d",minute);
                        SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        try {
                            cita.setFhInicio(sdfh.parse(sfh).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                dlgHora.show();
                //Toast.makeText(getActivity(), "PULSADO DIA: MOSTRAR DATE PICKER", Toast.LENGTH_SHORT).show();
            }
        });
        // poner aqui los demas listeners
        tvHHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sacar dia, mes, año de la fecha cita
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cita.getFhFin());
                // declaramos el dialogo
                dlgHora = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // cambio el texto
                        tvHHasta.setText(hourOfDay + ":" + minute);
                        // para el objeto cita necesito fecha+hora. La fecha la saco del tvDiaDetalle
                        String sfh = tvDiaDetalle.getText() + " " + String.format("%02d:%02d",hourOfDay) + ":" + String.format("%02d:%02d",minute);
                        SimpleDateFormat sdfh = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        try {
                            cita.setFhFin(sdfh.parse(sfh).getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                dlgHora.show();
                //Toast.makeText(getActivity(), "PULSADO DIA: MOSTRAR DATE PICKER", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadDataInViews(Cita cita) {
        // cargar los datos
        tvPacienteDetalle.setText(cita.getNombrePaciente());
        tvDiaDetalle.setText(cita.getDia(cita.getFhInicio()));
        tvDiaSemDetalle.setText(cita.getLongWeekDayFromMilliseconds(cita.getFhInicio()));
        tvHDesde.setText(cita.getHoraInicio());
        tvHHasta.setText(cita.getHoraFin());
        etLugar.setText(cita.getNombreDespacho());
        etObservaciones.setText(cita.getObservaciones());
    }

    private void findViewsById(View view) {
         tvPacienteDetalle = (TextView)view.findViewById(R.id.tvPacienteDetalle);
         tvDiaSemDetalle = (TextView)view.findViewById(R.id.tvDiaSemDetalle);
         tvDiaDetalle = (TextView)view.findViewById(R.id.tvDiaDetalle);
         tvHDesde = (TextView)view.findViewById(R.id.tvHDesde);
         tvHHasta = (TextView)view.findViewById(R.id.tvHHasta);
         etLugar = (EditText)view.findViewById(R.id.etLugar);
         etObservaciones = (EditText)view.findViewById(R.id.etObservaciones);
    }


}
