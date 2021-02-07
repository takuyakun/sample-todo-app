package com.sample.todo.controller;

import java.util.List;

import com.sample.todo.entity.TodoApp;
import com.sample.todo.service.TodoAppService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ブラウザからのリクエストはここにくる
 */
@Controller
public class TodoAppController {

    @Autowired
    private TodoAppService service;

    /**
     * valueの部分がURL<br>
     * POSTを許可しているのは、{@code #register(TodoApp, Model)} からredirectしてくるため
     */
    @RequestMapping(value = { "/", "index" }, method = { RequestMethod.GET, RequestMethod.POST })
    String index(Model model) {
        List<TodoApp> todoList = service.getTodoAppList();
        model.addAttribute("todoList", todoList);// ここの"todoList"というキーがindex.htmlで参照されている
        return "index";// resources/index.htmlを指している
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    String add(@ModelAttribute("newForm") TodoApp todoApp) {
        return "detail";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    String register(@Validated @ModelAttribute("newForm") TodoApp todoApp, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "detail";
        }
        service.register(todoApp.getCategory(), todoApp.getTitle(), todoApp.getDetail());
        return "redirect:index";// 登録したらindexに移る
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    String edit(@ModelAttribute("editForm") TodoApp todoApp, @RequestParam(value = "todoId") int todoId, Model model) {
        TodoApp editForm = service.findOne(todoId);
        model.addAttribute("editForm", editForm);// ここの"editForm"というキーがedit.htmlで参照されている
        return "edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    String update(@Validated @ModelAttribute("editForm") TodoApp todoApp, BindingResult bindingResult, @RequestParam int todoId, Model model) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        todoApp.setTodoId(todoId);
        service.update(todoApp);
        return "redirect:index";
    }

    @RequestMapping(value = "/delete", method = { RequestMethod.GET, RequestMethod.POST })
    String destroy(@RequestParam int todoId, Model model) {
        service.delete(todoId);
        return "redirect:index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
	public String search(@ModelAttribute("searchForm") TodoApp todoApp, BindingResult bindingResult, Model model) {
		List<TodoApp> todoResult = service.search(todoApp.getTodoId(),todoApp.getCategory(), todoApp.getTitle(), todoApp.getDetail());
        model.addAttribute("todoList", todoResult);
        return "index";
    }

}