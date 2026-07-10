package view;

import util.ConnectionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DimensionViewerPanel extends JPanel {

    private JComboBox<String> cbDimensionTables;
    private JTable tableData;
    private DefaultTableModel tableModel;

    public DimensionViewerPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 1. Header
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("DIMENSION EXPLORER - DATA WAREHOUSE");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(new JLabel("Chọn Bảng Dimension: "));

        String[] dimTables = {
            "DIM_KhachHang", "DIM_SanBay", "DIM_MayBay", 
            "DIM_LoaiMayBay", "DIM_ChuyenBay", "DIM_ThoiGian", 
            "DIM_Ghe", "DIM_LoaiVe", "DIM_DichVu"
        };
        cbDimensionTables = new JComboBox<>(dimTables);
        cbDimensionTables.setPreferredSize(new Dimension(200, 30));
        
        JButton btnLoad = new JButton("Tải Dữ Liệu");
        btnLoad.setBackground(new Color(52, 152, 219));
        btnLoad.setForeground(Color.WHITE);
        btnLoad.setFocusPainted(false);

        controlPanel.add(cbDimensionTables);
        controlPanel.add(btnLoad);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(controlPanel, BorderLayout.EAST);

        // 2. Table Data
        tableModel = new DefaultTableModel();
        tableData = new JTable(tableModel);
        tableData.setRowHeight(25);
        tableData.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableData.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(tableData);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 3. Sự kiện tải dữ liệu
        btnLoad.addActionListener(e -> loadDataToTable());
        
        // Tự động tải bảng đầu tiên
        loadDataToTable();
    }

    private void loadDataToTable() {
        String tableName = (String) cbDimensionTables.getSelectedItem();
        if (tableName == null) return;

        // Reset Table
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        String sql = "SELECT TOP 100 * FROM " + tableName; // Lấy 100 dòng demo

        try (Connection conn = ConnectionManager.getConnection("QuanLyChuyenBay_DWH_Snowflake");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Set Columns
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Set Rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                tableModel.addRow(row);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                    "Không thể tải dữ liệu bảng " + tableName + "!\nLỗi: " + ex.getMessage(), 
                    "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}
