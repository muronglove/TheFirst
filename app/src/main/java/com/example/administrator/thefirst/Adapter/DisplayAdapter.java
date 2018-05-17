package com.example.administrator.thefirst.Adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thefirst.Classification.ClassAdapter;
import com.example.administrator.thefirst.Classification.Classification;
import com.example.administrator.thefirst.DisplayActivity;
import com.example.administrator.thefirst.Interface.ItemTouchHelperAdapter;
import com.example.administrator.thefirst.LoginActivity;
import com.example.administrator.thefirst.R;
import com.example.administrator.thefirst.helper.MyDatabaseHelper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    //数据
    List<Map<String,Object>> mData;
    Context mContext;


    private OnItemClickListener mOnItemClickListener = new OnItemClickListener();

    public class OnItemClickListener{
        void OnItemClickListener(int position){
            try{Dialog dia = new Dialog(mContext, R.style.edit_AlertDialog_style);
            dia.setContentView(R.layout.activity_dialog);
            ImageView imageView = (ImageView) dia.findViewById(R.id.dialog_image);
            imageView.setImageBitmap((Bitmap) mData.get(position).get("imageBitmap"));
            TextView textView = (TextView) dia.findViewById(R.id.dialog_caption);
            textView.setText((String)mData.get(position).get("caption"));
            dia.show();

            //选择true的话点击其他地方可以使dialog消失，为false的话不会消失
            dia.setCanceledOnTouchOutside(true); // Sets whether this dialog is
            Window w = dia.getWindow();
            WindowManager.LayoutParams lp = w.getAttributes();
            lp.x = 0;
            lp.y = 40;
            dia.onWindowAttributesChanged(lp);}catch (Exception e){
                e.printStackTrace();
            }
        }
    }
//    public void setOnItemClickListener(OnItemClickListener listener){
//        mOnItemClickListener=listener;
//    }

    public DisplayAdapter(List<Map<String,Object>> list,Context context){
        mData=list;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_item,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        try{Map map = mData.get(position);
        String caption = (String)map.get("caption");
        String label = (String)map.get("label");
        String number = (String)map.get("number");
        String color = (String)map.get("color");
        String str_position = (String)map.get("position");
        String date = (String)map.get("date");
        Bitmap image = (Bitmap)map.get("imageBitmap");

        holder.caption.setText(caption);
        holder.label.setText(label);
        holder.number.setText(number);
        holder.color.setText(color);
        holder.position.setText(str_position);
        holder.date.setText(date);
        holder.image.setImageBitmap(image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.OnItemClickListener(position);
            }
        });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView caption;
        TextView label;
        TextView number;
        TextView color;
        TextView position;
        TextView date;
        ImageView image;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            try{cardView=(CardView) itemView;
            caption = itemView.findViewById(R.id.caption);
            label = itemView.findViewById(R.id.label);
            number = itemView.findViewById(R.id.number);
            color = itemView.findViewById(R.id.color);
            position = itemView.findViewById(R.id.position);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);}catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mData,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        MyDatabaseHelper dbHelper = MyDatabaseHelper.getInstance(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("a",1);
        db.update("storage",values,"uuid = ?",new String[]{(String)mData.get(position).get("uuid")});
        mData.remove(position);
        notifyItemRemoved(position);
    }
}
