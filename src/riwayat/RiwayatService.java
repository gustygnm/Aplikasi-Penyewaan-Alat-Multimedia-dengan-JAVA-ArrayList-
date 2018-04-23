/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riwayat;

import javax.swing.table.DefaultTableModel;
import product.Product;

/**
 *
 * @author gusty_g.n.m
 */
public interface RiwayatService {

    public void insert(Riwayat riwayat);

    public DefaultTableModel view();
}
