package com.exp.sign;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import com.qfdqc.views.seattable.SeatTable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TableActivity extends BaseActivity implements SeatTable.CheckListener {
    public SeatTable seatTableView;
    public HashSet<String> tables;
    private Car mCar;

    public static SimpleDateFormat sSimpleDateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
    public static SimpleDateFormat sSimpleDateFormat13 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    public static SimpleDateFormat sSimpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static SimpleDateFormat sSimpleDateFormat11 = new SimpleDateFormat("yyyy年MM月dd日");

    public static int c1;
    public static int c2;

    private String from;
    private String end;
    private String date;

    private long f;
    private long e;
    private long d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCar = (Car) getIntent().getSerializableExtra("data");
        setContentView(R.layout.activity_table);
        seatTableView = (SeatTable) findViewById(R.id.seatView);
        seatTableView.setScreenName("校车座位");//设置屏幕名称
        seatTableView.setMaxSelected(1);//设置最多选中

        from = getIntent().getStringExtra("from");
        end = getIntent().getStringExtra("end");
        date = getIntent().getStringExtra("date");

        try {
            f = sSimpleDateFormat1.parse(from).getTime();
            e = sSimpleDateFormat1.parse(end).getTime();
            d = sSimpleDateFormat2.parse("2020-1-1 " + date).getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        setok();

        seatTableView.setSeatChecker(new SeatTable.SeatChecker() {

            @Override
            public boolean isValidSeat(int row, int column) {
                return true;
            }

            @Override
            public boolean isSold(int row, int column) {
                if (tables.contains((column + 1) + "," + (row + 1))) {
                    return true;
                }
                return false;
            }

            @Override
            public void checked(int row, int column) {

            }

            @Override
            public void unCheck(int row, int column) {

            }

            @Override
            public String[] checkedSeatTxt(int row, int column) {
                return null;
            }

        });
        seatTableView.setData(mCar.getCol(), mCar.getRow());
        seatTableView.setCheckListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheck() {
        ArrayList<String> selectedSeat = seatTableView.getSelectedSeat();
        final String sc = selectedSeat.get(0);
        String sa = sc.replace(",","#");
        String ss[] = sa.split("#");
        String s1 = Integer.parseInt(ss[0]) + 1 + "";
        String s2 = Integer.parseInt(ss[1]) + 1 + "";
        final String s = s1 + "#" + s2;
        c1 = Integer.parseInt(s2);
        c2 = Integer.parseInt(s1);
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("是否预约" + (s2 + "排" + s1+"座") + "？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                })
                .setNegativeButton("取消", null)
                .create().show();
    }

    @Override
    public void onSoldedClick(final String s) {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setok();
        seatTableView.invalidate();
    }

    public void setok() {
        tables = new HashSet<>();
        List<PositionBean> ps =  mCar.getBs();
        long f1 = 0L;
        long d1 = 0L;
        long e1 = 0L;
        for (PositionBean p : ps) {
            try {
                f1 = sSimpleDateFormat11.parse(p.getFromDate()).getTime();
                e1 = sSimpleDateFormat11.parse(p.getEndDate()).getTime();
                d1 = sSimpleDateFormat2.parse("2020-1-1 " + p.getDate()).getTime();
                if(!(f1 > e || e1 < f) && Math.abs(d1 - d) < 1000 * 60 * 45) {
                    tables.add(p.getCol() + "," + p.getRow());
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }
    }

}
