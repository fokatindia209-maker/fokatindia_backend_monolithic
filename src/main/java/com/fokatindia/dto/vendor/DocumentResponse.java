package com.fokatindia.dto.vendor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentResponse {
    private Long documentId;
    private Long userId;
    private String documentType;
    private String documentUrl;
    private String status;
    private String remarks;
    private LocalDateTime uploadedAt;
    private LocalDateTime verifiedAt;
}
