package pendapatan;

import transaksi.Transaksi;

/**
 *
 * @author gusty_g.n.m
 */
public class Pendapatan {

    private String idTransaksi;
    private int denda;
    private int totalPendapatan;

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getDenda() {
        return denda;
    }

    public void setDenda(int denda) {
        this.denda = denda;
    }

    public int getTotalPendapatan() {
        return totalPendapatan;
    }

    public void setTotalPendapatan(int totalPendapatan) {
        this.totalPendapatan = totalPendapatan;
    }

}
