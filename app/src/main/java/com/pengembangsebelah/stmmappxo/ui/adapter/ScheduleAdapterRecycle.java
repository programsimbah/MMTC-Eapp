package com.pengembangsebelah.stmmappxo.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pengembangsebelah.stmmappxo.R;
import com.pengembangsebelah.stmmappxo.model.ProgramModel;
import com.pengembangsebelah.stmmappxo.model.ScheduleProgram;

import java.util.List;

class ScheduleAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    RecyclerView recyclerView;
    TextView title;

    public ScheduleAdapterHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.title_item_vertical_view);
        recyclerView=itemView.findViewById(R.id.recyleview_item_vertical_view);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

public class ScheduleAdapterRecycle extends RecyclerView.Adapter<ScheduleAdapterHolder>
{
    List<ScheduleProgram> topChartData;
    Context context;
    public ScheduleAdapterRecycle(List<ScheduleProgram> topChartData, Context context) {
        this.topChartData=topChartData;
        this.context=context;
    }

    @NonNull
    @Override
    public ScheduleAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_vertical_view,viewGroup,false);
        return new ScheduleAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapterHolder topChartAdapterHolder, int i) {
        String[] DAY = topChartData.get(i).day.split("");
        String c= "\n";
        StringBuilder d= new StringBuilder();
        for (int f=0;f<DAY.length;f++){
            d.append(DAY[f]).append(c);
        }

        topChartAdapterHolder.title.setText(d.toString());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        topChartAdapterHolder.recyclerView.setLayoutManager(layoutManager);

        ProgramAdapterRecycle adapterRecycle = new ProgramAdapterRecycle(topChartData.get(i).programModels ,context);
        topChartAdapterHolder.recyclerView.setAdapter(adapterRecycle);
//        adapterRecycle.setHasStableIds(true);

    }

    @Override
    public int getItemCount() {
        if(topChartData!=null)
        return topChartData.size();
        else return 0;
    }
}

class ProgramAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{

    TextView title;
    TextView number;
    public ProgramAdapterHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.item_title);
        number = itemView.findViewById(R.id.item_subtitle);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

class ProgramAdapterRecycle extends RecyclerView.Adapter<ProgramAdapterHolder>{
    List<ProgramModel> audioData;
    Context context;

    public ProgramAdapterRecycle(List<ProgramModel> audioData, Context context) {
        this.audioData=audioData;
        this.context=context;
    }

    @NonNull
    @Override
    public ProgramAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule,viewGroup,false);
        return new ProgramAdapterHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProgramAdapterHolder albumAdapterHolder, int i) {

        albumAdapterHolder.number.setText(audioData.get(i).startO+" - "+audioData.get(i).endO);
        albumAdapterHolder.title.setText(audioData.get(i).program);
    }

    @Override
    public int getItemCount() {
        if(audioData!=null) {
            return audioData.size();
        }else return 0;
    }
}
