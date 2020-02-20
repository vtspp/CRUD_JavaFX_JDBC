package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection connection;
	
	public DepartmentDaoJDBC(Connection connection) {
		this.connection = connection;
	}
	
	private Department instantiateDepartement() {
		Department obj = new Department();
		obj.getId();
		obj.getName();
		return obj;
	}

	@Override
	public void insert(Department obj) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(
					"INSERT INTO department "
					+ "(Name) "
					+ "VALUES "
					+ "(?)", Statement.RETURN_GENERATED_KEYS);
			
			preparedStatement.setString(1, obj.getName());
			
			Integer rowsAffected = preparedStatement.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()) {
					Integer id = resultSet.getInt(1);
					obj.setId(id);
				}
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(
					"UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			preparedStatement.setString(1, obj.getName());
			preparedStatement.setInt(2, obj.getId());
			
			preparedStatement.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}
	
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(
					"DELETE FROM department "
					+ "WHERE Id = ?");
			preparedStatement.setInt(1, id);
			
			preparedStatement.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT * FROM department "
					+ "WHERE Id = ?");
			preparedStatement.setInt(1, id);
			
			resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Department department = instantiateDepartement();
				department.setId(resultSet.getInt("Id"));
				department.setName(resultSet.getString("Name"));
				return department;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}
		
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		List<Department> list = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(
					"SELECT * FROM department "
					+ "ORDER BY Name");
			
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				Department department = instantiateDepartement();
				department.setId(resultSet.getInt("Id"));
				department.setName(resultSet.getString("Name"));
				list.add(department);
			
			}
			return list;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(preparedStatement);
		}

	}

}
