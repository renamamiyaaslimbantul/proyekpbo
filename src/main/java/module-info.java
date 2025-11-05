module com.tiket.tproyek {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.tiket.tproyek to javafx.fxml;
    exports com.tiket.tproyek;
    exports com.tiket.tproyek.model;
    exports com.tiket.tproyek.service;
    exports com.tiket.tproyek.database;
}