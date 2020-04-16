package com.exp.sign;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;

import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddCardActivity extends BaseActivity {


    private EditText checi;
    private EditText siji;
    private EditText chepai;
    private EditText phone;
    private EditText col;
    private EditText row;
    private Button btGo;

    private Car mCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_car);
        ButterKnife.inject(this);
        checi = (EditText) findViewById(R.id.checi);
        siji = (EditText) findViewById(R.id.siji);
        chepai = (EditText) findViewById(R.id.chepai);
        phone = (EditText) findViewById(R.id.phone);
        col = (EditText) findViewById(R.id.col);
        row = (EditText) findViewById(R.id.row);
        btGo = (Button) findViewById(R.id.bt_go);

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        mCar = (Car) getIntent().getSerializableExtra("data");

        if(mCar != null) {
            checi.setText(mCar.getName());
            siji.setText(mCar.getCarPer());
            chepai.setText(mCar.getChepai());
            phone.setText(mCar.getPhone());
            col.setText(mCar.getCol() + "");
            row.setText(mCar.getRow() + "");
        }

    }

    public void next(View view) {

        final String checis = checi.getText().toString();
        final String sijis = siji.getText().toString();
        final String chepais = chepai.getText().toString();
        final String phones = phone.getText().toString();
        final String cols = col.getText().toString();
        final String rows = row.getText().toString();

        if (TextUtils.isEmpty(checis) || TextUtils.isEmpty(sijis)
                || TextUtils.isEmpty(chepais)
                || TextUtils.isEmpty(phones)
                || TextUtils.isEmpty(cols)
                || TextUtils.isEmpty(rows)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        if(Integer.parseInt(cols) <= 0) {
            Toast.makeText(this, "座位排数必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        if(Integer.parseInt(rows) <= 0) {
            Toast.makeText(this, "每排个数必须大于0", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mCar != null) {
            mCar.setCarPer(sijis);
            mCar.setChepai(chepais);
            mCar.setCol(Integer.parseInt(cols));
            mCar.setRow(Integer.parseInt(rows));
            mCar.setName(checis);
            mCar.setPhone(phones);
            mCar.update(this, new UpdateListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddCardActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                    gotoHome();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(AddCardActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            mCar = new Car();
            mCar.setId(System.currentTimeMillis());
            mCar.setCarPer(sijis);
            mCar.setChepai(chepais);
            mCar.setCol(Integer.parseInt(cols));
            mCar.setRow(Integer.parseInt(rows));
            mCar.setName(checis);
            mCar.setPhone(phones);
            mCar.save(this, new SaveListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AddCardActivity.this, "操作成功，请返回添加路线", Toast.LENGTH_SHORT).show();
                    gotoHome();
                }

                @Override
                public void onFailure(int i, String s) {
                    Toast.makeText(AddCardActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void gotoHome() {
        finish();
    }

}