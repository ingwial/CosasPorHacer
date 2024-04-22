package com.example.cosasporhacer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.containerMain)
    CoordinatorLayout containerMain;

    private TareaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FlowManager.init(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        configToolbar();
        configAdapter();
        configRecyclerView();

        if (SQLite.select().from(Tarea.class).count() == 0) {
            generateTareas();
        }
    }

    private void generateTareas() {
        String[] descripciones = {"Trabajar", "Comer", "Estudiar", "Orar", "Dormir"};

        for (int i = 0; i < 5; i++) {
            Tarea tarea = new Tarea(i + 1, descripciones[i], i + 1);
            adapter.add(tarea);
        }

    }

    private void configToolbar() {
        setSupportActionBar(toolbar);
    }

    private void configAdapter() {
        adapter = new TareaAdapter(new ArrayList<Tarea>(), this);
    }

    private void configRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.setList(getTareasFromDB());
    }

    private List<Tarea> getTareasFromDB() {
        return SQLite
                .select()
                .from(Tarea.class)
                .orderBy(Tarea_Table.orden, true)
                .queryList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /******
     *      Métodos implementados por la interface OnItemClickListener
     * ******/
    @Override
    public void OnItemClick(Tarea tarea) {
        Intent intent = new Intent(MainActivity.this, EditTareaActivity.class);
        intent.putExtra(Tarea.ID, tarea.getId());
        startActivity(intent);
    }

    @Override
    public void OnLongItemClick(Tarea tarea) {
        Vibrator vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null){
            vibrator.vibrate(60);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.main_dialogDelete_title)
                .setMessage(String.format(Locale.ROOT, getString(R.string.main_dialogDelete_message),
                        tarea.getDescripcion()))
                .setPositiveButton(R.string.label_dialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            tarea.delete();
                            adapter.remove(tarea);
                            showMessage(R.string.main_message_delete_success);
                        } catch (Exception e) {
                            e.printStackTrace();
                            showMessage(R.string.main_message_delete_fail);
                        }
                    }
                })
                .setNegativeButton(R.string.label_dialog_cancel, null);
        builder.show();
    }

    @OnClick(R.id.fab)
    public void addTarea() {
        Intent intent = new Intent(MainActivity.this, AddTareaActivity.class);
        intent.putExtra(Tarea.ORDEN, adapter.getItemCount()+1);
        startActivityForResult(intent, 1);
    }

    private void showMessage(int resource) {
        Snackbar.make(containerMain, resource, Snackbar.LENGTH_SHORT).show();
    }
}