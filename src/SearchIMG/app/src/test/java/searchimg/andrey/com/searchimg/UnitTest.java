package searchimg.andrey.com.searchimg;

/**
 * Created by torre on 11/15/2017.
 */

import android.os.Environment;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UnitTest extends MainActivity{
    MainActivity mainacivity = new MainActivity();

    UnitTest(){
    }

    @Test
    public void isConnected_test(){
        assertEquals(mainacivity.isNetworkAvailable(), false);
    }

    @Test
    public void permissions_test(){
        assertEquals(mainacivity.checkAndRequestPermissions(), false);
    }

    @Test
    public void addTogallery_test(){
        assertEquals(mainacivity.galleryAddPic(Environment.getExternalStorageDirectory() + "/SearchIMG/"), false);
    }


}
