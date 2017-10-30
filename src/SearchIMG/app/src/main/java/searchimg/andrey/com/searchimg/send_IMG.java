package searchimg.andrey.com.searchimg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by torre on 10/29/2017.
 */

public class send_IMG extends MainActivity {
    protected ImageView preview;
    protected Bitmap bitmapcopy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_img);
        //preview = (ImageView)findViewById(R.id.preview);
        //preview.setImageBitmap(bitmap);
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
                        // do nothing
                    }
                })*/
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    protected void setBitmapView(Bitmap bitmap){
        preview.setImageBitmap(bitmap);
    }

}
