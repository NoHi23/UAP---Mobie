package com.example.uapmobie;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ExamScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewExams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạm thời, chúng ta sẽ tạo một danh sách rỗng.
        // Sau này, bạn sẽ kết nối Adapter và dữ liệu thật ở đây.
        // List<Exam> examList = new ArrayList<>();
        // ExamAdapter adapter = new ExamAdapter(examList);
        // recyclerView.setAdapter(adapter);
    }
}