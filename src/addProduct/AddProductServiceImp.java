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
public class AddProductServiceImp implements AddProductService {

    @Override
    public void insert(Transaksi transaksi) {
        DataAddProduct.listAddProduct.add(transaksi);
    }

    @Override
    public void delete(int index) {
        DataAddProduct.listAddProduct.remove(index);
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Product", "Jenis Product", "Nama Product", "Harga Sewa"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Transaksi m : DataAddProduct.listAddProduct) {
            Object[] kolom = new Object[5];
            kolom[0] = m.getProduct().getIdProduct();
            kolom[1] = m.getProduct().getJenisProduct();
            kolom[2] = m.getProduct().getNamaProduct();
            kolom[3] = m.getProduct().getHargaSewa();
            kolom[4] = m.getId();
            dtm.addRow(kolom);
        }
        return dtm;
    }

        @Override
    public List<Product> loadProducts() {
        return DataProduct.list;
    }

    @Override
    public void addItem(Transaksi transaksi) {
        DataAddProduct.listAddProduct.add(transaksi);
    }

}
