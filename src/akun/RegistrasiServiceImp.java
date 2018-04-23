/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package akun;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gusty_g.n.m
 */
public class RegistrasiServiceImp implements RegistrasiService {

    @Override
    public void insert(Registrasi login) {
        DataRegistrasi.list.add(login);
        JOptionPane.showMessageDialog(null, "Registrasi berhasil!");
    }

}
