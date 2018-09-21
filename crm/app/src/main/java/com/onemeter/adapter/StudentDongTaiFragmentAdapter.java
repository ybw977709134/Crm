package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.entity.DongtaiInfo;
import com.onemeter.utils.Utils;

import java.util.List;

/**
 * 描述：学员合同列表适配器
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/6 18:07
 * 备注：
 */
public class StudentDongTaiFragmentAdapter extends BaseAdapter {
    public static List<DongtaiInfo> dInfo;
    Context mContext;
    public StudentDongTaiFragmentAdapter(Context mContext, List<DongtaiInfo> dInfo){
        this.mContext=mContext;
        this.dInfo=dInfo;
    }

    @Override
    public int getCount() {
        if (dInfo == null) {
            return 0;
        } else {
            return dInfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (dInfo == null) {
            return null;
        } else {
            return dInfo.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.student_dongtai_fragment_listview_item_layout, null);
            holder.student_dongtai_date= (TextView) convertView.findViewById(R.id.student_dongtai_date);
            holder.student_comment= (TextView) convertView.findViewById(R.id.student_comment);
            holder.student_way= (TextView) convertView.findViewById(R.id.student_way);
            holder.student_salesman= (TextView) convertView.findViewById(R.id.student_salesman);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        DongtaiInfo dongtaiInfo=dInfo.get(position);
        /** 格式化日期 **/
        holder.student_dongtai_date.setText(Utils.parseDate(dongtaiInfo.getDongtaiDate()));
        holder.student_comment.setText(dongtaiInfo.getDongtaiContext().toString());
        holder.student_way.setText(dongtaiInfo.getDongtaiAction().toString());
        holder.student_salesman.setText(dongtaiInfo.getDongtaiSalesman().toString());

        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        private TextView student_dongtai_date;
        private TextView student_comment;
        private TextView student_way;
        private TextView student_salesman;


    }
}