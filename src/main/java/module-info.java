module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires com.gluonhq.maps;
    requires java.desktop;
    requires com.opencsv;
    //requires jfxrt;
    //requires rt;

    opens com.example.s201 to javafx.fxml;
    exports com.example.s201;
}