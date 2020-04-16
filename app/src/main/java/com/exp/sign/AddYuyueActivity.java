package com.exp.sign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddYuyueActivity extends BaseActivity {


    private CardView cvAdd;
    private TextView checi;
    private TextView zuowei;
    private TextView startDate;
    private TextView endDate;
    private TextView date;
    private TextView fromState;
    private TextView endState;
    private FloatingActionButton fab;

    private int sy;
    private int sm;
    private int sd;
    private int ey;
    private int em;
    private int ed;

    private Button btGo;

    private Car mCar;
    private PositionBean mPositionBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_yuyue);
        TableActivity.c1 = 0;
        TableActivity.c2 = 0;
        ButterKnife.inject(this);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        checi = (TextView) findViewById(R.id.checi);
        zuowei = (TextView) findViewById(R.id.zuowei);
        fromState = (TextView) findViewById(R.id.startState);
        endState = (TextView) findViewById(R.id.endState);
        startDate = (TextView) findViewById(R.id.startDate);
        endDate = (TextView) findViewById(R.id.endDate);
        date = (TextView) findViewById(R.id.date);
        btGo = (Button) findViewById(R.id.bt_go);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        btGo = (Button) findViewById(R.id.bt_go);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog pickerDialog = new TimePickerDialog(AddYuyueActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                date.setText(hourOfDay + ":" + minute);
                                zuowei.setText("选择座位");
                            }
                        }, 8, 0, true);
                pickerDialog.show();
            }
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddYuyueActivity.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        sy = year;
                        sm = month;
                        sd = dayOfMonth;
                        startDate.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                        zuowei.setText("选择座位");
                    }
                });
                datePickerDialog.show();
            }
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddYuyueActivity.this);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        ey = year;
                        em = month;
                        ed = dayOfMonth;
                        endDate.setText(year + "年" + (month + 1) + "月" + dayOfMonth + "日");
                        zuowei.setText("选择座位");
                    }
                });
                datePickerDialog.show();
            }
        });

        zuowei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startDate.getText().toString().equals("开始日期") || endDate.getText().toString().equals("结束日期") || date.getText().toString().equals("乘车时间")) {
                    Toast.makeText(AddYuyueActivity.this, "请先选择好开始日期、结束日期、乘车时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(AddYuyueActivity.this, TableActivity.class)
                        .putExtra("from",startDate.getText().toString())
                        .putExtra("end", endDate.getText().toString())
                        .putExtra("date", date.getText().toString())
                        .putExtra("data", mCar));
            }
        });
        fromState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> a = mCar.getLux();
                final String[] items = new String[a.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = a.get(i);
                }
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(AddYuyueActivity.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fromState.setText(items[which]);
                    }
                });
                listDialog.show();
            }
        });

        endState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> a = mCar.getLux();
                final String[] items = new String[a.size()];
                for (int i = 0; i < items.length; i++) {
                    items[i] = a.get(i);
                }
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(AddYuyueActivity.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        endState.setText(items[which]);
                    }
                });
                listDialog.show();
            }
        });

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        mCar = (Car) getIntent().getSerializableExtra("data");
        mPositionBean = (PositionBean) getIntent().getSerializableExtra("bean");

        if (mCar != null) {
            checi.setText(mCar.getName());
            if (mPositionBean != null) {
                zuowei.setText(mPositionBean.zuowei());
                startDate.setText(mPositionBean.getFromDate());
                endDate.setText(mPositionBean.getEndDate());
                fromState.setText(mPositionBean.getFromState());
                endState.setText(mPositionBean.getEndState());
                date.setText(mPositionBean.getDate());
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(TableActivity.c1 != 0) {
            zuowei.setText(TableActivity.c1 + "排" + TableActivity.c2 + "座");
        }
    }

    public void next(View view) {

        final String checis = checi.getText().toString().replaceAll("车次", "");
        final String zuoweis = zuowei.getText().toString().replaceAll("选择座位", "");
        final String startDates = startDate.getText().toString().replaceAll("开始日期", "");

        final String endDates = endDate.getText().toString().replaceAll("结束日期", "");

        final String fromStates = fromState.getText().toString().replaceAll("起始站", "");

        final String endStates = endState.getText().toString().replaceAll("末尾站", "");

        final String dates = date.getText().toString().replaceAll("乘车时间", "");

        if (TextUtils.isEmpty(checis) || TextUtils.isEmpty(zuoweis)
                || TextUtils.isEmpty(startDates)
                || TextUtils.isEmpty(endDates)
                || TextUtils.isEmpty(fromStates)
                || TextUtils.isEmpty(fromStates)
                || TextUtils.isEmpty(endStates)
                || TextUtils.isEmpty(dates)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sy > ey) {
            Toast.makeText(this, "开始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sm > em && sy == ey) {
            Toast.makeText(this, "开始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
            return;
        }
        if (sm == em && sy == ey && sd > ed) {
            Toast.makeText(this, "开始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
            return;
        }

        if (fromStates.equalsIgnoreCase(endStates)) {
            Toast.makeText(this, "起始站和末尾站不能一样", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] zw = zuoweis.split("排");
        int col = Integer.parseInt(zw[0]);
        int row = Integer.parseInt(zw[1].replaceAll("座", ""));
        mPositionBean = new PositionBean();
        mPositionBean.setCarId(mCar.getId());
        mPositionBean.setCol(col);
        mPositionBean.setRow(row);
        mPositionBean.setDate(dates);
        mPositionBean.setEndDate(endDates);
        mPositionBean.setFromDate(startDates);
        mPositionBean.setEndState(endStates);
        mPositionBean.setFromState(fromStates);
        mPositionBean.setUname((String) SPUtil.get(this, "username", ""));
        List<PositionBean> ps = mCar.getBs();
        ps.add(mPositionBean);
        mCar.setPosition(JSONArray.toJSONString(ps));
        mCar.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddYuyueActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                gotoHome();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(AddYuyueActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoHome() {
        finish();
    }

}
