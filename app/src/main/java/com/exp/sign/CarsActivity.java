package com.exp.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class CarsActivity extends BaseActivity {

    private ListView listView;
    private List<Car> list;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userss);
        findViewById(R.id.rg).setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final String[] items = {"修改信息", "路线管理", "删除校车"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(CarsActivity.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startActivity(new Intent(CarsActivity.this, AddCardActivity.class)
                                    .putExtra("data", list.get(i)));
                        }
                        if (which == 1) {
                            startActivity(new Intent(CarsActivity.this, LuxActivity.class)
                                    .putExtra("data", list.get(i)));
                        }
                        if (which == 2) {
                            new AlertDialog.Builder(CarsActivity.this)
                                    .setTitle("提示")
                                    .setMessage("是否确认删除校车？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            list.get(i).delete(CarsActivity.this, new DeleteListener() {
                                                @Override
                                                public void onSuccess() {
                                                    mArrayAdapter.remove(list.get(i));
                                                }

                                                @Override
                                                public void onFailure(int i, String s) {
                                                    Toast.makeText(CarsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    }
                });
                listDialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            startActivity(new Intent(this, AddCardActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        BmobQuery<Car> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> list) {
                CarsActivity.this.list = list;
                mArrayAdapter = new ArrayAdapter<>
                        (CarsActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(mArrayAdapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(CarsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
