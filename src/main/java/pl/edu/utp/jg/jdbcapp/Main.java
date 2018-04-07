package pl.edu.utp.jg.jdbcapp;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {

    public static void main(String[] args) {

        try(Connection conn = DBUtil.connectToDB(DBType.H2)) {
            DBUtil.createDB();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("appGUI.fxml"));

        primaryStage.setTitle("Employee DB");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> DBUtil.close());


    }


}
