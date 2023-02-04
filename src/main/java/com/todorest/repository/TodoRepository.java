package com.todorest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todorest.model.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	
}
