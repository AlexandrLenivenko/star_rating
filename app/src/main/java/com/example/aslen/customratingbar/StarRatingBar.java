package com.example.aslen.customratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.VectorDrawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;


public class StarRatingBar extends View {
    public static final int MARGING_TOP_AND_BOTTOM = 20;
    private int starSpacing;
    private int ratingColor;
    private float rating;
    private int maxRating;
    private Bitmap starFull;
    private Bitmap starEmpty;
    private Bitmap starHalf;
    private Paint backgroundPaint;
    private Paint starPaint;
    private int starHeight;
    private int starWidth;
    private int ratingRound;
    private boolean hasHalf;

    public StarRatingBar(Context context) {
        super(context);
    }

    public StarRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.StarRatingBar,
                0, 0);

        try {
            ratingColor = a.getColor(R.styleable.StarRatingBar_star_color, Color.BLUE);
            rating = a.getFloat(R.styleable.StarRatingBar_star_rating, 4);
            maxRating = a.getInteger(R.styleable.StarRatingBar_max_rating, 5);
            starSpacing = a.getInteger(R.styleable.StarRatingBar_star_spacing, 0);

        } finally {
            a.recycle();
        }


        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        starPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        starFull = createBitmapFromVectorDrawable((VectorDrawable) getResources().getDrawable(R.drawable.ic_star_black));
        starEmpty = createBitmapFromVectorDrawable((VectorDrawable) getResources().getDrawable(R.drawable.ic_star_border));
        starHalf = createBitmapFromVectorDrawable((VectorDrawable) getResources().getDrawable(R.drawable.ic_star_half));

        starHeight = starFull.getHeight();
        starWidth = starFull.getWidth() + starSpacing;
        setRating(rating);
        //LightingColorFilter filter = new LightingColorFilter(ratingColor, 0);
        //starPaint.setColorFilter(filter);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, starHeight + MARGING_TOP_AND_BOTTOM);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int startPoint = (getWidth() - starWidth * maxRating) / 2;

        for (int i = startPoint, j = 1; i < startPoint + maxRating * starWidth; i += starWidth, j++) {
            if(j <= ratingRound) {
                canvas.drawBitmap(starFull, i, 0, starPaint);
            }else {
                if( j == ratingRound + 1 && hasHalf) {
                    canvas.drawBitmap(starHalf, i, 0, starPaint);
                }else {
                    canvas.drawBitmap(starEmpty, i, 0, starPaint);
                }
            }
           /* if (j == ratingRound) {
                canvas.drawBitmap(bitmapStar, i, 0, starPaint);
                canvas.drawBitmap(bitmapThumb, i, starHeight, starPaint);
                setColorFilter(Color.GRAY, starPaint);

            } else {
                canvas.drawBitmap(bitmapStar, i, 25, starPaint);
            }*/

        }

        //setColorFilter(ratingColor, starPaint);
    }

    public void setRating(float rating) {
        this.rating = rating;
        ratingRound = (int) rating;
        if(rating == 0 ) {
            hasHalf = false;
        }else {
            hasHalf = rating % ratingRound != 0;
        }
        invalidate();
        requestLayout();
    }


    private Bitmap createBitmapFromVectorDrawable(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }
}
