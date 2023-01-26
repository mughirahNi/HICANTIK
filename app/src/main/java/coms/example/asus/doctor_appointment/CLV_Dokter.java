package coms.example.asus.doctor_appointment;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CLV_Dokter extends ArrayAdapter<String> {
    //Declarasi variable
    private final Activity context;
    private ArrayList<String> vNama_dokter;
    private ArrayList<String> vKeahlian;
    private ArrayList<String> vKontak;
    private ArrayList<String> vJadwal;
    private ArrayList<String> vAlamat_klinik;
    private  ArrayList<String> vPhoto;

    public CLV_Dokter(Activity context, ArrayList<String> Nama_dokter, ArrayList<String> Keahlian, ArrayList<String> Kontak, ArrayList<String> Jadwal, ArrayList<String> Alamat_klinik, ArrayList<String> Photo) {
        super(context, R.layout.item_dokter, Nama_dokter);
        this.context            = context;
        this.vNama_dokter       = Nama_dokter;
        this.vKeahlian          = Keahlian;
        this.vKontak            = Kontak;
        this.vJadwal            = Jadwal;
        this.vAlamat_klinik     = Alamat_klinik;
        this.vPhoto             = Photo;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView = inflater.inflate(R.layout.item_dokter, null, true);

        //Declarasi komponen
        TextView nama_dokter = rowView.findViewById(R.id.Tvnamadok);
        TextView keahlian = rowView.findViewById(R.id.TVkeahlian);
        TextView kontak = rowView.findViewById(R.id.Tvkontak);
        TextView jadwal = rowView.findViewById(R.id.TVjadwal);
        TextView alamat_klinik = rowView.findViewById(R.id.TVnama_klinik);
        CircleImageView photo = rowView.findViewById(R.id.profil);

        //Set Parameter Value sesuai widget textview
        nama_dokter.setText(vNama_dokter.get(position));
        keahlian.setText(vKeahlian.get(position));
        kontak.setText(vKontak.get(position));
        jadwal.setText(vJadwal.get(position));
        alamat_klinik.setText(vAlamat_klinik.get(position));

        if (vPhoto.get(position).equals(""))
        {
            Picasso.get().load("https://tekajeapunya.com/kelompok_1/image/noimage.png").into(photo);
        }
        else
        {
            Picasso.get().load("https://tekajeapunya.com/kelompok_1/image/"+vPhoto.get(position)).into(photo);
        }

        //Load the animation from the xml file and set it to the row
        //load animasi untuk listview
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        return rowView;
    }
}



