package com.exp.sign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class UsersActivity extends BaseActivity {

    private ListView listView;
    private AVLoadingIndicatorView loadingIndicatorView;
    private List<BmobUser> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        listView = (ListView) findViewById(R.id.list);
        loadingIndicatorView = (AVLoadingIndicatorView) findViewById(R.id.loading);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(UsersActivity.this,PersonActivity.class)
                        .putExtra("username",list.get(i).getUsername()));
            }
        });
        BmobQuery<BmobUser> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<BmobUser>() {
                    @Override
                    public void onSuccess(List<BmobUser> list) {
                        loadingIndicatorView.hide();
                        UsersActivity.this.list = list;
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                (UsersActivity.this, android.R.layout.simple_list_item_1, getStringList(list));
                        listView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(int i, String s) {
                        loadingIndicatorView.hide();
                        Toast.makeText(UsersActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<String> getStringList(List<BmobUser> list) {
        List<String> ss = new ArrayList<>();
        for(BmobUser u:list){
            ss.add(u.getName()+"(" + u.getUsername() + ")");
        }
        return ss;
    }
}
