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

public class CLV_Klinik extends ArrayAdapter<String> {
    //Declarasi variable
    private final Activity context;
    private ArrayList<String> vNamaklinik;
    private ArrayList<String> vTagline;
    private ArrayList<String> vEmail;
    private ArrayList<String> vAlamat;
    private  ArrayList<String> vPhoto;

    public CLV_Klinik(Activity context, ArrayList<String> Nama_klinik, ArrayList<String> Tagline, ArrayList<String> Email, ArrayList<String> Alamat, ArrayList<String> Photo) {
        super(context, R.layout.item_klinik, Nama_klinik);
        this.context        = context;
        this.vNamaklinik    = Nama_klinik;
        this.vTagline       = Tagline;
        this.vEmail         = Email;
        this.vAlamat        = Alamat;
        this.vPhoto         = Photo;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView = inflater.inflate(R.layout.item_klinik, null, true);

        //Declarasi komponen
        TextView nama_klinik = rowView.findViewById(R.id.TVnama_klinik);
        TextView tagline = rowView.findViewById(R.id.idtag);
        TextView Email = rowView.findViewById(R.id.email);
        TextView Alamat = rowView.findViewById(R.id.alamat);
        CircleImageView photo = rowView.findViewById(R.id.kliniklist);

        //Set Parameter Value sesuai widget textview
        nama_klinik.setText(vNamaklinik.get(position));
        tagline.setText(vTagline.get(position));
        Email.setText(vEmail.get(position));
        Alamat.setText(vAlamat.get(position));

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



