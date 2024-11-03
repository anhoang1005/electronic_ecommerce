package com.example.ecommerce.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
	
	@CreatedBy
	@Column(name = "created_by", length = 100, updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String createdBy;
	
	@CreatedDate
	@Column(columnDefinition = "TIMESTAMP", updatable = false)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private LocalDateTime createdAt;
	
	@LastModifiedBy
	@Column(name = "modified_by", length = 100)
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String modifiedBy;
	
	@LastModifiedDate
	@Column(columnDefinition = "TIMESTAMP")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private LocalDateTime modifiedAt;

}
