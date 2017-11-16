package searchimg.andrey.com.searchimg;

/**
 * Created by torre on 11/15/2017.
 */


import android.os.Environment;

import org.junit.Test;
import android.support.v7.app.AppCompatActivity;

import static org.junit.Assert.*;

public class UnitTest extends AppCompatActivity {
    MainActivity mainacivity = new MainActivity();
    send_IMG send_img = new send_IMG();

    @Test
    public void isConnected_test(){
        assertEquals(false, mainacivity.isNetworkAvailable());
    }

    @Test
    public void permissions_test(){
        assertEquals(true, mainacivity.checkAndRequestPermissions());
    }

    @Test
    public void addTogallery_test(){
        assertEquals(true, mainacivity.galleryAddPic(Environment.getExternalStorageDirectory() + "/SearchIMG/"));
    }

    @Test
    public void encode_test(){
        assertEquals("null", send_img.EncodeImage(null));
    }

    @Test
    public void decode_test() {
        assertNull(send_img.DecodeStr("InvalidString"));
    }
}
