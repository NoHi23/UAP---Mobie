package com.uap.data.remote;

import com.uap.data.remote.request.LoginRequest;
import com.uap.data.remote.request.student.StudentEvaluateRequest;
import com.uap.data.remote.request.UpdateProfileRequest;
import com.uap.data.remote.request.student.SubmitStudentRequest;
import com.uap.data.remote.request.teacher.CreateAnnouncementRequest;
import com.uap.data.remote.request.teacher.UpdateAttendanceRequest;
import com.uap.data.remote.request.teacher.UpdateLecturerProfileRequest;
import com.uap.data.remote.response.BasicResponse;
import com.uap.data.remote.response.teacher.CreateAnnouncementResponse;
import com.uap.data.remote.response.teacher.LecturerClassesResponse;
import com.uap.data.remote.response.teacher.LecturerProfileResponse;
import com.uap.data.remote.response.teacher.LecturerScheduleResponse;
import com.uap.data.remote.response.teacher.ScheduleAnnouncementResponse;
import com.uap.data.remote.response.LoginResponse;
import com.uap.data.remote.response.UpdateProfileResponse;
import com.uap.data.remote.response.student.AnnouncementResponse;
import com.uap.data.remote.response.student.RequestResponse;
import com.uap.data.remote.response.student.StudentClassListResponse;
import com.uap.data.remote.response.student.StudentClassmateResponse;
import com.uap.data.remote.response.student.StudentEvaluateResponse;
import com.uap.data.remote.response.student.StudentGradeResponse;
import com.uap.data.remote.response.student.StudentNotificationResponse;
import com.uap.data.remote.response.student.StudentExamResponse;
import com.uap.data.remote.response.student.StudentTimetableResponse;
import com.uap.data.remote.response.student.TransactionHistoryResponse;
import com.uap.data.remote.response.teacher.ScheduleStudentsResponse;
import com.uap.domain.model.StudentProfile;
import com.uap.data.remote.response.student.StudentAttendanceResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SOService {
    @POST("account/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @GET("student/profile")
    Call<StudentProfile> getStudentProfile();

    @PUT("student/profile")
    Call<UpdateProfileResponse> updateStudentProfile(@Body UpdateProfileRequest body);

    @GET("student/schedules/my-week")
    Call<StudentTimetableResponse> getStudentTimetable();

    @GET("student/exams")
    Call<StudentExamResponse> getStudentExams();

    @GET("student/notifications/slot/{slotId}")
    Call<StudentNotificationResponse> getSlotNotifications(@Path("slotId") String slotId);

    @GET("student/classes/{classId}/classmates")
    Call<StudentClassmateResponse> getClassmates(@Path("classId") String classId);

    @GET("student/grades")
    Call<StudentGradeResponse> getStudentGrades();

    @GET("student/attendance/summary")
    Call<StudentAttendanceResponse> getStudentAttendance();

    @GET("student/evaluations/classes-to-review")
    Call<StudentClassListResponse> getStudentClasses();

    @POST("student/evaluations")
    Call<StudentEvaluateResponse> sendStudentEvaluation(@Body StudentEvaluateRequest body);

    @GET("announcements")
    Call<AnnouncementResponse> getStudentAnnouncements();

    @GET("student/tuition/transactions")
    Call<TransactionHistoryResponse> getStudentTransactions();

    @GET("student/requests/me")
    Call<RequestResponse> getStudentRequests();

    @POST("student/requests")
    Call<Void> submitStudentRequest(@Body SubmitStudentRequest body);


    @GET("lecturer/notifications/slots")
    Call<ScheduleAnnouncementResponse> getScheduleAnnouncements(@Query("scheduleId") String scheduleId);

    @POST("lecturer/notifications/slots")
    Call<CreateAnnouncementResponse> createScheduleAnnouncement(
            @Body CreateAnnouncementRequest body
    );

    @GET("lecturer/studentsbyclass/{scheduleId}")
    Call<ScheduleStudentsResponse> getScheduleStudents(@Path("scheduleId") String scheduleId);

    @POST("lecturer/attendance/mark")
    Call<BasicResponse> updateStudentAttendance(@Body UpdateAttendanceRequest body);

    @POST("lecturer/schedules/my-week")
    Call<LecturerScheduleResponse> getLecturerSchedules();


    @GET("lecturer/profile")
    Call<LecturerProfileResponse> getLecturerProfile();

    @PUT("lecturer/profile")
    Call<BasicResponse> updateLecturerProfile(@Body UpdateLecturerProfileRequest body);

    @GET("lecturer/classes-by-semester")
    Call<LecturerClassesResponse> getLecturerClasses();
}
