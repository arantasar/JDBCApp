package pl.edu.utp.jg.jdbcapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements EmployeeDAO {

    private Integer id = null;
    private String name, email;
    private Double salary = null;

    public Employee(String name, String email, Double salary) {
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

     public Optional<Employee> findOne(Integer id) {

         Optional<Employee> ans = Optional.empty();

         String sql = "SELECT * FROM employee WHERE ID = ?";

         try(Connection conn = DBUtil.connectToDB(DBType.H2);
             PreparedStatement pstmt = conn.prepareStatement(sql);
         ) {

             pstmt.setInt(1, id);
             ResultSet rs = pstmt.executeQuery();

             if(rs.next()) {

                 ans = Optional.of(new Employee(rs.getInt("ID"), rs.getString("Name"),
                         rs.getString("Email"), rs.getDouble("Salary")));
             }

         } catch (SQLException e) {
             e.printStackTrace();
         }

         return ans;
     }

    public List<Employee> findAll() {

        List<Employee> result = new ArrayList<>();
        String sql = "SELECT * FROM employee";

        try(Connection conn = DBUtil.connectToDB(DBType.H2);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
        ) {

            while(rs.next()) {

                String name = rs.getString("Name");
                String email = rs.getString("Email");
                int id = rs.getInt("ID");
                Double salary = rs.getDouble("Salary");

                result.add(new Employee(id, name, email, salary));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Optional<Employee> findByNAme(String name) {

        Optional<Employee> ans = Optional.empty();

        String sql = "SELECT * FROM employee WHERE Name = ?";

        try(Connection conn = DBUtil.connectToDB(DBType.H2);
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {

                ans = Optional.of(new Employee(rs.getInt("ID"), rs.getString("Name"),
                        rs.getString("Email"), rs.getDouble("Salary")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ans;

    }

    public boolean isEmpty(Employee employee) {

        boolean ans = true;

        if(employee.getId() == null
                && employee.getEmail() == null
                && employee.getName() == null
                && employee.getSalary() == null) {
            ans = false;
        }

        return ans;
    }

    public void delete(Employee employee) {

        if(findOne(employee.getId()).isPresent()) {

            String sql = "DELETE FROM Employee WHERE ID = ?";
            try (Connection conn = DBUtil.connectToDB(DBType.H2);
                 PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {
                pstmt.setInt(1, employee.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(Employee employee) {

        Optional<Employee> tmp = Optional.empty();
        String sql;

        if(employee.getId() != null) {

            tmp = findOne(employee.getId());
        }

        if(!tmp.isPresent()) {

            sql = "INSERT INTO Employee (Name, Email, Salary) VALUES (?, ?, ?)";

            try(Connection conn = DBUtil.connectToDB(DBType.H2);
                PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {

                String name = employee.getName() == null ? "" : employee.getName();
                String email = employee.getEmail() == null ? "" : employee.getEmail();
                Double salary = employee.getSalary() == null ? 0 : employee.getSalary();

                pstmt.setString(1, name);
                pstmt.setString(2, email);
                pstmt.setDouble(3, salary);

                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {

            sql = "UPDATE Employee SET Name = ?, Email = ?, Salary = ? WHERE ID = ?";

            try(Connection conn = DBUtil.connectToDB(DBType.H2);
                PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {

                pstmt.setString(1, employee.getName());
                pstmt.setString(2, employee.getEmail());
                pstmt.setDouble(3, employee.getSalary());
                pstmt.setInt(4, employee.getId());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
