package com.bank.entities;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.bank.enums.OperationType;

public class AccountOperation 
{
	 @Id 
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id; 
	 private Date operationDate;
	 private double amount;
	 private OperationType type;
	 private String description;

	 @ManyToOne
	 @OnDelete(action = OnDeleteAction.CASCADE)
	  private BankAccount bankAccount;

}
