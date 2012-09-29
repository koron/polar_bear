package net.kaoriya.polar_bear;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

public class EncounterActivity extends Activity
    implements PolarBear
{

    public final static long ANIMATION_DELAY = 300;

    public final static long ANIMATION_PERIOD = 600;

    private final static int[] BEAR_IMAGES = new int[] {
        R.drawable.bear01a,
        R.drawable.bear02a,
        R.drawable.bear03a,
        R.drawable.bear04a,
        R.drawable.bear05a,
    };

    private final static int[] BEAR_SOUNDEFFECTS = new int[] {
        R.raw.se_clock,
        R.raw.se_kasha,
        R.raw.se_kah,
        R.raw.se_karan,
        R.raw.se_powa,
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

    class PrepareBearTask extends AsyncTask<int[], Void, Bitmap[]>
            implements DialogInterface.OnCancelListener
    {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            this.dialog = ProgressDialog.show(EncounterActivity.this,
                    getString(R.string.prepare_title),
                    getString(R.string.prepare_message), true, true, this);
        }

        @Override
        protected Bitmap[] doInBackground(int[]... args) {
            try {
                prepareSounds();
                return prepareBitmaps(args[0]);
            } catch (InterruptedException e) {
                return null;
            }
        }

        private void closeDialog() {
            if (this.dialog != null) {
                this.dialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            closeDialog();
            finish();
        }

        @Override
        protected void onPostExecute(Bitmap[] bitmaps) {
            closeDialog();
            bearBitmaps = bitmaps;
            setAnimationRunning(true);
        }

        public void onCancel(DialogInterface dialog) {
            cancel(true);
        }
    };

    private boolean bearActive = false;

    private boolean bearFocus = false;

    private Handler handler = new Handler();

    private Timer animationTimer = null;

    private int animationFrame = 0;

    private Random random = new Random();

    private Bitmap[] bearBitmaps = null;

    private int lastIndex = -1;

    private boolean lastReverse = false;

    private SoundPool soundPool = null;

    private int[] soundIds = null;

    private int soundFrame = 0;

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

    private void setBearBitmap(Bitmap bitmap, boolean reverse)
    {
        Matrix m = new Matrix();
        if (reverse) {
            m.postScale(-1f, 1f);
            m.postTranslate(bitmap.getWidth(), 0);
        }
        ImageView v = (ImageView)findViewById(R.id.bear);
        v.setImageMatrix(m);
        v.setImageBitmap(bitmap);
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

    private void checkBearAnimation()
    {
        if (this.bearActive && this.bearFocus) {
            if (prepareAnimation()) {
                setAnimationRunning(true);
            }
        } else {
            setAnimationRunning(false);
        }
    }

    private synchronized void setAnimationRunning(boolean enable)
    {
        if (enable) {
            // start animation.
            if (this.animationTimer != null) {
                this.animationTimer.cancel();
            }
            this.soundFrame = 0;
            this.animationTimer = new Timer(false);
            this.animationTimer.schedule(new AnimationTask(),
                    ANIMATION_DELAY, ANIMATION_PERIOD);
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
        int len = this.bearBitmaps != null ? this.bearBitmaps.length : 0;
        if (len == 0) {
            return;
        }

        // Determine a bear image to show.
        int index = this.lastIndex;
        boolean reverse = this.lastReverse;
        while (index == this.lastIndex && reverse == this.lastReverse) {
            index = this.random.nextInt(len);
            reverse = this.random.nextBoolean();
        }
        this.lastIndex = index;
        this.lastReverse = reverse;

        // Determine a sound effect.
        int soundIndex = 0;
        int soundPriority = 0;
        int soundMax = this.soundIds.length - 1;
        this.soundFrame = (this.soundFrame + 1) % soundMax;
        if (this.soundFrame == 0) {
            soundIndex = this.random.nextInt(soundMax) + 1;
            soundPriority = 1;
        }

        this.soundPool.play(this.soundIds[soundIndex], 1f, 1f,
                soundPriority, 0, 1f);
        setBearBitmap(this.bearBitmaps[index], reverse);
    }

    private synchronized boolean prepareAnimation()
    {
        if (this.bearBitmaps != null) {
            return true;
        }
        this.bearBitmaps = new Bitmap[0];
        PrepareBearTask task = new PrepareBearTask();
        task.execute(BEAR_IMAGES);
        return false;
    }

    private Bitmap loadBitmap(int id) throws InterruptedException
    {
        BitmapDrawable d = (BitmapDrawable)getResources().getDrawable(id);
        int srcW = d.getMinimumWidth();
        int srcH = d.getMinimumHeight();

        ImageView v = (ImageView)findViewById(R.id.bear);
        int dstW = v.getWidth();
        int dstH = v.getHeight();

        int x = 0;
        int y = 0;
        int w = dstW;
        int h = dstH;
        if (srcW * dstH < srcH * dstW) {
            w = srcW * dstH / srcH;
            x = (dstW - w) / 2;
        } else {
            h = srcH * dstW / srcW;
            y = (dstH - h) / 2;
        }

        Bitmap b = Bitmap.createBitmap(dstW, dstH, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(b);
        d.setAntiAlias(true);
        d.setBounds(x, y, x + w, y + h);
        d.draw(c);
        return b;
    }

    private void prepareSounds() throws InterruptedException
    {
        if (this.soundPool != null) {
            return;
        }
        this.soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        this.soundPool.setOnLoadCompleteListener(
                new SoundPool.OnLoadCompleteListener()
        {
            public void onLoadComplete(
                SoundPool soundPool,
                int sampleId,
                int status)
            {
            }
        });
        this.soundIds = new int[BEAR_SOUNDEFFECTS.length];
        for (int i = 0, I = BEAR_IMAGES.length; i < I; ++i) {
            this.soundIds[i] = this.soundPool.load(this,
                    BEAR_SOUNDEFFECTS[i], 1);
        }
        // TODO: wait to complete to load sound effects.
    }

    private Bitmap[] prepareBitmaps(int[] ids) throws InterruptedException
    {
        Bitmap[] bitmaps = new Bitmap[ids.length];
        for (int i = 0, I = ids.length; i < I; ++i) {
            bitmaps[i] = loadBitmap(ids[i]);
        }
        return bitmaps;
    }

}
