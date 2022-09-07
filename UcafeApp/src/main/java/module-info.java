module example.myapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;
    requires javafx.media;
    requires java.base;
    requires javafx.graphics;
    requires java.logging;
    requires org.mongodb.bson;
  requires org.mongodb.driver.core;
  requires org.mongodb.driver.sync.client;

  opens example.myapp to javafx.fxml;
    exports example.myapp;
    exports digit;
    opens digit to javafx.fxml;
}
