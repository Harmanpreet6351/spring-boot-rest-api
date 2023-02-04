package com.todorest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.todorest.model.Todo;
import com.todorest.model.User;
import com.todorest.repository.TodoRepository;
import com.todorest.repository.UserRepository;

@SpringBootApplication
public class TodoRestApiApplication implements ApplicationRunner {

	@Autowired
	PasswordEncoder passEncoder;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TodoRepository todoRepo;
	
	public static void main(String[] args) {
		SpringApplication.run(TodoRestApiApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		User user = new User();
		user.setUsername("admin");
		user.setPassword(passEncoder.encode("admin"));
		userRepo.save(user);
		Todo todo = new Todo();
		todo.setTask("Go to Poop");
		todo.setDone(false);
		todo.setUser(user);
		todoRepo.save(todo);
	}

}
