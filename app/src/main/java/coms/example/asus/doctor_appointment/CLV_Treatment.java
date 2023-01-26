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

import coms.example.asus.doctor_appointment.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CLV_Treatment extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> vJenis_Treatment;
    private ArrayList<String> vDeskripsi;
    private ArrayList<String> vBiaya;
    private ArrayList<String> vPhoto;


    public CLV_Treatment(Activity context, ArrayList<String> Jenis_treatment, ArrayList<String> Deskripsi, ArrayList<String> Biaya, ArrayList<String> Photo) {
        super(context, R.layout.item_treatment,Jenis_treatment);
        this.context          = context;
        this.vJenis_Treatment = Jenis_treatment;
        this.vDeskripsi       = Deskripsi;
        this.vBiaya           = Biaya;
        this.vPhoto           = Photo;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate(R.layout.item_treatment, null, true);

        //Declarasi komponen
        TextView jenis_treatment  = rowView.findViewById(R.id.TVjenis);
        TextView deskripsi          = rowView.findViewById(R.id.iddeskripsi);
        TextView biaya             = rowView.findViewById(R.id.idbiaya);
        CircleImageView photo      = rowView.findViewById(R.id.treatmentlist);

        //Set Parameter Value sesuai widget textview
        jenis_treatment.setText(vJenis_Treatment.get(position));
        deskripsi.setText(vDeskripsi.get(position));
        biaya.setText(vBiaya.get(position));

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