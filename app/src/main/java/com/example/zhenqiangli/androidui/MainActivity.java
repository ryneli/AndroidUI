package com.example.zhenqiangli.androidui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  RecyclerView rvContainer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    rvContainer = (RecyclerView) findViewById(R.id.rv_container);
    rvContainer.setLayoutManager(layoutManager);
    RvContainerAdapter adapter = new RvContainerAdapter(this);
    rvContainer.setAdapter(adapter);
    rvContainer.setOnScrollChangeListener(adapter);
    RvContainerSnapHelper snapHelper = new RvContainerSnapHelper();
    snapHelper.attachToRecyclerView(rvContainer);
  }

  private class RvContainerSnapHelper extends LinearSnapHelper {

    @Override
    public View findSnapView(LayoutManager layoutManager) {
      View v = super.findSnapView(layoutManager);
      if (v instanceof TextView) {
        Log.d(TAG, "findSnapView: " + ((TextView)v).getText());
      }
      return v;
    }
  }

  private class RvContainerAdapter extends RecyclerView.Adapter<RvViewHolder> implements RecyclerView.OnScrollChangeListener {

    private List<String> datas = new LinkedList<>();
    Context context;

    private class ScrolledObserver {
      private boolean scrolled = false;

      void setScrolled() {
        this.scrolled = true;
      }

      private void onItemSelected(View v) {
        if (v == null) {
          Log.d(TAG, "onItemSelected: null");
        }
      }

      void onBinding(View v, int position) {
        if (scrolled) return;

        if (position == 0) {
          onItemSelected(v);
        } else {
          onItemSelected(null);
        }
      }
    }
    private ScrolledObserver scrolledObserver = new ScrolledObserver();

    RvContainerAdapter(Context context) {
      Log.d(TAG, "RvContainerAdapter: ");
      for (int i = 0; i < 100; i++) {
        datas.add("Hello " + i);
      }
      this.context = context;
    }

    @Override
    public RvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      Log.d(TAG, "onCreateViewHolder: " + viewType);
      View v = LayoutInflater.from(context).inflate(R.layout.item_rv, parent, false);
      return new RvViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RvViewHolder holder, int position) {
      Log.d(TAG, "onBindViewHolder: " + holder + " " + position);
      holder.bindData(datas.get(position));
      scrolledObserver.onBinding(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
      Log.d(TAG, "getItemCount: " + datas.size());
      return datas.size();
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
      ViewHolder vh = rvContainer.findViewHolderForLayoutPosition(0);
      Log.d(TAG, "onScrollChange: " + vh);
      scrolledObserver.setScrolled();
    }
  }

  private class RvViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
    boolean selected = false;
    Drawable drawable = getDrawable(R.drawable.item_rv_background_replace);

    @Override
    public void onClick(View v) {
      Drawable newDrawable = itemView.getBackground();
      itemView.setBackground(drawable);
      drawable = newDrawable;
      Log.d(TAG, "onClick: ");
    }

    RvViewHolder(View v) {
      super(v);
      v.setOnClickListener(this);
      Log.d(TAG, "RvViewHolder: ");
    }

    void bindData(String s) {
      Log.d(TAG, "bindData: ");
      ((TextView) itemView).setText(s);
    }
  }
}
