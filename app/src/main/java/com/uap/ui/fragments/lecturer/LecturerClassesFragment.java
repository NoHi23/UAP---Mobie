package com.uap.ui.fragments.lecturer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.uap.App;
import com.uap.R;
import com.uap.data.remote.ApiService;
import com.uap.data.remote.response.lecturer.LecturerClassesResponse;
import com.uap.ui.adapters.lecturer.ClassesAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LecturerClassesFragment extends Fragment {

    private RecyclerView recyclerView;
    private ClassesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lecturer_classes, container, false);
        recyclerView = v.findViewById(R.id.rvClasses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ClassesAdapter(new ArrayList<>(), this::openClassStudents);
        recyclerView.setAdapter(adapter);

        loadClasses();
        return v;
    }

    private void loadClasses() {
        ApiService.getSOService().getLecturerClasses()
                .enqueue(new Callback<LecturerClassesResponse>() {
                    @Override
                    public void onResponse(Call<LecturerClassesResponse> call, Response<LecturerClassesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setData(response.body().getData());
                        } else {
                            Toast.makeText(App.getContext(), "Không có dữ liệu lớp học", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LecturerClassesResponse> call, Throwable t) {
                        Toast.makeText(App.getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openClassStudents(LecturerClassesResponse.ClassInfo cls) {
        ClassStudentsBottomSheetFragment sheet = ClassStudentsBottomSheetFragment.newInstance(cls.getClassId(), cls.getClassName());
        sheet.show(getParentFragmentManager(), "students");
    }
}
