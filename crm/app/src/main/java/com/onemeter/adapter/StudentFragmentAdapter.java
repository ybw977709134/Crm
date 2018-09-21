package com.onemeter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onemeter.R;
import com.onemeter.app.MyApplication;
import com.onemeter.entity.StudentInfo;

import java.util.List;

/**
 * 描述：学员列表适配器
 * 项目名称：CrmWei
 * 作者：angelyin
 * 时间：2016/5/4 21:22
 * 备注：
 */
public class StudentFragmentAdapter extends BaseAdapter{
    public static List<StudentInfo> vfo;
    Context mContext;
    public StudentFragmentAdapter(Context mContext,List<StudentInfo> vfo){
        this.mContext=mContext;
        this.vfo=vfo;
    }

    @Override
    public int getCount() {
        if (vfo == null) {
            return 0;
        } else {
            return vfo.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (vfo == null) {
            return null;
        } else {
            return vfo.get(position);
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.student_fragment_listview_item_layout, null);
            holder.student_name= (TextView) convertView.findViewById(R.id.student_name);
            holder.student_phone= (TextView) convertView.findViewById(R.id.student_phone);
            holder.student_card= (TextView) convertView.findViewById(R.id.student_card);
            holder.student_contacts= (TextView) convertView.findViewById(R.id.student_contacts);
            holder.student_class= (TextView) convertView.findViewById(R.id.student_class);
            holder.student_school= (TextView) convertView.findViewById(R.id.student_school);
            holder.fragment_student_rel= (LinearLayout) convertView.findViewById(R.id.fragment_student_rel);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        if(MyApplication.fragmentStudentPositon.size()>0){
            if(MyApplication.fragmentStudentPositon.get(0).equals(position+"")){
                holder.fragment_student_rel.setBackgroundColor(convertView.getResources().getColor(R.color.bule));
                holder.student_name.setTextColor(convertView.getResources().getColor(R.color.white));
                holder.student_phone.setTextColor(convertView.getResources().getColor(R.color.white));
                holder.student_card.setTextColor(convertView.getResources().getColor(R.color.white));
                holder.student_contacts.setTextColor(convertView.getResources().getColor(R.color.white));
                holder.student_class.setTextColor(convertView.getResources().getColor(R.color.white));
                holder.student_school.setTextColor(convertView.getResources().getColor(R.color.white));

            }else {
                holder.fragment_student_rel.setBackgroundColor(convertView.getResources().getColor(R.color.white));
                holder.student_name.setTextColor(convertView.getResources().getColor(R.color.txt_bule));
                holder.student_phone.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
                holder.student_card.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
                holder.student_contacts.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
                holder.student_class.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
                holder.student_school.setTextColor(convertView.getResources().getColor(R.color.txt_bule));
            }
        }else {
            holder.student_name.setTextColor(convertView.getResources().getColor(R.color.txt_bule));
            holder.student_phone.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
            holder.student_card.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
            holder.student_contacts.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
            holder.student_class.setTextColor(convertView.getResources().getColor(R.color.bianji_text));
            holder.student_school.setTextColor(convertView.getResources().getColor(R.color.txt_bule));
            holder.fragment_student_rel.setBackgroundColor(convertView.getResources().getColor(R.color.white));
        }

        holder.student_name.setText(vfo.get(position).getName());
        holder.student_phone.setText(vfo.get(position).getPhone());
        holder.student_card.setText(vfo.get(position).getCard());
        holder.student_contacts.setText(vfo.get(position).getContacts());
        holder.student_class.setText(vfo.get(position).getNclass());
        holder.student_school.setText(vfo.get(position).getSchool());
        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        private TextView student_name;
        private TextView student_phone;
        private TextView student_card;
        private TextView student_contacts;
        private TextView student_class;
        private TextView student_school;
        private LinearLayout  fragment_student_rel;//选中item的属性

    }
}
