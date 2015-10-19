package it.quip.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import it.quip.android.R;

public class OverlayListView extends LinearLayout {

    private static final int DEFAULT_NUM_OVERLAYS = 5;

    private int numOverlays = DEFAULT_NUM_OVERLAYS;

    private View[] overlays;
    private ListAdapter adapter;

    private OnItemSelectedListener listener;

    public OverlayListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
        initialize();
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.OverlayListView, 0, 0);

        try {
            numOverlays = a.getInt(R.styleable.OverlayListView_maxItems, DEFAULT_NUM_OVERLAYS);
        } finally {
            a.recycle();
        }
    }

    private void initialize() {
        overlays = new View[numOverlays];
        setOrientation(VERTICAL);
        setClickable(true);
    }

    public void setAdapter(final ListAdapter adapter) {
        this.adapter = adapter;
        this.adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                update();
            }
        });
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    private void update() {
        clear();

        int numVisibleOverlays = Math.min(this.numOverlays, adapter.getCount());
        for (int i = 0; i < numVisibleOverlays; i++) {
            View overlay = getOverlay(i);
            overlay.setVisibility(VISIBLE);
        }

        bringToFront();
    }

    private void clear() {
        for (View overlay : overlays) {
            if (overlay != null) {
                overlay.setVisibility(GONE);
            }
        }
    }

    private View getOverlay(int position) {
        View overlay = overlays[position];
        if (overlay != null) {
            overlay = adapter.getView(position, overlay, this);
        } else {
            overlay = initializeOverlay(position);
            overlays[position] = overlay;
            addView(overlay);
        }

        return overlay;
    }

    private View initializeOverlay(final int position) {
        View overlay = adapter.getView(position, null, this);
        overlay.setVisibility(GONE);
        overlay.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemSelected(position, v);
                }
            }
        });

        return overlay;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position, View view);
    }

}
