package view;

import util.ConnectionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;

public class FactViewerPanel extends JPanel {

    private JTable tableData;
    private DefaultTableModel tableModel;
    
    private JLabel lblTotalTickets;
    private JLabel lblTotalRevenue;

    public FactViewerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Header & Summary Stats
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("FACT ANALYZER - DATA WAREHOUSE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        
        lblTotalTickets = new JLabel("Tổng số vé: 0");
        lblTotalTickets.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalTickets.setForeground(new Color(41, 128, 185));
        
        lblTotalRevenue = new JLabel("Tổng doanh thu DWH: 0 đ");
        lblTotalRevenue.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotalRevenue.setForeground(new Color(192, 57, 43));
        
        JButton btnRefresh = new JButton("Làm Mới Dữ Liệu");
        btnRefresh.setBackground(new Color(243, 156, 18));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);

        statsPanel.add(lblTotalTickets);
        statsPanel.add(lblTotalRevenue);
        statsPanel.add(btnRefresh);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(statsPanel, BorderLayout.EAST);

        // 2. Table Data
        tableModel = new DefaultTableModel();
        tableData = new JTable(tableModel);
        tableData.setRowHeight(25);
        tableData.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableData.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Cố định một số cột cơ bản hoặc tự động load như Dimension
        
        JScrollPane scrollPane = new JScrollPane(tableData);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                "Bảng FACT_Ve (100 dòng mới nhất)", 
                javax.swing.border.TitledBorder.LEFT, 
                javax.swing.border.TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 12), Color.DARK_GRAY));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Sự kiện tải dữ liệu
        btnRefresh.addActionListener(e -> {
            loadFactData();
            loadSummaryStats();
        });
        
        // Tự động tải
        loadFactData();
        loadSummaryStats();
    }

    private void loadFactData() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        String sql = "SELECT TOP 100 * FROM FACT_Ve ORDER BY SK_ThoiGianDat DESC";

        try (Connection conn = ConnectionManager.getConnection("QuanLyChuyenBay_DWH_Snowflake");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            System.err.println("Lỗi load FACT_Ve: " + ex.getMessage());
        }
    }

    private void loadSummaryStats() {
        String sql = "SELECT COUNT(*) AS TotalTickets, SUM(GiaVe + ISNULL(DoanhThuDV, 0)) AS TotalRevenue FROM FACT_Ve";
        
        try (Connection conn = ConnectionManager.getConnection("QuanLyChuyenBay_DWH_Snowflake");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int totalTickets = rs.getInt("TotalTickets");
                long totalRevenue = rs.getLong("TotalRevenue");

                NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
                
                lblTotalTickets.setText("Tổng số vé: " + formatter.format(totalTickets));
                lblTotalRevenue.setText("Tổng doanh thu DWH: " + formatter.format(totalRevenue) + " đ");
            }

        } catch (Exception ex) {
            System.err.println("Lỗi tính toán thống kê FACT: " + ex.getMessage());
        }
    }
}
