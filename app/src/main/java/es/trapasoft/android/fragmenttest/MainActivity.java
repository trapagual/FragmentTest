package es.trapasoft.android.fragmenttest;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FragmentAgenda.OnFragmentAgendaSelectedListener {

    private static final String TAG = "MainActivity";
    private String NOMBRE_CUENTA;
    private long ID_CALENDARIO;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crearToolbar();

        NOMBRE_CUENTA = leerCuenta();

        ID_CALENDARIO = leerCalendario(NOMBRE_CUENTA);

        crearFragmentoListaEventos();

    }

    private void crearFragmentoListaEventos() {
        // crear dinamicamente el fragmento
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentAgenda fta = FragmentAgenda.newInstance(ID_CALENDARIO);
        ft.replace(R.id.fragment_placeholder, fta);

        ft.commit();
    }


    private String leerCuenta() {
        // aqui hay que leer preferencias y si no existen, preguntarle
        // al usuario mostrandole todas las cuentas configuradas en un dialogo
        // DEBUG de momento devuelvo el nombre de la cuenta del tiron
        return "trapagual@gmail.com";
    }

    private long leerCalendario(String cuenta) {
        // aqui hay que leer preferencias y si no existen, preguntarle al usuario
        // mostrandole todos los calendarios sincronizados y visibles del dispositivo
        // que pertenezcan a "cuenta"
        // DEBUG de momento devuelvo el id del calendario del tiron
        return 10;
    }
    private void crearToolbar() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnu_config:
                //display in short period of time
                Toast.makeText(getApplicationContext(), "Has pulsado CONFIGURACION", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mnu_siguiente:
                //display in short period of time
                Toast.makeText(getApplicationContext(), "Has pulsado SIGUIENTE", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.mnu_borrar_preferencias:
                borrarPreferencias();
                return true;
            /* otros casos
            case R.id.help:
                showHelp();
                return true;
            */
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Este es un metodo de debug
     * que borra las preferencias para probar que se recrean correctamente
     * No utilizar en producción
     */
    private void borrarPreferencias(){
        SharedPreferences settings = getSharedPreferences("FragmentTestPrefs", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    /**
     * Este metodo es de FragmentAgenda.
     * Sirve para que el fragmento me devuelva un parametro
     * que me servira para llamar al otro fragmento, el de detalle
     * @param itemID el ID del calendario, que hay que pasarle al otro fragmento
     */
    @Override
    public void onItemSelected(long itemID) {
        Log.d(TAG, "En la actividad, el ID es " + itemID);
        // Toast.makeText(this, "En Activity, Item Id:" + itemID, Toast.LENGTH_SHORT).show();


        // SUSTITUIR EL FRAGMENTO DE LA LISTA POR EL DETALLE
        crearFragmentoDetalle(ID_CALENDARIO, itemID);

    }

    /**
     * Crea el fragmento de detalle y le pasa el
     */
    private void crearFragmentoDetalle(long calID, long itemID) {
        Fragment fragDetalle = FragmentDetalle.newInstance(calID, itemID);

        // AÑADIR EL FRAGMENTO A LA ACTIVIDAD SUSTITUYENDO AL QUE HUBIERA
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, fragDetalle);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }
}
