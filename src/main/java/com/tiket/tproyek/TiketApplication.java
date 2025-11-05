package com.tiket.tproyek;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.tiket.tproyek.model.*;
import com.tiket.tproyek.service.Pemesanan;
import com.tiket.tproyek.database.DatabaseHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TiketApplication extends Application {

    private Pemesanan pemesananService = new Pemesanan();
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private List<Transportasi> transportasiList = new ArrayList<>();
    private VBox transactionContainer;
    private Label pricePreviewLabel;
    private Label availableSeatsLabel;
    private Label totalBookingsLabel;
    private Label totalRevenueLabel;

    @Override
    public void start(Stage stage) {
        // Initialize sample transportation data
        initializeTransportasiData();

        // Main layout
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.getStyleClass().add("root");

        // Header
        VBox headerBox = createHeader();
        root.getChildren().add(headerBox);

        // Main content - Two column layout (equal 50/50 width)
        HBox mainContent = new HBox(20);

        // Left side - Booking Form (50% width)
        VBox leftSide = createBookingForm();
        leftSide.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(leftSide, Priority.ALWAYS);

        // Right side - Transaction History (50% width)
        VBox rightSide = createTransactionHistory();
        rightSide.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(rightSide, Priority.ALWAYS);

        mainContent.getChildren().addAll(leftSide, rightSide);

        // Wrap in ScrollPane for scrollability
        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.getChildren().add(scrollPane);

        Scene scene = new Scene(root, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Aplikasi Pemesanan Tiket Transportasi");
        stage.setScene(scene);
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();

        // Initial display
        updateTransactionDisplay();
    }

    private VBox createHeader() {
        VBox headerBox = new VBox(5);
        headerBox.getStyleClass().add("header-box");
        headerBox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("🚆 Sistem Pemesanan Tiket Transportasi");
        titleLabel.getStyleClass().add("header-label");

        Label subtitleLabel = new Label("Bus • Kereta • Pesawat");
        subtitleLabel.getStyleClass().add("header-subtitle");

        headerBox.getChildren().addAll(titleLabel, subtitleLabel);
        return headerBox;
    }

    private VBox createBookingForm() {
        VBox container = new VBox(5);
        container.getStyleClass().add("card");
        container.setMinWidth(0); // Allow container to shrink

        // Card title
        Label cardTitle = new Label("📝 Form Pemesanan Tiket");
        cardTitle.getStyleClass().add("card-title");

        // Statistics
        HBox statsBox = createStatsBox();

        // Form fields
        VBox formFields = new VBox(10);
        VBox.setVgrow(formFields, Priority.ALWAYS);

        // Name field
        VBox nameBox = createFormField("Nama Lengkap", "Masukkan nama lengkap Anda");
        TextField namaField = (TextField) ((VBox) nameBox.getChildren().get(1)).getChildren().get(0);

        // Email field
        VBox emailBox = createFormField("Email", "contoh@email.com");
        TextField emailField = (TextField) ((VBox) emailBox.getChildren().get(1)).getChildren().get(0);

        // Transportation selection
        VBox transportBox = new VBox(5);
        Label transportLabel = new Label("Pilih Transportasi");
        transportLabel.getStyleClass().add("form-label");

        ComboBox<String> transportasiCombo = new ComboBox<>();
        for (Transportasi t : transportasiList) {
            transportasiCombo.getItems().add(t.getTipe() + " - " + t.getNama());
        }
        transportasiCombo.setPromptText("Pilih jenis transportasi");
        transportasiCombo.setMaxWidth(Double.MAX_VALUE);

        transportBox.getChildren().addAll(transportLabel, transportasiCombo);

        // Info box for selected transportation
        VBox infoBox = new VBox(5);
        infoBox.getStyleClass().add("info-box");
        infoBox.setVisible(false);
        infoBox.setManaged(false);

        availableSeatsLabel = new Label();
        availableSeatsLabel.getStyleClass().add("info-label");

        pricePreviewLabel = new Label();
        pricePreviewLabel.getStyleClass().add("info-label");

        infoBox.getChildren().addAll(availableSeatsLabel, pricePreviewLabel);

        // Transportation combo change listener
        transportasiCombo.setOnAction(e -> {
            int selectedIndex = transportasiCombo.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                Transportasi t = transportasiList.get(selectedIndex);
                availableSeatsLabel.setText("💺 Jumlah Kursi: " + t.getJumlahKursi());
                pricePreviewLabel.setText("💰 Harga: Rp " + String.format("%,.0f", t.hitungTotal()));
                infoBox.setVisible(true);
                infoBox.setManaged(true);
            }
        });

        // Date field
        VBox dateBox = new VBox(5);
        Label dateLabel = new Label("Tanggal Keberangkatan");
        dateLabel.getStyleClass().add("form-label");
        DatePicker tanggalPicker = new DatePicker();
        tanggalPicker.setValue(LocalDate.now());
        tanggalPicker.setMaxWidth(Double.MAX_VALUE);
        dateBox.getChildren().addAll(dateLabel, tanggalPicker);

        // Seat number field
        VBox seatBox = createFormField("Nomor Kursi", "Masukkan nomor kursi (contoh: 15)");
        TextField kursiField = (TextField) ((VBox) seatBox.getChildren().get(1)).getChildren().get(0);

        formFields.getChildren().addAll(nameBox, emailBox, transportBox, infoBox, dateBox, seatBox);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button bookButton = new Button("✓ Pesan Tiket");
        bookButton.getStyleClass().add("btn-primary");
        bookButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(bookButton, Priority.ALWAYS);

        Button clearButton = new Button("✕ Bersihkan");
        clearButton.getStyleClass().add("btn-secondary");
        clearButton.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(clearButton, Priority.ALWAYS);

        buttonBox.getChildren().addAll(bookButton, clearButton);

        // Event handlers
        bookButton.setOnAction(e -> handleBooking(
            namaField, emailField, transportasiCombo, tanggalPicker, kursiField
        ));

        clearButton.setOnAction(e -> {
            namaField.clear();
            emailField.clear();
            transportasiCombo.getSelectionModel().clearSelection();
            tanggalPicker.setValue(LocalDate.now());
            kursiField.clear();
            infoBox.setVisible(false);
            infoBox.setManaged(false);
        });

        Separator separator = new Separator();
        separator.getStyleClass().add("separator");

        container.getChildren().addAll(cardTitle, statsBox, separator, formFields, buttonBox);
        return container;
    }

    private HBox createStatsBox() {
        HBox statsBox = new HBox(15);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setPadding(new Insets(5, 0, 5, 0));

        // Total bookings stat
        VBox bookingsBox = new VBox(3);
        bookingsBox.getStyleClass().add("stat-box");
        bookingsBox.setAlignment(Pos.CENTER);
        bookingsBox.setPrefWidth(150);

        totalBookingsLabel = new Label("0");
        totalBookingsLabel.getStyleClass().add("stat-value");

        Label bookingsTextLabel = new Label("Total Pemesanan");
        bookingsTextLabel.getStyleClass().add("stat-label");

        bookingsBox.getChildren().addAll(totalBookingsLabel, bookingsTextLabel);

        // Total revenue stat
        VBox revenueBox = new VBox(3);
        revenueBox.setStyle("-fx-background-color: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); " +
                           "-fx-background-radius: 8; -fx-padding: 15;");
        revenueBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(revenueBox, Priority.ALWAYS);

        totalRevenueLabel = new Label("Rp 0");
        totalRevenueLabel.getStyleClass().add("stat-value");

        Label revenueTextLabel = new Label("Total Pendapatan");
        revenueTextLabel.getStyleClass().add("stat-label");

        revenueBox.getChildren().addAll(totalRevenueLabel, revenueTextLabel);

        statsBox.getChildren().addAll(bookingsBox, revenueBox);
        return statsBox;
    }

    private VBox createFormField(String labelText, String promptText) {
        VBox fieldBox = new VBox(5);

        Label label = new Label(labelText);
        label.getStyleClass().add("form-label");

        TextField field = new TextField();
        field.setPromptText(promptText);
        field.setMaxWidth(Double.MAX_VALUE);

        VBox inputWrapper = new VBox(field);

        fieldBox.getChildren().addAll(label, inputWrapper);
        return fieldBox;
    }

    private VBox createTransactionHistory() {
        VBox container = new VBox(8);
        container.getStyleClass().add("card");
        container.setMinWidth(0); // Allow container to shrink

        // Header
        Label cardTitle = new Label("📋 Riwayat Transaksi");
        cardTitle.getStyleClass().add("card-title");

        // Scroll pane for transactions
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        transactionContainer = new VBox(10);
        transactionContainer.setPadding(new Insets(10));
        scrollPane.setContent(transactionContainer);

        container.getChildren().addAll(cardTitle, scrollPane);
        return container;
    }

    private void handleBooking(TextField namaField, TextField emailField,
                               ComboBox<String> transportasiCombo,
                               DatePicker tanggalPicker, TextField kursiField) {
        try {
            String nama = namaField.getText();
            String email = emailField.getText();
            int selectedIndex = transportasiCombo.getSelectionModel().getSelectedIndex();
            String tanggal = tanggalPicker.getValue() != null ? tanggalPicker.getValue().toString() : "";
            int nomorKursi = Integer.parseInt(kursiField.getText());

            if (nama.isEmpty() || email.isEmpty() || selectedIndex < 0 || tanggal.isEmpty()) {
                showAlert("Error", "Semua field harus diisi!", Alert.AlertType.ERROR);
                return;
            }

            User user = new User(nama, email);
            Transportasi transportasi = transportasiList.get(selectedIndex);

            // Check if seat is already booked
            if (databaseHandler.isSeatBooked(transportasi.getId(), tanggal, nomorKursi)) {
                showAlert("Error", "Kursi nomor " + nomorKursi + " sudah dipesan untuk " +
                         transportasi.getNama() + " pada tanggal " + tanggal, Alert.AlertType.ERROR);
                return;
            }

            Tiket tiket = pemesananService.pemesananTiket(transportasi, user, tanggal, nomorKursi);
            databaseHandler.saveTiket(tiket);

            // Update display
            updateTransactionDisplay();
            updateStatistics();

            showAlert("Berhasil!",
                     "Tiket berhasil dipesan!\n\n" +
                     "ID Tiket: " + tiket.getTiketID() + "\n" +
                     "Total Harga: Rp " + String.format("%,.0f", tiket.getHarga()),
                     Alert.AlertType.INFORMATION);

            // Clear form
            namaField.clear();
            emailField.clear();
            transportasiCombo.getSelectionModel().clearSelection();
            tanggalPicker.setValue(LocalDate.now());
            kursiField.clear();

        } catch (NumberFormatException ex) {
            showAlert("Error", "Nomor kursi harus berupa angka!", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException ex) {
            showAlert("Error Validasi", ex.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception ex) {
            showAlert("Error", "Terjadi kesalahan: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void updateTransactionDisplay() {
        transactionContainer.getChildren().clear();
        List<Tiket> allTickets = databaseHandler.getAllTiket();

        if (allTickets.isEmpty()) {
            Label emptyLabel = new Label("Belum ada transaksi pemesanan");
            emptyLabel.setStyle("-fx-text-fill: #9ca3af; -fx-font-size: 14px;");
            transactionContainer.getChildren().add(emptyLabel);
            return;
        }

        int count = 1;
        for (Tiket tiket : allTickets) {
            VBox transactionCard = createTransactionCard(tiket, count);
            transactionContainer.getChildren().add(transactionCard);
            count++;
        }
    }

    private VBox createTransactionCard(Tiket tiket, int number) {
        VBox card = new VBox(8);
        card.getStyleClass().add("transaction-card");

        // Header with number and ID
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label numberLabel = new Label("#" + number);
        numberLabel.getStyleClass().add("transaction-header");

        Label idLabel = new Label("ID: " + tiket.getTiketID());
        idLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #6b7280; -fx-font-family: monospace;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Transport badge
        Label badge = new Label(getTransportIcon(tiket.getTransportasi()) + " " +
                               tiket.getTransportasi().getTipe());
        badge.getStyleClass().addAll("badge", getTransportBadgeClass(tiket.getTransportasi()));

        headerBox.getChildren().addAll(numberLabel, idLabel, spacer, badge);

        // Details
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(15);
        detailsGrid.setVgap(6);

        addDetailRow(detailsGrid, 0, "👤 Nama", tiket.getUser().getNama());
        addDetailRow(detailsGrid, 1, "✉️ Email", tiket.getUser().getEmail());
        addDetailRow(detailsGrid, 2, "🚌 Transportasi", tiket.getTransportasi().getNama());
        addDetailRow(detailsGrid, 3, "📅 Tanggal", formatDate(tiket.getTanggal()));
        addDetailRow(detailsGrid, 4, "💺 Kursi", String.valueOf(tiket.getNomorKursi()));

        // Price
        HBox priceBox = new HBox(5);
        priceBox.setAlignment(Pos.CENTER_LEFT);
        Label priceIconLabel = new Label("💰");
        Label priceLabel = new Label("Rp " + String.format("%,.0f", tiket.getHarga()));
        priceLabel.getStyleClass().add("transaction-price");
        priceBox.getChildren().addAll(priceIconLabel, priceLabel);

        card.getChildren().addAll(headerBox, new Separator(), detailsGrid, priceBox);
        return card;
    }

    private void addDetailRow(GridPane grid, int row, String label, String value) {
        Label labelNode = new Label(label);
        labelNode.getStyleClass().add("transaction-label");
        labelNode.setPrefWidth(120);

        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("transaction-value");

        grid.add(labelNode, 0, row);
        grid.add(valueNode, 1, row);
    }

    private String getTransportIcon(Transportasi t) {
        if (t instanceof Bis) return "🚌";
        if (t instanceof Kereta) return "🚆";
        if (t instanceof Pesawat) return "✈️";
        return "🚗";
    }

    private String getTransportBadgeClass(Transportasi t) {
        if (t instanceof Bis) return "badge-bus";
        if (t instanceof Kereta) return "badge-train";
        if (t instanceof Pesawat) return "badge-plane";
        return "badge-bus";
    }

    private String formatDate(String date) {
        try {
            LocalDate localDate = LocalDate.parse(date);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            return localDate.format(formatter);
        } catch (Exception e) {
            return date;
        }
    }

    private void updateStatistics() {
        List<Tiket> allTickets = databaseHandler.getAllTiket();
        int totalBookings = allTickets.size();
        double totalRevenue = allTickets.stream().mapToDouble(Tiket::getHarga).sum();

        totalBookingsLabel.setText(String.valueOf(totalBookings));
        totalRevenueLabel.setText("Rp " + String.format("%,.0f", totalRevenue));
    }

    private void initializeTransportasiData() {
        // Add sample buses
        transportasiList.add(new Bis("B001", "Sinar Jaya", 40, 150000, true));
        transportasiList.add(new Bis("B002", "Pahala Kencana", 35, 120000, false));

        // Add sample trains
        transportasiList.add(new Kereta("K001", "Argo Bromo", 200, 200000, "Eksekutif"));
        transportasiList.add(new Kereta("K002", "Taksaka", 250, 150000, "Bisnis"));
        transportasiList.add(new Kereta("K003", "Ekonomi AC", 300, 100000, "Ekonomi"));

        // Add sample planes
        transportasiList.add(new Pesawat("P001", "Garuda Indonesia", 150, 1500000, true));
        transportasiList.add(new Pesawat("P002", "Lion Air", 180, 800000, false));
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Style the dialog
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
