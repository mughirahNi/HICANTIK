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

public class CLV_verifikasi extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> vnama_lengkap;
    private ArrayList<String> vTreatment;
    private ArrayList<String> vdokter;
    private ArrayList<String> vtanggal;
    private ArrayList<String> vwaktu;
    private ArrayList<String> vnomor_antrian;

    public CLV_verifikasi(Activity context, ArrayList<String> Nama_lengkap, ArrayList<String> Treatment, ArrayList<String> Dokter, ArrayList<String> Tanggal, ArrayList<String>Waktu, ArrayList<String>Nomor_antrian) {
        super(context, R.layout.activity_finish_reservasi,Nama_lengkap);
        this.context            = context;
        this.vnama_lengkap      = Nama_lengkap;
        this.vTreatment         = Treatment;
        this.vdokter            = Dokter;
        this.vtanggal           = Tanggal;
        this.vwaktu             =Waktu;
        this.vnomor_antrian     = Nomor_antrian;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate(R.layout.activity_finish_reservasi, null, true);

        //Declarasi komponen
        TextView nama_lengkap         = rowView.findViewById(R.id.nma_lengkap);
        TextView treatment            =  rowView.findViewById(R.id.jenis);
        TextView dokter               =  rowView.findViewById(R.id.dokter);
        TextView tanggal              = rowView.findViewById(R.id.tvtgl);
        TextView waktu                = rowView.findViewById(R.id.waktu);
        TextView nomor_antrian        = rowView.findViewById(R.id.nomor);


        //Set Parameter Value sesuai widget textview
        nama_lengkap.setText(vnama_lengkap.get(position));
        treatment.setText(vTreatment.get(position));
        dokter.setText(vdokter.get(position));
        tanggal.setText(vtanggal.get(position));
        waktu.setText(vwaktu.get(position));
        nomor_antrian.setText(vnomor_antrian.get(position));

        //Load the animation from the xml file and set it to the row
        //load animasi untuk listview
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        return rowView;
    }


}


