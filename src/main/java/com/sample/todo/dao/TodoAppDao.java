package com.sample.todo.dao;

import java.util.List;

import com.sample.todo.entity.TodoApp;
import com.sample.todo.entity.TodoAppRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

/**
 * データアクセスオブジェクト（DataAccessObject=Dao）<br>
 * データアクセス関連を記述するクラス
 * @Repositoryと書いておくと、他からはは@Autowiredと記述すれば利用できる。Spring Beanという概念。
 */
@Repository
public class TodoAppDao {

    private static final String FIND_ALL_SQL = "SELECT * FROM TODO_APP";

    private static final String FIND_TODO_ID_SQL = "SELECT ISNULL(MAX(TODO_ID)+1, 1) FROM TODO_APP";

    private static final String INSERT_SQL = "INSERT INTO TODO_APP VALUES(:todoId, :category, :title, :detail)";

    private static final String FIND_ONE_SQL = "SELECT * FROM TODO_APP WHERE TODO_ID = :todoId";

    private static final String UPDATE_BY_KEY_SQL = "UPDATE TODO_APP SET CATEGORY = category, TITLE = :title, DETAIL = :detail WHERE TODO_ID = :todoId";

    private static final String DELETE_BY_KEY_SQL = "DELETE FROM TODO_APP WHERE(TODO_ID = :todoId)";

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public List<TodoApp> getTodoAppList() {
        List<TodoApp> resultList = jdbcTemplate.query(FIND_ALL_SQL, new MapSqlParameterSource(null),
                new TodoAppRowMapper());
        return resultList;
    }

    public int getNextId() {
        int maxTodoId = jdbcTemplate.queryForObject(FIND_TODO_ID_SQL, new MapSqlParameterSource(null), Integer.class);
        return maxTodoId;
    }

    public <T> void insert(int todoId, String category, String title, String detail) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("todoId", todoId);
        paramMap.addValue("category", category);
        paramMap.addValue("title", title);
        paramMap.addValue("detail", detail);
        jdbcTemplate.update(INSERT_SQL, paramMap);
    }

    public TodoApp findById(int todoId) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource().addValue("todoId", todoId);
        return jdbcTemplate.queryForObject(FIND_ONE_SQL, paramMap, new TodoAppRowMapper());
	}

	public int update(TodoApp todoApp) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(todoApp);
        return jdbcTemplate.update(UPDATE_BY_KEY_SQL, param);
    }

    public int delete(int todoId) {
        SqlParameterSource param = new MapSqlParameterSource().addValue("todoId", todoId);
        return jdbcTemplate.update(DELETE_BY_KEY_SQL, param);
    }

    public List<TodoApp> search(int todoId, String category, String title, String detail) {
        MapSqlParameterSource paramMap = new MapSqlParameterSource();
        paramMap.addValue("todoId", todoId);
        paramMap.addValue("category", "%" + category + "%");
        paramMap.addValue("title", "%" + title + "%");
        paramMap.addValue("detail", "%" + detail + "%");

		StringBuilder sql = new StringBuilder();// 条件に応じて変化するSQL文を作成
		sql.append("SELECT * FROM TODO_APP WHERE ");
        boolean andFlg = false;

		if (todoId != 0) {
			sql.append("TODO_ID LIKE :todoId ");
			andFlg = true;
        }
		if (!"".equals(category)) {
			if (andFlg) sql.append(" AND ");
			sql.append("CATEGORY LIKE :category ");
			andFlg = true;
		}
		if (!"".equals(title)) {
			if (andFlg) sql.append(" AND ");
			sql.append("TITLE LIKE :title ");
			andFlg = true;
		}
		if (!"".equals(detail)) {
			if (andFlg) sql.append(" AND ");
			sql.append("DETAIL LIKE :detail ");
			andFlg = true;
        }

        List<TodoApp> result = jdbcTemplate.query(sql.toString(), paramMap, new TodoAppRowMapper());
        return result;
	}

}
