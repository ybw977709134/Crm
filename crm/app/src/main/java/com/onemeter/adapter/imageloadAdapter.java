package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.onemeter.R;

import java.util.List;

/**图片画廊适配器
 * Created by G510 on 2016/5/8.
 */
public class imageloadAdapter extends BaseAdapter{
    public static List<String> imgPath;
    Context mContext;
    int count;
    private BitmapUtils bitmapUtils;
    public imageloadAdapter(Context mContext, List<String> imgPath) {
        this.mContext=mContext;
        this.imgPath=imgPath;
        bitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    public int getCount() {

        if (imgPath == null) {
            return 0;
        } else {
            return imgPath.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (imgPath == null) {
            return null;
        } else {
            return imgPath.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.fragment_course_gallery_item_layout, null);
            holder.gallery_item_img= (ImageView) convertView.findViewById(R.id.gallery_item_img);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        ImageLoader.getInstance().displayImage(imgPath.get(position).toString(), holder.gallery_item_img);
        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        @ViewInject(R.id.course_item_img)
        private ImageView gallery_item_img;//图片

    }

}

