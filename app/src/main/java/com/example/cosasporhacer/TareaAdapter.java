package com.example.cosasporhacer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {

    private List<Tarea> tareas;
    private Context context;
    private OnItemClickListener listener;

    public TareaAdapter(List<Tarea> tareas, OnItemClickListener listener) {
        this.tareas = tareas;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent,
                false);
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Tarea tarea = tareas.get(position);

        holder.setListener(tarea, listener);
        holder.tvDescripcion.setText(tarea.getDescripcion());
        holder.tvOrden.setText(String.valueOf(tarea.getOrden()));
    }

    @Override
    public int getItemCount() {
        return this.tareas.size();
    }

    public void add(Tarea tarea) {
        if (!tareas.contains(tarea)) {
            tareas.add(tarea);
            notifyDataSetChanged();
        }
    }

    public void setList(List<Tarea> list) {
        this.tareas = list;
        notifyDataSetChanged();
    }

    public void remove(Tarea Tarea) {
        if (tareas.contains(Tarea)) {
            tareas.remove(Tarea);
            notifyDataSetChanged();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvOrden)
        AppCompatTextView tvOrden;

        @BindView(R.id.tvDescripcion)
        AppCompatTextView tvDescripcion;

        @BindView(R.id.containerMain)
        RelativeLayout containerMain;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setListener(Tarea tarea, final OnItemClickListener listener) {
            containerMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(tarea);
                }
            });
            containerMain.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.OnLongItemClick(tarea);
                    return true;
                }
            });
        }
    }
}