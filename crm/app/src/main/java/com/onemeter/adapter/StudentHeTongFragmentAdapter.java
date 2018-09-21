package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.entity.StudentHetongInfo;

import java.util.List;

/**
 * 描述：学员合同列表适配器
 * 项目名称：CrmWei
 * 作者：Administrator
 * 时间：2016/5/6 18:07
 * 备注：
 */
public class StudentHeTongFragmentAdapter extends BaseAdapter {
    public  List<StudentHetongInfo> sshtfo;
    Context mContext;
    public StudentHeTongFragmentAdapter(Context mContext,List<StudentHetongInfo> sshtfo){
        this.mContext=mContext;
        this.sshtfo=sshtfo;
    }

    @Override
    public int getCount() {
        if (sshtfo == null) {
            return 0;
        } else {
            return sshtfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (sshtfo == null) {
            return null;
        } else {
            return sshtfo.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.student_hetong_fragment_listview_item_layout, null);
            holder.student_hetong_num= (TextView) convertView.findViewById(R.id.student_hetong_num);
            holder.student_course_name= (TextView) convertView.findViewById(R.id.student_course_name);
            holder.student_class= (TextView) convertView.findViewById(R.id.student_class);
            holder.student_lesson_num= (TextView) convertView.findViewById(R.id.student_lesson_num);
            holder.student_total= (TextView) convertView.findViewById(R.id.student_total);
            holder.student_refund_money= (TextView) convertView.findViewById(R.id.student_refund_money);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        StudentHetongInfo studentHetongInfo=sshtfo.get(position);
        holder.student_hetong_num.setText(studentHetongInfo.getHeTongNum().toString());
        holder.student_course_name.setText(studentHetongInfo.getCourseName().toString());
        holder.student_class.setText(studentHetongInfo.getClassName().toString());
        holder.student_lesson_num.setText(studentHetongInfo.getLessonNum().toString());
        holder.student_total.setText(studentHetongInfo.getTotal()+"");
        holder.student_refund_money.setText(studentHetongInfo.getRefundMoney()+"");

        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        private TextView student_hetong_num;
        private TextView student_course_name;
        private TextView student_class;
        private TextView student_lesson_num;
        private TextView student_total;
        private TextView student_refund_money;

    }
}