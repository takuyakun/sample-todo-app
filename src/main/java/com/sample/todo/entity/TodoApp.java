package com.sample.todo.entity;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

/**
 * TODO_APPテーブルに該当するエンティティクラス<br>
 * JavaBeansのルールに従っています。
 */
public class TodoApp implements Serializable {
    /**
     * おまじない
     */
    private static final long serialVersionUID = 1L;

    private int todoId;
    @Length(max=30)
    private String category;
    @Length(max=30)
    private String title;
    @Length(max=100)
    private String detail;

    public TodoApp() {
    }

    public int getTodoId() {
        return this.todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
