package com.konifar.activitytransitionsample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity {

    private static final int SAMPLE_COUNTS = 30;

    @InjectView(R.id.grid_main)
    GridView mGridMain;

    private PhotosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find by id をbutterknifeで実行
        ButterKnife.inject(this);
        //grid viewのイニシャライズ
        initGridView();
        //data binding 実行
        List<PhotoModel> photos = PhotoUtils.getSamplePhotos(SAMPLE_COUNTS);
        for (PhotoModel photo : photos) {
            adapter.add(photo);
        }

    }

    private void initGridView() {
        //アダプター作成、
        adapter = new PhotosAdapter(this);
        //アダプターセット
        mGridMain.setAdapter(adapter);
        //gridviewのクリックリスナ
        mGridMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PhotoModel photo = adapter.getItem(position);
                //detailactivityの起動
                DetailActivity.start(MainActivity.this, view, photo);
            }
        });
    }

    private class PhotosAdapter extends ArrayAdapter<PhotoModel> {

        public PhotosAdapter(Context context) {
            super(context, R.layout.item_photo, new ArrayList<PhotoModel>());
        }

        @Override
        public View getView(int pos, View view, ViewGroup parent) {
            ViewHolder holder;
            final PhotoModel photo = getItem(pos);
            //view model ここでxmlの塚一回しを判定。なければ作る。あれば飛ばす。
            if (view == null || view.getTag() == null) {
                //使い回しxml設定
                //
                view = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
                //
                holder = new ViewHolder(view);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            //ホルダーに
            holder.mImgPreview.setImageResource(photo.resId);
            view.setTag(holder);
            return view;
        }

    }
    //viewholderは表示もとのxmlの指定。また、表示先との同一イメージidとなるように指定する。
    static class ViewHolder {
        //R.id.img_previewは、cmd + clickすると、２つでる。飛び元、飛び先のxmlをさす。
        @InjectView(R.id.img_preview)
        //imageview拡張のメンバ変数によって、画像のリサイズ、切り取りして、表示。
        AspectRatioImageView mImgPreview;
        //butterknifeでidの指定で。
        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
