package com.todorest.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.todorest.model.Todo;
import com.todorest.model.User;
import com.todorest.repository.TodoRepository;
import com.todorest.request.TodoRequest;

@RestController
public class TodoController {

	@Autowired
	TodoRepository todoRepo;
	
	@GetMapping("/")
	public Map<String, String> welcome() {
		Map<String, String> obj = new HashMap<>();
		obj.put("msg", "Welcome to Todo App");
		return obj;
	}
	
	@GetMapping("/todo/all")
	public ResponseEntity<List<Todo>> getAllTodos() {
		List<Todo> todos = todoRepo.findAll();
		if(todos.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(todos, HttpStatus.OK);
	}
	
	@PostMapping("/todo/new")
	public ResponseEntity<Todo> createTodo(@RequestBody TodoRequest todoRequest) {
		Todo todo = new Todo();
		todo.setTask(todoRequest.getTask());
		todo.setDone(todoRequest.isDone());
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		todo.setUser(user);
		todoRepo.save(todo);
		return new ResponseEntity<>(todo, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/todo/delete/{id}")
	public ResponseEntity<Object> deleteTodo(@PathVariable("id") String id) {
		Map<String, String> obj = new HashMap<>();
		Long deleteId = Long.parseLong(id);
		Optional<Todo> todo = todoRepo.findById(deleteId);
		if(todo.isEmpty()) {
			obj.put("msg", "No such sodo");
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		todoRepo.deleteById(todo.get().getId());
		obj.put("msg", "Todo Deleted");
		return new ResponseEntity<>(null, HttpStatus.OK);
	}
	
	@PatchMapping("/todo/mark-done/{id}")
	public ResponseEntity<Object> markDone(@PathVariable String id) {
		Long doneId = Long.parseLong(id);
		Optional<Todo> todo = todoRepo.findById(doneId);
		if(todo.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		Todo newTodo = todo.get();
		newTodo.setDone(true);
		todoRepo.save(newTodo);
		return new ResponseEntity<>(null, HttpStatus.OK); 
	}
	
	@PatchMapping("/todo/mark-undone/{id}")
	public ResponseEntity<Object> markUnDone(@PathVariable String id) {
		Long doneId = Long.parseLong(id);
		Optional<Todo> todo = todoRepo.findById(doneId);
		if(todo.isEmpty()) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		Todo newTodo = todo.get();
		newTodo.setDone(false);
		todoRepo.save(newTodo);
		return new ResponseEntity<>(null, HttpStatus.OK); 
	}
	
	
}
