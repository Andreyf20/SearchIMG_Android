package searchimg.andrey.com.searchimg;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by torre on 10/29/2017.
 */

public class send_IMG extends MainActivity implements ISend {
    protected Bitmap bitmapcopy;
    protected String encodedIMG;
    private ImageView imageview1;
    private TextView txt;
    //private String serverURL = "http://192.168.43.211:10491/api/Request/1";
    //private String serverURL = "http://192.168.43.211:10491/api/Request";
    private String serverURL = "https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoList.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_img);
        imageview1 = findViewById(R.id.imageView1);
        if(bitmap != null)
            encodedIMG = EncodeImage(bitmap);
        txt = (TextView) findViewById(R.id.textView);
        new JSONtask().execute(serverURL);
    }

    private String getFileNameJPG(){
        String df = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String fname = "PNG"+ df + ".jpg";

        return fname;
    }

    public void saveIMG(View view) {

        String fname = getFileNameJPG();

        File myDir = new File(Environment.getExternalStorageDirectory() + "/SearchIMG/");
        myDir.mkdirs();

        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmapcopy.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            galleryAddPic(myDir.getAbsolutePath());
            diagBox("Saved!", "The image has been saved");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void diagBox(String str1, String str2) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog);
        builder.setTitle(str1)
                .setMessage(str2)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Does something if yes
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    public String EncodeImage(Bitmap bmp){
        if(bmp == null)
            return "null";
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public Bitmap DecodeStr(String str) {
        try{
            byte[] decodedBytes = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    class JSONtask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(serverURL);
                connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                //connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.connect();


                //POST METHOD

                /*JSONObject postData = new JSONObject();
                postData.put("image", encodedIMG);
                String postStr = postData.toString();
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(postStr);
                wr.flush();
                wr.close();*/




                //GET MEHTOD CONNETION STREAM
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();

                String line = "";
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }


                String stringBuffer = buffer.toString();
                /*JSONObject parenObject = new JSONObject(stringBuffer);

                JSONArray parentArray = parenObject.getJSONArray("image");

                String img = null;

                for(int i = 0; i < parentArray.length(); i++){
                    JSONObject final_object = parentArray.getJSONObject(i);

                    img = final_object.getString("encodedimg");
                }*/

                return stringBuffer;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                diagBox("ERROR1", "MalformedURL");
            } catch (IOException e) {
                e.printStackTrace();
                diagBox("ERROR2", "IOException");
            } finally {
                if(connection != null)
                    connection.disconnect();
                if(reader != null)
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            return null;
        }

        @Override
        protected void onPostExecute(final String result) {
            super.onPostExecute(result);
            txt.setText(result);
            diagBox("PROCESO TERMINADO", "Se termino el proceso de http");
        }
    }
}


