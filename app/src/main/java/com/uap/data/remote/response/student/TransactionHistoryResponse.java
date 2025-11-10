package com.uap.data.remote.response.student;


import java.util.List;

public class TransactionHistoryResponse {
    private boolean success;
    private List<TransactionItem> data;

    public boolean isSuccess() {
        return success;
    }

    public List<TransactionItem> getData() {
        return data;
    }

    public static class TransactionItem {
        private String _id;
        private String studentId;
        private FeeId feeId;
        private String orderId;
        private long amount;
        private String status;        // Pending / Success / Failed
        private String paymentMethod; // VNPAY ...
        private String createdAt;

        public String get_id() { return _id; }
        public String getStudentId() { return studentId; }
        public FeeId getFeeId() { return feeId; }
        public String getOrderId() { return orderId; }
        public long getAmount() { return amount; }
        public String getStatus() { return status; }
        public String getPaymentMethod() { return paymentMethod; }
        public String getCreatedAt() { return createdAt; }

        public static class FeeId {
            private String _id;
            private SemesterId semesterId;

            public String get_id() { return _id; }
            public SemesterId getSemesterId() { return semesterId; }

            public static class SemesterId {
                private String _id;
                private String semesterName;

                public String get_id() { return _id; }
                public String getSemesterName() { return semesterName; }
            }
        }
    }
}
