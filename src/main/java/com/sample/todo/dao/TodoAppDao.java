package com.sample.todo.dao;

import java.util.List;

import com.sample.todo.entity.TodoApp;
import com.sample.todo.entity.TodoAppRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

/**
 * データアクセスオブジェクト（DataAccessObject=Dao）<br>
 * データアクセス関連を記述するクラス
 */
@Component
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
        paramMap.addValue("category", category);
        paramMap.addValue("title", title);
        paramMap.addValue("detail", detail);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM TODO_APP WHERE ");
        boolean andFlg = false;
        /*boolean todoIdFlg = false;
		boolean categoryFlg = false;
		boolean titleFlg = false;
		boolean detailFlg = false;*/
		if (todoId != 0) {
			sql.append("TODO_ID LIKE :todoId ");
			//todoIdFlg = true;
			andFlg = true;
        }

		if (!"".equals(category) && category != null) {
			if (andFlg) sql.append(" AND ");
			sql.append("CATEGORY LIKE :category ");
			//categoryFlg = true;
			andFlg = true;
		}
		if (!"".equals(title) && title != null) {
			if (andFlg) sql.append(" AND ");
			sql.append("TITLE LIKE :title ");
			//titleFlg = true;
			andFlg = true;
		}
		if (!"".equals(detail) && detail != null) {
			if (andFlg) sql.append(" AND ");
			sql.append("DETAIL LIKE :detail ");
			//detailFlg = true;
			andFlg = true;
		}
		//Query query = manager.createQuery(sql.toString());
		//if (todoIdFlg) ((Object) query).setParameter("todoId", "%" + todoId + "%");
		//if (categoryFlg) query.setParameter("category", "%" + category + "%");
		//if (titleFlg) query.setParameter("title", "%" + title + "%");
		//if (detailFlg) query.setParameter("detail", "%" + detail + "%");
        List<TodoApp> result = jdbcTemplate.query(sql.toString(), paramMap, new TodoAppRowMapper());
        return result;
	}

}
