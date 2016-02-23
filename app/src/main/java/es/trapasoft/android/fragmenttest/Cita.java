package es.trapasoft.android.fragmenttest;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Modelo para una cita
 * Lo hacemos Parcelable para poder pasarlo como parametro
 * a los fragmentos
 *
 * Created by Administrador on 07/02/2016.
 */
public class Cita implements Parcelable{
    private long idCalendario;
    private long idEvento;
    private int idPaciente;
    private String nombrePaciente;
    private long fhInicio;
    private long fhFin;
    private int idDespacho;
    private String nombreDespacho;
    private String observaciones;
    private Double tarifa;
    private boolean facturar;

    @Override
    public String toString() {
        return "Cita{" +
                "nombrePaciente='" + nombrePaciente + '\'' +
                ", cita=" + getfHoraInicio() +
                '}';
    }

    // CONSTRUCTORES
    public Cita() {
    }

    public Cita(long idCalendario, long idEvento, String nombrePaciente, long fhInicio, long fhFin, String observaciones) {
        this.idCalendario=idCalendario;
        this.idEvento = idEvento;
        this.nombrePaciente = nombrePaciente;
        this.fhInicio = fhInicio;
        this.fhFin = fhFin;
        this.observaciones = observaciones;
    }
    public Cita(long idCalendario, String nombrePaciente, long fhInicio, long fhFin, String observaciones) {
        this.idCalendario=idCalendario;
        this.nombrePaciente = nombrePaciente;
        this.fhInicio = fhInicio;
        this.fhFin = fhFin;
        this.observaciones = observaciones;
    }

    public Cita(long idCalendario, int idPaciente, String nombrePaciente, long fhInicio, long fhFin, int idDespacho, String observaciones, Double tarifa, boolean facturar) {
        this.idCalendario=idCalendario;
        this.idPaciente = idPaciente;
        this.nombrePaciente = nombrePaciente;
        this.fhInicio = fhInicio;
        this.fhFin = fhFin;
        this.idDespacho = idDespacho;
        this.observaciones = observaciones;
        this.tarifa = tarifa;
        this.facturar = facturar;
    }
    // esto es un constructor especifico para Parcel
    private Cita(Parcel in) {
        idCalendario=in.readLong();
        idPaciente=in.readInt();
        fhInicio=in.readLong();
        fhFin=in.readLong();

        idDespacho=in.readInt();
        tarifa=in.readDouble();
        // no hay metodo para booleanos: leemos uno o cero
        facturar=(in.readInt()==1);

        nombreDespacho = in.readString();
        nombrePaciente = in.readString();
        observaciones = in.readString();
    }
    // GETTERS - SETTERS


    public long getIdCalendario() {
        return idCalendario;
    }

    public void setIdCalendario(long idCalendario) {
        this.idCalendario = idCalendario;
    }

    public long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(long idEvento) {
        this.idEvento = idEvento;
    }

    public String getNombreDespacho() {
        return nombreDespacho;
    }

    public void setNombreDespacho(String nombreDespacho) {
        this.nombreDespacho = nombreDespacho;
    }

    public long getFhInicio() {
        return fhInicio;
    }

    public void setFhInicio(long fhInicio) {
        this.fhInicio = fhInicio;
    }

    public long getFhFin() {
        return fhFin;
    }

    public void setFhFin(long fhFin) {
        this.fhFin = fhFin;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }


    public int getIdDespacho() {
        return idDespacho;
    }

    public void setIdDespacho(int idDespacho) {
        this.idDespacho = idDespacho;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Double getTarifa() {
        return tarifa;
    }

    public void setTarifa(Double tarifa) {
        this.tarifa = tarifa;
    }

    public boolean isFacturar() {
        return facturar;
    }

    public void setFacturar(boolean facturar) {
        this.facturar = facturar;
    }

    // metodos de gestion de fechas
    public String getDia(long milis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        return String.format("%02d/%02d/%04d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)+1, // LOS MESES EMPIEZAN EN CERO
                cal.get(Calendar.YEAR));
    }

    public Date getDiaAsDate(long milis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        return cal.getTime();
    }

    public String getHoraInicio() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fhInicio);
        return String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
    }

    public String getHoraFin() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fhFin);
        return String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));

    }

    public String getfHoraInicio() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fhInicio);
        return String.format("%02d/%02d/%04d %02d:%02d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
                );
    }

    public String getfHoraFin() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(fhFin);
        return String.format("%02d/%02d/%04d %02d:%02d",
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.MONTH)+1,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
        );    }

    public String getFechaHoraAsString(Date laFechaHora) {
        if (laFechaHora != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss");
            return sdf.format(laFechaHora);
        } else {
            return null;
        }
    }


    public String getWeekDayFromMilliseconds(Long milis) {
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

    public String getLongWeekDayFromMilliseconds(Long milis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(milis);
        int diasem = cal.get(Calendar.DAY_OF_WEEK);
        switch (diasem) {
            case Calendar.MONDAY:
                return "LUNES";
            case Calendar.TUESDAY:
                return "MARTES";
            case Calendar.WEDNESDAY:
                return "MIÉRCOLES";
            case Calendar.THURSDAY:
                return "JUEVES";
            case Calendar.FRIDAY:
                return "VIERNES";
            case Calendar.SATURDAY:
                return "SÁBADO";
            case Calendar.SUNDAY:
                return "DOMINGO";
            default:
                return "???";
        }
    }
    /**
     * Devuelve el hash del objeto
     * basado en su cadena toString
     *
     * @return
     */
    public int getId() {
        return toString().hashCode();
    }


    // METODOS OBLIGATORIOS PARA LA INTERFAZ PARCELABLE

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(idCalendario);
        dest.writeInt(idPaciente);
        dest.writeLong(fhInicio);
        dest.writeLong(fhFin);

        dest.writeInt(idDespacho);
        dest.writeDouble(tarifa);
        // no hay metodo para booleanos: guardamos uno o cero
        dest.writeInt(facturar ? 1 : 0);
        // no hay metodo para Date: guardamos milliseconds
        dest.writeLong(fhInicio);
        dest.writeLong(fhFin);
        dest.writeString(nombreDespacho);
        dest.writeString(nombrePaciente);
        dest.writeString(observaciones);
    }



    // obligatorio segun http://developer.android.com/intl/es/reference/android/os/Parcelable.html
    public static final Creator<Cita> CREATOR = new Creator<Cita>() {

        @Override
        public Cita createFromParcel(Parcel source) {
            return new Cita(source);
        }

        @Override
        public Cita[] newArray(int size) {
            return new Cita[size];
        }
    };
}
