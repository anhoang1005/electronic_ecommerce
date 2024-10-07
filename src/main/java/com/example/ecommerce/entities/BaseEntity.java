package com.example.ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
	
	@CreatedBy
	@Column(name = "created_by", length = 100)
	private String createdBy;
	
	@CreatedDate
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime createdAt;
	
	@LastModifiedBy
	@Column(name = "modified_by", length = 100)
	private String modifiedBy;
	
	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime modifiedAt;

}
