package transaksi;

import java.text.DateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import pendapatan.Pendapatan;
import pendapatan.ServicePendapatanImp;
import product.Product;
import riwayat.Riwayat;
import riwayat.RiwayatServiceImp;
import transaksi.CekCustomer;
import transaksi.DetailProduct;

/**
 *
 * @author gusty_g.n.m
 */
public class TransaksiForm extends javax.swing.JFrame {

    private TransaksiService service;
    private CekCustomerService serviceCekCustomer = new CekCustomerServiceImp();
    private AddProductService serviceAddProduct = new AddProductServiceImp();
    private DetailProductService serviceDetailProduct = new DetailProductServiceImp();
    private DetailTransaksiService serviceproductYangDisewa = new DetailTransaksiServiceImpl();
    private pendapatan.ServicePendapatan servicePendapatan = new ServicePendapatanImp();
    private riwayat.RiwayatService serviceRiwayatService = new RiwayatServiceImp();
    private int productIndex;
    private int tag = 0;
    private int index;
    public int denda;

    public TransaksiForm() {
        initComponents();
        service = new TransaksiServiceImp();
        this.viewTransaksi();
        this.viewAddProduct();
        tfID.requestFocus();
        setLocationRelativeTo(null);
        loadProduct();
    }

//method Add item    
    //**********************************************************************
    public void addItem() {
        Transaksi item = new Transaksi();
        Product product = serviceAddProduct.loadProducts().get(productIndex);
        item.setId(tfID.getText());
        item.setProduct(product);
        serviceAddProduct.addItem(item);
    }

    public void viewAddProduct() {
        tableAddItem.setModel(serviceAddProduct.view());
    }

    public void loadProduct() {
        cmbNamaProduct.removeAllItems();
        cmbNamaProduct.addItem("... Pilih Product ...");
        for (Product p : serviceAddProduct.loadProducts()) {
            cmbNamaProduct.addItem(p.getNamaProduct());
        }
    }

    public void getProduct() {
        if (cmbNamaProduct.getSelectedIndex() > 0) {
            productIndex = cmbNamaProduct.getSelectedIndex() - 1;
        }
    }

    public void deleteItem() {
        int record = tableAddItem.getRowCount();
        if (record > 0) {
            int seleksi = tableAddItem.getSelectedRowCount();
            if (seleksi > 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(rootPane, "hapus Product?", "konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    index = tableAddItem.getSelectedRow();
                    serviceAddProduct.delete(index);
                    viewAddProduct();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Data masih kosong!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

//method detail product
    //*************************************************************************
    public void saveDetailProduct() {
        for (int i = 0; i < DataAddProduct.listAddProduct.size(); i++) {
            DataDetailProduct.listDetailProduct.add(DataAddProduct.listAddProduct.get(i));
        }
    }

//method Transaksi dan cek customer
    //************************************************************************
    public void viewTransaksi() {
        tableTransaksi.setModel(service.view());
    }

    public void saveTransaksi() {
        Transaksi r = new Transaksi();
        r.setId(tfID.getText());
        r.setNoIdentitas(tfNoIdentitas.getText());
        r.setJenisIdentitas(tfJenisIdentitas.getText());
        r.setNamaCustomer(tfNamaPenyewa.getText());
        r.setNoHp(tfNoHp.getText());
        r.setAlamat(txtAlamat.getText());
        r.setTglSewa(tfTanggalSewa.getText());
        r.setTglKembali(tfTanggalKembali.getText());
        r.setLamaSewa(tfLamaSewa.getText());
        r.setTotalHargaSewa(lblTotalHarga.getText());
        boolean already_exists = false;

        for (Transaksi m : DataTransaksi.listTransaksis) {
            if (m.getId().equals(tfID.getText())) {
                already_exists = true;
                JOptionPane.showMessageDialog(null, "Data sudah tersedia", "Warning", JOptionPane.WARNING_MESSAGE);
                tfID.requestFocus();
                break;
            }
        }
        if (DataTransaksi.listTransaksis.isEmpty() && DataCekCustomer.listCekCustomer.isEmpty()) {
            service.insert(r);
            serviceCekCustomer.insert(r);
        } else if (!DataTransaksi.listTransaksis.isEmpty() && !DataCekCustomer.listCekCustomer.isEmpty() && !already_exists) {
            service.insert(r);
            serviceCekCustomer.insert(r);
        }
    }

    public void deleteTransaksi() {
        index = tableTransaksi.getSelectedRow();
        service.delete(index);
        serviceCekCustomer.delete(index);
        DataDetaiTransaksi.listProductYangDisewa.clear();
    }

    public void hitungTotalSewa() {
        int hargaSewa = 0, totalHargaSewa = 0, lamaSewa, tampung1, tampung2;
        int record = tableAddItem.getRowCount();
        if (record == 1) {
            for (Transaksi m : DataAddProduct.listAddProduct) {
                hargaSewa = Integer.parseInt(m.getProduct().getHargaSewa());
            }
        } else if (record > 1) {
            for (Transaksi m : DataAddProduct.listAddProduct) {
                tampung1 = Integer.parseInt(m.getProduct().getHargaSewa());
                hargaSewa += tampung1;
                tampung1 = 0;
            }
        }
        lamaSewa = Integer.valueOf(tfLamaSewa.getText());
        totalHargaSewa = hargaSewa * lamaSewa;
        lblTotalHarga.setText(String.valueOf(totalHargaSewa));
    }

    public void reset() {
        tfID.setText("");
        tfID.requestFocus();
        tfNoIdentitas.setText("");
        tfJenisIdentitas.setText("");
        tfNamaPenyewa.setText("");
        tfNoHp.setText("");
        txtAlamat.setText("");
        tfLamaSewa.setText("0");
        tfTanggalSewa.setText("");
        tfTanggalKembali.setText("");
        lblTotalHarga.setText("0");
        DataAddProduct.listAddProduct.clear();
        viewAddProduct();
        loadProduct();
    }

    public void done() {
        int index = tableTransaksi.getSelectedRow();
        Pendapatan t = new Pendapatan();

        t.setIdTransaksi(tableTransaksi.getValueAt(index, 0).toString());
        t.setTotalPendapatan(Integer.valueOf(tableTransaksi.getValueAt(index, 4).toString()));
        t.setDenda(denda);

        if (pendapatan.DataPendapatan.listPendapatan.isEmpty()) {
            servicePendapatan.insert(t);
        } else if (!pendapatan.DataPendapatan.listPendapatan.isEmpty()) {
            servicePendapatan.insert(t);
        }
    }

    public void detailTransaksiCustomer() {
        index = tableTransaksi.getSelectedRow();
        Transaksi t = new Transaksi();
        DetailTransaksi tr = new DetailTransaksi();
        t.setId(tableTransaksi.getValueAt(index, 0).toString());
        for (Transaksi m : DataCekCustomer.listCekCustomer) {
            if (t.getId().equals(m.getId())) {
                tr.lblIdTransaksi.setText(t.getId());
                tr.lblTanggalTransaksi.setText(tableTransaksi.getValueAt(index, 1).toString());
                tr.lblNoIdentitas.setText(m.getNoIdentitas());
                tr.lblJenisIdentitas.setText(m.getJenisIdentitas());
                tr.lblNamaCustomer.setText(m.getNamaCustomer());
                tr.lblNoHp.setText(m.getNoHp());
                tr.txtAlamat.setText(m.getAlamat());
                tr.setVisible(true);
            }
        }
    }

    public void detailTransaksiProduct() {
        for (int i = 0; i < DataDetailProduct.listDetailProduct.size(); i++) {
            DataDetaiTransaksi.listProductYangDisewa.add(DataDetailProduct.listDetailProduct.get(i));
        }
    }

    public void saveRiwayat() {
        int index = tableTransaksi.getSelectedRow();
        String arrayProduct = "";
        riwayat.Riwayat getRiwayat = new Riwayat();
        CekCustomer cc = new CekCustomer();

        cc.setVisible(true);
        cc.hide();
        String id = tableTransaksi.getValueAt(index, 0).toString();
        String noIdentitas = CekCustomer.tableCekCustomer.getValueAt(index, 1).toString();
        String jenisIdentitas = CekCustomer.tableCekCustomer.getValueAt(index, 2).toString();
        String namaCustomer = CekCustomer.tableCekCustomer.getValueAt(index, 3).toString();
        String noHp = CekCustomer.tableCekCustomer.getValueAt(index, 4).toString();
        String alamat = CekCustomer.tableCekCustomer.getValueAt(index, 5).toString();
        String tglSewa = tableTransaksi.getValueAt(index, 1).toString();
        String lamaSewa = tableTransaksi.getValueAt(index, 2).toString();

        for (int i = 0; i < DataDetailProduct.listDetailProduct.size(); i++) {
            DetailProduct d = new DetailProduct();
            d.setVisible(true);
            d.hide();
            String tampungArray = DetailProduct.tableCekProduct.getValueAt(i, 1).toString();
            arrayProduct += tampungArray + " ; ";
        }

            
        getRiwayat.setIdTransaksi(id);

        getRiwayat.setNoIdentitas(noIdentitas);

        getRiwayat.setJenisIdentitas(jenisIdentitas);

        getRiwayat.setNamaCustomer(namaCustomer);

        getRiwayat.setNoHp(noHp);

        getRiwayat.setAlamat(alamat);

        getRiwayat.setTglSewa(tglSewa);

        getRiwayat.setLamaSewa(lamaSewa);

        getRiwayat.setIdProduct(arrayProduct);

        if (riwayat.DataRiwayat.listRiwayat.isEmpty()) {
            serviceRiwayatService.insert(getRiwayat);
        } else if (!riwayat.DataRiwayat.listRiwayat.isEmpty()) {
            serviceRiwayatService.insert(getRiwayat);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        tfID = new javax.swing.JTextField();
        tfNamaPenyewa = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        tfNoHp = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tfNoIdentitas = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        tfJenisIdentitas = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableTransaksi = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btnSewa = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        tfLamaSewa = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        tfTanggalSewa = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        cmbNamaProduct = new javax.swing.JComboBox<>();
        btnAddItem = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAddItem = new javax.swing.JTable();
        btnDelAddItem = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        lebel1 = new javax.swing.JLabel();
        lblTotalHarga = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        tfTanggalKembali = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btnCalcel = new javax.swing.JButton();
        btnCekCustomer = new javax.swing.JButton();
        btnFinish = new javax.swing.JButton();
        btnDetailProduct = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();

        setTitle("Transaksi");
        setMinimumSize(new java.awt.Dimension(1100, 690));

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("ID Transaksi");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Nama Customer");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Alamat");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("No. Hp");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Identitas Customer :");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("No. Identitas");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Jenis Identitas");

        jScrollPane3.setViewportView(txtAlamat);

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/img/medium 130.png"))); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel16)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel8)
                        .addComponent(jLabel15)))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tfID)
                    .addComponent(tfNoHp)
                    .addComponent(tfNamaPenyewa)
                    .addComponent(tfNoIdentitas)
                    .addComponent(tfJenisIdentitas)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNoIdentitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfJenisIdentitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNamaPenyewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfNoHp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );

        tableTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Transaksi", "Tanggal Sewa", "Lama Sewa", "Tanggal Kembali", "Total Sewa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableTransaksiMouseClicked(evt);
            }
        });
        tableTransaksi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableTransaksiKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tableTransaksi);

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));

        btnSewa.setText("Sewa");
        btnSewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSewaActionPerformed(evt);
            }
        });

        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSewa, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSewa)
                    .addComponent(btnReset))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(0, 102, 153));

        tfLamaSewa.setText("0");
        tfLamaSewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfLamaSewaActionPerformed(evt);
            }
        });
        tfLamaSewa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfLamaSewaKeyReleased(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Lama Sewa");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Tanggal Sewa");

        tfTanggalSewa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTanggalSewaMouseClicked(evt);
            }
        });
        tfTanggalSewa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTanggalSewaActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Nama Product");

        cmbNamaProduct.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        cmbNamaProduct.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "... Pilih Product ..." }));
        cmbNamaProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbNamaProductActionPerformed(evt);
            }
        });

        btnAddItem.setText("Tambah");
        btnAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddItemActionPerformed(evt);
            }
        });

        tableAddItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Product", "Jenis Product", "Nama Product", "Harga Sewa"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableAddItem);

        btnDelAddItem.setText("Hapus");
        btnDelAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelAddItemActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Total Sewa :");

        lebel1.setFont(new java.awt.Font("Tahoma", 0, 28)); // NOI18N
        lebel1.setForeground(new java.awt.Color(255, 255, 255));
        lebel1.setText("Rp. ");

        lblTotalHarga.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        lblTotalHarga.setText("0");
        lblTotalHarga.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        lblTotalHarga.setEnabled(false);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Tanggal Kembali");

        tfTanggalKembali.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tfTanggalKembaliMouseClicked(evt);
            }
        });
        tfTanggalKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfTanggalKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfTanggalSewa)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(tfLamaSewa)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(cmbNamaProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(tfTanggalKembali)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lebel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTotalHarga)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(cmbNamaProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAddItem)
                    .addComponent(btnDelAddItem))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfLamaSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTanggalSewa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTanggalKembali, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTotalHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lebel1))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 102, 153));

        btnCalcel.setText("Cancel");
        btnCalcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalcelActionPerformed(evt);
            }
        });

        btnCekCustomer.setText("Cek Customer");
        btnCekCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCekCustomerActionPerformed(evt);
            }
        });

        btnFinish.setText("Done!");
        btnFinish.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinishActionPerformed(evt);
            }
        });

        btnDetailProduct.setText("Detail Transaksi");
        btnDetailProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetailProductActionPerformed(evt);
            }
        });

        jButton3.setText("Cek Product");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCalcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCekCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFinish, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDetailProduct, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnCekCustomer)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDetailProduct)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFinish)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCalcel)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel5.setBackground(new java.awt.Color(0, 102, 153));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Menu Transaksi");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(810, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(399, 399, 399)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(202, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableTransaksiMouseClicked

    }//GEN-LAST:event_tableTransaksiMouseClicked

    private void btnSewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSewaActionPerformed
        if (tfID.getText().trim().equals("") || tfNoIdentitas.getText().trim().equals("") || tfJenisIdentitas.getText().trim().equals("") || tfNamaPenyewa.getText().trim().equals("") || tfNoHp.getText().trim().equals("")
                || txtAlamat.getText().trim().equals("") || serviceAddProduct.loadProducts().isEmpty() || tfLamaSewa.getText().equals("0") || tfLamaSewa.getText().equals(null) || tfTanggalSewa.getText().equals(null)) {
            JOptionPane.showMessageDialog(null, "Maaf... Data belum lengkap!");
            tfID.requestFocus();
        } else {
            saveTransaksi();
            saveDetailProduct();
            viewTransaksi();
            reset();
        }
    }//GEN-LAST:event_btnSewaActionPerformed

    private void btnCalcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalcelActionPerformed
        int record = tableTransaksi.getRowCount();
        if (record > 0) {
            int seleksi = tableTransaksi.getSelectedRowCount();
            if (seleksi > 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(rootPane, "Cancel Penyewaan.\nData akan dihapus?", "konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    deleteTransaksi();
                    DataDetailProduct.listDetailProduct.clear();
                    viewTransaksi();
                    reset();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data yang ingin dihapus!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Data masih kosong!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnCalcelActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        reset();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnCekCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCekCustomerActionPerformed
        new CekCustomer().setVisible(true);
    }//GEN-LAST:event_btnCekCustomerActionPerformed

    private void btnFinishActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinishActionPerformed
        int record = tableTransaksi.getRowCount();
        if (record > 0) {
            int seleksi = tableTransaksi.getSelectedRowCount();
            if (seleksi > 0) {
                int konfirmasi = JOptionPane.showConfirmDialog(rootPane, "Transaksi selesai! \nHapus dari daftar transaksi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (konfirmasi == JOptionPane.YES_OPTION) {
                    denda = Integer.parseInt(JOptionPane.showInputDialog(this, "Input Denda", JOptionPane.OK_CANCEL_OPTION));
                    done();
                    saveRiwayat();
                    deleteTransaksi();
                    DataDetailProduct.listDetailProduct.clear();
                    viewTransaksi();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Data masih kosong!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnFinishActionPerformed

    private void tfTanggalSewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTanggalSewaActionPerformed

    }//GEN-LAST:event_tfTanggalSewaActionPerformed

    private void tfTanggalSewaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTanggalSewaMouseClicked
        DateFormat df = new java.text.SimpleDateFormat("YYYY-MM-dd");
        Date tglS = new Date();
        String tglSekarang = df.format(tglS);
        tfTanggalSewa.setText(String.valueOf(tglSekarang));
    }//GEN-LAST:event_tfTanggalSewaMouseClicked

    private void btnDetailProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetailProductActionPerformed
        int record = tableTransaksi.getRowCount();
        if (record > 0) {
            int seleksi = tableTransaksi.getSelectedRowCount();
            if (seleksi > 0) {
                DataDetaiTransaksi.listProductYangDisewa.clear();
                detailTransaksiProduct();
                detailTransaksiCustomer();
            } else {
                JOptionPane.showMessageDialog(null, "Pilih data terlebih dahulu", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Data masih kosong!", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_btnDetailProductActionPerformed

    private void btnAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddItemActionPerformed
        addItem();
        viewAddProduct();
    }//GEN-LAST:event_btnAddItemActionPerformed

    private void cmbNamaProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbNamaProductActionPerformed
        getProduct();
    }//GEN-LAST:event_cmbNamaProductActionPerformed

    private void btnDelAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelAddItemActionPerformed
        deleteItem();
        viewAddProduct();
    }//GEN-LAST:event_btnDelAddItemActionPerformed

    private void tfLamaSewaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfLamaSewaKeyReleased
        hitungTotalSewa();
    }//GEN-LAST:event_tfLamaSewaKeyReleased

    private void tableTransaksiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableTransaksiKeyPressed

    }//GEN-LAST:event_tableTransaksiKeyPressed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        new DetailProduct().setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void tfTanggalKembaliMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tfTanggalKembaliMouseClicked
        DateFormat df = new java.text.SimpleDateFormat("YYYY-MM-dd");
        Date tglS = new Date();
        String tglSekarang = df.format(tglS);
        tfTanggalKembali.setText(String.valueOf(tglSekarang));
    }//GEN-LAST:event_tfTanggalKembaliMouseClicked

    private void tfTanggalKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfTanggalKembaliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTanggalKembaliActionPerformed

    private void tfLamaSewaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfLamaSewaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfLamaSewaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TransaksiForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransaksiForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransaksiForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransaksiForm.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransaksiForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddItem;
    private javax.swing.JButton btnCalcel;
    private javax.swing.JButton btnCekCustomer;
    private javax.swing.JButton btnDelAddItem;
    private javax.swing.JButton btnDetailProduct;
    private javax.swing.JButton btnFinish;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSewa;
    private javax.swing.JComboBox<String> cmbNamaProduct;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField lblTotalHarga;
    private javax.swing.JLabel lebel1;
    private javax.swing.JTable tableAddItem;
    public static javax.swing.JTable tableTransaksi;
    private javax.swing.JTextField tfID;
    private javax.swing.JTextField tfJenisIdentitas;
    private javax.swing.JTextField tfLamaSewa;
    private javax.swing.JTextField tfNamaPenyewa;
    private javax.swing.JTextField tfNoHp;
    private javax.swing.JTextField tfNoIdentitas;
    private javax.swing.JTextField tfTanggalKembali;
    private javax.swing.JTextField tfTanggalSewa;
    private javax.swing.JTextPane txtAlamat;
    // End of variables declaration//GEN-END:variables
}
