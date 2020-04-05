package com.example.recyclerview;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ProgrammingAdapter extends RecyclerView.Adapter<ProgrammingAdapter.ProgramingViewHolder> {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    int pos;
    String TAG="MyTag";
    HashMap<Integer,String> data;
     CityAdapterEvents cityAdapterEvents;
    public ProgrammingAdapter(CityAdapterEvents cityAdapterEvents ,HashMap<Integer,String> data) {
        this.data=data;
        this.cityAdapterEvents=cityAdapterEvents;
    }

    @Override
    public int getItemViewType(int position) {
        pos=position;
        Log.d(TAG, "getItemViewType: --------");
        if (data.get(position).split("#")[0].equals("TRUE")){
            return VIEW_TYPE_MESSAGE_SENT;
        }
        else{
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ProgramingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//execution order 2
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        Log.d(TAG, "onCreateViewHolder: data size="+(data.size()-1)+" value="+data.get(data.size()-1));
        View view;
        if (viewType==VIEW_TYPE_MESSAGE_SENT){
            view=inflater.inflate(R.layout.list_item_msg_send,parent,false);

        }
        else {
            view=inflater.inflate(R.layout.list_item_message_recv,parent,false);

        }



        return new ProgramingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramingViewHolder holder, int position) {//execution order 4
        Log.d(TAG, "onBindViewHolder: "+(data.size()-1)+" "+data.get(data.size()-1));
        String bbb[]=data.get(pos).split("#");
        holder.textView.setText(bbb[1]);

    }

    @Override
    public int getItemCount() {//execution order 1
        return data.size();
    }

    protected class ProgramingViewHolder extends RecyclerView.ViewHolder {// implements View.OnClickListener , View.OnLongClickListener {
        ImageView imageView;
        TextView textView;
        public ProgramingViewHolder(@NonNull View itemView) {//execution order 3
            super(itemView);
            Log.d(TAG, "ProgramingViewHolder: "+(data.size()-1)+" "+data.get(data.size()-1));
            textView=itemView.findViewById(R.id.text_message);
        }


    }
    public interface CityAdapterEvents{

        void onCityClicked(int position);
    }

}
