package com.exp.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class HomeNuActivity extends AppCompatActivity {

    public static HomeNuActivity ins;
    private List<Wrapper2> mWrapper2s = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ins = this;
    }

    public void check() {
        BmobQuery<BmobUser> us = new BmobQuery<>();
        us.addWhereEqualTo("username", SPUtil.get(this, "username", ""));
        us.findObjects(this, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(final List<BmobUser> list1) {
                BmobQuery<Car> query = new BmobQuery<>();
                query.findObjects(HomeNuActivity.this, new FindListener<Car>() {
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
                        List<Wrapper2> wrapper2s = new ArrayList<>();
                        for (Car car : list) {
                            for (PositionBean b : car.getBs()) {
                                if (b.yes(list1.get(0).getNotify())) {
                                    wrapper2s.add(new Wrapper2(car, b));
                                }
                            }
                        }
                        if(mWrapper2s.isEmpty() && wrapper2s.isEmpty()) {
                            return;
                        }
                        if (mWrapper2s == null || (mWrapper2s.isEmpty() && wrapper2s.isEmpty())
                                || mWrapper2s.equals(wrapper2s)) {
                            mWrapper2s = wrapper2s;
                            new AlertDialog.Builder(HomeNuActivity.this)
                                    .setTitle("提示")
                                    .setMessage("有即将到达上车时间的车次，是否前往查看？")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(HomeNuActivity.this, XxActivity.class));
                                        }
                                    })
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    }

                    @Override
                    public void onError(int i, String s) {

                    }
                });
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        check();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.exit) {
            SPUtil.put(this, "username", "");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // gr
    public void yhgl(View view) {
        startActivity(new Intent(this, PersonActivity.class)
                .putExtra("username", (String) SPUtil.get(this, "username", "")));
    }

    // 预约
    public void jldj(View view) {
        startActivity(new Intent(this, YuyuActivity.class));
    }

    // 消息
    public void jtwt(View view) {
        startActivity(new Intent(this, XxActivity.class));
    }

    // 设置
    public void zxgl(View view) {
        final String[] items = {"预约消息提醒", "系统问题反馈"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    final String[] items = {"上车前10分钟", "上车前20分钟", "上车前30分钟", "上车前60分钟"};
                    AlertDialog.Builder listDialog =
                            new AlertDialog.Builder(HomeNuActivity.this);
                    listDialog.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            BmobQuery<BmobUser> query = new BmobQuery<>();
                            query.addWhereEqualTo("username", (String) SPUtil.get(HomeNuActivity.this, "username", ""));
                            query.findObjects(HomeNuActivity.this, new FindListener<BmobUser>() {
                                @Override
                                public void onSuccess(List<BmobUser> list) {
                                    if (list == null || list.isEmpty()) {
                                        return;
                                    }
                                    if (which == 0) {
                                        list.get(0).setNotify(10);
                                    }
                                    if (which == 1) {
                                        list.get(0).setNotify(20);
                                    }
                                    if (which == 2) {
                                        list.get(0).setNotify(30);
                                    }
                                    if (which == 3) {
                                        list.get(0).setNotify(60);
                                    }
                                    list.get(0).update(HomeNuActivity.this, new UpdateListener() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Toast.makeText(HomeNuActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(HomeNuActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    listDialog.show();
                }
                if (which == 1) {
                    final EditText editText = new EditText(HomeNuActivity.this);
                    AlertDialog.Builder inputDialog =
                            new AlertDialog.Builder(HomeNuActivity.this);
                    inputDialog.setTitle("问题反馈").setView(editText);
                    inputDialog.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String noti = editText.getText().toString();
                                    if (TextUtils.isEmpty(noti)) {
                                        return;
                                    }
                                    Wenti wenti = new Wenti();
                                    wenti.setUn((String) SPUtil.get(HomeNuActivity.this, "username", ""));
                                    wenti.setMsg(noti);
                                    wenti.save(HomeNuActivity.this, new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            Toast.makeText(HomeNuActivity.this, "反馈成功", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            Toast.makeText(HomeNuActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
}
