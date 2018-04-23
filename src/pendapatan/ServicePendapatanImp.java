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
public class ServicePendapatanImp implements ServicePendapatan {

    @Override
    public void insert(Pendapatan pendapatan) {
        DataPendapatan.listPendapatan.add(pendapatan);
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Transaksi", "Total Sewa","Denda"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Pendapatan m : DataPendapatan.listPendapatan) {
            Object[] kolom = new Object[3];
            kolom[0] = m.getIdTransaksi();
            kolom[1] = m.getTotalPendapatan();
            kolom[2] = m.getDenda();
            dtm.addRow(kolom);
        }
        return dtm;
    }

}
