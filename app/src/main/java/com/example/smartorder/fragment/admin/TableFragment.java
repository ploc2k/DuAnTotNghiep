package com.example.smartorder.fragment.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.adapter.admin.TableAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.model.table.Table;
import com.google.gson.JsonObject;
import com.melnykov.fab.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {

    private FloatingActionButton fabAddTable;
    private RecyclerView rvListTable;
    private RetrofitAPI retrofitAPI;
    private TableAdapter tableAdapter;
    private List<Table> tableList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        initRecycleView();
        retrofitAPI.getAllTable().enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                List<Table> table = response.body();
                for (int i = 0; i < table.size(); i++) {
                    String id = table.get(i).getId();
                    Integer tableCode = table.get(i).getTableCode();
                    Integer tableSeats = table.get(i).getTableSeats();
                    boolean status = table.get(i).getStatus();
                    tableList.add(new Table(id, tableCode, tableSeats, status));
                    tableAdapter.notifyDataSetChanged();
                }
                Collections.sort(tableList, new Comparator<Table>() {
                    @Override
                    public int compare(Table o1, Table o2) {
                        if (o1.getTableCode() > o2.getTableCode()) {
                            return 1;
                        } else {
                            if (o1.getTableCode() == o2.getTableCode()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        fabAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddTable();
            }
        });
        return view;
    }

    private void initRecycleView() {
        tableList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
        rvListTable.setLayoutManager(gridLayoutManager);
        tableAdapter = new TableAdapter(getContext(), tableList, new TableAdapter.OnClickListener() {
            @Override
            public void delete(int position, String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có muốn xóa bàn số " + tableList.get(position).getTableCode() + " không ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                retrofitAPI.deleteTable(id).enqueue(new Callback<ServerResponse>() {
                                    @Override
                                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        Fragment tableFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentTable);
                                        if (tableFragment != null) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(tableFragment).replace(R.id.frm, new TableFragment(), Constants.fragmentTable).commit();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }

            @Override
            public void update(int position, List<Table> tableList) {
                dialogUpdateTable(position, tableList);

            }
        });
        rvListTable.setHasFixedSize(true);
        rvListTable.setAdapter(tableAdapter);
    }


    private void dialogUpdateTable(int position, List<Table> tableList) {
        Table table = tableList.get(position);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_table, null);
        alertDialog.setTitle("Chỉnh sửa bàn");
        alertDialog.setView(alert);
        alertDialog.setCancelable(false);
        EditText edtTableCode = alert.findViewById(R.id.edtTableCode);
        EditText edtTableSeats = alert.findViewById(R.id.edtTableSeats);
        Button btnUpdateTable = alert.findViewById(R.id.btnAddTable);
        Button btnCancel = alert.findViewById(R.id.btnCancel);
        btnUpdateTable.setText("Cập nhật");
        edtTableCode.setText(String.valueOf(table.getTableCode()));
        edtTableSeats.setText(String.valueOf(table.getTableSeats()));
        btnUpdateTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkValidate(edtTableCode, edtTableSeats)) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    table.setTableCode(Integer.valueOf(edtTableCode.getText().toString()));
                    table.setTableSeats(Integer.valueOf(edtTableSeats.getText().toString()));
                    retrofitAPI.updateTable(table.getId(), table).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            alertDialog.dismiss();
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Fragment tableFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentTable);
                            if (tableFragment != null) {
                                getActivity().getSupportFragmentManager().beginTransaction().remove(tableFragment).replace(R.id.frm, new TableFragment(), Constants.fragmentTable).commit();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private boolean checkValidate(EditText edTableCode, EditText edtSeats) {
        if (edTableCode.getText().toString().isEmpty()) {
            edTableCode.setError("Chưa nhập số bàn");
            return false;
        } else if (edtSeats.getText().toString().isEmpty()) {
            edtSeats.setError("Chưa nhập số ghế");
            return false;
        }
        return true;
    }

    private void initView(View view) {
        rvListTable = (RecyclerView) view.findViewById(R.id.rvListTable);
        fabAddTable = (FloatingActionButton) view.findViewById(R.id.fabAddTable);
        fabAddTable.attachToRecyclerView(rvListTable);
    }

    private void dialogAddTable() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_table, null);
        alertDialog.setTitle("Tạo mới bàn");
        alertDialog.setView(alert);
        alertDialog.setCancelable(false);
        EditText edtTableSeats = alert.findViewById(R.id.edtTableSeats);
        Button btnAddTable = alert.findViewById(R.id.btnAddTable);
        Button btnCancel = alert.findViewById(R.id.btnCancel);
        btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtTableSeats.getText().toString().isEmpty()) {
                    edtTableSeats.setError("Table Seats Empty");
                } else {
                    int tableSeats = Integer.parseInt(edtTableSeats.getText().toString().trim());
                    retrofitAPI.createTable(tableSeats).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Fragment tableFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentTable);
                                if (tableFragment != null) {
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(tableFragment).replace(R.id.frm, new TableFragment(), Constants.fragmentTable).commit();
                                }
                            } else {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}