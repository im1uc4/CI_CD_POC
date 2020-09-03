package sg.com.ncs.brain.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonSetter;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Audit implements Serializable {

	private static final long serialVersionUID = 667567978704837495L;

	@CreatedDate
// @Column(name = "createdAt", nullable = false, updatable = false)
	@Column(name = "createdAt", updatable = false)
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime createdAt;

	@LastModifiedDate
	//@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatedAt")
	private LocalDateTime updatedAt;

	@CreatedBy
	@JsonSetter("created_by")
	@Column(name = "created_by")
	private String createdBy;

	@LastModifiedBy
	@Column(name = "updated_by")
	@JsonSetter("updated_by")
	private String updatedBy;
}