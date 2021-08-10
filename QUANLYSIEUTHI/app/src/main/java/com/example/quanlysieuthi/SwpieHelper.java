package com.example.quanlysieuthi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwpieHelper extends ItemTouchHelper.SimpleCallback {

    int btnWidth;
    private RecyclerView recyclerView;
    private List<DeleteButton> buttonList;
    private GestureDetector gestureDetector;
    private  int swipePosition = -1;
    private  float swipeThreshold = 0.5f;
    private Map<Integer, List<DeleteButton>> btnBuffer;
    private Queue<Integer> removeQueue;

    private  GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener()
    {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for(DeleteButton btn : buttonList)
            {
                if(btn.onClick_(e.getX(), e.getY()))
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(swipePosition < 0) return false;
            Point point = new Point((int) event.getRawX(), (int)event.getRawY());

            RecyclerView.ViewHolder swipeViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition);
            View swipeItem = swipeViewHolder.itemView;
            Rect rect = new Rect();
            swipeItem.getGlobalVisibleRect(rect);

            if(event.getAction() == MotionEvent.ACTION_DOWN
                    || event.getAction() == MotionEvent.ACTION_UP
                    || event.getAction() == MotionEvent.ACTION_MOVE)
            {
                if(rect.top < point.y && rect.bottom > point.y)
                {
                    gestureDetector.onTouchEvent(event);
                }
                else
                {
                    removeQueue.add(swipePosition);
                    swipePosition = -1;
                    recoverSwipeItem();
                }
            }
            return false;
        }
    };

    private synchronized void recoverSwipeItem() {
        while (removeQueue.isEmpty())
        {
            int pos = removeQueue.poll();
            if(pos> -1)
            {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    public SwpieHelper(Context context, RecyclerView recyclerView, int buttonWidth) {
        super(0, ItemTouchHelper.LEFT);
        this.buttonList = new ArrayList<>();
        this.recyclerView = recyclerView;
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        this.btnBuffer = new HashMap<>();
        this.btnWidth = buttonWidth;

        removeQueue = new LinkedList<Integer>()
        {
            @Override
            public boolean add(Integer integer) {
                if(contains(integer))
                    return false;
                else
                    return  super.add(integer);
            }
        };

        attachSwipe();
    }

    private void attachSwipe() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        if(swipePosition != pos)
            removeQueue.add(swipePosition);
        swipePosition = pos;
        if(btnBuffer.containsKey(swipePosition))
            buttonList = btnBuffer.get(swipePosition);
        else
            buttonList.clear();
        btnBuffer.clear();
        swipeThreshold = 0.5f * buttonList.size() * btnWidth;
        recoverSwipeItem();
    }

    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return  0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float transition = dX;
        View itemView = viewHolder.itemView;
        if(pos < 0)
        {
            swipePosition = pos;
            return;
        }
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            if(dX < 0)
            {
                List<DeleteButton> buffer = new ArrayList<>();
                if(!btnBuffer.containsKey(pos))
                {
                    instantiateCartButton (viewHolder,buffer);
                    btnBuffer.put(pos,buffer);
                }
                else
                {
                    buffer = btnBuffer.get(pos);
                }
                transition = dX * buffer.size()* btnWidth / itemView.getWidth();
                drawButton(c, itemView, buffer, pos, transition);
            }
            super.onChildDraw(c,recyclerView, viewHolder, transition, dY,actionState,isCurrentlyActive);
        }
    }

    protected  void drawButton(Canvas c, View itemView, List<DeleteButton> buffer, int pos, float transition)
    {
        float right = itemView.getRight();
        float dButtonWidth = -1* transition / buffer.size();
        for (DeleteButton btn : buffer)
        {
            float left = right - dButtonWidth;
            btn.onDraw(c,new RectF(left,itemView.getTop(),right,itemView.getBottom()),pos);
            right = left;
        }
    }

    public abstract void instantiateCartButton(RecyclerView.ViewHolder viewHolder, List<DeleteButton> buffer);

    public static class DeleteButton
    {
        private  String text;
        private  int imgResID, textSize, position, color;
        private RectF clickRegion;
        private  DeleteButtonListener listener;
        private Context context;
        private Resources resource;

        public DeleteButton(Context context,String text,  int textSize,int imgResID, int color, DeleteButtonListener listener) {
            this.text = text;
            this.imgResID = imgResID;
            this.textSize = textSize;
            this.color = color;
            this.listener = listener;
            this.context = context;
            resource = context.getResources();
        }

        public  boolean onClick_ (float x, float y)
        {
            if(clickRegion != null && clickRegion.contains(x,y))
            {
                listener.onClick(position);
                return true;
            }
            return false;
        }

        public  void onDraw(Canvas c , RectF rectF, int pos)
        {
            Paint p = new Paint();
            p.setColor(color);
            c.drawRect(rectF,p);
            //text
            p.setColor(Color.WHITE);
            p.setTextSize(textSize);
            //
            Rect rect = new Rect();
            float cHeight = rectF.height();
            float cWidth = rectF.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text,0,text.length(),rect);
            float x = 0, y = 0;
            if(imgResID == 0)
            {
                x = cWidth/2f - rect.width()/2f - rect.left;
                y = cHeight/2f - rect.height()/2f - rect.bottom;
                c.drawText(text,rectF.left + x, rectF.top + y, p);
            }
            else
            {
                Drawable drawable = ContextCompat.getDrawable(context,imgResID);
                Bitmap bitmap = drawableToBitMap(drawable);
                c.drawBitmap(bitmap,(rectF.left + rectF.right)/2, (rectF.top + rectF.bottom)/2,p);
            }
            clickRegion = rectF;
            this.position = pos;
        }

        private Bitmap drawableToBitMap(Drawable drawable) {
            if (drawable instanceof BitmapDrawable)
                return  ((BitmapDrawable)drawable).getBitmap();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),  Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
    }
}
