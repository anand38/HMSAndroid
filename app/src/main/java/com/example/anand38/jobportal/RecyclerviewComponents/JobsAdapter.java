package com.example.anand38.jobportal.RecyclerviewComponents;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anand38.jobportal.R;
import com.example.anand38.jobportal.bean.Job;

import java.util.List;



public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.MyViewHolder> {
    private List<Job> jobList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView position, salary, location;

        public MyViewHolder(View view) {
            super(view);
            position = (TextView) view.findViewById(R.id.position);
            salary = (TextView) view.findViewById(R.id.salary);
            location = (TextView) view.findViewById(R.id.location);
        }
    }

    public JobsAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.position.setText(job.getPosition());
        holder.salary.setText(job.getSalary());
        holder.location.setText(job.getLocation());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

}


