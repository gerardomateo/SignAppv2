package com.exp.sign;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import static com.exp.sign.MainActivity.instance;

public class RegisterActivity extends AppCompatActivity {

    @InjectView(R.id.fab)
    FloatingActionButton fab;
    @InjectView(R.id.cv_add)
    CardView cvAdd;

    @InjectView(R.id.et_username)
    EditText editText;
    @InjectView(R.id.et_password)
    EditText etPassword;
    @InjectView(R.id.et_repeatpassword)
    EditText etReaptPassword;

    @InjectView(R.id.male)
    RadioButton male;

    @InjectView(R.id.putong)
    RadioButton rbpt;
    @InjectView(R.id.guanli)
    RadioButton rbgl;

    @InjectView(R.id.et_no)
    EditText etno;
    @InjectView(R.id.et_name)
    EditText etname;
    @InjectView(R.id.et_phone)
    EditText etphone;
    @InjectView(R.id.et_email)
    EditText etemail;
    @InjectView(R.id.loading)
    AVLoadingIndicatorView loadingIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        loadingIndicatorView.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }


        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    public void next(View view) {
        final String username = editText.getText().toString();
        final String password = etPassword.getText().toString();
        String repeatPassword = etReaptPassword.getText().toString();
        String no = etno.getText().toString();
        final String name = etname.getText().toString();
        String phone = etphone.getText().toString();
        String email = etemail.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)
                || TextUtils.isEmpty(no) || TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)|| TextUtils.isEmpty(email)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!repeatPassword.equals(password)) {
            Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        String sex = "男";
        if (!male.isChecked())
            sex = "女";
        loadingIndicatorView.show();
        final BmobUser user = new BmobUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setSex(sex);
        user.setName(name);
        user.setNumber(no);
        user.setEmail(email);
        user.setPhone(phone);
        BmobQuery<BmobUser> userBmobQuery = new BmobQuery<>();
        userBmobQuery
                .addWhereEqualTo("username", username)
                .findObjects(this, new FindListener<BmobUser>() {
            @Override
            public void onSuccess(List<BmobUser> list) {
                if (list != null && list.size() > 0) {
                    loadingIndicatorView.hide();
                    Toast.makeText(RegisterActivity.this, "用户已存在，请更换用户名", Toast.LENGTH_SHORT).show();
                    return;
                }
                user.save(RegisterActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        loadingIndicatorView.hide();
                        SPUtil.put(RegisterActivity.this, "name", name);
                        SPUtil.put(RegisterActivity.this, "username", username);
                        SPUtil.put(RegisterActivity.this, "password", password);
                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                        gotoHome();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        loadingIndicatorView.hide();
                        Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                loadingIndicatorView.hide();
                Toast.makeText(RegisterActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void gotoHome() {
        if (instance != null) {
            instance.finish();
            instance = null;
        }
        startActivity(new Intent(this, HomeNuActivity.class));
        finish();
    }
}
