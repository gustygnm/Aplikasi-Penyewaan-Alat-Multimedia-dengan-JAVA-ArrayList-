/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import product.ProductForm;

/**
 *
 * @author gusty_g.n.m
 */
public class ProductServiceImp implements ProductService {

    @Override
    public void insert(Product product) {
        DataProduct.list.add(product);
        JOptionPane.showMessageDialog(null, "Data berhasil ditambah!");
    }

    @Override
    public void update(int index, Product product) {
        DataProduct.list.set(index, product);
        JOptionPane.showMessageDialog(null, "Update berhasil!");
    }

    @Override
    public void delete(int index) {
        DataProduct.list.remove(index);
        JOptionPane.showMessageDialog(null, "Data telah dihapus!");
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Product", "Jenis Product", "Nama Product", "Harga Sewa"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Product m : DataProduct.list) {
            Object[] kolom = new Object[4];
            kolom[0] = m.getIdProduct();
            kolom[1] = m.getJenisProduct();
            kolom[2] = m.getNamaProduct();
            kolom[3] = m.getHargaSewa();
            dtm.addRow(kolom);
        }
        return dtm;
    }

}
