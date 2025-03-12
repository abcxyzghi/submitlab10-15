package com.example.lab11;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.lab11.models.Trainee;

import java.util.ArrayList;

public class TraineesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Trainee> trainees;
    private LayoutInflater inflater;

    public int selectingIndex = -1;

    public TraineesAdapter(Context context, ArrayList<Trainee> trainees) {
        this.context = context;
        this.trainees = trainees;
        this.inflater = LayoutInflater.from(context);
    }

    public void setTrainees(ArrayList<Trainee> trainees) {
        this.trainees = trainees;
    }

    @Override
    public int getCount() {
        return trainees.size();
    }

    @Override
    public Object getItem(int i) {
        return trainees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = inflater.inflate(R.layout.activity_trainee_item, viewGroup, false);
        }

        Trainee trainee = trainees.get(index);

        TextView txtId = view.findViewById(R.id.txtId);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtEmail = view.findViewById(R.id.txtEmail);
        TextView txtPhone = view.findViewById(R.id.txtPhone);
        TextView txtGender = view.findViewById(R.id.txtGender);

        txtId.setText(String.valueOf(trainee.getId()));
        txtName.setText(trainee.getName());
        txtEmail.setText(trainee.getEmail());
        txtPhone.setText(trainee.getPhone());
        txtGender.setText(trainee.getGender());

        int backgroundColorId = (index == selectingIndex) ? R.color.colorItemSelected : android.R.color.transparent;

        view.setBackgroundColor(ContextCompat.getColor(context, backgroundColorId));

        return view;
    }

    public void setSelectedItemIndex(int index) {
        selectingIndex = index;
        notifyDataSetChanged();
    }
}
