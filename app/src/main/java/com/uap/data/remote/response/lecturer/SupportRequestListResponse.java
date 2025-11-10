package com.uap.data.remote.response.lecturer;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SupportRequestListResponse {

    private boolean success;
    private String message;
    private List<SupportRequest> data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public List<SupportRequest> getData() { return data; }

    public static class SupportRequest {
        @SerializedName("_id")
        private String id;
        private String accountId;
        private String request;
        private String status;
        private String createdAt;

        public String getId() { return id; }
        public String getAccountId() { return accountId; }
        public String getRequest() { return request; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
    }
}
