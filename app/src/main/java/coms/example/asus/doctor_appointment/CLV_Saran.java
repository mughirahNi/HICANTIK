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

public class CLV_Saran extends ArrayAdapter<String> {

    private final Activity context;
    private ArrayList<String> vNama;
    private ArrayList<String> vemail;
    private ArrayList<String> vSaran;


    public CLV_Saran(Activity context, ArrayList<String> Nama, ArrayList<String> Email, ArrayList<String> saran) {
        super(context, R.layout.list_item_saran,Nama);
        this.context = context;
        this.vNama = Nama;
        this.vemail = Email;
        this.vSaran = saran;

    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //Load Custom Layout untuk list
        View rowView= inflater.inflate(R.layout.list_item_saran, null, true);

        //Declarasi komponen
        TextView nama             = rowView.findViewById(R.id.sarannama);
        TextView email            =  rowView.findViewById(R.id.saranemail);
        TextView saran            =  rowView.findViewById(R.id.tvsaran);

        //Set Parameter Value sesuai widget textview
        nama.setText(vNama.get(position));
        email.setText(vemail.get(position));
        saran.setText(vSaran.get(position));

        //Load the animation from the xml file and set it to the row
        //load animasi untuk listview
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.down_from_top);
        animation.setDuration(500);
        rowView.startAnimation(animation);

        return rowView;
    }


}


