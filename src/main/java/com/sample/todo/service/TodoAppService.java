package com.sample.todo.service;

import java.util.List;

import com.sample.todo.dao.TodoAppDao;
import com.sample.todo.entity.TodoApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoAppService {

    @Autowired
    private TodoAppDao dao;

    public List<TodoApp> getTodoAppList() {
        return dao.getTodoAppList();
    }
}
