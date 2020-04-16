package com.exp.sign;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class FillInfoActivity extends BaseActivity {

    private CardView cvAdd;
    private EditText etName;
    private RadioButton male;
    private RadioButton female;
    private EditText etNo;
    private EditText etPhone;
    private EditText etEmail;
    private Button btGo;
    private FloatingActionButton fab;
    private LinearLayout llCpwd;
    private EditText etPwd;
    private AVLoadingIndicatorView loadingIndicatorView;

    private String username;
    private BmobUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);
        ButterKnife.inject(this);

        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading);
        loadingIndicatorView.show();
        cvAdd = (CardView) findViewById(R.id.cv_add);
        etName = (EditText) findViewById(R.id.et_name);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);
        etNo = (EditText) findViewById(R.id.et_no);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etEmail = (EditText) findViewById(R.id.et_email);
        btGo = (Button) findViewById(R.id.bt_go);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        llCpwd = (LinearLayout) findViewById(R.id.ll_cpwd);
        etPwd = (EditText) findViewById(R.id.et_pwd);

        username = getIntent().getStringExtra("username");

        if(getIntent().getBooleanExtra("ccpwd",false)){
            llCpwd.setVisibility(View.VISIBLE);
        }

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next(view);
            }
        });

        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username",username)
                .findObjects(this, new FindListener<BmobUser>() {
                    @Override
                    public void onSuccess(List<BmobUser> list) {
                        loadingIndicatorView.hide();
                        if(list!=null && list.size() > 0){
                            user = list.get(0);
                            etPwd.setText(user.getPassword());
                            etEmail.setText(user.getEmail());
                            etName.setText(user.getName());
                            etNo.setText(user.getNumber());
                            etPhone.setText(user.getPhone());
                            if(user.getSex().equalsIgnoreCase("女")){
                                female.setChecked(true);
                            }
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        loadingIndicatorView.hide();
                        Toast.makeText(FillInfoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void next(View view) {

        final String name = etName.getText().toString();
        final String email = etEmail.getText().toString();
        final String sex = male.isChecked()?"男":"女";
        final String phone = etPhone.getText().toString();
        final String no = etNo.getText().toString();
        final String pwd = etPwd.getText().toString();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)
                || TextUtils.isEmpty(pwd)
                || TextUtils.isEmpty(sex)
                || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(no)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        loadingIndicatorView.show();
        user.setPassword(pwd);
        user.setName(name);
        user.setEmail(email);
        user.setNumber(no);
        user.setPhone(phone);
        user.setSex(sex);
        user.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                loadingIndicatorView.hide();
                Toast.makeText(FillInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                gotoHome();
            }

            @Override
            public void onFailure(int i, String s) {
                loadingIndicatorView.hide();
                Toast.makeText(FillInfoActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoHome() {
        finish();
    }
}
