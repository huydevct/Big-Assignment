package com.example.sqlite.fragment;

import static com.example.sqlite.StatisticsActivity.getEndTimestamp;
import static com.example.sqlite.StatisticsActivity.getStartTimestamp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqlite.ModifyItemActivity;
import com.example.sqlite.R;
import com.example.sqlite.StatisticsActivity;
import com.example.sqlite.adapter.RecyclerViewAdapter;
import com.example.sqlite.dal.SQLiteHelper;
import com.example.sqlite.model.Item;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentSearch extends Fragment implements View.OnClickListener, RecyclerViewAdapter.ItemListener, RecyclerViewAdapter.ContextItemListener  {
    private RecyclerViewAdapter adapter;
    private SQLiteHelper db;
    private RecyclerView recyclerView;
    private TextView tvTong;
    private Button btSearch, btViewStat;
    private SearchView searchView;
    private EditText eFrom, eTo;
    private Spinner spCategory, spMonth;
    private final String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private String selectedMonth = "Tất cả";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        db = new SQLiteHelper(getContext());
        List<Item> list = db.getAll();

        adapter = new RecyclerViewAdapter();
        adapter.setList(list);
        tvTong.setText("Tổng tiền: " + sumPrice(list) + "k");

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setItemListener(this);
        adapter.setContextItemListener(this);

        // Search by title
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Item> list = db.searchByTitle(newText);
                tvTong.setText("Tổng tiền: " + sumPrice(list) + "k");
                adapter.setList(list);
                return true;
            }
        });

        eFrom.setOnClickListener(this);
        eTo.setOnClickListener(this);
        btSearch.setOnClickListener(this);

        // Search by category
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String category = spCategory.getItemAtPosition(position).toString();
                List<Item> list;

                if (category.equalsIgnoreCase("Tất cả")) {
                    list = db.getAll();
                } else {
                    list = db.searchByCategory(category);
                }
                adapter.setList(list);
                tvTong.setText("Tổng tiền: " + sumPrice(list) + "k");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Item> list = db.getAll();
        adapter.setList(list);
        tvTong.setText("Tổng tiền: " + sumPrice(list) + "k");
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        tvTong = view.findViewById(R.id.tvTong);
        btSearch = view.findViewById(R.id.btSearch);
        btViewStat = view.findViewById(R.id.btViewStat);
        searchView = view.findViewById(R.id.search);
        eFrom = view.findViewById(R.id.eFrom);
        eTo = view.findViewById(R.id.eTo);
        spCategory = view.findViewById(R.id.spCategory);
        spMonth = view.findViewById(R.id.spMonth);

        // Init category spinner items
        String[] arr = getResources().getStringArray(R.array.category);
        String[] arr1 = new String[arr.length + 1];

        arr1[0] = "Tất cả";
        System.arraycopy(arr, 0, arr1, 1, arr.length);
        spCategory.setAdapter(new ArrayAdapter<>(getContext(), R.layout.item_spinner, arr1));
    }

    private int sumPrice(List<Item> list) {
        int sum = 0;
        for (Item i : list) {
            sum += Integer.parseInt(i.getPrice());
        }
        return sum;
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Item item=adapter.getItem(position);
        Intent intent=new Intent(getActivity(), ModifyItemActivity.class);
        intent.putExtra(  "item", item);
        startActivity(intent);
    }

    // search by date and search event
    @Override
    public void onClick(View view) {
        if (view == eFrom) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), (datePicker, y, m, d) -> {
                m += 1;
                // if month > 9 or day>9 -> nothing to do, else add '0' before each of them
                String D = d > 9 ? d + "" : "0" + d;
                String M = m > 9 ? m + "" : "0" + m;
                String date = D + "/" + M + "/" + y;

                eFrom.setText(date);
            }, year, month, day);

            dialog.show();
        }

        if (view == eTo) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getContext(), (datePicker, y, m, d) -> {
                m += 1;
                // if month > 9 or day>9 -> nothing to do, else add '0' before each of them
                String D = d > 9 ? d + "" : "0" + d;
                String M = m > 9 ? m + "" : "0" + m;
                String date = D + "/" + M + "/" + y;

                eTo.setText(date);
            }, year, month, day);

            dialog.show();
        }

        if (view == btSearch) {
            String from = eFrom.getText().toString();
            String to = eTo.getText().toString();
            if (!from.isEmpty() && !to.isEmpty()) {
                String sDate = from.substring(0, 2);
                String sMonth = from.substring(3, 5);
                String eDate = to.substring(0, 2);
                String eMonth = to.substring(3, 5);

                System.out.println(sDate + " " + sMonth + " " +eDate + " " +eMonth);

                List<Item> list = db.searchByDate(sDate, sMonth, eDate, eMonth);
                adapter.setList(list);
                tvTong.setText("Tổng tiền: " + sumPrice(list) + "K");
            }
        }

        if (view == btViewStat) {
            Intent intent = new Intent(getActivity(), StatisticsActivity.class);
            intent.putExtra("month", selectedMonth);
            startActivity(intent);
        }
    }

    @Override
    public void onContextItemClickListener(int action, int position) {
        Item item = adapter.getItem(position);

        if (action == 1) {
            Intent intent = new Intent(getActivity(), ModifyItemActivity.class);
            intent.putExtra("item", item);
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle("Thông báo");
            builder.setMessage("Bạn có chắc muốn xóa " + item.getTitle() + " không?");
            builder.setIcon(R.drawable.ic_remove);

            builder.setPositiveButton("Có", (dialogInterface, i) -> {
                SQLiteHelper db = new SQLiteHelper(getContext());
                db.deleteItem(item.getId());
                onResume();
            });

            builder.setNegativeButton("Không", (dialogInterface, i) -> {
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}
