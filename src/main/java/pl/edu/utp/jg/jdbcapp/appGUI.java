package pl.edu.utp.jg.jdbcapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

public class appGUI implements Initializable {

    List<Employee> rs;
    ObservableList<Employee> ans;
    MyDoubleStringConverter mdsc = new MyDoubleStringConverter();

    @FXML
    private TableView<Employee> tblEmployees;

    @FXML
    private TableColumn<Employee, Integer> colID;

    @FXML
    private TableColumn<Employee, String> colName;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, Double> colSalary;

    @FXML
    void findAll() {

        Employee test = new Employee();
        rs = test.findAll();
        ans = FXCollections.observableArrayList(rs);
        ans.add(test);

        colID.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );
        colName.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        colEmail.setCellValueFactory(
                new PropertyValueFactory<>("email")
        );
        colSalary.setCellValueFactory(
                new PropertyValueFactory<>("salary")
        );

        tblEmployees.setItems(ans);

    }

    @FXML
    public void findOne(ActionEvent actionEvent) {

        TextInputDialog prompt = new TextInputDialog();
        prompt.setContentText("ID: ");
        prompt.setTitle("Find by ID");
        prompt.setHeaderText(null);
        Optional<String> promptAns = prompt.showAndWait();

        if(promptAns.isPresent()) {

            Employee test = new Employee();
            ans.clear();
            if(promptAns.get().equals("")) {
            } else {
                Optional<Employee> queryAns = test.findOne(Integer.parseInt(promptAns.get()));
                if(queryAns.isPresent()) {
                    ans.add(queryAns.get());
                }
            }
        }
    }

    @FXML
    public void delete(ActionEvent actionEvent) {

        if(!tblEmployees.getSelectionModel().isEmpty()) {

            Employee e = tblEmployees.getSelectionModel().getSelectedItem();

            if(e.getId() != null && e.findOne(e.getId()).isPresent()) {
                e.delete(e);
            }

            findAll();
        }
    }

    @FXML
    public void save(ActionEvent actionEvent) {

        Employee test = new Employee();

        for (Employee e: ans) {

            if(test.isEmpty(e)) {
                test.save(e);
            }
        }

        findAll();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colName.setCellFactory(forTableColumn());
        colName.setOnEditCommit(this::changeName);


        colSalary.setCellFactory(forTableColumn(mdsc));
        colSalary.setOnEditCommit(this::changeSalary);

        colEmail.setCellFactory(forTableColumn());
        colEmail.setOnEditCommit(this::changeEmail);

        findAll();
    }

    public void changeName(TableColumn.CellEditEvent<Employee,String> employeeStringCellEditEvent) {
        Employee e = tblEmployees.getSelectionModel().getSelectedItem();
        e.setName(employeeStringCellEditEvent.getNewValue());
    }

    public void changeEmail(TableColumn.CellEditEvent<Employee,String> employeeStringCellEditEvent) {

        String newMail = employeeStringCellEditEvent.getNewValue();

        Pattern p = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        Matcher m = p.matcher(newMail);
        boolean b = m.matches();

        if(b) {
            Employee e = tblEmployees.getSelectionModel().getSelectedItem();
            e.setEmail(employeeStringCellEditEvent.getNewValue());
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("Invalid email!");
            a.showAndWait();
        }
    }

    public void changeSalary(TableColumn.CellEditEvent<Employee,Double> employeeStringCellEditEvent) {
        Employee e = tblEmployees.getSelectionModel().getSelectedItem();
        if(mdsc.isFlagSalary()) {
            e.setSalary(employeeStringCellEditEvent.getNewValue());
        } else {
            e.setSalary(employeeStringCellEditEvent.getOldValue());
        }
    }

    @FXML
    public void findByName(ActionEvent actionEvent) {

        TextInputDialog prompt = new TextInputDialog();
        prompt.setContentText("Name: ");
        prompt.setTitle("Find by name");
        prompt.setHeaderText(null);
        Optional<String> promptAns = prompt.showAndWait();

        if(promptAns.isPresent()) {

            Employee test = new Employee();
            Optional<Employee> queryAns = test.findByNAme(promptAns.get());
            ans.clear();
            if(queryAns.isPresent()) {
                ans.add(queryAns.get());
            }

        }

    }
}
