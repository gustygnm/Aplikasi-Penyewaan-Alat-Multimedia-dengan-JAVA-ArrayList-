/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaksi;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import product.DataProduct;
import product.Product;

/**
 *
 * @author gusty_g.n.m
 */
public class CekCustomerServiceImp implements CekCustomerService {

    @Override
    public void insert(Transaksi transaksi) {
        DataCekCustomer.listCekCustomer.add(transaksi);
    }
    @Override
    public void delete(int index) {
        DataCekCustomer.listCekCustomer.remove(index);
    }
    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Transaksi", "No. Identitas", "Jenis Identitas", "Nama Customer", "No. Hp", "Alamat"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Transaksi m : DataCekCustomer.listCekCustomer) {
            Object[] kolom = new Object[6];
            kolom[0] = m.getId();
            kolom[1] = m.getNoIdentitas();
            kolom[2] = m.getJenisIdentitas();
            kolom[3] = m.getNamaCustomer();
            kolom[4] = m.getNoHp();
            kolom[5] = m.getAlamat();
            dtm.addRow(kolom);
        }
        return dtm;
    }
}
