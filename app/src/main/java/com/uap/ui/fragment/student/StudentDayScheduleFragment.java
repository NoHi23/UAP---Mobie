package com.uap.ui.fragment.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uap.R;
import com.uap.domain.model.TimetableItem;
import com.uap.ui.adapters.student.StudentScheduleAdapter;

import java.util.ArrayList;
import java.util.List;

public class StudentDayScheduleFragment extends Fragment {

    private static final String ARG_LIST = "arg_list";
    private ArrayList<TimetableItem> items;

    public static StudentDayScheduleFragment newInstance(List<TimetableItem> listForDay) {
        StudentDayScheduleFragment f = new StudentDayScheduleFragment();
        Bundle b = new Bundle();
        b.putSerializable(ARG_LIST, new ArrayList<>(listForDay));
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_student_day_schedule, container, false);
        RecyclerView rv = v.findViewById(R.id.recyclerView);
        TextView empty = v.findViewById(R.id.txtEmpty);

        items = (ArrayList<TimetableItem>) getArguments().getSerializable(ARG_LIST);
        if (items == null) items = new ArrayList<>();

        if (items.isEmpty()) {
            empty.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(new StudentScheduleAdapter(items));
        }

        return v;
    }
}
