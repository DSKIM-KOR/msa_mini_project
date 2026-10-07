package com.example.msa_db_sep_hongkd.common.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class BaseTimeEntity {
    @CreationTimestamp
    private LocalDateTime createdTime;

    @UpdateTimestamp
    private LocalDateTime updatedTime;
}
