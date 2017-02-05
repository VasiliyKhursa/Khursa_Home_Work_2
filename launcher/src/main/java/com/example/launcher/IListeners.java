package com.example.launcher;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Paradise on 23.01.2017.
 */

public class IListeners {

    Context context;

    public IListeners(Context context, View view) {
        this.context = context;
        view.setOnClickListener(this.onClickListener());
        view.setOnLongClickListener(this.onLongClickListener());
        view.setOnDragListener(this.onDragListener());
        view.setOnTouchListener(this.onTouchListener());
    }

    public View.OnLongClickListener onLongClickListener(){
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData clipData = ClipData.newPlainText("","");
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(clipData,dragShadowBuilder,view, 0);
                view.setAlpha(0.1f);
                return true;
            }
        };
    }

    public View.OnDragListener onDragListener(){
        return new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                View currentView =(View) dragEvent.getLocalState();
                ViewGroup parent =(ViewGroup) currentView.getParent();
                int currentViewId = parent.indexOfChild(currentView);
                int nextViewId = parent.indexOfChild(view);
                switch (dragEvent.getAction()){
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if (currentViewId!= nextViewId && currentViewId < nextViewId){
                            parent.removeViewAt(nextViewId);
                            parent.removeViewAt(currentViewId);
                            parent.addView(view,currentViewId);
                            parent.addView(currentView, nextViewId);
                        }
                        else if (currentViewId > nextViewId){
                            parent.removeViewAt(currentViewId);
                            parent.removeViewAt(nextViewId);
                            parent.addView(currentView, nextViewId);
                            parent.addView(view,currentViewId);
                        }
                        break;
                    case DragEvent.ACTION_DROP:
                        currentView.setAlpha(1.0f);
                        view.setBackgroundColor(Color.BLUE);
                        break;
                    default:
                        break;
                }

                return true;
            }
        };
    }

    public View.OnTouchListener onTouchListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        view.setBackgroundColor(Color.RED);
                        break;


                }

                GestureDetector gd = new GestureDetector(context, new IGesture());
                gd.onTouchEvent(event);

                return false;
            }
        };
    }

    public View.OnClickListener onClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.GREEN);
            }
        };
    }
}
