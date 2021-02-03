package com.sample.todo.service;

import java.util.ArrayList;
import java.util.List;

import com.sample.todo.dao.TodoAppDao;
import com.sample.todo.entity.TodoApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ロジックを記述するクラス<br>
 *
 * @Componentと書いておくと、他からはは@Autowiredと記述すれば利用できる。Spring Beanという概念。
 */
@Component
public class TodoAppService {

    /**
     * TodoAppDaoは@Componentを持っているので、@Autowiredで利用できる（裏でSpringがこっそりセットしています）
     */
    @Autowired
    private TodoAppDao dao;

    public List<TodoApp> getTodoAppList() {
        return dao.getTodoAppList();
    }

    public void register(String category, String title, String detail) {
        int nextId = dao.getNextId();
        dao.insert(nextId, category, title, detail);
    }

    public TodoApp findOne(int todoId) {
        return dao.findById(todoId);
    }

	public int update(TodoApp todoApp) {
        return dao.update(todoApp);
	}

    public void delete(int todoId) {
        dao.delete(todoId);
    }

    public List<TodoApp> search(int todoId, String category, String title, String detail) {
        List<TodoApp> result = new ArrayList<TodoApp>();
		if (todoId == -1 && "".equals(category) && "".equals(title) && "".equals(detail)) {
			result = dao.getTodoAppList();
		} else {
			result = dao.search(todoId, category, title, detail);
		}
		return result;
	}
}
