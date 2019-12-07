package JDBCDemo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class FindGradeWithPreparedStatement extends Application {

    private PreparedStatement preparedStatement;
    private TextField tfSIN = new TextField();
    private Label lblStatus = new Label();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        //Initialize database connection and create a statement object
        initializeDB();

        Button btShowgrade = new Button("Show Grade");
        HBox hBox = new HBox(10);
        hBox.setStyle("-fx-margin:20,10,10,10; " +
                "-fx-padding:10,10,10,10;" +
                "-fx-background-color:WHITE;" +
                "-fx-border-radius:10;" +
                "-fx-padding:5;" +
                "-fx-background-radius:10");
        hBox.getChildren().addAll(new Label("SIN"),tfSIN,(btShowgrade));
        btShowgrade.setStyle("-fx-font: 16 Verdana; " +
                "-fx-base: #cccc00;" +
                "-fx-border-radius:10;" +
                "-fx-padding:5,5,5,5;" +
                "-fx-background-radius:10;");
        tfSIN.setStyle("-fx-font: 14 Verdana;" +
                " -fx-base: #cccc00;" +
                "-fx-border-radius:10;" +
                "-fx-padding:5,5,5,5;" +
                "-fx-background-radius:10");
        tfSIN.setPrefWidth(200);
        VBox vBox = new VBox(10);
        btShowgrade.setMouseTransparent(false);
        vBox.setStyle("-fx-margin:20,10,10,10;" +
                "-fx-padding:10,10,10,10;" +
                "-fx-background-color:#ffff99;" +
                "-fx-border-radius:10;" +
                "-fx-padding:5;" +
                "-fx-background-radius:10;");
        vBox.getChildren().addAll(hBox,lblStatus);
        lblStatus.setStyle("-fx-font:18 Verdana;");
        tfSIN.setPrefColumnCount(5);

        //Register handler
        btShowgrade.setOnAction(e->showGrade());

        //routin for UI
        Scene scene = new Scene(vBox,420,300);
        primaryStage.setTitle("FindGrade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeDB() {
        try {
            //load jdbc driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            //for sqlserver
            //Class.forName(come.mysql.jdbc.Driver");
            System.out.println("Driver loaded.");

            //establish a connection
            Connection connection = DriverManager.getConnection(
                    "jdvc:oracle:thin:@oracle1.centennialcollege.ca:1521:SQLD",
                    "COMP214_F17_156",
                    "password");
            System.out.println("Database connected.");
            String queryString = "select firstName, mi, lastName, grade from Student where sin = ?";
            //create a statement
            preparedStatement = connection.prepareStatement(queryString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
     private void showGrade(){
        String sin = tfSIN.getText();
        try {
            preparedStatement.setString(1,sin);
            ResultSet rset = preparedStatement.executeQuery();

            if(rset.next()){
                String lastName = rset.getNString(1);
                String mi = rset.getString(2);
                String firstName = rset.getString(3);
                String grade = rset.getString(4);

                //display result in a label
                lblStatus.setText(firstName+" " +mi+" "+lastName+"'s grade is " +grade);
            }
            else {
                lblStatus.setText("Not found.");
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }
     }
}
