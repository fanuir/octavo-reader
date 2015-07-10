package com.fanuir.octavoreader;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ivy on 7/9/15.
 */
public class Reader extends TextView {

    public Reader(Context context, AttributeSet attrs){
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context);
    }
}
