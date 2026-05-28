package com.fokatindia.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tokens")
public class Token {
    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("jwt_token")
    private String jwtToken;

    @Column("fcm_token")
    private String fcmToken;
}
