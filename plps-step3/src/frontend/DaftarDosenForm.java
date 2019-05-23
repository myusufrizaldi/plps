package frontend;

import backend.Database;
import backend.ReturnLoginJSON;
import backend.Security;
import backend.Session;
import component.*;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;



public class DaftarDosenForm extends javax.swing.JFrame {

    private Dimension screenSize;
    private Database db;
    private Session session;
    private Security security;
    private ImageIcon icon;
    int id_fokus=0;
    private BufferedImage bImage;
    private ImageIcon iconLogin;
    private ImageIcon iconRegister;
    private ImageIcon iconBack;

    
    /**
     * Creates new form MainForm
     */
    public DaftarDosenForm() {
        //hai
        initComponents();
        this.icon = new ImageIcon(DaftarDosenForm.class.getResource("/res/LOGO-StudyAdvisor.png"));
        this.setIconImage(this.icon.getImage());
        
        this.setMinimumSize(Styling.WXGA_SCREEN);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); //SetFullscreen
        
        this.db = new Database();
        this.security = new Security();
        this.session = new Session();
        
        this.panelLogin.setVisible(false);
        this.panelDaftar.setVisible(true);
        
        db.showComboFokusDaftarDosen(this);
        
//           CmbFokus.setSelectedIndex(id_fokus);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelLogin = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        textNomorIndukLogin = new CustomTextField();
        jLabel3 = new javax.swing.JLabel();
        textPasswordLogin = new CustomPasswordField();
        lblDaftar = new javax.swing.JLabel();
        btnLogIn = new CustomPrimaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Login.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconLogin = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah5 = new javax.swing.JLabel(this.iconLogin);
        lblEditMataKuliah5 = new javax.swing.JLabel();
        panelDaftar = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        textNamaLengkapDaftar = new CustomTextField();
        jLabel6 = new javax.swing.JLabel();
        textNomorIndukDaftar = new CustomTextField();
        jLabel7 = new javax.swing.JLabel();
        textPasswordDaftar = new CustomPasswordField();
        btnDaftar = new CustomPrimaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Check.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconRegister = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah6 = new javax.swing.JLabel(this.iconRegister);
        lblEditMataKuliah6 = new javax.swing.JLabel();
        btnBackToLogIn = new CustomSecondaryButton();
        this.bImage = null;
        try {
            bImage = ImageIO.read(new File(DosenForm.class.getResource("/res/ICON-Back.png").getFile()));
            Image image = bImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            this.iconBack = new ImageIcon(image);
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        iconEditMataKuliah7 = new javax.swing.JLabel(this.iconBack);
        jLabel8 = new javax.swing.JLabel();
        CmbFokus = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Rancreid Study Advisor: Informatics Engineering");
        setFont(new java.awt.Font("Oswald", 0, 10)); // NOI18N
        setForeground(new java.awt.Color(0, 0, 0));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        panelLogin.setBackground(new java.awt.Color(255, 255, 255));
        panelLogin.setPreferredSize(new java.awt.Dimension(400, 560));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setText("Admin/NIM/NIP");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel2.setText("LOGIN");

        textNomorIndukLogin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        textNomorIndukLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNomorIndukLoginKeyPressed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel3.setText("Password");

        textPasswordLogin.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        textPasswordLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textPasswordLoginKeyPressed(evt);
            }
        });

        lblDaftar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        lblDaftar.setForeground(new java.awt.Color(51, 153, 255));
        lblDaftar.setText("Daftar");
        lblDaftar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lblDaftar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDaftarMouseClicked(evt);
            }
        });

        btnLogIn.setBackground(new java.awt.Color(51, 153, 255));
        btnLogIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnLogInMouseClicked(evt);
            }
        });

        iconEditMataKuliah5.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah5.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah5.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah5.setForeground(new java.awt.Color(255, 255, 255));
        lblEditMataKuliah5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah5.setText("Log In");

        javax.swing.GroupLayout btnLogInLayout = new javax.swing.GroupLayout(btnLogIn);
        btnLogIn.setLayout(btnLogInLayout);
        btnLogInLayout.setHorizontalGroup(
            btnLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnLogInLayout.createSequentialGroup()
                .addComponent(iconEditMataKuliah5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblEditMataKuliah5, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        btnLogInLayout.setVerticalGroup(
            btnLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnLogInLayout.createSequentialGroup()
                .addGroup(btnLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah5, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelLoginLayout = new javax.swing.GroupLayout(panelLogin);
        panelLogin.setLayout(panelLoginLayout);
        panelLoginLayout.setHorizontalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(panelLoginLayout.createSequentialGroup()
                            .addComponent(lblDaftar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(textNomorIndukLogin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                            .addComponent(textPasswordLogin, javax.swing.GroupLayout.Alignment.LEADING))))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLoginLayout.createSequentialGroup()
                    .addGap(48, 48, 48)
                    .addComponent(jLabel2)
                    .addContainerGap(246, Short.MAX_VALUE)))
        );
        panelLoginLayout.setVerticalGroup(
            panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLoginLayout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textNomorIndukLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textPasswordLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLoginLayout.createSequentialGroup()
                        .addComponent(lblDaftar)
                        .addGap(13, 13, 13)))
                .addGap(46, 46, 46))
            .addGroup(panelLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLoginLayout.createSequentialGroup()
                    .addGap(36, 36, 36)
                    .addComponent(jLabel2)
                    .addContainerGap(476, Short.MAX_VALUE)))
        );

        panelDaftar.setBackground(new java.awt.Color(255, 255, 255));
        panelDaftar.setPreferredSize(new java.awt.Dimension(400, 560));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel4.setText("DAFTAR DOSEN");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel5.setText("Nama Lengkap");

        textNamaLengkapDaftar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        textNamaLengkapDaftar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNamaLengkapDaftarKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setText("NIP");

        textNomorIndukDaftar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        textNomorIndukDaftar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textNomorIndukDaftarKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel7.setText("Password");

        textPasswordDaftar.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        textPasswordDaftar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textPasswordDaftarKeyPressed(evt);
            }
        });

        btnDaftar.setBackground(new java.awt.Color(51, 153, 255));
        btnDaftar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDaftarMouseClicked(evt);
            }
        });

        iconEditMataKuliah6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah6.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblEditMataKuliah6.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        lblEditMataKuliah6.setForeground(new java.awt.Color(255, 255, 255));
        lblEditMataKuliah6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblEditMataKuliah6.setText("Daftar");

        javax.swing.GroupLayout btnDaftarLayout = new javax.swing.GroupLayout(btnDaftar);
        btnDaftar.setLayout(btnDaftarLayout);
        btnDaftarLayout.setHorizontalGroup(
            btnDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnDaftarLayout.createSequentialGroup()
                .addComponent(iconEditMataKuliah6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(lblEditMataKuliah6, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnDaftarLayout.setVerticalGroup(
            btnDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btnDaftarLayout.createSequentialGroup()
                .addGroup(btnDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(iconEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEditMataKuliah6, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnBackToLogIn.setBackground(new java.awt.Color(236, 240, 241));
        btnBackToLogIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnBackToLogInMouseClicked(evt);
            }
        });

        iconEditMataKuliah7.setFont(new java.awt.Font("Roboto", 0, 24)); // NOI18N
        iconEditMataKuliah7.setForeground(new java.awt.Color(51, 51, 51));
        iconEditMataKuliah7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout btnBackToLogInLayout = new javax.swing.GroupLayout(btnBackToLogIn);
        btnBackToLogIn.setLayout(btnBackToLogInLayout);
        btnBackToLogInLayout.setHorizontalGroup(
            btnBackToLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnBackToLogInLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(iconEditMataKuliah7, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        btnBackToLogInLayout.setVerticalGroup(
            btnBackToLogInLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btnBackToLogInLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(iconEditMataKuliah7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel8.setText("Fokus ");

        CmbFokus.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CmbFokusItemStateChanged(evt);
            }
        });
        CmbFokus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                CmbFokusMouseClicked(evt);
            }
        });
        CmbFokus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmbFokusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelDaftarLayout = new javax.swing.GroupLayout(panelDaftar);
        panelDaftar.setLayout(panelDaftarLayout);
        panelDaftarLayout.setHorizontalGroup(
            panelDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDaftarLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(panelDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(panelDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel8)
                        .addComponent(jLabel6)
                        .addComponent(textNamaLengkapDaftar)
                        .addComponent(jLabel5)
                        .addComponent(textNomorIndukDaftar)
                        .addGroup(panelDaftarLayout.createSequentialGroup()
                            .addComponent(btnBackToLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(56, 56, 56)
                            .addComponent(btnDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel4)
                        .addComponent(textPasswordDaftar)
                        .addComponent(CmbFokus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        panelDaftarLayout.setVerticalGroup(
            panelDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelDaftarLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textNamaLengkapDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textNomorIndukDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(CmbFokus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addGap(7, 7, 7)
                .addComponent(textPasswordDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addGroup(panelDaftarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBackToLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(324, 324, 324)
                .addComponent(panelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(panelDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(307, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelDaftar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void refreshSize() {
        if(this.getExtendedState() == JFrame.MAXIMIZED_BOTH){
            //this.setSize(this.getWidth(), this.getHeight() - 1);
            this.setExtendedState(JFrame.NORMAL);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }else{
            this.setSize(this.getWidth(), this.getHeight() - 1);
            this.setSize(this.getWidth(), this.getHeight() + 1);
        }
    }
    
    private void centeringPanel(JPanel panel) {
        if(this.screenSize != null) panel.setLocation((int) ((this.screenSize.getWidth() / 2) - (panel.getWidth() / 2)), (int) ((this.screenSize.getHeight() / 2) - (panel.getHeight() / 2)));  
        else panel.setLocation((int) ((this.getWidth() / 2) - (panel.getWidth() / 2)), (int) ((this.getHeight() / 2) - (panel.getHeight() / 2)));
    }
    
    private void daftar() {
        System.out.println("idf="+id_fokus);
        ReturnLoginJSON json = this.db.daftarDosen(this.textNamaLengkapDaftar.getText(), this.textNomorIndukDaftar.getText(), this.textPasswordDaftar.getText(),id_fokus);
        JOptionPane.showMessageDialog(this, json.getMessage());
        
        System.out.println("json="+json.getMessage());
        if(json.isSuccess()){
            this.session = json.getSession();
            this.textNamaLengkapDaftar.setText("");
            this.textNomorIndukDaftar.setText("");
            this.textPasswordDaftar.setText("");
            this.CmbFokus.removeAllItems();
            
            DosenForm DosenForm = new DosenForm(this.db, this.session);
            DosenForm.setVisible(true);
            this.dispose();
        }
    }
    
    private void login() {
        ReturnLoginJSON json = this.db.login(this.textNomorIndukLogin.getText(), this.textPasswordLogin.getText());
        JOptionPane.showMessageDialog(null, json.getMessage());
        if(json.isSuccess()){
            this.session = json.getSession();
            this.textNomorIndukLogin.setText("");
        }
        System.out.println("session="+json.getSession());
        System.out.println("pass="+textPasswordLogin.getText());
        System.out.println("json="+json);
        this.textPasswordLogin.setText("");
        
        System.out.println("json="+json.isSuccess());
        if(json.isSuccess()){
            System.out.println("jsonstat="+json.getSession().getStatus());
            if(json.getSession().getStatus() == 1){
                WelcomeForm welcomeForm = new WelcomeForm(this.db, this.session);
                welcomeForm.setVisible(true);
            }else if(json.getSession().getStatus() == 2){
                DosenForm dosenForm = new DosenForm(this.db, this.session);
                dosenForm.setVisible(true);
            }
            else if (json.getSession().getStatus() == 3){
                AdminForm adminForm = new AdminForm(this.db, this.session);
                adminForm.setVisible(true);
//                WelcomeForm welcomeForm = new WelcomeForm(this.db, this.session);
//                welcomeForm.setVisible(true);
            }
            else{
                System.out.println("gagal");
            }
            
            this.dispose();
            
        }
    }
    
    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        if(this.panelLogin.isVisible()) Styling.centeringPanel(this, panelLogin);
        if(this.panelDaftar.isVisible()) Styling.centeringPanel(this, panelDaftar);
    }//GEN-LAST:event_formComponentResized

    private void textNomorIndukLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNomorIndukLoginKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.login();
        }
    }//GEN-LAST:event_textNomorIndukLoginKeyPressed

    private void textPasswordLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPasswordLoginKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.login();
        }
    }//GEN-LAST:event_textPasswordLoginKeyPressed

    private void textNamaLengkapDaftarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNamaLengkapDaftarKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.daftar();
        }
    }//GEN-LAST:event_textNamaLengkapDaftarKeyPressed

    private void textNomorIndukDaftarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNomorIndukDaftarKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.daftar();
        }
    }//GEN-LAST:event_textNomorIndukDaftarKeyPressed

    private void textPasswordDaftarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textPasswordDaftarKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER){
            this.daftar();
        }
    }//GEN-LAST:event_textPasswordDaftarKeyPressed

    private void lblDaftarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDaftarMouseClicked
        this.panelDaftar.setVisible(true);
        this.panelLogin.setVisible(false);
        
        Styling.refreshSize(this);
        this.textNamaLengkapDaftar.grabFocus();
    }//GEN-LAST:event_lblDaftarMouseClicked

    private void btnLogInMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnLogInMouseClicked
        this.login();
    }//GEN-LAST:event_btnLogInMouseClicked

    private void btnDaftarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDaftarMouseClicked
        this.daftar();
    }//GEN-LAST:event_btnDaftarMouseClicked

    private void btnBackToLogInMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnBackToLogInMouseClicked
        this.panelDaftar.setVisible(false);
        this.panelLogin.setVisible(true);
        
        Styling.refreshSize(this);
        this.textNomorIndukLogin.grabFocus();
    }//GEN-LAST:event_btnBackToLogInMouseClicked

    private void CmbFokusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmbFokusActionPerformed
        // TODO add your handling code here:
//         CmbFokus.setSelectedIndex(id_fokus);
       
//         CmbFokus.setSelectedIndex(id_fokus);
        
    }//GEN-LAST:event_CmbFokusActionPerformed

    private void CmbFokusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_CmbFokusMouseClicked
        // TODO add your handling code here:
         CmbFokus.removeAllItems();     
         
         db.showComboFokusDaftarDosen(this);   
//        int id_fokus=CmbFokus.getSelectedIndex()+1;
           
//           CmbFokus.setSelectedIndex(id_fokus);
        
        Styling.refreshSize(this);   
    }//GEN-LAST:event_CmbFokusMouseClicked

    private void CmbFokusItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CmbFokusItemStateChanged
        // TODO add your handling code here:
        id_fokus = CmbFokus.getSelectedIndex()+1;
           System.out.println("id"+id_fokus);
    }//GEN-LAST:event_CmbFokusItemStateChanged

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JComboBox CmbFokus;
    private javax.swing.JPanel btnBackToLogIn;
    private javax.swing.JPanel btnDaftar;
    private javax.swing.JPanel btnLogIn;
    private javax.swing.JLabel iconEditMataKuliah5;
    private javax.swing.JLabel iconEditMataKuliah6;
    private javax.swing.JLabel iconEditMataKuliah7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel lblDaftar;
    private javax.swing.JLabel lblEditMataKuliah5;
    private javax.swing.JLabel lblEditMataKuliah6;
    private javax.swing.JPanel panelDaftar;
    private javax.swing.JPanel panelLogin;
    private javax.swing.JTextField textNamaLengkapDaftar;
    private javax.swing.JTextField textNomorIndukDaftar;
    private javax.swing.JTextField textNomorIndukLogin;
    private javax.swing.JPasswordField textPasswordDaftar;
    private javax.swing.JPasswordField textPasswordLogin;
    // End of variables declaration//GEN-END:variables
}
