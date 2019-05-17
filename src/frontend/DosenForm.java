/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frontend;

import backend.Database;
import backend.Mahasiswa;
import backend.MataKuliah;
import backend.Security;
import backend.Session;
import component.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import stringProcessing.*;

/**
 *
 * @author ASUS
 */
public class DosenForm extends javax.swing.JFrame {

    private Dimension screenSize;
    private Database db;
    private Session session;
    private Security security;
    private InputDatabaseMatkul stringProcessor;
    
    private BufferedImage bImage;
    private ImageIcon icon;
    private ImageIcon iconEdit;
    private ImageIcon iconAdd;
    private ImageIcon iconSave;
    private ImageIcon iconBack;
    private ImageIcon iconDelete;
    
    private ArrayList<MataKuliah> mataKuliah;
    
    /**
     * Creates new form WelcomeForm
     */
    public DosenForm(Database db, Session session) {
        initComponents();
        this.icon = new ImageIcon(MainForm.class.getResource("/res/LOGO-StudyAdvisor.png"));
        this.setIconImage(this.icon.getImage());
        
        this.setMinimumSize(Styling.WXGA_SCREEN);
        this.setSize(Styling.WXGA_SCREEN);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //SetFullscreen
        
        this.mataKuliah = new ArrayList();
        
        this.db = db;
        this.session = session;        
        this.stringProcessor = new InputDatabaseMatkul();
        
        this.lblNamaDosen.setText("Selamat Datang, " + this.session.getDosen().getNama() + "!");
        this.panelEditMataKuliah.setVisible(false);
        this.panelTambahMataKuliah.setVisible(false);
        try {
            this.loadStatistics();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
        
        this.textInputTambahMatkul.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                convertToTable();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                convertToTable();
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {

            }
        });
    }
    
    private void showInstructions() {
        this.getContentPane().remove(this.panelWelcome);
        this.panelWelcome = new PanelInstruksi();
        this.panelWelcome.setVisible(true);
        JPanel newContentPane = new JPanel();
        newContentPane.setSize(this.getWidth(), this.getHeight());
        newContentPane.setMinimumSize(new Dimension(1024, 1024));
        newContentPane.setMaximumSize(this.getMaximumSize());
        newContentPane.add(this.panelWelcome);
        this.getContentPane().add(newContentPane);
        this.invalidate();
        this.revalidate();
        this.repaint();
        //if(this.screenSize != null) this.screenSize.setSize(this.getWidth(), this.getHeight());
        Styling.refreshSize(this);
        Styling.centeringPanel(this, this.panelWelcome);
    }
    
    private void loadStatistics() throws SQLException {
        try {
            this.db.runQuery("SELECT COUNT(nim) FROM mahasiswa;");
            if(this.db.getResult().next()) this.lblJumlahMahasiswa.setText(Integer.toString(this.db.getResult().getInt(1)));
            
            this.db.runQuery("SELECT COUNT(id_matkul) FROM matkul;");
            if(this.db.getResult().next()) this.lblJumlahMataKuliah.setText(Integer.toString(this.db.getResult().getInt(1)));
            
            this.db.runQuery("SELECT COUNT(id_fokus) FROM fokus;");
            if(this.db.getResult().next()) this.lblJumlahBidang.setText(Integer.toString(this.db.getResult().getInt(1)));
            
            this.db.runQuery("SELECT COUNT(nip) FROM dosen;");
            if(this.db.getResult().next()) this.lblJumlahDosen.setText(Integer.toString(this.db.getResult().getInt(1)));
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    private void loadMataKuliah() { 
        if(this.tabelMataKuliah.getRowCount() != 0){
            int jumlahRow = ((DefaultTableModel)(this.tabelMataKuliah.getModel())).getRowCount();
            for(int i=0; i<jumlahRow; i++){
                ((DefaultTableModel)(this.tabelMataKuliah.getModel())).removeRow(0);
            }
        }
        
        this.mataKuliah = this.db.importTabelMataKuliah();
        for(MataKuliah matkul: mataKuliah){
            ((DefaultTableModel)(this.tabelMataKuliah.getModel())).addRow(new Object[]{matkul.getIdMatkul(), matkul.getNamaMatkul(), matkul.getSks(), matkul.getSemester(), matkul.isWajib(), matkul.getPrioritas(), matkul.getLogicPointRate(), matkul.getMathPointRate(), matkul.getMemoryPointRate()});
        }
    }
    
    private void convertToTable() {
        if(this.tabelTambahMatkul.getRowCount() != 0){
            int jumlahRow = ((DefaultTableModel)(this.tabelTambahMatkul.getModel())).getRowCount();
            for(int i=0; i<jumlahRow; i++){
                ((DefaultTableModel)(this.tabelTambahMatkul.getModel())).removeRow(0);
            }
        }

        InputDatabaseMatkulJSON json;

        for(String line: textInputTambahMatkul.getText().split("\\n")){
            if(!line.equals("")){
                json = this.stringProcessor.extract(line);
                if(json.isAccepted()){
                    ((DefaultTableModel)(this.tabelTambahMatkul.getModel())).addRow(new Object[]{json.getIdMatkul(), json.getNamaMatkul(), json.getSks(), json.getSemester(), json.isWajib(), json.getPrioritas(), json.getLogicPointRate(), json.getMathPointRate(), json.getMemoryPointRate()});
                }
            }
        }
    }
    
    private void tableToDatabase() {
        TableModel tableModel = this.tabelTambahMatkul.getModel();
        int rowTotal = this.tabelTambahMatkul.getRowCount();
        
        InputDatabaseMatkulJSON json;
        int totalInsert = 0;
        
        for(int i=0; i<rowTotal; i++){
            json = new InputDatabaseMatkulJSON(true, 1, tableModel.getValueAt(i, 0).toString(), tableModel.getValueAt(i, 1).toString(), tableModel.getValueAt(i, 2).toString(), tableModel.getValueAt(i, 3).toString(), (tableModel.getValueAt(i, 4).toString().equals("true")), tableModel.getValueAt(i, 5).toString(), tableModel.getValueAt(i, 6).toString(), tableModel.getValueAt(i, 7).toString(), tableModel.getValueAt(i, 8).toString());
            
            try{
                this.db.execute("INSERT INTO matkul (id_matkul, nama, sks, semester, wajib, prioritas, logic_point_rate, math_point_rate, memory_point_rate) VALUES ('" + json.getIdMatkul() + "', '" + json.getNamaMatkul() + "', " + json.getSks() + ", " + json.getSemester() + ", '" + ((json.isWajib())? 1 : 0) + "', " + json.getPrioritas() + ", " + json.getLogicPointRate() + ", " + json.getMathPointRate() + ", " + json.getMemoryPointRate() + ");");
                totalInsert++;
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        }
        
        JOptionPane.showMessageDialog(null, "Sukses menambahkan " + totalInsert + " mata kuliah.");
    }
    
    private void updateDatabase() {
        try{
            int semester = Integer.parseInt(Security.mysql_real_escape_string(this.db.getConnection(), this.txtSemester.getText()));
            String nama = Security.mysql_real_escape_string(this.db.getConnection(), this.txtNamaMataKuliah.getText());
            int sks = Integer.parseInt(Security.mysql_real_escape_string(this.db.getConnection(), this.txtSKS.getText()));
            double logicPointRate = Double.parseDouble(Security.mysql_real_escape_string(this.db.getConnection(), this.txtLogicPointRate.getText()));
            double mathPointRate = Double.parseDouble(Security.mysql_real_escape_string(this.db.getConnection(), this.txtMathPointRate.getText()));;
            double memoryPointRate = Double.parseDouble(Security.mysql_real_escape_string(this.db.getConnection(), this.txtMemoryPointRate.getText()));;
            
            if(semester > 0 && semester < 12 && sks > 0 && sks < 6 && logicPointRate >= 0 && logicPointRate <= (100-mathPointRate-memoryPointRate) && mathPointRate >= 0 && mathPointRate <= (100-logicPointRate-memoryPointRate) && memoryPointRate >= 0 && memoryPointRate <= (100-mathPointRate-logicPointRate)){
                this.db.execute("UPDATE matkul SET semester=" + semester + ", nama='" + nama + "', sks=" + sks + ", logic_point_rate=" + logicPointRate + ", math_point_rate=" + mathPointRate + ", memory_point_rate=" + memoryPointRate + " WHERE id_matkul='" + ((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 0).toString() + "';" );
                JOptionPane.showMessageDialog(null, "Sukses mengupdate mata kuliah!");
                this.loadMataKuliah();
            }else{
                JOptionPane.showMessageDialog(null, "Oops, data mata kuliah tidak valid.");
            }
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }
    
    private void deleteSelected() {
        if(this.tabelMataKuliah.getSelectedRow() >= 0){
            int dialogResult = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus mata kuliah " + ((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 1).toString() + "?", "PERINGATAN!", JOptionPane.YES_NO_OPTION);
            if(dialogResult == JOptionPane.YES_OPTION){
                try {
                    this.db.execute("DELETE FROM matkul WHERE id_matkul='" + ((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 0).toString() + "';");
                    JOptionPane.showMessageDialog(null, "Sukses menghapus 1 mata kuliah!");
                    this.loadMataKuliah();
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "Tolong klik baris yang ingin dihapus terlebih dahulu.");
        }
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelWelcome = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        lblJumlahDosen = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        lblJumlahMataKuliah = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        lblJumlahMahasiswa = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        lblJumlahBidang = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        btnEditMataKuliah = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Edit.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconEdit = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah = new javax.swing.JLabel(this.iconEdit);
        lblEditMataKuliah = new javax.swing.JLabel();
        lblNamaDosen = new javax.swing.JLabel();
        btnEditMataKuliah7 = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Back.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconBack = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah7 = new javax.swing.JLabel(this.iconBack);
        lblEditMataKuliah5 = new javax.swing.JLabel();
        panelEditMataKuliah = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtSemester = new CustomTextField();
        jLabel25 = new javax.swing.JLabel();
        txtNamaMataKuliah = new CustomTextField();
        jLabel26 = new javax.swing.JLabel();
        txtSKS = new CustomTextField();
        jLabel27 = new javax.swing.JLabel();
        txtLogicPointRate = new CustomTextField();
        jLabel28 = new javax.swing.JLabel();
        txtMathPointRate = new CustomTextField();
        jLabel29 = new javax.swing.JLabel();
        txtMemoryPointRate = new CustomTextField();
        checkBoxWajib = new javax.swing.JCheckBox();
        btnEditMataKuliah1 = new CustomPrimaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Save.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconSave = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah1 = new javax.swing.JLabel(this.iconSave);
        lblEditMataKuliah1 = new javax.swing.JLabel();
        btnEditMataKuliah2 = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-AddPlus.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconAdd = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah2 = new javax.swing.JLabel(this.iconAdd);
        lblEditMataKuliah2 = new javax.swing.JLabel();
        btnEditMataKuliah3 = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Back.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconBack = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah3 = new javax.swing.JLabel(this.iconBack);
        lblEditMataKuliah3 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelMataKuliah = new javax.swing.JTable();
        btnEditMataKuliah5 = new CustomDangerButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Delete.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconDelete = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah5 = new javax.swing.JLabel(this.iconDelete);
        panelTambahMataKuliah = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        textInputTambahMatkul = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelTambahMatkul = new javax.swing.JTable();
        jLabel20 = new javax.swing.JLabel();
        btnEditMataKuliah4 = new CustomPrimaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Save.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconSave = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah4 = new javax.swing.JLabel(this.iconSave);
        lblEditMataKuliah4 = new javax.swing.JLabel();
        btnEditMataKuliah6 = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Back.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconBack = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah6 = new javax.swing.JLabel(this.iconBack);
        lblEditMataKuliah6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rancreid Study Advisor: Dashboard");
        setFont(new java.awt.Font("Oswald", 0, 10)); // NOI18N
        setMinimumSize(new java.awt.Dimension(1024, 1024));
        setSize(new java.awt.Dimension(1024, 768));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        panelWelcome.setBackground(new java.awt.Color(255, 255, 255));
        panelWelcome.setPreferredSize(new java.awt.Dimension(1024, 720));
        panelWelcome.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setText("Dashboard");
        panelWelcome.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 50, -1, -1));

        jLabel5.setFont(new java.awt.Font("Oswald", 0, 36)); // NOI18N
        jLabel5.setText("4");
        panelWelcome.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1173, 512, -1, -1));

        jPanel1.setBackground(new java.awt.Color(243, 156, 18));

        jLabel17.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Dosen");

        lblJumlahDosen.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblJumlahDosen.setForeground(new java.awt.Color(255, 255, 255));
        lblJumlahDosen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJumlahDosen.setText("999");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addComponent(lblJumlahDosen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(lblJumlahDosen, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap())
        );

        panelWelcome.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 150, 180, 180));

        jPanel2.setBackground(new java.awt.Color(221, 75, 57));

        jLabel15.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Mata Kuliah");

        lblJumlahMataKuliah.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblJumlahMataKuliah.setForeground(new java.awt.Color(255, 255, 255));
        lblJumlahMataKuliah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJumlahMataKuliah.setText("999");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJumlahMataKuliah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(lblJumlahMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addContainerGap())
        );

        panelWelcome.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 150, 180, 180));

        jPanel3.setBackground(new java.awt.Color(0, 192, 239));

        jLabel14.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Mahasiswa");

        lblJumlahMahasiswa.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblJumlahMahasiswa.setForeground(new java.awt.Color(255, 255, 255));
        lblJumlahMahasiswa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJumlahMahasiswa.setText("999");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJumlahMahasiswa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(lblJumlahMahasiswa, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        panelWelcome.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, 180, 180));

        jPanel4.setBackground(new java.awt.Color(0, 166, 90));

        jLabel16.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Bidang");

        lblJumlahBidang.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        lblJumlahBidang.setForeground(new java.awt.Color(255, 255, 255));
        lblJumlahBidang.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblJumlahBidang.setText("999");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblJumlahBidang, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(lblJumlahBidang, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addContainerGap())
        );

        panelWelcome.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 180, 180));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        panelWelcome.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 360, -1, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel23.setText("What's up?");
        panelWelcome.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 200, -1));

        btnEditMataKuliah.setBackground(new java.awt.Color(236, 240, 241));
        btnEditMataKuliah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliahMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliahMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliahMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliahMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliahMouseReleased(evt);
            }
        });

        iconEditMataKuliah.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah.setText("Edit Mata Kuliah");

        javax.swing.GroupLayout btnEditMataKuliahLayout = new javax.swing.GroupLayout(btnEditMataKuliah);
        btnEditMataKuliah.setLayout(btnEditMataKuliahLayout);
        btnEditMataKuliahLayout.setHorizontalGroup(
            btnEditMataKuliahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliahLayout.createSequentialGroup()
                .addComponent(iconEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lblEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
        );
        btnEditMataKuliahLayout.setVerticalGroup(
            btnEditMataKuliahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(iconEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelWelcome.add(btnEditMataKuliah, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 420, 260, 60));

        lblNamaDosen.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNamaDosen.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblNamaDosen.setText("Selamat Datang, namaAnu!");
        panelWelcome.add(lblNamaDosen, new org.netbeans.lib.awtextra.AbsoluteConstraints(406, 70, 550, -1));

        btnEditMataKuliah7.setBackground(new java.awt.Color(236, 240, 241));
        btnEditMataKuliah7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah7MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah7MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah7MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah7MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah7MouseReleased(evt);
            }
        });

        iconEditMataKuliah7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah7.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah5.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah5.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah5.setText("Log out");

        javax.swing.GroupLayout btnEditMataKuliah7Layout = new javax.swing.GroupLayout(btnEditMataKuliah7);
        btnEditMataKuliah7.setLayout(btnEditMataKuliah7Layout);
        btnEditMataKuliah7Layout.setHorizontalGroup(
            btnEditMataKuliah7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah7Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEditMataKuliah5, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        btnEditMataKuliah7Layout.setVerticalGroup(
            btnEditMataKuliah7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEditMataKuliah5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(iconEditMataKuliah7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelWelcome.add(btnEditMataKuliah7, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 600, 180, 60));

        panelEditMataKuliah.setBackground(new java.awt.Color(255, 255, 255));
        panelEditMataKuliah.setPreferredSize(new java.awt.Dimension(1024, 720));
        panelEditMataKuliah.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelEditMataKuliahComponentShown(evt);
            }
        });
        panelEditMataKuliah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Klik pada baris tabel untuk mengedit");
        panelEditMataKuliah.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 107, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel18.setText("Edit Mata Kuliah");
        panelEditMataKuliah.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 50, -1, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel24.setText("Semester");
        panelEditMataKuliah.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 170, 190, 20));

        txtSemester.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtSemester.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSemesterKeyPressed(evt);
            }
        });
        panelEditMataKuliah.add(txtSemester, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 190, 270, 50));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel25.setText("Nama Mata Kuliah");
        panelEditMataKuliah.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 260, 270, 20));

        txtNamaMataKuliah.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtNamaMataKuliah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNamaMataKuliahKeyPressed(evt);
            }
        });
        panelEditMataKuliah.add(txtNamaMataKuliah, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 280, 270, 50));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel26.setText("SKS");
        panelEditMataKuliah.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 350, 270, 20));

        txtSKS.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtSKS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSKSKeyPressed(evt);
            }
        });
        panelEditMataKuliah.add(txtSKS, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 370, 270, 50));
        txtSemester.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel27.setText("Logic");
        panelEditMataKuliah.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 440, 70, 30));

        txtLogicPointRate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtLogicPointRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLogicPointRateKeyPressed(evt);
            }
        });
        panelEditMataKuliah.add(txtLogicPointRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 470, 70, 50));
        txtSemester.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel28.setText("Math");
        panelEditMataKuliah.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 440, 70, 30));

        txtMathPointRate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtMathPointRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMathPointRateKeyPressed(evt);
            }
        });
        panelEditMataKuliah.add(txtMathPointRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 470, 70, 50));
        txtSemester.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel29.setText("Memo");
        panelEditMataKuliah.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 440, 90, 30));

        txtMemoryPointRate.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtMemoryPointRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMemoryPointRateKeyPressed(evt);
            }
        });
        panelEditMataKuliah.add(txtMemoryPointRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 470, 70, 50));
        txtSemester.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));

        checkBoxWajib.setBackground(new java.awt.Color(255, 255, 255));
        checkBoxWajib.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        checkBoxWajib.setText("  Mata Kuliah Wajib");
        checkBoxWajib.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkBoxWajibActionPerformed(evt);
            }
        });
        panelEditMataKuliah.add(checkBoxWajib, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 540, -1, -1));

        btnEditMataKuliah1.setBackground(new java.awt.Color(51, 153, 255));
        btnEditMataKuliah1.setForeground(new java.awt.Color(255, 255, 255));
        btnEditMataKuliah1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah1MouseReleased(evt);
            }
        });

        iconEditMataKuliah1.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah1.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah1.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah1.setForeground(new java.awt.Color(255, 255, 255));
        lblEditMataKuliah1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah1.setText("Simpan");

        javax.swing.GroupLayout btnEditMataKuliah1Layout = new javax.swing.GroupLayout(btnEditMataKuliah1);
        btnEditMataKuliah1.setLayout(btnEditMataKuliah1Layout);
        btnEditMataKuliah1Layout.setHorizontalGroup(
            btnEditMataKuliah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah1Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEditMataKuliah1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        btnEditMataKuliah1Layout.setVerticalGroup(
            btnEditMataKuliah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah1Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEditMataKuliah.add(btnEditMataKuliah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 600, 190, 60));

        btnEditMataKuliah2.setBackground(new java.awt.Color(236, 240, 241));
        btnEditMataKuliah2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah2MouseReleased(evt);
            }
        });

        iconEditMataKuliah2.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah2.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah2.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah2.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah2.setText("Tambah");

        javax.swing.GroupLayout btnEditMataKuliah2Layout = new javax.swing.GroupLayout(btnEditMataKuliah2);
        btnEditMataKuliah2.setLayout(btnEditMataKuliah2Layout);
        btnEditMataKuliah2Layout.setHorizontalGroup(
            btnEditMataKuliah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah2Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah2, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblEditMataKuliah2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnEditMataKuliah2Layout.setVerticalGroup(
            btnEditMataKuliah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah2Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah2, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEditMataKuliah.add(btnEditMataKuliah2, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 600, 180, 60));

        btnEditMataKuliah3.setBackground(new java.awt.Color(236, 240, 241));
        btnEditMataKuliah3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah3MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah3MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah3MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah3MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah3MouseReleased(evt);
            }
        });

        iconEditMataKuliah3.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah3.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah3.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah3.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah3.setText("Kembali");

        javax.swing.GroupLayout btnEditMataKuliah3Layout = new javax.swing.GroupLayout(btnEditMataKuliah3);
        btnEditMataKuliah3.setLayout(btnEditMataKuliah3Layout);
        btnEditMataKuliah3Layout.setHorizontalGroup(
            btnEditMataKuliah3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah3Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah3, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblEditMataKuliah3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnEditMataKuliah3Layout.setVerticalGroup(
            btnEditMataKuliah3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah3Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah3, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEditMataKuliah.add(btnEditMataKuliah3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 600, 180, 60));

        tabelMataKuliah.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Mata Kuliah", "SKS", "Smt.", "Wajib", "Prior", "Logic %", "Math %", "Memo %"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelMataKuliah.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMataKuliahMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tabelMataKuliah);
        if (tabelMataKuliah.getColumnModel().getColumnCount() > 0) {
            tabelMataKuliah.getColumnModel().getColumn(0).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(0).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(1).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(1).setPreferredWidth(128);
            tabelMataKuliah.getColumnModel().getColumn(2).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(2).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(3).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(3).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(4).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(4).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(5).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(5).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(6).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(6).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(7).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(7).setPreferredWidth(1);
            tabelMataKuliah.getColumnModel().getColumn(8).setResizable(false);
            tabelMataKuliah.getColumnModel().getColumn(8).setPreferredWidth(1);
        }

        panelEditMataKuliah.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 570, 410));

        btnEditMataKuliah5.setBackground(new java.awt.Color(231, 76, 60));
        btnEditMataKuliah5.setForeground(new java.awt.Color(255, 255, 255));
        btnEditMataKuliah5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah5MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah5MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah5MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah5MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah5MouseReleased(evt);
            }
        });

        iconEditMataKuliah5.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah5.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout btnEditMataKuliah5Layout = new javax.swing.GroupLayout(btnEditMataKuliah5);
        btnEditMataKuliah5.setLayout(btnEditMataKuliah5Layout);
        btnEditMataKuliah5Layout.setHorizontalGroup(
            btnEditMataKuliah5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah5Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        btnEditMataKuliah5Layout.setVerticalGroup(
            btnEditMataKuliah5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah5Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelEditMataKuliah.add(btnEditMataKuliah5, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 600, 64, 60));

        panelTambahMataKuliah.setBackground(new java.awt.Color(255, 255, 255));
        panelTambahMataKuliah.setPreferredSize(new java.awt.Dimension(1024, 720));
        panelTambahMataKuliah.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel19.setText("Masukkan data-data terurut seperti pada tabel ke dalam kotak teks di bawah tabel");
        panelTambahMataKuliah.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 107, -1, -1));

        textInputTambahMatkul.setColumns(20);
        textInputTambahMatkul.setRows(5);
        textInputTambahMatkul.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textInputTambahMatkulInputMethodTextChanged(evt);
            }
        });
        jScrollPane3.setViewportView(textInputTambahMatkul);

        panelTambahMataKuliah.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 440, 880, 130));

        tabelTambahMatkul.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode", "Mata Kuliah", "SKS", "Smt.", "Wajib", "Prior", "Logic %", "Math %", "Memo %"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Boolean.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tabelTambahMatkul);
        if (tabelTambahMatkul.getColumnModel().getColumnCount() > 0) {
            tabelTambahMatkul.getColumnModel().getColumn(0).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(0).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(1).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(1).setPreferredWidth(128);
            tabelTambahMatkul.getColumnModel().getColumn(2).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(2).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(3).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(3).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(4).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(4).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(5).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(5).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(6).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(6).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(7).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(7).setPreferredWidth(1);
            tabelTambahMatkul.getColumnModel().getColumn(8).setResizable(false);
            tabelTambahMatkul.getColumnModel().getColumn(8).setPreferredWidth(1);
        }

        panelTambahMataKuliah.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 880, 260));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel20.setText("Tambah Mata Kuliah");
        panelTambahMataKuliah.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 50, -1, -1));

        btnEditMataKuliah4.setBackground(new java.awt.Color(51, 153, 255));
        btnEditMataKuliah4.setForeground(new java.awt.Color(255, 255, 255));
        btnEditMataKuliah4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah4MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah4MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah4MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah4MouseReleased(evt);
            }
        });

        iconEditMataKuliah4.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah4.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah4.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah4.setForeground(new java.awt.Color(255, 255, 255));
        lblEditMataKuliah4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah4.setText("Tambah");

        javax.swing.GroupLayout btnEditMataKuliah4Layout = new javax.swing.GroupLayout(btnEditMataKuliah4);
        btnEditMataKuliah4.setLayout(btnEditMataKuliah4Layout);
        btnEditMataKuliah4Layout.setHorizontalGroup(
            btnEditMataKuliah4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah4Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(lblEditMataKuliah4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnEditMataKuliah4Layout.setVerticalGroup(
            btnEditMataKuliah4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah4Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah4, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTambahMataKuliah.add(btnEditMataKuliah4, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 600, 280, 60));

        btnEditMataKuliah6.setBackground(new java.awt.Color(236, 240, 241));
        btnEditMataKuliah6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah6MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah6MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah6MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah6MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                btnEditMataKuliah6MouseReleased(evt);
            }
        });

        iconEditMataKuliah6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah6.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah6.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah6.setText("Kembali");

        javax.swing.GroupLayout btnEditMataKuliah6Layout = new javax.swing.GroupLayout(btnEditMataKuliah6);
        btnEditMataKuliah6.setLayout(btnEditMataKuliah6Layout);
        btnEditMataKuliah6Layout.setHorizontalGroup(
            btnEditMataKuliah6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah6Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblEditMataKuliah6, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnEditMataKuliah6Layout.setVerticalGroup(
            btnEditMataKuliah6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah6Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelTambahMataKuliah.add(btnEditMataKuliah6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 600, 180, 60));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(panelWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTambahMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelEditMataKuliah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTambahMataKuliah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(this.panelWelcome.isVisible()) Styling.centeringPanel(this, this.panelWelcome);
        if(this.panelEditMataKuliah.isVisible()) Styling.centeringPanel(this, panelEditMataKuliah);
        if(this.panelTambahMataKuliah.isVisible()) Styling.centeringPanel(this, panelTambahMataKuliah);
    }//GEN-LAST:event_formComponentResized

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Styling.centeringPanel(this, this.panelWelcome);
        Styling.refreshSize(this);
        
        System.out.println(this.panelWelcome.getSize());
    }//GEN-LAST:event_formWindowActivated

    private void btnEditMataKuliahMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseEntered
        this.btnEditMataKuliah.setBackground(Color.decode("#bdc3c7"));
    }//GEN-LAST:event_btnEditMataKuliahMouseEntered

    private void btnEditMataKuliahMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseExited
        this.btnEditMataKuliah.setBackground(Color.decode("#ecf0f1"));
    }//GEN-LAST:event_btnEditMataKuliahMouseExited

    private void btnEditMataKuliahMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMousePressed
        this.btnEditMataKuliah.setBackground(Color.decode("#adb3b7"));
    }//GEN-LAST:event_btnEditMataKuliahMousePressed

    private void btnEditMataKuliahMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseReleased
        this.btnEditMataKuliah.setBackground(Color.decode("#bdc3c7"));
    }//GEN-LAST:event_btnEditMataKuliahMouseReleased

    private void btnEditMataKuliahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseClicked
        
        this.panelEditMataKuliah.setVisible(true);
        this.panelWelcome.setVisible(false);

        Styling.refreshSize(this);
    }//GEN-LAST:event_btnEditMataKuliahMouseClicked

    private void txtSemesterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSemesterKeyPressed

    }//GEN-LAST:event_txtSemesterKeyPressed

    private void txtNamaMataKuliahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNamaMataKuliahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaMataKuliahKeyPressed

    private void txtSKSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSKSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSKSKeyPressed

    private void txtLogicPointRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLogicPointRateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLogicPointRateKeyPressed

    private void txtMathPointRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMathPointRateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMathPointRateKeyPressed

    private void txtMemoryPointRateKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMemoryPointRateKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMemoryPointRateKeyPressed

    private void checkBoxWajibActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkBoxWajibActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_checkBoxWajibActionPerformed

    private void btnEditMataKuliah1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah1MouseClicked
        this.updateDatabase();
    }//GEN-LAST:event_btnEditMataKuliah1MouseClicked

    private void btnEditMataKuliah1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah1MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah1MouseEntered

    private void btnEditMataKuliah1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah1MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah1MouseExited

    private void btnEditMataKuliah1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah1MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah1MousePressed

    private void btnEditMataKuliah1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah1MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah1MouseReleased

    private void btnEditMataKuliah2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah2MouseClicked
        this.panelEditMataKuliah.setVisible(false);
        this.panelTambahMataKuliah.setVisible(true);

        Styling.refreshSize(this);
    }//GEN-LAST:event_btnEditMataKuliah2MouseClicked

    private void btnEditMataKuliah2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah2MouseEntered

    private void btnEditMataKuliah2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah2MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah2MouseExited

    private void btnEditMataKuliah2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah2MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah2MousePressed

    private void btnEditMataKuliah2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah2MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah2MouseReleased

    private void btnEditMataKuliah3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah3MouseClicked
        try {
            this.loadStatistics();
        } catch (SQLException ex) {
            
        }
        this.panelEditMataKuliah.setVisible(false);
        this.panelWelcome.setVisible(true);

        Styling.refreshSize(this);
    }//GEN-LAST:event_btnEditMataKuliah3MouseClicked

    private void btnEditMataKuliah3MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah3MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah3MouseEntered

    private void btnEditMataKuliah3MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah3MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah3MouseExited

    private void btnEditMataKuliah3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah3MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah3MousePressed

    private void btnEditMataKuliah3MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah3MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah3MouseReleased

    private void textInputTambahMatkulInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textInputTambahMatkulInputMethodTextChanged
        
    }//GEN-LAST:event_textInputTambahMatkulInputMethodTextChanged

    private void btnEditMataKuliah4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah4MouseClicked
        this.tableToDatabase();
    }//GEN-LAST:event_btnEditMataKuliah4MouseClicked

    private void btnEditMataKuliah4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah4MouseEntered

    private void btnEditMataKuliah4MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah4MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah4MouseExited

    private void btnEditMataKuliah4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah4MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah4MousePressed

    private void btnEditMataKuliah4MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah4MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah4MouseReleased

    private void btnEditMataKuliah6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah6MouseClicked
        this.panelEditMataKuliah.setVisible(true);
        this.panelTambahMataKuliah.setVisible(false);

        Styling.refreshSize(this);
    }//GEN-LAST:event_btnEditMataKuliah6MouseClicked

    private void btnEditMataKuliah6MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah6MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah6MouseEntered

    private void btnEditMataKuliah6MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah6MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah6MouseExited

    private void btnEditMataKuliah6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah6MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah6MousePressed

    private void btnEditMataKuliah6MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah6MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah6MouseReleased

    private void panelEditMataKuliahComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelEditMataKuliahComponentShown
        this.loadMataKuliah();
    }//GEN-LAST:event_panelEditMataKuliahComponentShown

    private void tabelMataKuliahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMataKuliahMouseClicked
        this.txtSemester.setText(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 3).toString());
        this.txtNamaMataKuliah.setText(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 1).toString());
        this.txtSKS.setText(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 2).toString());
        this.txtLogicPointRate.setText(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 6).toString());
        this.txtMathPointRate.setText(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 7).toString());
        this.txtMemoryPointRate.setText(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 8).toString());
        this.checkBoxWajib.setSelected((((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 4).toString().equals("true")));
        System.out.println(((DefaultTableModel)(this.tabelMataKuliah.getModel())).getValueAt(this.tabelMataKuliah.getSelectedRow(), 4).toString());
    }//GEN-LAST:event_tabelMataKuliahMouseClicked

    private void btnEditMataKuliah5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah5MouseClicked
        this.deleteSelected();
    }//GEN-LAST:event_btnEditMataKuliah5MouseClicked

    private void btnEditMataKuliah5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah5MouseEntered

    private void btnEditMataKuliah5MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah5MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah5MouseExited

    private void btnEditMataKuliah5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah5MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah5MousePressed

    private void btnEditMataKuliah5MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah5MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah5MouseReleased

    private void btnEditMataKuliah7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah7MouseClicked
        MainForm mainForm = new MainForm();
        mainForm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnEditMataKuliah7MouseClicked

    private void btnEditMataKuliah7MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah7MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah7MouseEntered

    private void btnEditMataKuliah7MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah7MouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah7MouseExited

    private void btnEditMataKuliah7MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah7MousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah7MousePressed

    private void btnEditMataKuliah7MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah7MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_btnEditMataKuliah7MouseReleased

    /**
     * @param args the command line arguments
     */
    
    //public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>

        /* Create and display the form */
    //    java.awt.EventQueue.invokeLater(new Runnable() {
    //        public void run(Database db, Session session) {
    //            new WelcomeForm(db, session).setVisible(true);
    //        }
    //    });
    //}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel btnEditMataKuliah;
    private javax.swing.JPanel btnEditMataKuliah1;
    private javax.swing.JPanel btnEditMataKuliah2;
    private javax.swing.JPanel btnEditMataKuliah3;
    private javax.swing.JPanel btnEditMataKuliah4;
    private javax.swing.JPanel btnEditMataKuliah5;
    private javax.swing.JPanel btnEditMataKuliah6;
    private javax.swing.JPanel btnEditMataKuliah7;
    private javax.swing.JCheckBox checkBoxWajib;
    private javax.swing.JLabel iconEditMataKuliah;
    private javax.swing.JLabel iconEditMataKuliah1;
    private javax.swing.JLabel iconEditMataKuliah2;
    private javax.swing.JLabel iconEditMataKuliah3;
    private javax.swing.JLabel iconEditMataKuliah4;
    private javax.swing.JLabel iconEditMataKuliah5;
    private javax.swing.JLabel iconEditMataKuliah6;
    private javax.swing.JLabel iconEditMataKuliah7;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JLabel lblEditMataKuliah;
    private javax.swing.JLabel lblEditMataKuliah1;
    private javax.swing.JLabel lblEditMataKuliah2;
    private javax.swing.JLabel lblEditMataKuliah3;
    private javax.swing.JLabel lblEditMataKuliah4;
    private javax.swing.JLabel lblEditMataKuliah5;
    private javax.swing.JLabel lblEditMataKuliah6;
    private javax.swing.JLabel lblJumlahBidang;
    private javax.swing.JLabel lblJumlahDosen;
    private javax.swing.JLabel lblJumlahMahasiswa;
    private javax.swing.JLabel lblJumlahMataKuliah;
    private javax.swing.JLabel lblNamaDosen;
    private javax.swing.JPanel panelEditMataKuliah;
    private javax.swing.JPanel panelTambahMataKuliah;
    private javax.swing.JPanel panelWelcome;
    private javax.swing.JTable tabelMataKuliah;
    private javax.swing.JTable tabelTambahMatkul;
    private javax.swing.JTextArea textInputTambahMatkul;
    private javax.swing.JTextField txtLogicPointRate;
    private javax.swing.JTextField txtMathPointRate;
    private javax.swing.JTextField txtMemoryPointRate;
    private javax.swing.JTextField txtNamaMataKuliah;
    private javax.swing.JTextField txtSKS;
    private javax.swing.JTextField txtSemester;
    // End of variables declaration//GEN-END:variables
}
