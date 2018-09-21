package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.entity.classDetailInfo;

import java.util.List;


/**班级成员列表适配器
 * Created by G510 on 2016/5/7.
 */
public class ClassdetailAdapter extends BaseAdapter {
    Context mContext;
    public static List<classDetailInfo> csdfo;//班级成员列表集合
    public ClassdetailAdapter(Context mContext,List<classDetailInfo> csdfo){
        this.mContext=mContext;
        this.csdfo=csdfo;
    }

    @Override
    public int getCount() {
        if (csdfo == null) {
            return 0;
        } else {
            return csdfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (csdfo == null) {
            return null;
        } else {
            return csdfo.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.class_detail_fragment_listview_item_layout, null);
            holder.class_detail_student_name= (TextView) convertView.findViewById(R.id.class_detail_student_name);
            holder.class_detail_date= (TextView) convertView.findViewById(R.id.class_detail_date);
            holder.class_detail_hours= (TextView) convertView.findViewById(R.id.class_detail_hours);
            holder.class_detail_total= (TextView) convertView.findViewById(R.id.class_detail_total);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据

        holder.class_detail_student_name.setText(csdfo.get(position).getName().toString());
        holder.class_detail_date.setText(csdfo.get(position).getCtime().toString());
        holder.class_detail_hours.setText(csdfo.get(position).getHours().toString());
        holder.class_detail_total.setText(csdfo.get(position).getTotal().toString());

        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        private TextView class_detail_student_name;
        private TextView class_detail_date;
        private TextView class_detail_hours;
        private TextView class_detail_total;

    }

}