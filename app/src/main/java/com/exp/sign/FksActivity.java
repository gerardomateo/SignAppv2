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
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

public class FksActivity extends BaseActivity {

    private ListView listView;
    private List<Wenti> list;
    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userss);
        listView = (ListView) findViewById(R.id.list);
        findViewById(R.id.rg).setVisibility(View.GONE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final String[] items = {"反馈用户", "删除反馈"};
                AlertDialog.Builder listDialog =
                        new AlertDialog.Builder(FksActivity.this);
                listDialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            startActivity(new Intent(FksActivity.this, PersonActivity.class)
                                    .putExtra("username", list.get(i).getUn()));
                        }
                        if (which == 1) {
                            list.get(i).delete(FksActivity.this, new DeleteListener() {
                                @Override
                                public void onSuccess() {
                                    list.remove(i);
                                    mArrayAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(int i, String s) {
                                    Toast.makeText(FksActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                listDialog.show();
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
        BmobQuery<Wenti> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<Wenti>() {
            @Override
            public void onSuccess(List<Wenti> list) {
                FksActivity.this.list = list;
                mArrayAdapter = new ArrayAdapter<>
                        (FksActivity.this, android.R.layout.simple_list_item_1, list);
                listView.setAdapter(mArrayAdapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(FksActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
