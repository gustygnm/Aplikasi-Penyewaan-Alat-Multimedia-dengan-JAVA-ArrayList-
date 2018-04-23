/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gusty_g.n.m
 */
public interface ProductService {

    public void insert(Product product);

    public void update(int index, Product product);

    public void delete(int index);

    public DefaultTableModel view();
}
