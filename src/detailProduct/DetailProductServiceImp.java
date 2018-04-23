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
public class DetailProductServiceImp implements DetailProductService {

    @Override
    public void insert(Transaksi transaksi) {
        DataDetailProduct.listDetailProduct.add(transaksi);
    }

    @Override
    public void delete(int index) {
        DataDetailProduct.listDetailProduct.remove(index);
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Transaksi","ID Product", "Jenis Product", "Nama Product", "Harga Sewa"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Transaksi m : DataDetailProduct.listDetailProduct) {
            Object[] kolom = new Object[5];
            kolom[0] = m.getId();
            kolom[1] = m.getProduct().getIdProduct();
            kolom[2] = m.getProduct().getJenisProduct();
            kolom[3] = m.getProduct().getNamaProduct();
            kolom[4] = m.getProduct().getHargaSewa();
            dtm.addRow(kolom);
        }
        return dtm;
    }
}
