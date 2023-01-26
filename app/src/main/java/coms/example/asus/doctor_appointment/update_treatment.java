package coms.example.asus.doctor_appointment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class update_treatment extends AppCompatActivity {

    EditText ETJenis_treatment,ETDeskrispi,ETbiaya;
    String jenis_treatment,deskripsi,biaya,photo;
    CircleImageView image;
    ProgressDialog progressDialog;
    ImageButton BTNImage;
    Button BTNupdate;

    String pilihan;
    //private static final int PHOTO_REQUEST_GALLERY = 1;//gallery
    static final int REQUEST_TAKE_PHOTO = 1;
    final int CODE_GALLERY_REQUEST = 999;
    String rPilihan[]= {"Take a Photo", "From Album"};
    public final String APP_TAG = "MyApp";
    Bitmap bitMap = null;
    public String photoFileName = "photo.jpg";
    File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_treatment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_kembali);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Intent, get data from main
        jenis_treatment = getIntent().getStringExtra("jenis_treatment");
        deskripsi              = getIntent().getStringExtra("deskripsi");
        biaya                = getIntent().getStringExtra("biaya");
        photo                = getIntent().getStringExtra("photo");


        ETJenis_treatment = findViewById(R.id.ETJenisupdate);
        ETDeskrispi       = findViewById(R.id.ETDeskripsi);
        ETbiaya           = findViewById(R.id.ETBiaya);
        image             = findViewById(R.id.idgmbrupdttreat);
        BTNImage          = findViewById(R.id.IM_updatetreat);
        BTNupdate         = findViewById(R.id.BTNupdate);
        progressDialog    = new ProgressDialog(this);


        ETJenis_treatment.setText(jenis_treatment);
        ETDeskrispi.setText(deskripsi);
        ETbiaya.setText(biaya);


        if (photo.equals("")) {
            Picasso.get().load("https://tekajeapunya.com/kelompok_1/image/noimage.png").into(image);
        } else {
            Picasso.get().load("https://tekajeapunya.com/kelompok_1/image/" + photo).into(image);
        }

        progressDialog = new ProgressDialog(this);

        BTNImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitMap != null) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(update_treatment.this);
                    alertDialogBuilder.setMessage("Do yo want to take photo again?");

                    alertDialogBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            //Toast.makeText(context,"You clicked yes button",Toast.LENGTH_LONG).show();
                            //call fuction of TakePhoto
                            TakePhoto();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                } else {
                    TakePhoto();
                }
            }
        });

        BTNupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(update_treatment.this)
                        .setMessage("Apakah kamu ingin mengubah data ini?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.setMessage("Mengubah Data...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                jenis_treatment     = ETJenis_treatment.getText().toString();
                                deskripsi           = ETDeskrispi.getText().toString();
                                biaya               = ETbiaya.getText().toString();
                                photo               = getStringImage(bitMap);

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        validatingData();
                                    }
                                },1000);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();

            }
        });
    }

    public  void TakePhoto(){
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        AlertDialog.Builder builder = new AlertDialog.Builder(update_treatment.this);
        builder.setTitle("Pilih");
        builder.setItems(rPilihan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                pilihan = String.valueOf(rPilihan[which]);

                if (pilihan.equals("Memotret foto")) {
                    // create Intent to take a picture and return control to the calling application
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Create a File reference to access to future access
                    photoFile = getPhotoFileUri(photoFileName);

                    // wrap File object into a content provider
                    // required for API >= 24
                    // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
                    String authorities = getPackageName() + ".fileprovider";
                    Uri fileProvider = FileProvider.getUriForFile(update_treatment.this, authorities, photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

                    // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
                    // So as long as the result is not null, it's safe to use the intent.
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        // Start the image capture intent to take photo
                        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
                    }
                } else {
                    ActivityCompat.requestPermissions(update_treatment.this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_GALLERY_REQUEST);
                }
            }
        });
        builder.show();
    }

    //permission
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), CODE_GALLERY_REQUEST);
            }else{
                Toast.makeText(getApplicationContext(), "Kamu tidak memiliki akses ke galeri!", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        //set photo size
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_TAKE_PHOTO) {

            if (resultCode == Activity.RESULT_OK) {
                bitMap = decodeSampledBitmapFromFile(String.valueOf(photoFile), 1000, 700);
                image.setImageBitmap(bitMap);
            } else { // Result was a failure
                Toast.makeText(update_treatment.this, "Foto tidak dapat di potret!", Toast.LENGTH_SHORT).show();
            }

        } else {

            if (intent != null) {
                Uri photoUri = intent.getData();
                // Do something with the photo based on Uri
                bitMap = null;
                try {
                    //InputStream inputStream = getContentResolver().openInputStream(photoUri);
                    //bitMap = BitmapFactory.decodeStream(inputStream);

                    //btnImage.setVisibility(View.VISIBLE);
                    bitMap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Load the selected image into a preview
                image.setImageBitmap(bitMap);
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    //get data photo
    public File getPhotoFileUri(String fileName)  {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            return file;
        }
        return null;
    }
    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    void validatingData(){
        if(jenis_treatment.equals("") || deskripsi.equals("") || biaya.equals("")){
            progressDialog.dismiss();
            Toast.makeText(update_treatment.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            updateData();
        }
    }

    void updateData(){
        String photo = getStringImage(bitMap);
        AndroidNetworking.post("https://tekajeapunya.com/kelompok_1/updatetreatment.php")
                .addBodyParameter("jenis_treatment", "" + jenis_treatment)
                .addBodyParameter("deskripsi", "" + deskripsi)
                .addBodyParameter("biaya", "" + biaya)
                .addBodyParameter("photo", "" + photo)
                .setPriority(Priority.MEDIUM)
                .setTag("Update Data")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("cekUpdate", "" + response);
                        try {
                            Boolean status = response.getBoolean("status");
                            String pesan = response.getString("result");
                            Toast.makeText(update_treatment.this, "" + pesan, Toast.LENGTH_SHORT).show();
                            Log.d("status",""+status);

                            if (status) {
                                new AlertDialog.Builder(update_treatment.this)
                                        .setMessage("Data berhasil di update!")
                                        .setCancelable(false)
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(update_treatment.this, item_treatment.class);
                                                startActivity(i);
                                                update_treatment.this.finish();
                                            }
                                        })
                                        .show();
                            } else {
                                new AlertDialog.Builder(update_treatment.this)
                                        .setMessage("Gagal mengupdate data !")
                                        .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = getIntent();
                                                setResult(RESULT_CANCELED, i);
                                                update_treatment.this.finish();
                                            }
                                        })
                                        .setCancelable(false)
                                        .show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        Log.d("Cannot update your data",""+anError.getErrorBody());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_delete){
            new AlertDialog.Builder(update_treatment.this)
                    .setMessage("Apakah kamu ingin menghapus data  "+jenis_treatment+"?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog.setMessage("Menghapus...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            AndroidNetworking.post("https://tekajeapunya.com/kelompok_1/hapustreatment.php")
                                    .addBodyParameter("jenis_treatment",""+jenis_treatment)
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            progressDialog.dismiss();
                                            try {
                                                Boolean status = response.getBoolean("status");
                                                Log.d("status",""+status);
                                                String result = response.getString("result");
                                                if(status){
                                                    new AlertDialog.Builder(update_treatment.this)
                                                            .setMessage("Data telah dihapus!")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Kembali", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Intent i = new Intent(update_treatment.this, item_treatment.class);
                                                                    startActivity(i);
                                                                    update_treatment.this.finish();
                                                                }
                                                            })
                                                            .show();

                                                }else{
                                                    Toast.makeText(update_treatment.this, ""+result, Toast.LENGTH_SHORT).show();
                                                }
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onError(ANError anError) {
                                            anError.printStackTrace();
                                        }
                                    });
                        }
                    })
                    .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .show();


        }
        return super.onOptionsItemSelected(item);
    }

}