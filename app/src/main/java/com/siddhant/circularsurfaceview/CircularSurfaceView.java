/*
 * Copyright 2018 Siddhant Panhalkar
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 *
 * Written by Siddhant Panhalkar - siddhant_panhalkar@yahoo.com
 * Last modified 5/4/18 2:02 PM
 */

package com.siddhant.circularsurfaceview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;

public class CircularSurfaceView extends SurfaceView {

    private int borderWidth;
    private int canvasSize;
    private Paint paintBorder;
    private Path path;

    public CircularSurfaceView(final Context context) {
        this(context, null);
    }

    public CircularSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.CircularSurfaceViewStyle);
    }

    public CircularSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // init paint
        paintBorder = new Paint();
        paintBorder.setAntiAlias(true);

        path = new Path();

        // load the styled attributes and set their properties
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircularSurfaceView, defStyle, 0);

        int defaultBorderSize = (int) (4 * getContext().getResources().getDisplayMetrics().density + 0.5f);
        setBorderWidth(attributes.getDimensionPixelOffset(R.styleable.CircularSurfaceView_border_width, defaultBorderSize));
        setBorderColor(attributes.getColor(R.styleable.CircularSurfaceView_border_color, Color.WHITE));
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        this.requestLayout();
        this.invalidate();
    }

    public void setBorderColor(int borderColor) {
        if (paintBorder != null)
            paintBorder.setColor(borderColor);
        this.invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        Log.d("view", "canvas");

        canvasSize = canvas.getWidth();
        if (canvas.getHeight() < canvasSize)
            canvasSize = canvas.getHeight();

        // circleCenter is the x or y of the view's center
        // radius is the radius in pixels of the cirle to be drawn
        // paint contains the shader that will texture the shape
        int circleCenter = (canvasSize - (borderWidth * 2)) / 2;
        canvas.drawCircle(circleCenter + borderWidth, circleCenter  + borderWidth,
                ((canvasSize - (borderWidth * 2)) / 2) - borderWidth - 4.0f, paintBorder);
        path.addCircle(circleCenter + borderWidth, circleCenter + borderWidth,
                ((canvasSize - (borderWidth * 2)) / 2) - 4.0f, Path.Direction.CW);
        canvas.clipPath(path);
        super.dispatchDraw(canvas);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        Log.d("width", String.valueOf(width));
        Log.d("height", String.valueOf(height));
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // The parent has not imposed any constraint on the child.
            result = canvasSize;
        }

        return result;
    }

    private int measureHeight(int measureSpecHeight) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpecHeight);
        int specSize = MeasureSpec.getSize(measureSpecHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            result = specSize;
        } else {
            // Measure the text (beware: ascent is a negative number)
            result = canvasSize;
        }

        return (result + 2);
    }
}
