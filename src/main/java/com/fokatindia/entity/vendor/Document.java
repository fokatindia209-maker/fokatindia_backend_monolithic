package com.fokatindia.entity.vendor;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("documents")
public class Document {

    @Id
    private Long documentId;

    // user from users table
    private Long userId;

    // AADHAAR, PAN, GST, POLICE_VERIFICATION
    private String documentType;


    private String documentUrl;

    // PENDING, APPROVED, REJECTED
    private String status;

    private String remarks;

    private LocalDateTime uploadedAt;

    private LocalDateTime verifiedAt;
}