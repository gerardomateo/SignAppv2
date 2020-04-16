package com.exp.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class YuyuActivity extends BaseActivity {

    private ListView listView;
    private List<Wrapper> list;
    private ArrayAdapter mArrayAdapter;

    private RadioGroup rg;
    private RadioButton rb1;
    private RadioButton rb2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userss);
        listView = (ListView) findViewById(R.id.list);
        rg = (RadioGroup) findViewById(R.id.rg);
        rb1 = (RadioButton) findViewById(R.id.rb1);
        rb2 = (RadioButton) findViewById(R.id.rb2);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getData();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Wrapper wrapper = (Wrapper) mArrayAdapter.getItem(i);
                if (rb1.isChecked()) {
                    final String[] items = {"查看路线", "预约校车", "联系司机"};
                    AlertDialog.Builder listDialog =
                            new AlertDialog.Builder(YuyuActivity.this);
                    listDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                List<String> ls = wrapper.getCar().getLux();
                                if (ls.isEmpty()) {
                                    Toast.makeText(YuyuActivity.this, "暂时没有路线", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                final String[] items = new String[ls.size()];
                                for (int i = 0; i < items.length; i++) {
                                    items[i] = ls.get(i);
                                }
                                AlertDialog.Builder listDialog =
                                        new AlertDialog.Builder(YuyuActivity.this);
                                listDialog.setTitle("校车路线");
                                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                listDialog.setPositiveButton("我知道了", null);
                                listDialog.show();
                            }
                            if (which == 1) {
                                List<String> ls = wrapper.getCar().getLux();
                                if (ls.isEmpty()) {
                                    Toast.makeText(YuyuActivity.this, "暂时没有路线，无法预约", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                startActivity(new Intent(YuyuActivity.this, AddYuyueActivity.class)
                                        .putExtra("data", wrapper.getCar()));
                            }
                            if (which == 2) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                Uri data = Uri.parse("tel:" + wrapper.getCar().getPhone());
                                intent.setData(data);
                                startActivity(intent);
                            }
                        }
                    });
                    listDialog.show();
                } else {
                    new AlertDialog.Builder(YuyuActivity.this)
                            .setTitle("提示")
                            .setMessage("是否取消预约？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Car car  = wrapper.getCar();
                                    List<PositionBean> beans = car.getBs();
                                    beans.remove(wrapper.getPositionBean());
                                    car.setPosition(JSONArray.toJSONString(beans));
                                    car.update(YuyuActivity.this, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {
                                            mArrayAdapter.remove(wrapper);
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Toast.makeText(YuyuActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (rb1.isChecked()) {
                    Iterator<Car> carIterator = list.iterator();
                    while (carIterator.hasNext()) {
                        Car car = carIterator.next();
                        if (car.getLux().isEmpty()) {
                            carIterator.remove();
                        }
                    }
                    YuyuActivity.this.list = new ArrayList<>();
                    for (Car car : list) {
                        YuyuActivity.this.list.add(new Wrapper(car, false));
                    }
                    mArrayAdapter = new ArrayAdapter<>
                            (YuyuActivity.this, android.R.layout.simple_list_item_1, YuyuActivity.this.list);
                    listView.setAdapter(mArrayAdapter);
                } else {
                    Iterator<Car> carIterator = list.iterator();
                    while (carIterator.hasNext()) {
                        Car car = carIterator.next();
                        if (car.getLux().isEmpty()) {
                            carIterator.remove();
                        } else {
                            List<PositionBean> bean = car.getBsS();
                            if (bean == null || bean.isEmpty()) {
                                carIterator.remove();
                            }
                        }
                    }
                    YuyuActivity.this.list = new ArrayList<>();
                    for (Car car : list) {
                        for (PositionBean b : car.getBs()) {
                            YuyuActivity.this.list.add(new Wrapper(car, b,true));
                        }
                    }
                    mArrayAdapter = new ArrayAdapter<>
                            (YuyuActivity.this, android.R.layout.simple_list_item_1, YuyuActivity.this.list);
                    listView.setAdapter(mArrayAdapter);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(YuyuActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
