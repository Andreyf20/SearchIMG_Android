package com.andrey.instapoo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int GALLERY_REQUEST_CODE = 3;
    private ImageView imageview;
    private String TAG = "LOGS"; // For debugging
    private String mCameraFileName; // Path of the image saved from camera
    protected static Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageview = (ImageView)findViewById(R.id.imageView);
        File folder = new File(Environment.getExternalStorageDirectory() + "/InstaPOO/");
        if(!folder.exists())
            folder.mkdirs();
        this.checkAndRequestPermissions();
    }

    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);

        int storagePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSIONS_REQUEST_CODE);
            return false;
        }
        return true;
    }

    public void takepic(View view){
        Log.d(TAG, "Está entrando en la función");

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String df = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String newPicFile = "JPEG_"+ df + ".jpg";
        String outPath = Environment.getExternalStorageDirectory() + "/InstaPOO/" + newPicFile;
        File outFile = new File(outPath);

        mCameraFileName = outFile.toString();
        Uri outuri = Uri.fromFile(outFile);
        Log.d(TAG, "Se creó el nuevo archivo");
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
        Log.d(TAG, "Se va a iniciar la cámara");

        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

    }

    public void choosegallery(View view){
        Intent choosegalleryintent = new Intent();
        choosegalleryintent.setType("image/*");
        choosegalleryintent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(choosegalleryintent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "Está entrando en la función de onactivityresult");
        if(resultCode == RESULT_OK && requestCode == CAMERA_REQUEST_CODE) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
            }
            if (uri == null && mCameraFileName != null) {
                uri = Uri.fromFile(new File(mCameraFileName));
            }
            File file = new File(mCameraFileName);
            if (!file.exists()) {
                file.mkdir();
                this.galleryAddPic(mCameraFileName);
            }

            bitmap = BitmapFactory.decodeFile(mCameraFileName);
            imageview.setImageBitmap(bitmap);
        }
        if(resultCode == RESULT_OK && requestCode == GALLERY_REQUEST_CODE){
            Uri pickedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap = BitmapFactory.decodeFile(imagePath, options);

            imageview.setImageBitmap(bitmap);
            cursor.close();
        }
    }

    protected void galleryAddPic(String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public void changelayout(View view) {
        Intent intent = new Intent(this, SearchIMG.class);
        startActivity(intent);
    }
}
