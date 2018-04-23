/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package riwayat;

import javax.swing.table.DefaultTableModel;
import riwayat.DataRiwayat;

/**
 *
 * @author gusty_g.n.m
 */
public class RiwayatServiceImp implements RiwayatService {

    @Override
    public void insert(Riwayat riwayat) {
        DataRiwayat.listRiwayat.add(riwayat);
    }

    @Override
    public DefaultTableModel view() {
        String[] judul = {"ID Transaksi", "No Identitas", "Jenis Identitas", "Nama Customer","No Hp","Alamat","Tanggal Sewa","Lama Sewa","ID Product"};
        DefaultTableModel dtm = new DefaultTableModel(null, judul);
        for (Riwayat m : DataRiwayat.listRiwayat) {
            Object[] kolom = new Object[9];
            kolom[0] = m.getIdTransaksi();
            kolom[1] = m.getNoIdentitas();
            kolom[2] = m.getJenisIdentitas();
            kolom[3] = m.getNamaCustomer();
            kolom[4] = m.getNoHp();
            kolom[5] = m.getAlamat();
            kolom[6] = m.getTglSewa();
            kolom[7] = m.getLamaSewa();
            kolom[8] = m.getIdProduct();
            dtm.addRow(kolom);
        }
        return dtm;
    }

}
