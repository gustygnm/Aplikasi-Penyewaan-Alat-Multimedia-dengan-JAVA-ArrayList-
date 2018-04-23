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
public class TransaksiServiceImp implements TransaksiService {

    @Override
    public void insert(Transaksi transaksi) {
        DataTransaksi.listTransaksis.add(transaksi);
        JOptionPane.showMessageDialog(null, "Data berhasil ditambah!");
    }

    @Override
    public void update(int index, Transaksi transaksi) {
        DataTransaksi.listTransaksis.set(index, transaksi);
        JOptionPane.showMessageDialog(null, "Update berhasil!");
    }

    @Override
    public void delete(int index) {
        DataTransaksi.listTransaksis.remove(index);
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Transaksi", "Tanggal Sewa", "Lama Sewa","Tanggal Kembali", "Total Sewa"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Transaksi m : DataTransaksi.listTransaksis) {
            Object[] kolom = new Object[5];
            kolom[0] = m.getId();
            kolom[1] = m.getTglSewa();
            kolom[2] = m.getLamaSewa();
            kolom[3] = m.getTglKembali();
            kolom[4] = m.getTotalHargaSewa();
            dtm.addRow(kolom);
        }
        return dtm;
    }

}
