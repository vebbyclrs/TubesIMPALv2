/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author vebbyclrs
 */
public class DatabaseConnection {
    private Connection conn;
    private Statement sta;
    
    public void connect()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/db_tubes_impal", "root", "");
            sta = conn.createStatement();
        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
     public void disconnect()
    {
        try
        {
            conn.close();
            sta.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public boolean manipulate(String query)
    {
        boolean berhasil = false;
        try
        {
            int rows = sta.executeUpdate(query);
            if (rows > 0)
            {
                berhasil = true;
            }

        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

        return berhasil;

    }

    public ResultSet getData(String query)
    {
        ResultSet rs = null;
        try
        {
            rs = sta.executeQuery(query);
        } catch (Exception ex)
        {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    public boolean saveMahasiswa (Mahasiswa m) {
        System.out.println(m.getTanggalLahir());
//            java.time.LocalDate dateDsn = new LocalDate;
            java.sql.Date dateMhs = new java.sql.Date(
                    m.getTanggalLahir().getYear(),
                    m.getTanggalLahir().getMonth(),
                    m.getTanggalLahir().getDay());
//            java.sql.Date dateDosen2 = (Date)d.getTglLahir(); //Cannot be cast
            System.out.println("dateDosen: "+dateMhs);
        boolean berhasil = false;
        try {
            String query = "insert into Mahasiswa ( ID_DOSEN,NAMA,IS_MALE,TANGGAL_LAHIR,"
                    + "TEMPAT_LAHIR,ALAMAT,NO_HP,ANGKATAN,STATUS_PEMBAYARAN,TOTAL_SKS,EMAIL,PASSWORD)"
                    + "values ("
//                    + "'"+ m.getNim()+"'"
                    + "'"+ m.getDosenWali().getKode() + "'"
                    + ",'"+ m.getNama()+"','"
                    + m.IsMale()+"','"
                    + dateMhs+"','"
                    + m.getTempatLahir()+"','"
                    + m.getAlamat()+"','"
                    + m.getNoHp()+"','"
                    + m.getAngkatan() +"','"
                    + m.isStatusPembayaran()+"','"
                    + m.getTotalSKS()+"','"
                    + m.getUsername()+"','"
                    + m.getPassword()+"');";
            System.out.println(query);
//            manipulate(query);
            sta.execute(query,Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = sta.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                m.setNim(generatedId);
                berhasil = true;
                
            }
        } catch (Exception e) {
            e.printStackTrace();
//            throw new IllegalArgumentException("Terjadi kesalahan saat save mahasiswa");
        }
        return berhasil;
    }
    
    public boolean saveMatkul(MataKuliah matkul) {
        
        boolean berhasil = false;
        try {
            String query = "insert into MATA_KULIAH (ID_MATKUL,ID_DOSEN,NAMA_MATKUL,SKS,TINGKAT)"
                    + "values ('"+matkul.getKodeMk()+"',"
                    + "'"+matkul.getDosen().getKode()+"',"
                    + "'"+matkul.getNamaMk()+"',"
                    + "'"+matkul.getSKS()+"',"
                    + "'"+matkul.getTingkat()+"');";
            System.out.println(query);
            berhasil = manipulate(query);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Terjadi kesalahan saat save matkul2");
        }
        return berhasil;
    }
    public boolean saveDosen (Dosen d)/*DONE*/ {
        boolean berhasil = false;
        try {
            System.out.println(d.getTglLahir());
//            java.time.LocalDate dateDsn = new LocalDate;
            java.sql.Date dateDosen = new java.sql.Date(
                    d.getTglLahir().getYear(),
                    d.getTglLahir().getMonth(),
                    d.getTglLahir().getDay());
//            java.sql.Date dateDosen2 = (Date)d.getTglLahir(); //Cannot be cast
            System.out.println("dateDosen: "+dateDosen);
//            System.out.println("dateDosen2: "+dateDosen2);
            String query = "insert into DOSEN (ID_DOSEN,NAMA,TANGGAL_LAHIR,TEMPAT_LAHIR,ALAMAT,ISMALE,NO_HP,EMAIL,PASSWORD) values ('"
                    +d.getKode()+ "','"
                    +d.getNama()+"','"
                    +dateDosen+"','"
                    +d.getTempatLahir()+"','"
                    +d.getAlamat()+"','"
                    +d.getIsMale()+"','"
                    +d.getNoHp()+"','"
                    +d.getUsername()+"','"
                    +d.getPassword()+"');";
            berhasil = manipulate(query);
            if (! berhasil) {
                throw new IllegalArgumentException("Error save Dosen ke DB");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return berhasil;
    }

    public boolean saveJadwal (Jadwal j) {
        boolean berhasil = false;
        String query = ( "INSERT INTO JADWAL (IDJADWAL, ID_MATKUL,PUKUL,HARI) VALUES ('"
                    +j.getIdJadwal()+"','"
                    +j.getMatkul().getKodeMk()+"','"
                    +j.getPukul()+"','"
                    +j.getHari()+"');"
                );
        try {
            System.out.println(query);
            berhasil = manipulate(query);
            if (! berhasil) {
                throw new IllegalArgumentException("Error save Dosen ke DB");
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return berhasil;
    }

    public boolean saveJadwalDiambil(ArrayList<Jadwal> arrJadwal, long nim){
        boolean berhasil = false;
        try {
            connect();
            for (Jadwal jadwal : arrJadwal) {
                String nimm = Long.toString(nim);
                String takenJadwal = Integer.toString(jadwal.getIdJadwal());
                String query = "INSERT INTO `taken_schedule`(`NIM`, `IDJADWAL`) VALUES ("+nimm+","+takenJadwal+");";
                System.out.println(query);
                berhasil = manipulate(query);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error save jadwal diambil");

        }
        return berhasil;
    }
    
    
}
   
    
    

