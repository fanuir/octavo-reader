package com.fanuir.octavoreader;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by ivy on 7/26/15.
 */
public class WebReaderInterface {

    Context mContext;

    WebReaderInterface(Context context){
        mContext = context;
    }

    @JavascriptInterface
    public void showToast(String toast){
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

}
