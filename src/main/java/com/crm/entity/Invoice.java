package com.crm.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.crm.enums.PaymentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String invoiceNumber;

	private Long customerId;
	private String customerName;
	private String customerAddress;

	private Double amount;
	private Double discount;
	private Double gst;
	private Double finalAmount;

	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;

	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<InvoiceItem> items;

	@PrePersist
	public void prePersist() {
		this.createdAt = LocalDateTime.now();
	}
}
