package searchimg.andrey.com.searchimg;

import android.graphics.Bitmap;

/**
 * Created by torre on 10/30/2017.
 */

public interface ISend {
    String EncodeImage(Bitmap bmp);

    Bitmap DecodeStr(String str);


}
