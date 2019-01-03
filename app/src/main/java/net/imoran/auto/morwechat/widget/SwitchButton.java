package net.imoran.auto.morwechat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;

import net.imoran.auto.morwechat.R;

/**
 * SwitchButton.
 */
public class SwitchButton extends View implements Checkable {

    private boolean isChecked = false;
    /**
     *
     */
    private boolean isEventBroadcast = false;

    private Drawable thumbDrawable;
    private float textSize;
    int normalColor;
    int checkedColor;
    Paint paint;
    private String textOn;
    private String textOff;
    private Rect rect = new Rect();
    /**
     * 收拾是否按下
     */
    private boolean isTouchingDown = false;

    private OnCheckedChangeListener onCheckedChangeListener;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化参数
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        }
        paint = new Paint();
        thumbDrawable = typedArray.getDrawable(R.styleable.SwitchButton_thumb);
        textSize = typedArray.getDimension(R.styleable.SwitchButton_textSize, 10);

        checkedColor = typedArray.getColor(R.styleable.SwitchButton_normalColor, Color.WHITE);
        normalColor = typedArray.getColor(R.styleable.SwitchButton_checkedColor, Color.BLACK);

        textOn = typedArray.getString(R.styleable.SwitchButton_textOn);
        textOff = typedArray.getString(R.styleable.SwitchButton_textOff);
    }

    private static boolean optBoolean(TypedArray typedArray,
                                      int index,
                                      boolean def) {
        if (typedArray == null) {
            return def;
        }
        return typedArray.getBoolean(index, def);
    }

    public void setThumbDrawable(Drawable mThumbDrawable) {
        thumbDrawable = mThumbDrawable;
        invalidate();
    }

    /**
     * 绘制控件
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if (isChecked) {
            thumbDrawable.setBounds(width / 2, getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());
        } else {
            thumbDrawable.setBounds(getPaddingLeft(), getPaddingTop(), width / 2, height - getPaddingBottom());
        }
        thumbDrawable.draw(canvas);


        paint.setAntiAlias(true);
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(textOn);
        float textWidthOff = paint.measureText(textOff);

        paint.getTextBounds(textOn, 0, textOn.length(), rect);
        Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

        if (isChecked) {
            paint.setColor(checkedColor);
        } else {
            paint.setColor(normalColor);
        }
        canvas.drawText(textOff, width / 4 - textWidthOff / 2, baseline, paint);
        if (isChecked) {
            paint.setColor(normalColor);
        } else {
            paint.setColor(checkedColor);
        }
        canvas.drawText(textOn, 3 * width / 4 - textWidth/ 2, baseline, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        int actionMasked = event.getActionMasked();

        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN: {
                isTouchingDown = true;
                touchDownTime = System.currentTimeMillis();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP: {
                isTouchingDown = false;
                //取消准备进入拖动状态
                if (System.currentTimeMillis() - touchDownTime <= 300) {
                    //点击时间小于300ms，认为是点击操作
                    float upX = event.getX();
                    int halfWidth = getWidth()/2;
                    if (isChecked && (upX > halfWidth)) {
                        return true;
                    } else if (!isChecked && (upX < halfWidth)){
                        return true;
                    }
                    toggle();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                isTouchingDown = false;
                break;
            }
        }
        return true;
    }

    /**
     * 手势按下的时刻
     */
    private long touchDownTime;

    @Override
    public void setChecked(boolean checked) {
        if (checked == isChecked()) {
            postInvalidate();
            return;
        }
        toggle(false);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        toggle(true);
    }

    /**
     * 切换状态
     *
     * @param animate
     */
    public void toggle(boolean animate) {
        if (!isEnabled()) {
            return;
        }

//        if (isEventBroadcast) {
//            throw new RuntimeException("should NOT switch the state in method: [onCheckedChanged]!");
//        }

        isChecked = !isChecked;
        if (onCheckedChangeListener != null) {
//            isEventBroadcast = true;
            onCheckedChangeListener.onCheckedChanged(this, isChecked());
        }
        postInvalidate();
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchButton view, boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
        onCheckedChangeListener = l;
    }

}
