module com.example.paceapp {
    requires javafx.controls;
    opens com.example.paceapp.model to javafx.base;

    exports com.example.paceapp;
    exports com.example.paceapp.model;
    exports com.example.paceapp.view;
}