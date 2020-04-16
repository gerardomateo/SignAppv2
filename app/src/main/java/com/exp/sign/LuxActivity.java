package com.exp.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class LuxActivity extends BaseActivity {

    private ListView listView;
    private ArrayAdapter mArrayAdapter;
    private List<String> lxs;
    private Car mCar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userss);
        listView = (ListView) findViewById(R.id.list);
        findViewById(R.id.rg).setVisibility(View.GONE);
        mCar = (Car) getIntent().getSerializableExtra("data");
        BmobQuery<Car> query = new BmobQuery<>();
        query.addWhereEqualTo("id", mCar.getId());
        query.findObjects(this, new FindListener<Car>() {
            @Override
            public void onSuccess(List<Car> list) {
                if (list != null && !list.isEmpty()) {
                    mCar = list.get(0);
                    listView.setAdapter(mArrayAdapter = new ArrayAdapter(LuxActivity.this, android.R.layout.simple_list_item_1, lxs = mCar.getLux()));
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(LuxActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String item = (String) mArrayAdapter.getItem(position);
                final String[] items = {"修改站点", "添加上一站", "添加下一站", "删除站点"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(LuxActivity.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            final EditText editText = new EditText(LuxActivity.this);
                            AlertDialog.Builder inputDialog =
                                    new AlertDialog.Builder(LuxActivity.this);
                            editText.setText(item);
                            inputDialog.setTitle("路线站点").setView(editText);
                            inputDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String key = editText.getText().toString();
                                            if (TextUtils.isEmpty(key)) {
                                                return;
                                            }
                                            lxs.set(position, key);
                                            mArrayAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                        if (which == 1) {
                            final EditText editText = new EditText(LuxActivity.this);
                            AlertDialog.Builder inputDialog =
                                    new AlertDialog.Builder(LuxActivity.this);
                            inputDialog.setTitle("路线站点").setView(editText);
                            inputDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String key = editText.getText().toString();
                                            if (TextUtils.isEmpty(key)) {
                                                return;
                                            }
                                            lxs.add(position, key);
                                            mArrayAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                        if (which == 2) {
                            final EditText editText = new EditText(LuxActivity.this);
                            AlertDialog.Builder inputDialog =
                                    new AlertDialog.Builder(LuxActivity.this);
                            inputDialog.setTitle("路线站点").setView(editText);
                            inputDialog.setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            String key = editText.getText().toString();
                                            if (TextUtils.isEmpty(key)) {
                                                return;
                                            }
                                            lxs.add(position + 1, key);
                                            mArrayAdapter.notifyDataSetChanged();
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                        if (which == 3) {
                            new AlertDialog.Builder(LuxActivity.this)
                                    .setTitle("提示")
                                    .setMessage("是否确认删除路线？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            lxs.remove(position);
                                            mArrayAdapter.notifyDataSetChanged();
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
            final EditText editText = new EditText(this);
            AlertDialog.Builder inputDialog =
                    new AlertDialog.Builder(LuxActivity.this);
            inputDialog.setTitle("路线站点").setView(editText);
            inputDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String key = editText.getText().toString();
                            if (TextUtils.isEmpty(key)) {
                                return;
                            }
                            lxs.add(key);
                            mArrayAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        mCar.setRoute(JSONArray.toJSONString(lxs));
        mCar.update(App.sApp);
        super.finish();
    }
}
