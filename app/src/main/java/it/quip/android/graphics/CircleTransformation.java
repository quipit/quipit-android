package it.quip.android.graphics;


import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class CircleTransformation implements Transformation {

    public final int borderSize;
    public final int borderColor;

    /**
     * Creates a circle transformation with no border.
     */
    public CircleTransformation() {
        borderSize = 0;
        borderColor = Color.TRANSPARENT;
    }

    /**
     * Creates a circle transformation that will transform the image by drawing a border with
     * the specified size and color.
     */
    public CircleTransformation(int borderSize, int borderColor) {
        this.borderSize = borderSize;
        this.borderColor = borderColor;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());

        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }

        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);

        float r = size / 2f;

        if (borderSize > 0) {
            drawBorder(canvas, r);
        }

        // Draw the image smaller than the background so a little border will be seen
        canvas.drawCircle(r, r, r - borderSize, paint);

        squaredBitmap.recycle();
        return bitmap;
    }

    private void drawBorder(Canvas canvas, float radius) {
        // Prepare the background
        Paint paintBg = new Paint();
        paintBg.setColor(borderColor);
        paintBg.setAntiAlias(true);

        // Draw the background circle
        canvas.drawCircle(radius, radius, radius, paintBg);
    }

    @Override
    public String key() {
        return "circle(size=" + borderSize + ", color=" + borderColor + ")";
    }

}
