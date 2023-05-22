package com.example.sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sqlite.dal.SQLiteHelper;
import com.example.sqlite.model.Item;

import java.util.Calendar;

public class ModifyItemActivity extends AppCompatActivity implements View.OnClickListener {
    public Spinner sp;
    private EditText eTitle, ePrice, eDate;
    private Button btUpdate, btRemove, btCancel;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        initView();
        btUpdate.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btRemove.setOnClickListener(this);
        eDate.setOnClickListener(this);

        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());

        String d = item.getDate().substring(8, 10);
        String m = item.getDate().substring(5, 7);
        String y = item.getDate().substring(0, 4);
        eDate.setText(d + "/" + m + "/" + y);

        // set spinner item selection
        int position = 0;
        for (int i = 0; i < sp.getCount(); i++) {
            if (sp.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())) {
                position = i;
                break;
            }
        }
        sp.setSelection(position);
    }

    @Override
    public void onClick(View view) {
        if (view == eDate) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(ModifyItemActivity.this, (datePicker, y, m, d) -> {
                m += 1;
                // if month > 9 or day>9 -> nothing to do, else add '0' before each of them
                String D = d > 9 ? d + "" : "0" + d;
                String M = m > 9 ? m + "" : "0" + m;
                String date = D + "/" + M + "/" + y;

                eDate.setText(date);
            }, year, month, day);

            dialog.show();
        }
        if (view == btCancel) {
            finish();
        }
        if (view == btRemove) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc muốn xóa " + item.getTitle() + " không?");
            builder.setIcon(R.drawable.ic_remove);

            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                SQLiteHelper bb = new SQLiteHelper(getApplicationContext());
                bb.deleteItem(item.getId());
                finish();
            });

            builder.setNegativeButton("Không", (dialogInterface, i) -> {});
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (view == btUpdate) {
            String title = eTitle.getText().toString().trim();
            String price = ePrice.getText().toString().trim();
            String category = sp.getSelectedItem().toString();
            String date = eDate.getText().toString().trim();

            if (title.isEmpty() || date.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Các trường nhập không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!price.matches("\\d+")) {
                Toast.makeText(this, "Chi phí có định dạng không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            String d = date.substring(0, 2);
            String m = date.substring(3, 5);
            String y = date.substring(6, 10);
            date = y + "/" + m + "/" + d;

            Item newItem = new Item(item.getId(), title, category, price, date);
            SQLiteHelper db = new SQLiteHelper(this);
            db.updateItem(newItem);
            finish();
        }

    }

    private void initView() {
        sp = findViewById(R.id.spCategory);
        eTitle = findViewById(R.id.tvTitle);
        ePrice = findViewById(R.id.tvPrice);
        eDate = findViewById(R.id.tvDate);
        btUpdate = findViewById(R.id.btUpdate);
        btRemove = findViewById(R.id.btRemove);
        btCancel = findViewById(R.id.btCancel);
        sp.setAdapter(new ArrayAdapter<>(this, R.layout.item_spinner, getResources().getStringArray(R.array.category)));
    }
}