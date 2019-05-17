package frontend;

import backend.AmbilMataKuliah;
import backend.Bidang;
import backend.Database;
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
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import stringProcessing.*;

public class WelcomeForm extends javax.swing.JFrame {

    private Database db;
    private Session session;
    private Security security;
    private InputMatkulMahasiswa stringProcessor;
    private HashMap<String, MataKuliah> mataKuliah;
    private Bidang[] bidang;
    
    private BufferedImage bImage;
    private ImageIcon icon;
    private ImageIcon imageStep1;
    private ImageIcon imageStep2;
    private ImageIcon imageStep4;
    private ImageIcon iconForward;
    private ImageIcon iconBack;
    private ImageIcon iconSave;
    private ImageIcon iconUser;
    
    /**
     * Creates new form WelcomeForm
     */
    public WelcomeForm(Database db, Session session) {
        initComponents();
        this.icon = new ImageIcon(MainForm.class.getResource("/res/LOGO-StudyAdvisor.png"));
        this.setIconImage(this.icon.getImage());
        
        this.setMinimumSize(Styling.WXGA_SCREEN);
        this.setSize(Styling.WXGA_SCREEN);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //SetFullscreen
        
        this.db = db;
        this.session = session;     
        this.security = new Security();
        this.stringProcessor = new InputMatkulMahasiswa();
        this.mataKuliah = this.db.importHashMapMataKuliah();
        this.loadStatus();
        this.loadRecomendedFocus();
        
        this.jLabel2.setText("Selamat Datang, " + this.session.getMahasiswa().getNama() + "!");
        this.panelInput.setVisible(false);
        this.panelStatus.setVisible(false);
        
        this.textInput.getDocument().addDocumentListener(new DocumentListener() {

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
    
    private void convertToTable() {
        if(this.tabelAmbilMatkul.getRowCount() != 0){
            int jumlahRow = ((DefaultTableModel)(this.tabelAmbilMatkul.getModel())).getRowCount();
            for(int i=0; i<jumlahRow; i++){
                ((DefaultTableModel)(this.tabelAmbilMatkul.getModel())).removeRow(0);
            }
        }
        int semesterSekarang = 1;
        InputMatkulMahasiswaJSON json;
        
        for(String line: textInput.getText().split("\\n")){
            if(!line.equals("")){
                json = this.stringProcessor.extract(line);
                if(json.isAccepted()){
                    try{
                        if(this.mataKuliah.containsKey(json.getIdMatkul())){
                            ((DefaultTableModel)(this.tabelAmbilMatkul.getModel())).addRow(new Object[]{semesterSekarang, json.getIdMatkul(), this.mataKuliah.get(json.getIdMatkul()).getNamaMatkul(), this.mataKuliah.get(json.getIdMatkul()).getSks(), json.getNilai()});
                        }else{
                            JOptionPane.showMessageDialog(null, "Oops, mata kuliah " + json.getIdMatkul() + " belum tersedia.");
                        }
                        
                    } catch(Exception ex){
                        JOptionPane.showMessageDialog(null, ex.toString());
                    }
                }
            }else{
                semesterSekarang++;
            }
        }
    }
    
    private void tableToDatabase() {
        TableModel tableModel = this.tabelAmbilMatkul.getModel();
        int rowTotal = this.tabelAmbilMatkul.getRowCount();
        
        InputDatabaseMatkulJSON json;
        int totalInsert = 0;
        
        int semester, sks;
        String idMatkul, namaMatkul, nilai;
        double nilaiAsli, logicPoint, mathPoint, memoryPoint;
        double totalLogicPoint = 0, totalMathPoint = 0, totalMemoryPoint = 0;
        
        for(int i=0; i<rowTotal; i++){
            semester = Integer.parseInt(tableModel.getValueAt(i, 0).toString());
            idMatkul = tableModel.getValueAt(i, 1).toString();
            namaMatkul = tableModel.getValueAt(i, 2).toString();
            sks = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
            nilai = tableModel.getValueAt(i, 4).toString();
            
            nilaiAsli = AmbilMataKuliah.convertNilai(nilai);
            logicPoint = (nilaiAsli * 10 / 16) * this.mataKuliah.get(idMatkul).getLogicPointRate();
            mathPoint = (nilaiAsli * 10 / 16) * this.mataKuliah.get(idMatkul).getMathPointRate();
            memoryPoint = (nilaiAsli * 10 / 16) * this.mataKuliah.get(idMatkul).getMemoryPointRate();
            
            
            try{
                if(this.db.execute("INSERT INTO mahasiswa_ambil_matkul (nim, id_matkul, semester, nilai, logic_point, math_point, memory_point) VALUES ('" + this.session.getMahasiswa().getNomorInduk() + "', " + Security.quote(this.db.getConnection(), idMatkul) + ", " + Security.mysql_real_escape_string(this.db.getConnection(), Integer.toString(semester)) + ", " + Security.quote(this.db.getConnection(), nilai) + ", " + logicPoint + ", " + mathPoint + ", " + memoryPoint + ");")){
                    totalLogicPoint += logicPoint;
                    totalMathPoint += mathPoint;
                    totalMemoryPoint += memoryPoint;
                    totalInsert++;
                }
                
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        }
        
        totalLogicPoint /= totalInsert;
        totalMathPoint /= totalInsert;
        totalMemoryPoint /= totalInsert;
        if(totalInsert >= 1){
            try{
                this.db.execute("UPDATE mahasiswa SET logic_point=" + totalLogicPoint + ", math_point=" + totalMathPoint + ", memory_point=" + totalMemoryPoint + " WHERE nim='" + this.session.getMahasiswa().getNomorInduk() + "';");
                this.session.getMahasiswa().updatePoint(totalLogicPoint, totalMathPoint, totalMemoryPoint);
                System.out.println("LP  : " + totalLogicPoint);
                System.out.println("MaP : " + totalMathPoint);
                System.out.println("MeP : " + totalMemoryPoint);
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString());
            }

            JOptionPane.showMessageDialog(null, "Sukses mengambil " + totalInsert + " mata kuliah.");
            this.loadStatus();
            this.loadRecomendedFocus();
            this.panelStatus.setVisible(true);
            this.panelInput.setVisible(false);
            
            Styling.refreshSize(this);
        }else{
            JOptionPane.showMessageDialog(null, "Gagal mengambil semua mata kuliah tersebut.");
        }
    }
    
    private void loadStatus() {
        this.lblNIM.setText(this.session.getMahasiswa().getNomorInduk());
        this.lblNama.setText(this.session.getMahasiswa().getNama());
        this.pbLogic.setValue((int) this.session.getMahasiswa().getLogicPoint());
        this.pbMath.setValue((int) this.session.getMahasiswa().getMathPoint());
        this.pbMemory.setValue((int) this.session.getMahasiswa().getMemoryPoint());
        this.lblLogicPoint.setText(Integer.toString((int) this.session.getMahasiswa().getLogicPoint()));
        this.lblMathPoint.setText(Integer.toString((int) this.session.getMahasiswa().getMathPoint()));
        this.lblMemoryPoint.setText(Integer.toString((int) this.session.getMahasiswa().getMemoryPoint()));
    }
    
    private void loadRecomendedFocus() {
        try{
            this.db.runQuery("SELECT COUNT(id_fokus) FROM fokus;");
            this.db.getResult().next();
            int rowCount = this.db.getResult().getInt(1);
            
            this.bidang = new Bidang[rowCount]; 

            this.db.runQuery("SELECT * FROM fokus;");
            int index = 0;
            
            int mLogicPoint = (int)this.session.getMahasiswa().getLogicPoint();
            int mMathPoint = (int)this.session.getMahasiswa().getMathPoint();
            int mMemoryPoint = (int)this.session.getMahasiswa().getMemoryPoint();
            int recomendPoint, selisih;
            int fLogicPoint, fMathPoint, fMemoryPoint;
            
            while(this.db.getResult().next()){
                recomendPoint = 3;
                fLogicPoint = (int)Double.parseDouble(this.db.getResult().getString("logic_point"));
                fMathPoint = (int)Double.parseDouble(this.db.getResult().getString("math_point"));
                fMemoryPoint = (int)Double.parseDouble(this.db.getResult().getString("memory_point"));
                
                
                if(mLogicPoint >= fLogicPoint) recomendPoint++;
                else recomendPoint--;
                
                if(mMathPoint >= fMathPoint) recomendPoint++;
                else recomendPoint--;
                
                if(mMemoryPoint >= fLogicPoint) recomendPoint++;
                else recomendPoint--;
                
                selisih = (mLogicPoint - fLogicPoint) + (mMathPoint - fMathPoint) + (mMemoryPoint - fMemoryPoint);
                
                this.bidang[index] = new Bidang(this.db.getResult().getString("nama_fokus"), this.db.getResult().getString("deskripsi"), Double.parseDouble(this.db.getResult().getString("logic_point")), Double.parseDouble(this.db.getResult().getString("math_point")), Double.parseDouble(this.db.getResult().getString("memory_point")), recomendPoint, selisih);
                index++;
            }
            
            
            int batas = rowCount - 1;
            Bidang swap;
            
            for(int i=0; i < rowCount; i++){  
                for(int j=1; j < (rowCount-i); j++){  
                    if(this.bidang[j-1].getSelisih() < this.bidang[j].getSelisih()){  
                        //swap elements  
                        swap = bidang[j-1];  
                        this.bidang[j-1] = this.bidang[j];  
                        this.bidang[j] = swap;  
                    }  
                }  
            }
            /*
            for(int i=0; i<batas-1; i++){
                if(this.bidang[i].getSelisih() <= this.bidang[i+1].getSelisih()){
                    swap = this.bidang[i];
                    this.bidang[i] = this.bidang[i+1];
                    this.bidang[i+1] = swap;
                }
               
                if(i == batas-1){
                    batas--;
                    i = -1;
                }
            }
            */
            if(this.tabelRekomendasi.getRowCount() != 0){
                int jumlahRow = ((DefaultTableModel)(this.tabelRekomendasi.getModel())).getRowCount();
                for(int i=0; i<jumlahRow; i++){
                    ((DefaultTableModel)(this.tabelRekomendasi.getModel())).removeRow(0);
                }
            }
            for(int i=0; i<rowCount; i++){
                ((DefaultTableModel)(this.tabelRekomendasi.getModel())).addRow(new Object[]{i+1, this.bidang[i].getNamaBidang()});
            }
        } catch(Exception ex) {
            System.out.println(ex.toString());
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
        jLabel1 = new javax.swing.JLabel();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(WelcomeForm.class.getResource("/res/ILLUSTRATION-SAStep2.png").getFile()));
            Image image = bImage.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
            this.imageStep2 = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        labelStep2 = new javax.swing.JLabel(this.imageStep2);
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(WelcomeForm.class.getResource("/res/ILLUSTRATION-SAStep1.png").getFile()));
            Image image = bImage.getScaledInstance(240, 240, Image.SCALE_SMOOTH);
            this.imageStep1 = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        labelStep1 = new javax.swing.JLabel(this.imageStep1);
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(WelcomeForm.class.getResource("/res/ILLUSTRATION-SAStep4.png").getFile()));
            Image image = bImage.getScaledInstance(480, 240, Image.SCALE_SMOOTH);
            this.imageStep4 = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        labelStep4 = new javax.swing.JLabel(this.imageStep4);
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnEditMataKuliah = new CustomPrimaryButton();
        lblEditMataKuliah = new javax.swing.JLabel();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(WelcomeForm.class.getResource("/res/ICON-Forward.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconForward = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah = new javax.swing.JLabel(this.iconForward);
        btnEditMataKuliah4 = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Back.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconBack = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah4 = new javax.swing.JLabel(this.iconBack);
        lblEditMataKuliah4 = new javax.swing.JLabel();
        panelInput = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textInput = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelAmbilMatkul = new javax.swing.JTable();
        btnEditMataKuliah3 = new CustomSecondaryButton();
        lblEditMataKuliah3 = new javax.swing.JLabel();
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
        lblEditMataKuliah7 = new javax.swing.JLabel();
        panelStatus = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(WelcomeForm.class.getResource("/res/ICON-User.png").getFile()));
            Image image = bImage.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
            this.iconUser = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        lblIconUser = new javax.swing.JLabel(this.iconUser);
        lblNama = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        pbMemory = new javax.swing.JProgressBar();
        lblNIM = new javax.swing.JLabel();
        lblMemoryPoint = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        pbLogic = new javax.swing.JProgressBar();
        pbMath = new javax.swing.JProgressBar();
        jLabel22 = new javax.swing.JLabel();
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
        lblLogicPoint = new javax.swing.JLabel();
        lblMathPoint = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelRekomendasi = new javax.swing.JTable();
        pbSelectedMemory = new javax.swing.JProgressBar();
        lblSelectedMemoryPoint = new javax.swing.JLabel();
        pbSelectedLogic = new javax.swing.JProgressBar();
        pbSelectedMath = new javax.swing.JProgressBar();
        lblSelectedLogicPoint = new javax.swing.JLabel();
        lblSelectedMathPoint = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        lblDeskripsi = new javax.swing.JTextArea();
        lblSelisihMemory = new javax.swing.JLabel();
        lblSelisihLogic = new javax.swing.JLabel();
        lblSelisihMath = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rancreid Study Advisor: Welcome");
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
        jLabel2.setText("Selamat Datang, namaAnu!");
        panelWelcome.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 50, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Silahkan ikuti instruksi di bawah untuk memasukkan nilai-nilai Anda secara otomatis.");
        panelWelcome.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 113, -1, -1));
        panelWelcome.add(labelStep2, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 160, 240, 240));

        labelStep1.setBackground(new java.awt.Color(51, 204, 255));
        panelWelcome.add(labelStep1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 240, 240));
        panelWelcome.add(labelStep4, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 160, 380, 240));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel3.setText("1");
        panelWelcome.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 400, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel4.setText("3");
        panelWelcome.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 400, -1, -1));

        jLabel5.setFont(new java.awt.Font("Oswald", 0, 36)); // NOI18N
        jLabel5.setText("4");
        panelWelcome.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1173, 512, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("login.");
        panelWelcome.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 500, 50, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 153, 255));
        jLabel8.setText("<html><u>Buka siakad.itera.ac.id</u></html>");
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        panelWelcome.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 470, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("<html>Pilih menu Kartu Hasil Studi yang<br>terletak pada menu bagian kiri.</html>");
        panelWelcome.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 470, 253, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("<html>Pilih semester lalu klik tampilkan. Setelah itu, blok tabel hasil studi dari nomor 1 hingga selesai, lalu salin dan tempel blok tersebut pada kotak teks input pada tahap selanjutnya.</html>");
        panelWelcome.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 460, 380, 127));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel14.setText("2");
        panelWelcome.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 400, -1, -1));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText(", lalu");
        panelWelcome.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 470, 50, -1));

        btnEditMataKuliah.setBackground(new java.awt.Color(51, 153, 255));
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

        lblEditMataKuliah.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah.setForeground(new java.awt.Color(255, 255, 255));
        lblEditMataKuliah.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah.setText("   Saya mengerti, lanjutkan");

        iconEditMataKuliah.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout btnEditMataKuliahLayout = new javax.swing.GroupLayout(btnEditMataKuliah);
        btnEditMataKuliah.setLayout(btnEditMataKuliahLayout);
        btnEditMataKuliahLayout.setHorizontalGroup(
            btnEditMataKuliahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliahLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblEditMataKuliah, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(iconEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnEditMataKuliahLayout.setVerticalGroup(
            btnEditMataKuliahLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(lblEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(iconEditMataKuliah, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panelWelcome.add(btnEditMataKuliah, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 610, 360, 60));

        btnEditMataKuliah4.setBackground(new java.awt.Color(236, 240, 241));
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
        lblEditMataKuliah4.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah4.setText("Log out");

        javax.swing.GroupLayout btnEditMataKuliah4Layout = new javax.swing.GroupLayout(btnEditMataKuliah4);
        btnEditMataKuliah4.setLayout(btnEditMataKuliah4Layout);
        btnEditMataKuliah4Layout.setHorizontalGroup(
            btnEditMataKuliah4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah4Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah4, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEditMataKuliah4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        btnEditMataKuliah4Layout.setVerticalGroup(
            btnEditMataKuliah4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah4Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah4, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelWelcome.add(btnEditMataKuliah4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 610, 180, 60));

        panelInput.setBackground(new java.awt.Color(255, 255, 255));
        panelInput.setPreferredSize(new java.awt.Dimension(1024, 720));
        panelInput.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel12.setText("Tambah Mata Kuliah");
        panelInput.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 50, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Silahkan salin semua tabel hasil studi (yang ada nilai) dari web siakad lalu tempelkan pada kotak teks di bawah");
        panelInput.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 107, -1, -1));

        textInput.setColumns(20);
        textInput.setRows(5);
        textInput.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                textInputInputMethodTextChanged(evt);
            }
        });
        jScrollPane1.setViewportView(textInput);

        panelInput.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 170, 410, 410));

        tabelAmbilMatkul.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Smt.", "Kode", "Mata Kuliah", "SKS", "Nilai"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelAmbilMatkul.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelAmbilMatkulMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelAmbilMatkul);
        if (tabelAmbilMatkul.getColumnModel().getColumnCount() > 0) {
            tabelAmbilMatkul.getColumnModel().getColumn(0).setResizable(false);
            tabelAmbilMatkul.getColumnModel().getColumn(0).setPreferredWidth(320);
            tabelAmbilMatkul.getColumnModel().getColumn(1).setResizable(false);
            tabelAmbilMatkul.getColumnModel().getColumn(1).setPreferredWidth(1);
            tabelAmbilMatkul.getColumnModel().getColumn(1).setHeaderValue("Logic");
            tabelAmbilMatkul.getColumnModel().getColumn(2).setResizable(false);
            tabelAmbilMatkul.getColumnModel().getColumn(2).setPreferredWidth(1);
            tabelAmbilMatkul.getColumnModel().getColumn(2).setHeaderValue("Math");
            tabelAmbilMatkul.getColumnModel().getColumn(3).setResizable(false);
            tabelAmbilMatkul.getColumnModel().getColumn(3).setPreferredWidth(1);
            tabelAmbilMatkul.getColumnModel().getColumn(3).setHeaderValue("Memory");
            tabelAmbilMatkul.getColumnModel().getColumn(4).setResizable(false);
            tabelAmbilMatkul.getColumnModel().getColumn(4).setPreferredWidth(4);
            tabelAmbilMatkul.getColumnModel().getColumn(4).setHeaderValue("Nilai");
        }

        panelInput.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, -1, 410));

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

        lblEditMataKuliah3.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah3.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEditMataKuliah3.setText("Lewati");

        javax.swing.GroupLayout btnEditMataKuliah3Layout = new javax.swing.GroupLayout(btnEditMataKuliah3);
        btnEditMataKuliah3.setLayout(btnEditMataKuliah3Layout);
        btnEditMataKuliah3Layout.setHorizontalGroup(
            btnEditMataKuliah3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEditMataKuliah3, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
        );
        btnEditMataKuliah3Layout.setVerticalGroup(
            btnEditMataKuliah3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah3Layout.createSequentialGroup()
                .addComponent(lblEditMataKuliah3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelInput.add(btnEditMataKuliah3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 610, 130, 60));

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
        lblEditMataKuliah1.setText("Simpan dan Lanjutkan");

        javax.swing.GroupLayout btnEditMataKuliah1Layout = new javax.swing.GroupLayout(btnEditMataKuliah1);
        btnEditMataKuliah1.setLayout(btnEditMataKuliah1Layout);
        btnEditMataKuliah1Layout.setHorizontalGroup(
            btnEditMataKuliah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah1Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEditMataKuliah1)
                .addGap(0, 31, Short.MAX_VALUE))
        );
        btnEditMataKuliah1Layout.setVerticalGroup(
            btnEditMataKuliah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah1Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah1, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelInput.add(btnEditMataKuliah1, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 610, 340, 60));

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

        lblEditMataKuliah7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah7.setForeground(new java.awt.Color(51, 51, 51));
        lblEditMataKuliah7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah7.setText("Kembali");

        javax.swing.GroupLayout btnEditMataKuliah7Layout = new javax.swing.GroupLayout(btnEditMataKuliah7);
        btnEditMataKuliah7.setLayout(btnEditMataKuliah7Layout);
        btnEditMataKuliah7Layout.setHorizontalGroup(
            btnEditMataKuliah7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah7Layout.createSequentialGroup()
                .addComponent(iconEditMataKuliah7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEditMataKuliah7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        btnEditMataKuliah7Layout.setVerticalGroup(
            btnEditMataKuliah7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah7Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah7, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelInput.add(btnEditMataKuliah7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 610, 180, 60));

        panelStatus.setBackground(new java.awt.Color(255, 255, 255));
        panelStatus.setPreferredSize(new java.awt.Dimension(1024, 720));
        panelStatus.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel15.setText("You're done!");
        panelStatus.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 50, -1, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel16.setText("Pilihan Bidang");
        panelStatus.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, -1, -1));
        panelStatus.add(lblIconUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 170, 144, 144));

        lblNama.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblNama.setText("Leonardo da Vinci");
        panelStatus.add(lblNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 600, 40));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setText("Memory Point");
        panelStatus.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, 130, 30));
        panelStatus.add(pbMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 300, 200, -1));

        lblNIM.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNIM.setForeground(new java.awt.Color(153, 153, 153));
        lblNIM.setText("14117000");
        panelStatus.add(lblNIM, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 160, 160, 40));

        lblMemoryPoint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMemoryPoint.setForeground(new java.awt.Color(102, 102, 102));
        lblMemoryPoint.setText("99");
        panelStatus.add(lblMemoryPoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 290, 40, 30));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setText("Math Point");
        panelStatus.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 130, 30));
        panelStatus.add(pbLogic, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 240, 200, -1));
        panelStatus.add(pbMath, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 270, 200, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel22.setText("Silahkan lihat status dan potensi yang Anda miliki");
        panelStatus.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 107, -1, -1));

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        btnEditMataKuliah6Layout.setVerticalGroup(
            btnEditMataKuliah6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnEditMataKuliah6Layout.createSequentialGroup()
                .addGroup(btnEditMataKuliah6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelStatus.add(btnEditMataKuliah6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 610, 180, 60));

        lblLogicPoint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLogicPoint.setForeground(new java.awt.Color(102, 102, 102));
        lblLogicPoint.setText("99");
        panelStatus.add(lblLogicPoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 230, 40, 30));

        lblMathPoint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblMathPoint.setForeground(new java.awt.Color(102, 102, 102));
        lblMathPoint.setText("99");
        panelStatus.add(lblMathPoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 260, 40, 30));

        tabelRekomendasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rekomendasi", "Bidang"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelRekomendasi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelRekomendasiMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tabelRekomendasi);
        if (tabelRekomendasi.getColumnModel().getColumnCount() > 0) {
            tabelRekomendasi.getColumnModel().getColumn(0).setResizable(false);
            tabelRekomendasi.getColumnModel().getColumn(0).setPreferredWidth(2);
            tabelRekomendasi.getColumnModel().getColumn(1).setResizable(false);
            tabelRekomendasi.getColumnModel().getColumn(1).setPreferredWidth(320);
        }

        panelStatus.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 390, 530, 190));
        panelStatus.add(pbSelectedMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 300, 200, -1));

        lblSelectedMemoryPoint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedMemoryPoint.setForeground(new java.awt.Color(102, 102, 102));
        lblSelectedMemoryPoint.setText("99");
        panelStatus.add(lblSelectedMemoryPoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 290, 40, 30));
        panelStatus.add(pbSelectedLogic, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 240, 200, -1));
        panelStatus.add(pbSelectedMath, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 270, 200, -1));

        lblSelectedLogicPoint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedLogicPoint.setForeground(new java.awt.Color(102, 102, 102));
        lblSelectedLogicPoint.setText("99");
        panelStatus.add(lblSelectedLogicPoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 230, 40, 30));

        lblSelectedMathPoint.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelectedMathPoint.setForeground(new java.awt.Color(102, 102, 102));
        lblSelectedMathPoint.setText("99");
        panelStatus.add(lblSelectedMathPoint, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 260, 40, 30));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setText("Logic Point");
        panelStatus.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 130, 30));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setText("Deskripsi");
        panelStatus.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 390, 130, 30));

        lblDeskripsi.setColumns(20);
        lblDeskripsi.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblDeskripsi.setForeground(new java.awt.Color(51, 51, 51));
        lblDeskripsi.setLineWrap(true);
        lblDeskripsi.setRows(5);
        lblDeskripsi.setWrapStyleWord(true);
        lblDeskripsi.setBorder(null);
        lblDeskripsi.setFocusable(false);
        jScrollPane4.setViewportView(lblDeskripsi);

        panelStatus.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 440, 330, 140));

        lblSelisihMemory.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelisihMemory.setForeground(new java.awt.Color(102, 102, 102));
        lblSelisihMemory.setText("99");
        panelStatus.add(lblSelisihMemory, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 290, 40, 30));

        lblSelisihLogic.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelisihLogic.setForeground(new java.awt.Color(102, 102, 102));
        lblSelisihLogic.setText("99");
        panelStatus.add(lblSelisihLogic, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 230, 40, 30));

        lblSelisihMath.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblSelisihMath.setForeground(new java.awt.Color(102, 102, 102));
        lblSelisihMath.setText("99");
        panelStatus.add(lblSelisihMath, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 260, 40, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(panelWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 1024, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelStatus, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(158, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(this.panelWelcome.isVisible()) Styling.centeringPanel(this, this.panelWelcome);
        if(this.panelInput.isVisible()) Styling.centeringPanel(this, panelInput);
        if(this.panelStatus.isVisible()) Styling.centeringPanel(this, panelStatus);
    }//GEN-LAST:event_formComponentResized

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        Styling.centeringPanel(this, this.panelWelcome);
        Styling.refreshSize(this);
        
        System.out.println(this.panelWelcome.getSize());
    }//GEN-LAST:event_formWindowActivated

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://siakad.itera.ac.id/"));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Oops, nampaknya url tidak bisa dibuka.");
        }
    }//GEN-LAST:event_jLabel8MouseClicked

    private void textInputInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_textInputInputMethodTextChanged
        
    }//GEN-LAST:event_textInputInputMethodTextChanged

    private void btnEditMataKuliahMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseClicked
        this.panelInput.setVisible(true);
        this.panelWelcome.setVisible(false);

        Styling.refreshSize(this);
    }//GEN-LAST:event_btnEditMataKuliahMouseClicked

    private void btnEditMataKuliahMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseEntered
        
    }//GEN-LAST:event_btnEditMataKuliahMouseEntered

    private void btnEditMataKuliahMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseExited
        
    }//GEN-LAST:event_btnEditMataKuliahMouseExited

    private void btnEditMataKuliahMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMousePressed
        
    }//GEN-LAST:event_btnEditMataKuliahMousePressed

    private void btnEditMataKuliahMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliahMouseReleased
        
    }//GEN-LAST:event_btnEditMataKuliahMouseReleased

    private void btnEditMataKuliah3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah3MouseClicked
        this.loadStatus();
        this.loadRecomendedFocus();
        this.panelInput.setVisible(false);
        this.panelStatus.setVisible(true);

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

    private void btnEditMataKuliah1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah1MouseClicked
        this.tableToDatabase();
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

    private void btnEditMataKuliah4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah4MouseClicked
        MainForm mainForm = new MainForm();
        mainForm.setVisible(true);
        this.dispose();
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
        this.panelInput.setVisible(true);
        this.panelStatus.setVisible(false);

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

    private void btnEditMataKuliah7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditMataKuliah7MouseClicked
        this.panelInput.setVisible(false);
        this.panelWelcome.setVisible(true);

        Styling.refreshSize(this);
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

    private void tabelAmbilMatkulMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelAmbilMatkulMouseClicked
        
        
        
    }//GEN-LAST:event_tabelAmbilMatkulMouseClicked

    private void tabelRekomendasiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelRekomendasiMouseClicked
        String selectedNama = ((DefaultTableModel)(this.tabelRekomendasi.getModel())).getValueAt(this.tabelRekomendasi.getSelectedRow(), 1).toString();
        
        for(int i=0; i<this.bidang.length; i++){
            if(selectedNama.equals(this.bidang[i].getNamaBidang())){
                int logicPoint = (int)(this.bidang[i].getLogicPoint());
                int mathPoint = (int)(this.bidang[i].getMathPoint());
                int memoryPoint = (int)(this.bidang[i].getMemoryPoint());
                
                pbSelectedLogic.setValue(logicPoint);
                pbSelectedMath.setValue(mathPoint);
                pbSelectedMemory.setValue(memoryPoint);
                
                lblSelectedLogicPoint.setText(Integer.toString(logicPoint));
                lblSelectedMathPoint.setText(Integer.toString(mathPoint));
                lblSelectedMemoryPoint.setText(Integer.toString(memoryPoint));
                
                
                int selisihLogic = ((int)this.session.getMahasiswa().getLogicPoint() - logicPoint);
                int selisihMath = ((int)this.session.getMahasiswa().getMathPoint() - mathPoint);
                int selisihMemory = ((int)this.session.getMahasiswa().getMemoryPoint() - memoryPoint);
                
                if(selisihLogic < 0) lblSelisihLogic.setForeground(Color.decode("#e74c3c"));
                else lblSelisihLogic.setForeground(Color.decode("#2ecc71"));
                if(selisihMath < 0) lblSelisihMath.setForeground(Color.decode("#e74c3c"));
                else lblSelisihMath.setForeground(Color.decode("#2ecc71"));
                if(selisihMemory < 0) lblSelisihMemory.setForeground(Color.decode("#e74c3c"));
                else lblSelisihMemory.setForeground(Color.decode("#2ecc71"));
                
                String textLogic = (selisihLogic < 0)? "" : "+";
                textLogic += Integer.toString(selisihLogic);
                String textMath = (selisihMath < 0)? "" : "+";
                textMath += Integer.toString(selisihMath);
                String textMemory = (selisihMemory < 0)? "" : "+";
                textMemory += Integer.toString(selisihMemory);
                        
                
                lblSelisihLogic.setText(textLogic);
                lblSelisihMath.setText(textMath);
                lblSelisihMemory.setText(textMemory);
                
                lblDeskripsi.setText(this.bidang[i].getDeskripsi());
                
                break;
            }
        }
        
        Styling.refreshSize(this);
    }//GEN-LAST:event_tabelRekomendasiMouseClicked

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
    private javax.swing.JPanel btnEditMataKuliah3;
    private javax.swing.JPanel btnEditMataKuliah4;
    private javax.swing.JPanel btnEditMataKuliah6;
    private javax.swing.JPanel btnEditMataKuliah7;
    private javax.swing.JLabel iconEditMataKuliah;
    private javax.swing.JLabel iconEditMataKuliah1;
    private javax.swing.JLabel iconEditMataKuliah4;
    private javax.swing.JLabel iconEditMataKuliah6;
    private javax.swing.JLabel iconEditMataKuliah7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel labelStep1;
    private javax.swing.JLabel labelStep2;
    private javax.swing.JLabel labelStep4;
    private javax.swing.JTextArea lblDeskripsi;
    private javax.swing.JLabel lblEditMataKuliah;
    private javax.swing.JLabel lblEditMataKuliah1;
    private javax.swing.JLabel lblEditMataKuliah3;
    private javax.swing.JLabel lblEditMataKuliah4;
    private javax.swing.JLabel lblEditMataKuliah6;
    private javax.swing.JLabel lblEditMataKuliah7;
    private javax.swing.JLabel lblIconUser;
    private javax.swing.JLabel lblLogicPoint;
    private javax.swing.JLabel lblMathPoint;
    private javax.swing.JLabel lblMemoryPoint;
    private javax.swing.JLabel lblNIM;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lblSelectedLogicPoint;
    private javax.swing.JLabel lblSelectedMathPoint;
    private javax.swing.JLabel lblSelectedMemoryPoint;
    private javax.swing.JLabel lblSelisihLogic;
    private javax.swing.JLabel lblSelisihMath;
    private javax.swing.JLabel lblSelisihMemory;
    private javax.swing.JPanel panelInput;
    private javax.swing.JPanel panelStatus;
    private javax.swing.JPanel panelWelcome;
    private javax.swing.JProgressBar pbLogic;
    private javax.swing.JProgressBar pbMath;
    private javax.swing.JProgressBar pbMemory;
    private javax.swing.JProgressBar pbSelectedLogic;
    private javax.swing.JProgressBar pbSelectedMath;
    private javax.swing.JProgressBar pbSelectedMemory;
    private javax.swing.JTable tabelAmbilMatkul;
    private javax.swing.JTable tabelRekomendasi;
    private javax.swing.JTextArea textInput;
    // End of variables declaration//GEN-END:variables
}
