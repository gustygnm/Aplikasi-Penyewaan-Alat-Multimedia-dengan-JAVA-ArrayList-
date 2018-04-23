/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pendapatan;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gusty_g.n.m
 */
public interface ServicePendapatan {
    
    public void insert(Pendapatan pendapatan);

    public DefaultTableModel view();
}
