package net.kaoriya.polar_bear;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class EncounterActivity extends Activity
    implements PolarBear
{

    public final static long ANIMATION_DELAY = 0;

    public final static long ANIMATION_PERIOD = 600;

    private final static int[] BEAR_IMAGES = new int[] {
        R.drawable.bear01a,
        R.drawable.bear02a,
        R.drawable.bear03a,
        R.drawable.bear04a,
        R.drawable.bear05a,
    };

    class AnimationTask extends TimerTask
    {
        @Override
        public void run() {
            handler.post(new Runnable() {
                public void run() {
                    updateBear();
                }
            });
        }
    }

    private boolean bearActive = false;

    private boolean bearFocus = false;

    private Handler handler = new Handler();

    private Timer animationTimer = null;

    private int animationFrame = 0;

    private Random random = new Random();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.encounter);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setBearActive(true);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        setBearActive(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        setBearFocus(hasFocus);
    }

    private void setBearImage(int id, boolean reverse)
    {
        ImageView v = (ImageView)findViewById(R.id.bear);

        Drawable d = getResources().getDrawable(id);

        Matrix m = new Matrix();
        float w1 = d.getIntrinsicWidth();
        float h1 = d.getIntrinsicHeight();
        float w2 = v.getWidth();
        float h2 = v.getHeight();
        RectF src = new RectF(0, 0, w1, h1);
        RectF dst = new RectF(0, 0, w2, h2);
        m.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);
        if (reverse) {
            m.postScale(-1f, 1f);
            m.postTranslate(w2, 0);
        }

        v.setImageMatrix(m);
        v.setImageDrawable(d);
    }

    private void setBearActive(boolean value)
    {
        this.bearActive = value;
        checkBearAnimation();
    }

    private void setBearFocus(boolean value)
    {
        this.bearFocus = value;
        checkBearAnimation();
    }

    private synchronized void checkBearAnimation()
    {
        if (this.bearActive && this.bearFocus) {
            // start animation.
            if (this.animationTimer != null) {
                this.animationTimer.cancel();
            }
            this.animationTimer = new Timer(false);
            this.animationTimer.schedule(new AnimationTask(),
                    ANIMATION_DELAY, ANIMATION_PERIOD);
            setBearImage(R.drawable.bear01a, false);
        } else {
            // stop animation.
            if (this.animationTimer != null) {
                this.animationTimer.cancel();
                this.animationTimer = null;
            }
        }
    }

    private void updateBear()
    {
        int index = this.random.nextInt(BEAR_IMAGES.length);
        boolean reverse = this.random.nextBoolean();
        setBearImage(BEAR_IMAGES[index], reverse);
    }

}
