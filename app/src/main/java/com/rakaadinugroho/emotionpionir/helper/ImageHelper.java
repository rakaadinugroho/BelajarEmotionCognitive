package com.rakaadinugroho.emotionpionir.helper;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.microsoft.projectoxford.emotion.contract.FaceRectangle;

/**
 * Created by Raka Adi Nugroho on 3/19/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class ImageHelper {
    public static Bitmap drawRectOnBitmap(Bitmap mBitmap, FaceRectangle faceRectangle, String status){
        Bitmap bitmap   = mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas   = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(12);

        canvas.drawRect(faceRectangle.left, faceRectangle.top, faceRectangle.left+faceRectangle.width, faceRectangle.top+faceRectangle.height, paint);

        int canvasX = faceRectangle.left + faceRectangle.width;
        int canvasY = faceRectangle.top + faceRectangle.height;

        drawTextOnBitmap(canvas, 100, canvasX / 2 + canvasX / 5, canvasY + 100, Color.BLUE, status );
        return bitmap;
    }

    private static void drawTextOnBitmap(Canvas canvas, int size, int vertical, int horizontal, int color, String status) {
        Paint textStatusWatermark   = new Paint();
        textStatusWatermark.setAntiAlias(true);
        textStatusWatermark.setStyle(Paint.Style.FILL);
        textStatusWatermark.setColor(color);
        textStatusWatermark.setTextSize(size);

        canvas.drawText(status, vertical, horizontal, textStatusWatermark);

    }
}
