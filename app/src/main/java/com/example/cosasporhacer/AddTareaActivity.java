package com.example.cosasporhacer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTareaActivity extends AppCompatActivity {

    @BindView(R.id.etDescripcion)
    TextInputEditText etDescripcion;

    private Tarea mTarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarea);
        ButterKnife.bind(this);

        configActionBar();
        configTarea(getIntent());
    }

    private void configActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void configTarea(Intent intent) {
        mTarea = new Tarea();
        mTarea.setOrden(intent.getIntExtra(Tarea.ORDEN, 0));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_guardar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                saveTarea();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveTarea() {
        if (validateFields()) {
            mTarea.setDescripcion(etDescripcion.getText().toString().trim());
            try {
                mTarea.save();
                Log.i("DBFlow", "Inserción correcta de datos.");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("DBFlow", "Error al insertar datos.");
            }
            finish();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){

        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}