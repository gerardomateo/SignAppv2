package com.exp.sign;

import android.app.ActivityOptions;
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
import android.widget.RadioButton;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import static com.exp.sign.R.id.loading;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.et_username)
    EditText etUsername;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.bt_go)
    Button btGo;
    @InjectView(R.id.cv)
    CardView cv;
    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.putong)
    RadioButton rbpt;
    @InjectView(R.id.guanli)
    RadioButton rbgl;
    @InjectView(loading)
    AVLoadingIndicatorView loadingIndicatorView;

    public static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Bmob.initialize(this,"230b12e3356c294c6abbb95d376a589a");
        ButterKnife.inject(this);
        gotoHome();
        instance = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void gotoHome() {
        instance = null;
        String un = (String) SPUtil.get(this, "username", "");
        if(!TextUtils.isEmpty(un)) {
            if(un.equalsIgnoreCase("admin")) {
                startActivity(new Intent(this, HomeAdActivity.class));
            } else {
                startActivity(new Intent(this, HomeNuActivity.class));
            }
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.bt_go:
                final String un = etUsername.getText().toString();
                final String pd = etPassword.getText().toString();
                if (TextUtils.isEmpty(un) || TextUtils.isEmpty(pd)) {
                    Toast.makeText(MainActivity.this, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(un.equalsIgnoreCase("admin") && pd.equalsIgnoreCase("123456")) {
                    SPUtil.put(MainActivity.this, "name", "管理员");
                    SPUtil.put(MainActivity.this, "username", "admin");
                    SPUtil.put(MainActivity.this, "password", "123456");
                    Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    gotoHome();
                    return;
                }
                loadingIndicatorView.show();
                BmobQuery<BmobUser> userBmobQuery = new BmobQuery<>();
                userBmobQuery.addWhereEqualTo("username", un)
                        .addWhereEqualTo("password", pd)
                        .findObjects(this, new FindListener<BmobUser>() {
                            @Override
                            public void onSuccess(List<BmobUser> list) {
                                loadingIndicatorView.hide();
                                if (list == null || list.size() == 0) {
                                    Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    SPUtil.put(MainActivity.this, "name", list.get(0).getName());
                                    SPUtil.put(MainActivity.this, "username", un);
                                    SPUtil.put(MainActivity.this, "password", pd);
                                    Toast.makeText(MainActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                    gotoHome();
                                }
                            }

                            @Override
                            public void onError(int i, String s) {
                                loadingIndicatorView.hide();
                                Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
