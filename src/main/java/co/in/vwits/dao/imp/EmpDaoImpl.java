package co.in.vwits.dao.imp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import co.in.vwits.dao.EmpDao;
import co.in.vwits.model.Emp;

public class EmpDaoImpl implements EmpDao {

	public List<Emp> findAll() {
		List<Emp> students = new ArrayList<>();
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp_data", "root", "root");
				PreparedStatement pstmt = con.prepareStatement("SELECT * FROM tbl_employee ");) {

			ResultSet rs = pstmt.executeQuery();// firing query-
			while (rs.next()) { 
				Emp foundEmp = new Emp();
				foundEmp.setId(rs.getInt(1));
				foundEmp.setName(rs.getString(2));
				foundEmp.setSalary(rs.getDouble(3));
				students.add(foundEmp);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return students;
	}

	public void save(Emp e) {

		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp_data", "root", "root");
				PreparedStatement pstmt = con.prepareStatement("Insert into tbl_employee(name,salary) values (?,?)",
						Statement.RETURN_GENERATED_KEYS);) {

			pstmt.setString(1, e.getName());
			pstmt.setDouble(2, e.getSalary());
			int no = pstmt.executeUpdate();

			try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					e.setId(generatedKeys.getInt(1));
				} else {
					throw new SQLException("Creating Employee failed, no ID obtained.");
				}
			}

			System.out.println("Number of row added " + no);
			System.out.println(e);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public Optional<Emp> findById(int empId) {
		Emp emp = null;
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp_data", "root", "root");
				PreparedStatement pstmt = con.prepareStatement("select * from tbl_employee where id = ?");) {
			pstmt.setInt(1, empId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println(rs.getInt(1) + " " + rs.getString(2) + " " + rs.getDouble(3));
					emp = new Emp(rs.getInt(1), rs.getString(2), rs.getDouble(3));
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return Optional.ofNullable(emp);
	}

	public void deleteByEmpId(int empId) {
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp_data", "root", "root");
				PreparedStatement pstmt = con.prepareStatement("delete from tbl_employee where id = ?");) {
			pstmt.setInt(1, empId);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Record with ID " + empId + " deleted successfully.");
			} else {
				System.out.println("No record found with ID " + empId + ". Nothing deleted.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void updateByEmpId(int empId, double modifiedSalary) {
		try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/emp_data", "root", "root");
				PreparedStatement pstmt = con.prepareStatement("UPDATE tbl_employee SET salary = ? WHERE id = ?");) {
			pstmt.setDouble(1, modifiedSalary);
			pstmt.setInt(2, empId);
			int rowsAffected = pstmt.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Record with ID " + empId + " updated successfully.");
			} else {
				System.out.println("No record found with ID " + empId + ". Nothing updated.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

}
