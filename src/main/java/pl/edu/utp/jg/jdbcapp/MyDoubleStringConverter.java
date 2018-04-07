package pl.edu.utp.jg.jdbcapp;

import javafx.scene.control.Alert;
import javafx.util.converter.DoubleStringConverter;

public class MyDoubleStringConverter extends DoubleStringConverter {

    boolean flagSalary = false;

    @Override
    public Double fromString(String value) {

        try{
            flagSalary = true;
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText(null);
            a.setContentText("Wrong value!");
            a.showAndWait();
            flagSalary = false;
            return 0.0;
        }

    }

    public boolean isFlagSalary() {
        return flagSalary;
    }

    public void setFlagSalary(boolean flagSalary) {
        this.flagSalary = flagSalary;
    }
}
