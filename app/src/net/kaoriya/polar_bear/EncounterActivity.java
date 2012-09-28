package net.kaoriya.polar_bear;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class EncounterActivity extends Activity
    implements PolarBear
{

    private static int[] BEAR_IMAGES = new int[] {
        R.drawable.bear01a,
        R.drawable.bear02a,
        R.drawable.bear03a,
        R.drawable.bear04a,
        R.drawable.bear05a,
    };

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
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setBearImage(R.drawable.bear01a, false);
        }
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
}
