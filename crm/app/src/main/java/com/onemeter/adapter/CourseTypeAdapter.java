package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout;
import com.onemeter.R;

import java.util.List;

/**课程分类适配器
 * Created by G510 on 2016/5/7.
 */
public class CourseTypeAdapter extends BaseAdapter{
    public static List<String> courseTypelist;
    Context mContext;
    private int selectedPosition;// 选中的位置
    public CourseTypeAdapter(Context mContext, List<String> courseTypelist) {
        this.mContext=mContext;
        this.courseTypelist=courseTypelist;
    }
//    // 选中行
//    public void (int position) {
//        if (position != selcetposition) {
//            selcetposition = position;
//            notifyDataSetChanged();
//        }
//    }
    @Override
    public int getCount() {
        if (courseTypelist == null) {
            return 0;
        } else {
            return courseTypelist.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (courseTypelist == null) {
            return null;
        } else {
            return courseTypelist.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.class_fragment_course_type_listview_item_layout, null);
            holder.class_left_course_type_text= (TextView) convertView.findViewById(R.id.class_left_course_type_text);
            holder.class_left_course_type_lin=(LinearLayout)convertView.findViewById(R.id.class_left_course_type_lin);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据

        holder.class_left_course_type_text.setText(courseTypelist.get(position).toString());
//        if (selcetposition == position) {
//            holder.class_left_course_type_lin.setSelected(true);
//            holder.class_left_course_type_lin.setPressed(true);
//            holder.class_left_course_type_lin.setBackgroundColor(R.color.btn_bule);
//        } else {
//            holder.class_left_course_type_lin.setSelected(false);
//            holder.class_left_course_type_lin.setPressed(false);
//            holder.class_left_course_type_lin.setBackgroundColor(R.color.snow);;
//        }
        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        private TextView class_left_course_type_text;//分类课程的名称
        private LinearLayout class_left_course_type_lin;

    }
}