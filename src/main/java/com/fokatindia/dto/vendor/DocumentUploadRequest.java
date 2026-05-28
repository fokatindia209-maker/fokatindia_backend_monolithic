package com.fokatindia.dto.vendor;

import lombok.Data;
import org.springframework.http.codec.multipart.FilePart;

@Data
public class DocumentUploadRequest {
    private Long userId;

    // AADHAAR, PAN, GST, POLICE_VERIFICATION
    private String documentType;
    private FilePart file;
}
