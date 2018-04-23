/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaksi;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import product.Product;

/**
 *
 * @author gusty_g.n.m
 */
public interface TransaksiService {

    public void insert(Transaksi transaksi);

    public void update(int index, Transaksi transaksi);

    public void delete(int index);

    public DefaultTableModel view();
}
