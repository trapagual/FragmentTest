package es.trapasoft.android.fragmenttest;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrador on 12/02/2016.
 */
public class FragmentAgenda extends ListFragment implements AdapterView.OnItemClickListener{

    public static long ID_CALENDARIO;

    private TextView tvLabelIdCalendario;




    // INTERFAZ QUE HA DE IMPLEMENTAR LA ACTIVITY PARA PASARLE EL CONTROL
    public interface OnFragmentAgendaSelectedListener {
        public void onItemSelected(long itemID);
    }

    // HACE FALTA UN OBJETO DE ESTA INTERFAZ PARA USARLO
    OnFragmentAgendaSelectedListener mCallback;

    /*
      La activity padre ha de implementar esta interfaz para poder recibir los clicks
      de los botones
      Usamos aqui un bloque try/catch en el metodo onAttach del gragmento para asegurarnos
      de que la actividad implementa la interfaz


        OJO este truco de sobrecargar onAttach se debe a un bug en la Google API 23
        Han deprecado onAttach(Activity) y hay creado onAttach(Context). Pero este ultimo
        metodo no es llamado si API < 23, luego hay que hacer una ñapa creando un tercer metodo
        y hacer que se llame desde los otros dos
        Si la version es >=23, se llama a ambos metodos, luego tenemos que poner un if para
        que no lo llame dos veces
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAttachToContext(context);
    }

    private void onAttachToContext(Context context) {
        // asegurar que la actividad padre inplementa la interfaz de callback
        try {
            // Instanciar la interfaz para poder enviar los clicks al host
            mCallback = (OnFragmentAgendaSelectedListener) context;
        } catch (ClassCastException e) {
            // if the activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " ha de implementar la interfaz OnFragmentAgendaSelectedListener");
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onAttach(Activity activity) {
        // esto sera llamado siempre (version <23 o >23
        super.onAttach(activity);
        // luego solo tenemos que llamar al metodo que tiene la chicha si SDK < 23
        if (Build.VERSION.SDK_INT < 23)
            onAttachToContext(activity);
    }

    // CONSTRUCTOR PUBLICO VACIO OBLIGATORIO
    public FragmentAgenda() {
    }

    // TAG UNICA PARA EL NEWINSTANCE
    public static final String TAG = "FragmentAgenda";

    // METODO NEWINSTANCE PARA PODER PASARLE ARGUMENTOS AL FRAGMENT
    // DESDE LA ACTIVITY
    public static FragmentAgenda newInstance(long id_calendario) {
        FragmentAgenda f = new FragmentAgenda();
        Bundle args = new Bundle();
        args.putLong("id_calendario", id_calendario);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ID_CALENDARIO = getArguments().getLong("id_calendario");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        tvLabelIdCalendario = (TextView)view.findViewById(R.id.tvLabelCalendario);
        // tvLabelIdCalendario.setText("Usando Calendario: " + ID_CALENDARIO);
        tvLabelIdCalendario.setText("Citas esta Semana: (" + ID_CALENDARIO + ")");
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.setTime(new Date());
        // produccion: la fecha de hoy
        long hoy = cal.getTimeInMillis();

        // debug poner un dia del que tenga eventos
                    cal.set(Calendar.DAY_OF_MONTH, 27);
                    cal.set(Calendar.MONTH, 0); // los meses empiezan por cero
                    cal.set(Calendar.YEAR, 2016);
                    hoy = cal.getTimeInMillis();
        // debug poner un dia del que tenga eventos

        CursorAdapter adapter = new CitasCursorAdapter(getActivity(),
                UtilCalendar.leerEventosCalendarioCursor(getActivity(),ID_CALENDARIO, sacaLunes(hoy), sacaViernes(hoy)),
                false);
        //ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Planets, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }


    // METODO QUE VUELVE A LA ACTIVITY LLAMANTE
    // PASANDOLE EL ID DEL ITEM SELECCIONADO
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Toast.makeText(getActivity(), "Item: " + position + " Id:" + id, Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Antes de volver a la actividad, el ID es " + id);
        mCallback.onItemSelected(id);
    }

    /**
     * Devuelve los milliseconds correspondientes al lunes de la semana de "fecha"
     * o, si "fecha" es sabado o domingo, el lunes siguiente
     * @param fecha en milliseconds
     * @return fecha en milliseconds
     */
    private long sacaLunes(long fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(fecha));

        int dow = cal.get(Calendar.DAY_OF_WEEK);
        // 1=domingo, 2=lunes, 3=martes, 4=miercoles, 5=jueves, 6=viernes, 7=sabado

        switch(dow) {
            case Calendar.MONDAY:
                return fecha;
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)+1);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                return cal.getTimeInMillis();
            default:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                return cal.getTimeInMillis();
        }

    }

    /**
     * Devuelve los milliseconds correspondientes al viernes de la semana de "fecha"
     * o, si "fecha" es sabado o domingo, el viernes siguiente
     * @param fecha en milliseconds
     * @return fecha en milliseconds
     */
    private static long sacaViernes(long fecha) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(fecha));



        int dow = cal.get(Calendar.DAY_OF_WEEK);
        // 1=domingo, 2=lunes, 3=martes, 4=miercoles, 5=jueves, 6=viernes, 7=sabado

        switch(dow) {
            case Calendar.FRIDAY:
                return fecha;
            case Calendar.SATURDAY:
            case Calendar.SUNDAY:
                cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR)+1);
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                return cal.getTimeInMillis();
            default:
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                return cal.getTimeInMillis();
        }

    }
}
