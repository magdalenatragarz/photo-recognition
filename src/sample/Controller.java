package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class Controller {

    LinkedList<String> helper = new LinkedList<String>();
    @FXML
    private ListView listView;

    @FXML
    private ImageView imageView;

    @FXML
    private BarChart<String, Number> chart;

    public void pressButton(ActionEvent event) {
        listView.getSelectionModel().clearSelection();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Pick a photo");
        File file = directoryChooser.showDialog(null);
        if (file != null) {
            String[] list = null;
            list = file.list();
            for (String i : list) {
                String absolutePath = file.getAbsolutePath() + "\\" + i;
                if (!(new File(absolutePath)).isDirectory()) {
                    listView.getItems().add(i);
                    helper.addLast(absolutePath);
                }
            }
        }
    }

    public void handleMouseClick(MouseEvent arg) {
        chart.getData().clear();
        chart.layout();
        String absolutePath = "";
        API recogniser = new API();
        for (String i : helper) {
            if (i.contains(listView.getSelectionModel().getSelectedItem().toString())) {
                absolutePath = i;
            }
        }
        File imgFile = new File(absolutePath);
        Image img = new Image(imgFile.toURI().toString());
        imageView.setImage(img);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Probability");
        chart.setTitle("What is that?");
        ArrayList<Pair<String, Double>> x1 = new ArrayList<Pair<String, Double>>();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        XYChart.Series<String, Number> series3 = new XYChart.Series<>();
        XYChart.Series<String, Number> series4 = new XYChart.Series<>();
        try {
            x1 = recogniser.recognise(absolutePath);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        series1.getData().add(new XYChart.Data<>(x1.get(0).first, x1.get(0).second));
        series2.getData().add(new XYChart.Data<>(x1.get(1).first, x1.get(1).second));
        series3.getData().add(new XYChart.Data<>(x1.get(2).first, x1.get(2).second));
        series4.getData().add(new XYChart.Data<>(x1.get(3).first, x1.get(3).second));
        chart.getData().addAll(series1,series2,series3,series4);
        chart.setAnimated(false);


    }
}
