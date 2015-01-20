package com.ct.image.imageasyncloaderdemo;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ct.image.imageasyncloader.view.CustomImageView;

import java.io.File;
import java.util.ArrayList;


public class DemoActivity extends ActionBarActivity {

    private ListView mLvTest = null;
    private ArrayList<String> mListData = new ArrayList<>();
    private ListViewAdapter mListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initViews();
    }

    private void loadAllImgPath() {
        mListData.clear();
        String folderPath = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
        File folder = new File(folderPath);
        String[] fileNames = folder.list();
        if (fileNames != null) {
            for (String fileName : fileNames) {
                if (!TextUtils.isEmpty(fileName)) {
                    mListData.add(folderPath + fileName);
                }
            }
        }
    }

    private void loadAllUrl() {
        mListData.clear();
        mListData.add("http://www.desk-site.com/uploads/uploads/allimg/141020/1-141020142226.jpg");
        mListData.add("http://www.desk-site.com/uploads/uploads/allimg/141020/1-141020142234.jpg");
        mListData.add("http://www.desk-site.com/uploads/uploads/allimg/141020/1-141020143329.jpg");
        mListData.add("http://www.desk-site.com/uploads/uploads/allimg/130816/1-130Q61A417.jpg");
    }

    private void initViews() {
        mLvTest = (ListView) findViewById(R.id.lv_test);
        loadAllImgPath();
        mListAdapter = new ListViewAdapter();
        mLvTest.setAdapter(mListAdapter);
    }

    public class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            long startTime = System.currentTimeMillis();
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(DemoActivity.this);
                View root = inflater.inflate(R.layout.list_item, null, false);
                holder.imageView = (CustomImageView) root.findViewById(R.id.iv_item);
                holder.textView = (TextView) root.findViewById(R.id.tv_item);
                convertView = root;
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String oldImgPath = holder.imageView.getImagePath();
            String oldImgUrl = holder.imageView.getImageUrl();
            if (!TextUtils.isEmpty(oldImgPath) && !oldImgPath.equals(mListData.get(position))
                    || !TextUtils.isEmpty(oldImgUrl) && !oldImgUrl.equals(mListData.get(position))) {
                holder.imageView.setImageDrawable(null);
                holder.imageView.setPadding(0, 0, 0, 0);
            }
            String key = mListData.get(position);
            if (key.startsWith("http")) {
                holder.imageView.setImageUrl(key);
            } else {
                holder.imageView.setImagePath(key);
            }
            holder.imageView.loadImage();
            holder.textView.setText(position + ": text information is showing here!");
            long costTime = System.currentTimeMillis() - startTime;
            Log.e("fuck", "tid:" + Thread.currentThread().getId() + "getView time:" + costTime);
            return convertView;
        }
    }

    public static class ViewHolder {
        public CustomImageView imageView;
        public TextView textView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, VollyDemoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_load_image:
                loadAllImgPath();
                mListAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_download_image:
                loadAllUrl();
                mListAdapter.notifyDataSetChanged();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
