/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaksi;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gusty_g.n.m
 */
public class DetailTransaksiServiceImpl implements DetailTransaksiService {

    @Override
    public void delete(int index) {
        DataDetaiTransaksi.listProductYangDisewa.remove(index);
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Product", "Jenis Product", "Nama Product", "Harga Sewa"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Transaksi p : DataDetaiTransaksi.listProductYangDisewa) {
            Object[] kolom = new Object[4];
            kolom[0] = p.getProduct().getIdProduct();
            kolom[1] = p.getProduct().getJenisProduct();
            kolom[2] = p.getProduct().getNamaProduct();
            kolom[3] = p.getProduct().getHargaSewa();
            dtm.addRow(kolom);
        }
        return dtm;
    }

    @Override
    public void insert(Transaksi transaksi) {
        DataDetaiTransaksi.listProductYangDisewa.add(transaksi);
    }
}
