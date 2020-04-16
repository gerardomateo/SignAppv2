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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class XxActivity extends BaseActivity {

    private ListView listView;
    private List<Wrapper2> list;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user1s);
        listView = (ListView) findViewById(R.id.list);
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
        BmobQuery<BmobUser> us = new BmobQuery<>();
        us.addWhereEqualTo("username", SPUtil.get(this, "username", ""));
        us.findObjects(this, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(final List<BmobUser> list1) {
                BmobQuery<Car> query = new BmobQuery<>();
                query.findObjects(XxActivity.this, new FindListener<Car>() {
                    @Override
                    public void onSuccess(List<Car> list) {
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
                        XxActivity.this.list = new ArrayList<>();
                        for (Car car : list) {
                            for (PositionBean b : car.getBs()) {
                                if(b.yes(list1.get(0).getNotify())) {
                                    XxActivity.this.list.add(new Wrapper2(car, b));
                                }
                            }
                        }
                        mArrayAdapter = new ArrayAdapter<>
                                (XxActivity.this, android.R.layout.simple_list_item_1, XxActivity.this.list);
                        listView.setAdapter(mArrayAdapter);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(XxActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(XxActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
