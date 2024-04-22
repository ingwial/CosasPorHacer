package com.example.cosasporhacer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditTareaActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.etDescripcion)
    TextInputEditText etDescripcion;
    @BindView(R.id.containerMain)
    NestedScrollView containerMain;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Tarea mTarea;
    private MenuItem mMenuItem;
    private boolean mIsEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tarea);
        ButterKnife.bind(this);

        configTarea(getIntent());
        configActionBar();
    }

    private void configActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configTarea(Intent intent) {
        getTarea(intent.getLongExtra(Tarea.ID, 0));

        etDescripcion.setText(mTarea.getDescripcion());
    }

    private void getTarea(long id) {
        mTarea = SQLite
                .select()
                .from(Tarea.class)
                .where(Tarea_Table.id.is(id))
                .querySingle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        mMenuItem = menu.findItem(R.id.action_save);
        mMenuItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveOrEdit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    public void saveOrEdit() {
        if (mIsEdit) {
            if (validateFields()) {
                mTarea.setDescripcion(etDescripcion.getText().toString().trim());

                try {
                    mTarea.update();
                    showMessage(R.string.edit_message_update_success);
                    Log.i("DBFlow", "Inserción correcta de datos.");
                } catch (Exception e) {
                    e.printStackTrace();
                    showMessage(R.string.edit_message_update_fail);
                    Log.i("DBFlow", "Error al insertar datos.");
                }
            }

            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_post_edit));
            enableUIElements(false);
            mIsEdit = false;
        } else {
            mIsEdit = true;
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_post_check));
            enableUIElements(true);
        }
    }

    private void enableUIElements(boolean enable) {
        etDescripcion.setEnabled(enable);

        mMenuItem.setVisible(enable);
        appBar.setExpanded(!enable);
        containerMain.setNestedScrollingEnabled(!enable);
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (etDescripcion.getText().toString().trim().isEmpty()){
            etDescripcion.setError(getString(R.string.addTarea_error_required));
            etDescripcion.requestFocus();
            isValid = false;
        }
        return isValid;
    }

    private void showMessage(int resource) {
        Snackbar.make(containerMain, resource, Snackbar.LENGTH_SHORT).show();
    }
}