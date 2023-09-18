package camtrack.cmeet.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class SignatureView extends View {

    private static final float DEFAULT_STROKE_WIDTH = 5f;
    private static final int DEFAULT_STROKE_COLOR = Color.BLACK;

    private Path signaturePath;
    private Paint signaturePaint;
    private Canvas canvas;
    private Bitmap canvasBitmap;

    public SignatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        signaturePath = new Path();
        signaturePaint = new Paint();
        signaturePaint.setAntiAlias(true);
        signaturePaint.setColor(DEFAULT_STROKE_COLOR);
        signaturePaint.setStyle(Paint.Style.STROKE);
        signaturePaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(canvasBitmap, 0, 0, signaturePaint);
        canvas.drawPath(signaturePath, signaturePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                signaturePath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                signaturePath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                signaturePath.lineTo(touchX, touchY);
                canvas.drawPath(signaturePath, signaturePaint);
                signaturePath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void clearSignature() {
        canvas.drawColor(Color.WHITE);
        invalidate();
    }

    public Bitmap getSignatureBitmap() {
        return canvasBitmap;
    }

}
