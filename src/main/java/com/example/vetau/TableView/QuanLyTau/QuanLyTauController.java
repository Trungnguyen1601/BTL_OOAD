package com.example.vetau.TableView.QuanLyTau;

import com.example.vetau.Show.Button_Extend;
import com.example.vetau.Show.Show_Window;
import com.example.vetau.helpers.Database;
import com.example.vetau.models.ChitietTau;
import com.example.vetau.models.Tau;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class QuanLyTauController implements Initializable {

    @FXML
    private TableColumn<ChitietTau, Button> Delete_col;

    @FXML
    private TableColumn<ChitietTau, Button> Detail_col;

    @FXML
    private TableColumn<ChitietTau, String> ID_Tau_col;

    @FXML
    private ComboBox<String> Loaitoa_id_combobox;

    @FXML
    private TableColumn<ChitietTau,Integer> Soluongghe_col;

    @FXML
    private TextField Soluongtoa_id;

    @FXML
    private TableView<ChitietTau> Tau_table;

    @FXML
    private TableColumn<ChitietTau, String> Toa_col;

    @FXML
    private ComboBox<String> Toa_id_combobox;

    @FXML
    private TableColumn<ChitietTau, Button> Update_col;

    @FXML
    private Label chucnang_id;

    @FXML
    private Button clear_btn_add_id;

    @FXML
    private Button clear_btn_search_id;

    @FXML
    private Button dashbroard_form_btn;

    @FXML
    private Button insert_btn_id;

    @FXML
    private TableColumn<ChitietTau, String> loaitoa_col;

    @FXML
    private Button passenger_form_btn;

    @FXML
    private Button quanlytau_close;

    @FXML
    private Button quanlytau_form_btn;

    @FXML
    private Button quanlytau_minus;

    @FXML
    private Button quanlytau_phongto;

    @FXML
    private Button search_btn_id;

    @FXML
    private Button signout_btn;

    @FXML
    private TextField tau_add_id;

    @FXML
    private ComboBox<Tau> tau_id_combobox;

    @FXML
    private Button train_mana_form_btn;


    String query = null;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    Tau tau = null;

    public static ChitietTau chitietTau = new ChitietTau();

    ObservableList<ChitietTau> TauList = FXCollections.observableArrayList();
    ObservableList<Button_Extend> Button_List = FXCollections.observableArrayList();
    @FXML
    void Clear_Click(MouseEvent event) {
        tau_id_combobox.setValue(null);
        Toa_id_combobox.setValue(null);
        Loaitoa_id_combobox.setValue(null);
    }

    @FXML
    void Search_click(MouseEvent event) {
        Search_Tau();
    }
    @FXML
    void Refresh_click(MouseEvent event) {
        loadData();
    }
    @FXML
    void Insert_tau_click(MouseEvent event) {
        String ID_tau = tau_add_id.getText();
        String SLtoa = Soluongtoa_id.getText();
        Tau tau_insert = new Tau(ID_tau,Integer.parseInt(SLtoa));
        chitietTau.setTau(tau_insert);
        String FXMLPATH_Them_tau = "/DashBroard/Quanlytau/ThemTau/themtau.fxml";
        try {
            Show_Window showWindow =new Show_Window();
            showWindow.Show(FXMLPATH_Them_tau);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void quanlytau_close_click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void quanlytau_minus_click(MouseEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void quanlytau_phongto_click(MouseEvent event) {

    }
    Parent root;
    Stage stage_dashbroard = new Stage();
    Stage stage_quanlytau = new Stage();


    {
        try {
            root = FXMLLoader.load(getClass().getResource("/DashBroard/dashbroard.fxml"));
            Scene scene_DashBroard = new Scene(root);
            stage_dashbroard.initStyle(StageStyle.TRANSPARENT);
            stage_dashbroard.setTitle("DashBroard");
            stage_dashbroard.setScene(scene_DashBroard);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void Switch_train_form(MouseEvent event) {
        stage_quanlytau = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage_dashbroard.show();
        stage_quanlytau.close();
    }

    public void loadData()
    {
        RefreshTable();
        Combobox_tau();
        combobox_loaitoa();
        ID_Tau_col.setCellValueFactory(new PropertyValueFactory<>("ID_Tau"));
        Toa_col.setCellValueFactory(new PropertyValueFactory<>("ID_toa"));
        Soluongghe_col.setCellValueFactory(new PropertyValueFactory<>("Soluongghe"));
        loaitoa_col.setCellValueFactory(new PropertyValueFactory<>("Loaitoa"));
        Delete_col.setCellValueFactory(new PropertyValueFactory<>("Delete_btn"));
        Detail_col.setCellValueFactory(new PropertyValueFactory<>("Detail_btn"));
        Update_col.setCellValueFactory(new PropertyValueFactory<>("Update_btn"));
        Delete_col.setCellFactory(param -> new TableCell<>() {
            Button deleteButton = new Button("Xóa");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    //System.out.println("Trung");
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> {

                        ChitietTau tau = getTableView().getItems().get(getIndex());
                        TauList.remove(tau);
                        //Delete_inDB(train.getID_train());
                        //System.out.println("Xóa sinh viên: " + tau.getID_train());

                    });
                }
            }
        });
        Update_col.setCellFactory(param -> new TableCell<>() {
            final Button UpdateButton = new Button("Sửa");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(UpdateButton);

                    UpdateButton.setOnAction(event -> {
                        ChitietTau tau = getTableView().getItems().get(getIndex());

                    });
                }
            }
        });
    }
    public void RefreshTable()
    {
        TauList.clear();
        connection = Database.connectionDB();
        query = "SELECT \n" +
                "\ttoa_tau.ID_Tau as ID_tau ,\n" +
                "    toa_tau.ID_Toatau as Toa,\n" +
                "    toa_tau.Loaitoa as loaitoa,\n" +
                "    toa_tau.Soluongghe as SLghe,\n" +
                "    tau.Soluongtoa as soluongtoa\n" +
                "FROM toa_tau\n" +
                "INNER JOIN tau ON tau.ID_Tau = toa_tau.ID_Tau ";
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_tau"),resultSet.getInt("soluongtoa"));

                TauList.add(new ChitietTau(tau,
                        resultSet.getString("Toa"),
                        resultSet.getInt("SLghe"),
                        resultSet.getString("loaitoa"))
                        );



            }
            Tau_table.setItems(TauList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void Combobox_tau()
    {
        try {
            Tau tau = null;
            query = "SELECT *  FROM tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                    System.out.println(resultSet.getInt("idTau"));
                String ID_Tau = resultSet.getString("ID_Tau");
                int soluongtoa = resultSet.getInt("Soluongtoa");

                tau = new Tau(ID_Tau,soluongtoa);
                tau_id_combobox.getItems().add(tau);
            }

            // Xử lý sự kiện khi combobox được chọn
//            gadi_id_combox.setOnAction(event -> {
//                String selectedTrain = gadi_id_combox.getValue();
//
//            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void Select_toa_combobox(MouseEvent event) {
        Toa_id_combobox.getItems().clear();
        Tau tau = tau_id_combobox.getValue();
        String ID_tau = tau.getIDTau();
        System.out.println(ID_tau);
        query = "SELECT ID_Toatau FROM toa_tau\n" +
                "WHERE ID_Tau = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,ID_tau);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Toa_id_combobox.getItems().add(resultSet.getString("ID_Toatau"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void combobox_loaitoa()
    {
        try {

            query = "SELECT DISTINCT Loaitoa FROM toa_tau";
            preparedStatement = connection.prepareStatement(query);

            // Lấy dữ liệu từ cơ sở dữ liệu và thêm vào combobox
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                    System.out.println(resultSet.getInt("idTau"));
                String loaitoa = resultSet.getString("Loaitoa");

                Loaitoa_id_combobox.getItems().add(loaitoa);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void Search_Tau()
    {
        connection = Database.connectionDB();
        ChitietTau chitietTau = new ChitietTau();
        Tau tau = null;
        String Tau = null;
        String Toa  = null;
        String Loaitoa = null;
        query = "SELECT *\n" +
                "FROM\n" +
                "(\n" +
                "\tSELECT \n" +
                "\t\ttoa_tau.ID_Tau as ID_tau ,\n" +
                "\t\ttoa_tau.ID_Toatau as Toa,\n" +
                "\t\ttoa_tau.Loaitoa as loaitoa,\n" +
                "\t\ttoa_tau.Soluongghe as SLghe,\n" +
                "\t\ttau.Soluongtoa as soluongtoa\n" +
                "\tFROM toa_tau\n" +
                "\n" +
                "\tINNER JOIN tau ON tau.ID_Tau = toa_tau.ID_Tau \n" +
                ") as Chitiettau\n" +
                "WHERE (ID_tau = ? OR ? is null ) " +
                "AND (Toa = ? OR ? is null ) " +
                "AND (loaitoa = ? OR ? is null) " ;
        TauList.clear();
        Tau = tau_id_combobox.getValue().getIDTau();
        Toa = Toa_id_combobox.getValue();
        Loaitoa = Loaitoa_id_combobox.getValue();
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,Tau);
            preparedStatement.setString(2,Tau);
            preparedStatement.setString(3,Toa);
            preparedStatement.setString(4,Toa);
            preparedStatement.setString(5,Loaitoa);
            preparedStatement.setString(6,Loaitoa);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next())
            {
                tau = new Tau(resultSet.getString("ID_tau"),resultSet.getInt("soluongtoa"));
                TauList.add(new ChitietTau(tau,
                        resultSet.getString("Toa"),
                        resultSet.getInt("SLghe"),
                        resultSet.getString("loaitoa")));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Tau_table.setItems(TauList);

    }
    public void Insert_Tau()
    {

    }

    public static ChitietTau getChitietTau() {
        return chitietTau;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
}
