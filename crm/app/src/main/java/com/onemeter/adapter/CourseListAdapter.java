package com.onemeter.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.onemeter.R;
import com.onemeter.entity.new_courseInfo;

import java.util.List;

/**课程列表适配器
 * Created by G510 on 2016/5/8.
 */
public class CourseListAdapter extends BaseAdapter{
    public static List<new_courseInfo.mdatas>  courseList;
    Context mcontext;
    private BitmapUtils bitmapUtils;
    Handler  mHandler;
    public CourseListAdapter(Context mcontext, List<new_courseInfo.mdatas> courseList) {
     this.mcontext=mcontext;
        this.courseList=courseList;
        bitmapUtils = new BitmapUtils(mcontext);
        mHandler=new Handler();
    }

    @Override
    public int getCount() {
        if (courseList == null) {
            return 0;
        } else {
            return courseList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (courseList == null) {
            return null;
        } else {
            return courseList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView , ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.course_fragment_gridview_item_layout, null);
            holder.course_item_img= (ImageView) convertView.findViewById(R.id.course_item_img);
            holder.course_item_title= (TextView) convertView.findViewById(R.id.course_item_title);
            holder.course_item_price= (TextView) convertView.findViewById(R.id.course_item_price);
            holder.course_item_text= (TextView) convertView.findViewById(R.id.course_item_text);
            holder.course_item_class_hours= (TextView) convertView.findViewById(R.id.course_item_class_hours);
            ViewUtils.inject(holder, convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(courseList.get(position).getImgs().size()>0) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
                        ImageLoader.getInstance().displayImage(courseList.get(position).getImgs().get(0).getSrc().toString().trim(), holder.course_item_img);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            },1000);


        }else{
            bitmapUtils.display(holder.course_item_img, "assets/processedimages/img3.png");
        }
         if(courseList.get(position).getName()==null){
             holder.course_item_title.setText("");
         }else{
             holder.course_item_title.setText(courseList.get(position).getName().toString());
         }

        if(courseList.get(position).getDesc()==null){
            holder.course_item_text.setText("");
        }else{
            holder.course_item_text.setText(courseList.get(position).getDesc().toString());
        }
        if(courseList.get(position).getHours()==null){
            holder.course_item_class_hours.setText("");
        } else{
            holder.course_item_class_hours.setText(courseList.get(position).getHours().toString()+"课时");
        }
        if(courseList.get(position).getPrice()==null||courseList.get(position).getHours()==null){
            holder.course_item_price.setText("¥ " +"");
        }else{
            double  p =  new Double(courseList.get(position).getPrice().toString()).doubleValue();
            int  n=Integer.valueOf(courseList.get(position).getHours().toString()).intValue();

            holder.course_item_price.setText("¥ " + p*n);
        }
        return convertView;
    }


    /** 存放控件 */

    public class ViewHolder {
        @ViewInject(R.id.course_item_img)
      private ImageView  course_item_img;//课程图片
        private TextView course_item_title;//课程标题
        private TextView  course_item_text;//课程介绍
        private TextView  course_item_price;//课程单价
        private TextView  course_item_class_hours;//课时

    }
}