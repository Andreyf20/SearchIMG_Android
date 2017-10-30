package searchimg.andrey.com.searchimg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by torre on 10/29/2017.
 */

public class send_IMG extends MainActivity implements ISend {
    protected ImageView preview;
    protected Bitmap bitmapcopy;
    private String encodedIMG;
    ImageView imageview1;
    DefaultHttpClient httpclient = new DefaultHttpClient();
    String serverURL = "http://192.168.1.124:49491/api/Request/1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_img);
        imageview1 = findViewById(R.id.imageView1);
        encodedIMG = EncodeImage(bitmap);
        try{
            sendRequest();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getFileNameJPG(){
        String df = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String fname = "PNG"+ df + ".jpg";

        return fname;
    }

    public void saveIMG(View view) {

        String fname = getFileNameJPG();

        File myDir = new File(Environment.getExternalStorageDirectory() + "/InstaPOO/");
        myDir.mkdirs();

        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            //bitmapcopy.compress(Bitmap.CompressFormat.JPEG, 100, out);
            bitmapcopy.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            galleryAddPic(myDir.getAbsolutePath());
            saveIMGdiagBox();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveIMGdiagBox(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Image Saved!")
                .setMessage("The image has been saved")
                .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Does something if yes
                    }
                })
                /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing hola :V
                    }
                })*/
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void DialogBoxHTTPDONE(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Todos funciona!")
                .setMessage("Funciono?")
                .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Does something if yes
                    }
                })
                /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing hola :V
                    }
                })*/
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void DialogBoxHTTPDONENeg(){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Todo NOOOO funciona!")
                .setMessage("NOOOOO?")
                .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Does something if yes
                    }
                })
                /*.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing hola :V
                    }
                })*/
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    protected void setBitmapView(Bitmap bitmap){
        preview.setImageBitmap(bitmap);
    }

    public String EncodeImage(Bitmap bmp){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap DecodeStr(String str){
        byte[] decodedBytes = Base64.decode(str, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void sendRequest() throws UnsupportedEncodingException, JSONException {
        HttpResponse httpresponse;
        HttpPost httppostreq;
        httppostreq = new HttpPost(serverURL);
        // Objeto JSON
        JSONObject JsObject = new JSONObject();
        // ESTO ESTA DE PRUEBA CAMBIARLO SI NECESARIO
        JsObject.put(null, encodedIMG);
        ////////////////////////////////////////
        StringEntity se = new StringEntity(JsObject.toString());
        //se.setContentType("application/json;charset=UTF-8");
        se.setContentType("application/json");
        httppostreq.setEntity(se);
        //
        //Aqui se envia y se recibe la respuesta
        try {
            httpresponse = httpclient.execute(httppostreq); //as a  Json object
            DialogBoxHTTPDONE();
        } catch (IOException e) {
            DialogBoxHTTPDONENeg();
            e.printStackTrace();
        }
        //

    }
}
