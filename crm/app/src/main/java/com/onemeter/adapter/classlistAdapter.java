package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.entity.ClasslistInfo;

import java.util.List;

/**班级列表数据源适配器
 * Created by G510 on 2016/5/7.
 */
public class classlistAdapter extends BaseAdapter{

    public static List<ClasslistInfo> mClasslistInfoList;
    Context mContext;

    public classlistAdapter(Context mContext, List<ClasslistInfo> mClasslistInfoList) {
        this.mContext=mContext;
        this.mClasslistInfoList=mClasslistInfoList;
    }


    @Override
    public int getCount() {
        if (mClasslistInfoList == null) {
            return 0;
        } else {
            return mClasslistInfoList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (mClasslistInfoList == null) {
            return null;
        } else {
            return mClasslistInfoList.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.class_fragment_listview_item_layout, null);
            holder.class_fragment_name= (TextView) convertView.findViewById(R.id.class_fragment_name);
            holder.class_fragment_startdata= (TextView) convertView.findViewById(R.id.class_fragment_startdata);
            holder.class_fragment_type= (TextView) convertView.findViewById(R.id.class_fragment_type);
            holder.class_fragment_biaoming_num= (TextView) convertView.findViewById(R.id.class_fragment_biaoming_num);
            holder.class_fragment_total= (TextView) convertView.findViewById(R.id.class_fragment_total);
            holder.class_fragment_stuts= (TextView) convertView.findViewById(R.id.class_fragment_stuts);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        holder.class_fragment_name.setText(mClasslistInfoList.get(position).getName().toString());
        holder.class_fragment_startdata.setText(mClasslistInfoList.get(position).getStartdate().toString());
        holder.class_fragment_type.setText(mClasslistInfoList.get(position).getType().toString());
        holder.class_fragment_biaoming_num.setText(mClasslistInfoList.get(position).getPurchasedNum().toString());
        holder.class_fragment_total.setText(mClasslistInfoList.get(position).getMaxStudents().toString());
        holder.class_fragment_stuts.setText(mClasslistInfoList.get(position).getStatus().toString());
        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        private TextView class_fragment_name;//班级名称
        private TextView class_fragment_startdata;//所属课程
        private TextView class_fragment_type;//班级类型
        private TextView class_fragment_biaoming_num;//已经报名的人数
        private TextView class_fragment_total;//最大人数
        private TextView class_fragment_stuts;//班级状态


    }
}