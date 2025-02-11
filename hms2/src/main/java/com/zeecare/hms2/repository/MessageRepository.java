package com.zeecare.hms2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zeecare.hms2.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
	
}
