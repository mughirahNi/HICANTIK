package coms.example.asus.doctor_appointment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CLV_Reservasi extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> vNama_lengkap;
    private ArrayList<String> vTreatment;
    private  ArrayList<String>vDokter;
    private ArrayList<String> vTanggal;
    private ArrayList<String> vWaktu;
    private  ArrayList<String>vReservasi;


    public CLV_Reservasi(Activity context, ArrayList<String> Nama_lengkap, ArrayList<String> Treatment, ArrayList<String> Dokter, ArrayList<String> Tanggal, ArrayList<String> Waktu, ArrayList<String> Reservasi) {
        super(context, R.layout.list_reservasi,Nama_lengkap);
        this.context = context;
        this.vNama_lengkap = Nama_lengkap;
        this.vTreatment = Treatment;
        this.vDokter = Dokter;
        this.vTanggal = Tanggal;
        this.vWaktu = Waktu;
        this.vReservasi = Reservasi;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate(R.layout.list_reservasi, null, true);

        //Declarasi komponen
        TextView nama_lengkap     = rowView.findViewById(R.id.nama_lengkap);
        TextView treatment        =  rowView.findViewById(R.id.treatmenreservasi);
        TextView dokter           = rowView.findViewById(R.id.dok);
        TextView tanggal          =  rowView.findViewById(R.id.TVtanggal);
        TextView waktu            =  rowView.findViewById(R.id.TVwaktu);


        //Set Parameter Value sesuai widget textview
        nama_lengkap.setText(vNama_lengkap.get(position));
        treatment.setText(vTreatment.get(position));
        dokter.setText(vDokter.get(position));
        tanggal.setText(vTanggal.get(position));
        waktu.setText(vWaktu.get(position));


        //Load the animation from the xml file and set it to the row
        //load animasi untuk listview
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        return rowView;
    }


}


