package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import com.squareup.picasso.Transformation;

class BlurTransformation implements Transformation {

    private final String key;
    private final Context context;

    BlurTransformation(String key, Context context) {
        this.key = key;
        this.context = context;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        Bitmap fastblur = fastblur(bitmap, 25.0f);
        return fastblur(fastblur, 25.0f);
    }

    public Bitmap fastblur(Bitmap sentBitmap, float radius) {
        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT );
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(sentBitmap);
        return sentBitmap;
    }

    @Override
    public String key() {
        return key;
    }
}
