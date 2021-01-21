/*
 * DB.java
 *
 * Created on December 4, 2007, 9:46 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package com.theoretics;

/**
 *
 * @author amd
 */
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataBaseHandler extends Thread {

    private String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private String MainServer_URL = "";
    private String SubServer_URL = "";
    //private String MainServer_URL = "jdbc:mysql://192.168.100.228/";
    //private String MainServer_URL = "jdbc:mysql://localhost/";
    //private String SubServer_URL = "jdbc:mysql://192.168.100.228/";
    //private String SubServer_URL = "jdbc:mysql://localhost/";
//    private String CAMipaddress = "192.168.100.220";
//    private String CAMusername = "admin";
//    private String CAMpassword = "user1234";
//    private String USERNAME = "root";   //root
//    private String PASSWORD = "sa";     //sa
//    private String USERNAME = "base";   //root
//    private String PASSWORD = "theoreticsinc";     //sa
    private Connection connection = null;
    private Statement st;
    public boolean mainorder;
    private boolean timeoutnow = false;
    private String dateTimeIN;
    private String dateTimeINStamp;
    private String dateTimePaid;
    private String dateTimePaidStamp;

    private static Logger log = LogManager.getLogger(DataBaseHandler.class.getName());
    Statement stmt = null;
    PreparedStatement statement = null;
    Connection conn = null;

    public DataBaseHandler() {
        MainServer_URL = "jdbc:mysql://" + CONSTANTS.serverIP + "";
        SubServer_URL = "jdbc:mysql://" + CONSTANTS.serverIP + "";

    }

    public DataBaseHandler(String serverIP) {
        try {
            //XMLreader xr = new XMLreader();
            ///home/pi/JTerminals
            MainServer_URL = "jdbc:mysql://" + serverIP + "";
            SubServer_URL = "jdbc:mysql://" + serverIP + "";
            //MainServer_URL = "jdbc:mysql://" + xr.getElementValue("/home/pi/net.xml", "main1") + "";
            //SubServer_URL = "jdbc:mysql://" + xr.getElementValue("/home/pi/net.xml", "sub1") + "";
            //getActiveRatesParameter();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost", "root", "sa");
            st = (Statement) con.createStatement();
            String str = "SELECT * FROM carpark.entrance";
            //st.execute(str);
            ResultSet rs = st.executeQuery(str);

            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt("entrance_id");
                String firstName = rs.getString("CardNumber");
                String lastName = rs.getString("PlateNumber");
                Date dateCreated = rs.getDate("TimeIN");

                // print the results
                System.out.format("%s, %s, %s, %s\n", id, firstName, lastName, dateCreated);
            }
            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }
    }

    public ResultSet selectDatabyFields(String sql) {
        ResultSet res = null;
        try {
            st = (Statement) connection.createStatement();
            res = st.executeQuery(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return res;
    }

    public void insertImageFromURLToDB() {
        Connection connection = null;
        PreparedStatement statement = null;
        URLConnection uc1 = null;
        URLConnection uc2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return hostname.equals(CONSTANTS.CAMipaddress1);
            }
        });
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONSTANTS.CAMusername, CONSTANTS.CAMpassword.toCharArray());
            }
        });
        try {
            String loginPassword = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            String encoded = new sun.misc.BASE64Encoder().encode(loginPassword.getBytes());

            //URL url = new URL("http://www.avajava.com/images/avajavalogo.jpg");
            //URL url = new URL("http://admin:user1234@192.168.1.64/Streaming/channels/1/picture");
            //URL url = new URL("http://192.168.1.64/onvif-http/snapshot?Profile_1");
            URL url1 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress1 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras
            URL url2 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress2 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras
            //URL url = new URL("http://192.168.1.190/onvifsnapshot/media_service/snapshot?channel=1&subtype=1");
            //http://admin:user1234@192.168.1.64/onvif-http/snapshot?Profile_1
            //URL url = new URL("http://admin:admin888888@192.168.1.190/cgi-bin/snapshot.cgi?loginuse=admin&loginpas=admin888888");
            //HttpURLConnection yc = (HttpURLConnection) url.openConnection();
            //yc.setRequestProperty("Authorization", "Basic " + encoded);
            //InputStream is = url.openStream();
            //**********************
            uc1 = url1.openConnection();
            uc2 = url2.openConnection();
            uc1.setConnectTimeout(1000);
            uc2.setConnectTimeout(1000);
            String userpass = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            //String userpass = "root" + ":" + "Th30r3t1cs";
            String basicAuth = "Basic " + new String(new sun.misc.BASE64Encoder().encode(userpass.getBytes()));
            uc1.setRequestProperty("Authorization", basicAuth);
            uc2.setRequestProperty("Authorization", basicAuth);
            //InputStream in = uc.getInputStream();

//if (yc.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
//     // Step 3. Create a authentication object from the challenge...
//     DigestAuthentication auth = DigestAuthentication.fromResponse(connection);
//     // ...with correct credentials
//     auth.username("user").password("passwd");
//
//     // Step 4 (Optional). Check if the challenge was a digest challenge of a supported type
//     if (!auth.canRespond()) {
//         // No digest challenge or a challenge of an unsupported type - do something else or fail
//         return;
//     }
//
//     // Step 5. Create a new connection, identical to the original one.
//     yc = (HttpURLConnection) url.openConnection();
//     // ...and set the Authorization header on the request, with the challenge response
//     yc.setRequestProperty(DigestChallengeResponse.HTTP_HEADER_AUTHORIZATION,
//         auth.getAuthorizationForRequest("GET", yc.getURL().getPath()));
// }
            /*
            is1 = (InputStream) uc1.getInputStream();
            is2 = (InputStream) uc2.getInputStream();
            connection = getConnection(false);
            statement = connection.prepareStatement("insert into unidb.timeindb(CardCode, Plate, PIC2, PIC) " + "values(?,?,?,?)");
            statement.setString(1, "HFJ93230");
            statement.setString(2, "ABCDEFG");
            statement.setBinaryStream(3, is1, 1024 * 32); //Last Parameter has to be bigger than actual 
            statement.setBinaryStream(4, is2, 1024 * 32); //Last Parameter has to be bigger than actual 
             */
            //statement.executeUpdate();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: - " + e);
        } catch (Exception e) {
            System.out.println("Exception: - " + e);
        } finally {

        }

        try {
            statement.executeUpdate();
            connection.close();
            statement.close();
            is1.close();
            is2.close();
        } catch (Exception e) {
            System.out.println("Exception Finally: - " + e);
        }
    }

    public boolean writeCGHEntryWithPix(String EntryID, String CardNumber, String trtype, String DateIN) {
        Connection connection = null;
        PreparedStatement statement = null;
        URLConnection uc1 = null;
        URLConnection uc2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return hostname.equals(CONSTANTS.CAMipaddress1);
            }
        });
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONSTANTS.CAMusername, CONSTANTS.CAMpassword.toCharArray());
            }
        });
////        try {
//////            OLD SQL
////            connection = getConnection(false);
////            statement = connection.prepareStatement("INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
////                    + "(NULL, ?, 'CAR' , NULL, NOW(), NULL, ?, NULL, NULL, 'ENTRY')");
////            statement.setString(1, CardNumber);
////            statement.setString(2, EntryID);
////            statement.executeUpdate();
////        } catch (Exception e) {
////            System.out.println(e.getMessage());
////            e.printStackTrace();
////        }

        try {
            String loginPassword = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            String encoded = new sun.misc.BASE64Encoder().encode(loginPassword.getBytes());

            URL url1 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress1 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras
            URL url2 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress2 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras

            //**********************
            uc1 = url1.openConnection();
            uc2 = url2.openConnection();
            String userpass = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            //String userpass = "root" + ":" + "Th30r3t1cs";
            String basicAuth = "Basic " + new String(new sun.misc.BASE64Encoder().encode(userpass.getBytes()));
            uc1.setRequestProperty("Authorization", basicAuth);
            uc2.setRequestProperty("Authorization", basicAuth);

            is1 = (InputStream) uc1.getInputStream();
            is2 = (InputStream) uc2.getInputStream();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: - " + e);
        } catch (Exception e) {
            System.out.println("Exception: - " + e);
        } finally {

            try {
                connection = getConnection(false);
//            statement = connection.prepareStatement("insert into unidb.timeindb(CardCode, Plate, PIC2, PIC) " + "values(?,?,?,?)");
//            statement.setString(1, "HFJ93230");
//            statement.setString(2, "ABCDEFG");
//            statement.setBinaryStream(3, is1, 1024 * 32); //Last Parameter has to be bigger than actual 
//            statement.setBinaryStream(4, is2, 1024 * 32); //Last Parameter has to be bigger than actual 
//            

                //WITH CAMERA TO DATABASE
                String SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
                        + "(NULL, ?, 'CAR' , '', NOW(), NULL, ?, ?, ?, 'ENTRY')";
                statement = connection.prepareStatement(SQL);
                if (null != is1 && null != is2) {
                    statement = connection.prepareStatement(SQL);
                    statement.setBinaryStream(3, is1, 1024 * 128); //Last Parameter has to be bigger than actual      
                    statement.setBinaryStream(4, is2, 1024 * 128); //Last Parameter has to be bigger than actual 
                }
                if (null == is1 && null != is2) {
                    SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
                            + "(NULL, ?, 'CAR' , '', NOW(), NULL, NULL, ?, ?, 'ENTRY')";
                    statement = connection.prepareStatement(SQL);
                    statement.setBinaryStream(3, is2, 1024 * 128); //Last Parameter has to be bigger than actual 
                }
                if (null != is1 && null == is2) {
                    SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
                            + "(NULL, ?, 'CAR' , '', NOW(), NULL, ?, NULL, ?, 'ENTRY')";
                    statement = connection.prepareStatement(SQL);
                    statement.setBinaryStream(3, is1, 1024 * 128); //Last Parameter has to be bigger than actual 
                }
                if (null == is1 && null == is2) {
                    SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
                            + "(NULL, ?, 'CAR' , '', NOW(), NULL, ?, NULL, NULL, 'ENTRY')";
                    statement = connection.prepareStatement(SQL);
                }
                statement.setString(1, CardNumber);
                statement.setString(2, EntryID);
                statement.executeUpdate();
                connection.close();
                statement.close();
                if (null != is1) {
                    is1.close();
                }
                if (null != is2) {
                    is2.close();
                }
                return true;
            } catch (Exception e) {
                System.out.println("Exception Finally: - " + e);
            }
        }

        return false;
    }
//
//    public void insertImageToDB() {
//        Connection connection = null;
//        PreparedStatement statement = null;
//        FileInputStream inputStream = null;
//
//        try {
//            File fileimage = new File("C:/Users/Theoretics Inc/Pictures/20190423_114857.jpg");
//            //BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_INDEXED);
//            //URL url = new URL("http://admin:admin888888@192.168.1.190/cgi-bin/snapshot.cgi?loginuse=admin&loginpas=admin888888");
//            //Image image = ImageIO.read(url);
//            inputStream = new FileInputStream(fileimage);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            InputStream is = null;
//            connection = getConnection(false);
//            statement = connection.prepareStatement("insert into unidb.timeindb(CardCode, Plate, PIC) " + "values(?,?,?)");
//            statement.setString(1, "HFJ93230");
//            statement.setString(2, "ABCDEFG");
//            statement.setBinaryStream(3, (InputStream) inputStream, (int) (fileimage.length()));
//            //statement.setBinaryStream(3, (InputStream) is);
//            statement.executeUpdate();
//
//        } catch (FileNotFoundException e) {
//            System.out.println("FileNotFoundException: - " + e);
//        } catch (SQLException e) {
//            System.out.println("SQLException: - " + e);
//        } finally {
//
//            try {
//                connection.close();
//                statement.close();
//            } catch (SQLException e) {
//                System.out.println("SQLException Finally: - " + e);
//            }
//        }
//    }

    public void ShowImageFromDB() {
        try {
            connection = getConnection(false);
            String sql = "SELECT CardCode, Plate, PIC FROM unidb.timeindb";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultSet = stmt.executeQuery();

            BufferedImage img = new BufferedImage(400, 400,
                    BufferedImage.TYPE_BYTE_INDEXED);

            while (resultSet.next()) {
                String name = resultSet.getString(1);
                String description = resultSet.getString(2);
                //File image = new File("C:\\card" + name + ".jpg");
                //FileOutputStream fos = new FileOutputStream(image);

                byte[] buffer = new byte[1];
                InputStream is = resultSet.getBinaryStream(3);
                //while (is.read(buffer) > 0) {
                //    fos.write(buffer);
                //}
                //is.close();

                //InputStream in = new FileInputStream("C:\\card" + name + ".jpg");
                img = ImageIO.read(is);
                is.close();
                //fos.close();
                //show(name, img, 7);
            }

            //Kernel kernel = new Kernel(3, 3, new float[] { -1, -1, -1, -1, 9, -1, -1,
            //    -1, -1 });
            //BufferedImageOp op = new ConvolveOp(kernel);
            //img = op.filter(img, null);
//        JFrame frame = new JFrame();
//        frame.getContentPane().setLayout(new FlowLayout());
//        frame.getContentPane().add(new JLabel(new ImageIcon(img)));
//        frame.pack();
//        frame.setVisible(true);
            //mediaPlayer.controls().stop();
            show("Captured", img, 7);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(DataBaseHandler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("serial")
    private static void show(String title, final BufferedImage img, int i) {
        JFrame frameX = new JFrame();
        if (null != img) {
            JFrame f = new JFrame(title);
            frameX.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frameX.setContentPane(new JPanel() {
                @Override
                protected void paintChildren(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.drawImage(img, null, 0, 0);
                }

                @Override
                public Dimension getPreferredSize() {
                    return new Dimension(img.getWidth(), img.getHeight());
                }
            });
            frameX.pack();
            frameX.setLocation(50 + (i * 5), 50 + (i * 5));
            frameX.setVisible(true);
        } else {
            System.out.println("No Image Captured");
        }
    }

    public String getLoginUsername(String loginCode, String password) {
        String username = "";
        try {

            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT * FROM pos_users.main WHERE usercode='" + loginCode + "' AND password = MD5('" + password + "')");

            // iterate through the java resultset
            while (rs.next()) {
                int id = rs.getInt(1);
                username = rs.getString("username");

            }
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return username;
    }

    public boolean getLoginPassword(String loginCode, String password) {
        boolean found = false;
        try {

            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT password FROM pos_users.main WHERE usercode='" + loginCode + "' AND password = MD5('" + password + "')");

            // iterate through the java resultset
            while (rs.next()) {
                found = true;
            }
            st.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        }

        return found;
    }

    public Connection getConnection(boolean mainorder)
            throws SQLException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            //System.exit(1);
        }
        DriverManager.setLoginTimeout(1);
        //Connection connection=null;
        if (mainorder == false) {
            try {
                connection = DriverManager.getConnection(MainServer_URL,
                        CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                connection.setNetworkTimeout(Executors.newFixedThreadPool(2), 2000);

                return (connection);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                try {
                    connection = DriverManager.getConnection(SubServer_URL,
                            CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                    connection.setNetworkTimeout(Executors.newFixedThreadPool(2), 2000);

                    return (connection);
                } catch (Exception ex2) {
                    System.out.println(ex2.getMessage());
                }
            }
        } else {
            try {
                connection = DriverManager.getConnection(SubServer_URL,
                        CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                connection.setNetworkTimeout(Executors.newFixedThreadPool(2), 2000);

                return (connection);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                connection = DriverManager.getConnection(MainServer_URL,
                        CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                connection.setNetworkTimeout(Executors.newFixedThreadPool(2), 2000);

                return (connection);
            }
        }
        return null;
    }

    public Connection getConnection1(boolean order)
            throws SQLException, Exception {
        mainorder = order;

        prewait pw = new prewait();
        Thread pt = new Thread(pw);
        pt.start();
        while (true) {
            if (timeoutnow == true) {
                pt.stop();
                return (connection);
            }
        }
    }

    public String getTimeIN() {
        return dateTimeIN;
    }

    public String getDateTimePaid() {
        return dateTimePaid;
    }

    public String getTimeINStamp() {
        return dateTimeINStamp;
    }

    public String getDateTimePaidStamp() {
        return dateTimePaidStamp;
    }

    public boolean writeManualEntrance(String entranceID, String CardNumber, String trtype, String DateIN, long DateInStamp, boolean isLost) {
        try {
            if (CardNumber.length() > 8) {
                CardNumber = CardNumber.substring(0, 8);
            }
            connection = getConnection(false);
            st = (Statement) connection.createStatement();
            String isLoststr;
            if (isLost) {
                isLoststr = "1";
            } else {
                isLoststr = "0";
            }
            String SQL = "INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp) VALUES ('P1', '" + entranceID + "', '" + CardNumber + "', '' , '" + trtype + "', " + isLost + ", '" + DateIN + "','" + DateInStamp + "')";
            st.execute(SQL);
            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public String getServerTime() throws Exception {
        String TimeIn = "";

        connection = getConnection(false);
        if (null != connection) {
            ResultSet rs = selectDatabyFields("SELECT NOW() as today");
            // iterate through the java resultset

            while (rs.next()) {
                TimeIn = rs.getString("today");
                //System.out.println("TIME IN:" + TimeIn);
            }
            st.close();
            connection.close();
        }
        return TimeIn;

    }

    public boolean findCGHCard(String cardNumber) throws SQLException, Exception {
        connection = getConnection(false);
        if (null != connection) {
            ResultSet rs = selectDatabyFields("SELECT CardCode, Timein FROM unidb.timeindb WHERE CardCode='" + cardNumber + "'");
            // iterate through the java resultset
            String CardCode = "";
            String TimeIn = "";
            while (rs.next()) {
                CardCode = rs.getString("CardCode");
                TimeIn = rs.getString("Timein");
                System.out.println("TIME IN:" + TimeIn);
            }
            st.close();
            connection.close();
            if (cardNumber.compareToIgnoreCase(CardCode) == 0) {
                return true;
            }
        }
        return false;
    }

    /*
        Do not Use Controller System Date and Time...
     */
    public boolean writeCGHEntry(String EntryID, String CardNumber, String trtype, String DateIN) {

//INSERT INTO `timeindb` (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES
//(618563, 'E01D2281', 'CAR', 'AAU7363', '2014-12-17 22:02:00', NULL, 'Entry Zone 2', NULL, NULL, 'ENTRY');
        try {
            if (CardNumber.length() > 8) {
                CardNumber = CardNumber.substring(0, 8);
            }
            connection = getConnection(false);
            if (null != connection) {
                st = (Statement) connection.createStatement();
                String isLoststr;

//INSERT INTO `timeindb` (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES
//(618563, 'E01D2281', 'CAR', 'AAU7363', '2014-12-17 22:02:00', NULL, 'Entry Zone 2', NULL, NULL, 'ENTRY');
                //String SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
                //        + "(NULL, '" + CardNumber + "', 'CAR' , NULL, '" + DateIN + "', NULL, '" + EntryID + "', NULL, NULL, 'ENTRY')";    
                String SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
                        + "(NULL, '" + CardNumber + "', 'CAR' , '', NOW(), NULL, '" + EntryID + "', NULL, NULL, 'ENTRY')";

                st.execute(SQL);
                st.close();
                connection.close();
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean testTransactionCGHCard(String entId, String cardNumber) {
        DateConversionHandler dch = new DateConversionHandler();
        java.util.Date nowStamp = new java.util.Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd H:mm:ss.S");
        String d2 = sdf.format(nowStamp);
        long timeStampIN = dch.convertJavaDate2UnixTime(nowStamp);
        return this.writeCGHEntryWithPix(entId, cardNumber, "R", d2);
//        return this.writeCGHEntry(entId, cardNumber, "R", d2);
    }

    public void resetEntryTransactions(String entranceID) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(MainServer_URL, "root", "sa");
            st = (Statement) con.createStatement();
            String delstr = "DELETE FROM unidb.timeindb WHERE PC = '" + entranceID + "'";

            st.execute(delstr);

            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class prewait extends Thread {

        Thread Tc1;
        Thread Tc2;
        connection1Thread Tconnect1 = new connection1Thread();
        connection2Thread Tconnect2 = new connection2Thread();
        public int count = 0;

        @Override
        public void run() {
            if (mainorder == true) {
                Tc1 = new Thread(Tconnect1);
                Tc2 = new Thread(Tconnect2);
            } else {
                Tc2 = new Thread(Tconnect1);
                Tc1 = new Thread(Tconnect2);
            }
            Tc1.start();
            try {
                while (count < 2) {
                    count++;
                    Thread.sleep(3000);
                }
                if (count == 2) {
                    Tc2.start();
                    count++;
                    Tc1.stop();
                    Thread.sleep(3000);
                }
                if (count == 3) {
                    Tc2.stop();
                    timeoutnow = true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.printStackTrace();
            }
        }
    }

    class connection1Thread extends Thread {

        @Override
        public void run() {
            try {
                Class.forName(DRIVER_CLASS_NAME);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            DriverManager.setLoginTimeout(3);
            try {
                //System.out.println("connecting to Mainserver..");
                connection = DriverManager.getConnection(MainServer_URL, CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                if (connection != null) {//System.out.println(connection + "connected to Mainserver..");
                    timeoutnow = true;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                try {
                    connection = DriverManager.getConnection(SubServer_URL, CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                    if (connection != null) {
                        timeoutnow = true;
                    }
                } catch (SQLException ex1) {
                    ex.printStackTrace();
                }
            }

        }
    }

    class connection2Thread extends Thread {

        @Override
        public void run() {
            try {
                Class.forName(DRIVER_CLASS_NAME);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            DriverManager.setLoginTimeout(3);
            try {
                //System.out.println("connecting to Subserver..");
                connection = DriverManager.getConnection(SubServer_URL, CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                if (connection != null) {//System.out.println(connection + "connected to subserver..");
                    timeoutnow = true;
                }
                Thread.sleep(1000);
            } catch (Exception ex) {
                try {
                    connection = DriverManager.getConnection(MainServer_URL, CONSTANTS.USERNAME, CONSTANTS.PASSWORD);
                    if (connection != null) {
                        timeoutnow = true;
                    }
                } catch (SQLException ex1) {
                    ex.printStackTrace();
                }

            }

        }
    }

    public boolean saveLogin(String logID, String userCode, String logname, String SentinelID) throws SQLException {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();
            st.execute("INSERT INTO colltrain.main (logINID, userCode, userName, SentinelID, loginStamp) VALUES ('+" + logID + "', '" + userCode + "', '" + logname + "', '" + SentinelID + "', CURRENT_TIMESTAMP)");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getPtypeName(String ptype) {
        String name = "";
        try {
            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT ptypename FROM parkertypes.main WHERE parkertype = '" + ptype + "'");

            while (rs.next()) {
                name = rs.getString("ptypename");
            }

            st.close();
            connection.close();
            return name;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return name;
    }

    public String getPtypecount(String parkerName, String logCode) throws SQLException {
        String data = "";
        try {
            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT " + parkerName.toLowerCase().trim() + "Count FROM colltrain.main WHERE logINID = '" + logCode + "'");
            // iterate through the java resultset
            while (rs.next()) {
                String count = rs.getString(parkerName.toLowerCase().trim() + "Count");
                data = count;
            }
            st.close();
            connection.close();
            return data;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public boolean updateRecord(String fieldName, String value, String logCode) {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();

            st.execute("UPDATE colltrain.main SET " + fieldName + " = '" + value + "' WHERE logINID = '" + logCode + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateTimeRecord(String fieldName, String value, String logCode) {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();

            st.execute("UPDATE colltrain.main SET " + fieldName + " = " + value + " WHERE logINID = '" + logCode + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String getImptCount(String fieldName, String logCode) throws SQLException {
        String data = "";
        try {
            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT " + fieldName + " FROM colltrain.main WHERE logINID = '" + logCode + "'");
            // iterate through the java resultset
            while (rs.next()) {
                String count = rs.getString(fieldName);
                data = count;
            }
            st.close();
            connection.close();
            return data;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public Float getImptAmount(String fieldName, String logCode) throws SQLException {
        float data = 0;
        try {
            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT " + fieldName + " FROM colltrain.main WHERE logINID = '" + logCode + "'");
            // iterate through the java resultset
            while (rs.next()) {
                float count = rs.getFloat(fieldName);
                data = count;
            }
            st.close();
            connection.close();
            return data;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public Float getPtypeAmount(String parkerName, String logCode) throws SQLException {
        float data = 0;
        try {
            connection = getConnection(false);
            ResultSet rs = selectDatabyFields("SELECT " + parkerName.toLowerCase().trim() + "Amount FROM colltrain.main WHERE logINID = '" + logCode + "'");
            // iterate through the java resultset
            while (rs.next()) {
                float count = rs.getFloat(parkerName.toLowerCase().trim() + "Amount");
                data = count;
            }
            st.close();
            connection.close();
            return data;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public boolean setImptCount(String fieldName, String logCode, int newCount) throws SQLException {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();

            st.execute("UPDATE colltrain.main SET " + fieldName + " = '" + newCount + "' WHERE logINID = '" + logCode + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setPtypecount(String parkerName, String logCode, int newCount) throws SQLException {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();

            st.execute("UPDATE colltrain.main SET " + parkerName.toLowerCase().trim() + "Count = '" + newCount + "' WHERE logINID = '" + logCode + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setImptAmount(String fieldName, String logCode, float newAmount) throws SQLException {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();

            st.execute("UPDATE colltrain.main SET " + fieldName + " = '" + newAmount + "' WHERE logINID = '" + logCode + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean setPtypeAmount(String parkerName, String logCode, float newAmount) throws SQLException {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();

            st.execute("UPDATE colltrain.main SET " + parkerName.toLowerCase().trim() + "Amount = '" + newAmount + "' WHERE logINID = '" + logCode + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ResultSet getSummaryCollbyLogCode(String logCode) {
        ResultSet rs = null;
        try {
            connection = getConnection(false);
            rs = selectDatabyFields("SELECT * FROM colltrain.main WHERE logINID = '" + logCode + "'");
            // iterate through the java resultset
            //while (rs.next()) {
            //    String r = rs.getString("retailCount");
            //data = r;
            //}
            st.close();
            connection.close();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public ResultSet getSummaryCollbydateColl(String logCode, String dateColl) {
        ResultSet rs = null;
        try {
            connection = getConnection(false);
            String sql = "SELECT * FROM colltrain.main WHERE DATE(logoutStamp) = '" + dateColl + "'";
            rs = selectDatabyFields(sql);
            // iterate through the java resultset
            //while (rs.next()) {
            //    String r = rs.getString("retailCount");
            //data = r;
            //}

            //connection.close();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rs;
    }

    public void manualOpen() {
        try {
            connection = getConnection(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void manualClose() {
        try {
            st.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getTransactionCGHCard(String cardNumber) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(MainServer_URL, "root", "sa");
            st = (Statement) con.createStatement();
            String str = "SELECT * FROM unidb.timeindb WHERE CardCode = '" + cardNumber + "'";

            ResultSet rs = st.executeQuery(str);

            //INSERT INTO `timeindb` (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES
//(618563, 'E01D2281', 'CAR', 'AAU7363', '2014-12-17 22:02:00', NULL, 'Entry Zone 2', NULL, NULL, 'ENTRY');
            while (rs.next()) {
                int id = rs.getInt("ID");
                String firstName = rs.getString("CardCode");
                String lastName = rs.getString("Vehicle");
                String plate = rs.getString("Plate");
                String timein = rs.getString("Timein");
                String operator = rs.getString("Operator");
                String pc = rs.getString("PC");
                String lane = rs.getString("Lane");

                System.out.format("%s, %s, %s, %s, %s, %s, %s, %s\n", id, firstName, lastName, plate, timein, operator, pc, lane);
            }

            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean updateEntryRecord(String cardNumber, String entryID) {
        try {
            connection = getConnection(false);
            st = (Statement) connection.createStatement();
            //String SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES "
            //       + "(NULL, '" + CardNumber + "', 'CAR' , NULL, NOW(), NULL, '" + EntryID + "', NULL, NULL, 'ENTRY')";    

            st.execute("UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PC = '" + entryID + "' WHERE CardCode = '" + cardNumber + "'");

            st.close();
            connection.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean findEntranceCard(String cardNumber) throws SQLException {
        boolean found = false;
        connection = getConnection(true);
        //ResultSet rs = selectDatabyFields("SELECT Timein FROM unidb.timeindb WHERE CardCode = '" + cardNumber + "'");
        ResultSet rs = selectDatabyFields("SELECT datetimeIN"
                + " FROM crdplt.main WHERE cardNumber = '" + cardNumber + "'");
        DateConversionHandler dch = new DateConversionHandler();
        // iterate through the java resultset
        while (rs.next()) {
            dateTimeIN = rs.getString("datetimeIN");
            found = true;
        }
        st.close();
        connection.close();
        return found;
    }

    public boolean saveParkerDB(String ipaddress, String AreaID, String entranceID, String Card, String Plate, String TRType, boolean isLost) {
        DataBaseHandler DB = new DataBaseHandler();
        //DateTimeIN should now be null because Mysql is inserting a default timestamp
        //DateTimeIN = "";
        URLConnection uc1 = null;
        URLConnection uc2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return hostname.equals(ipaddress);
            }
        });
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONSTANTS.CAMusername, CONSTANTS.CAMpassword.toCharArray());
            }
        });
        try {
            String loginPassword = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            String encoded = new sun.misc.BASE64Encoder().encode(loginPassword.getBytes());

            //URL url = new URL("http://www.avajava.com/images/avajavalogo.jpg");
            //HIKVISION IP Cameras Old Versions
            //URL url = new URL("http://" + username + ":" + password + "@" + ipaddress + "/Streaming/channels/1/picture");
            //HIKVISION IP Cameras
            URL url1 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress1 + "/onvif-http/snapshot?Profile_1");
            URL url2 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress2 + "/onvif-http/snapshot?Profile_1");
            //HIKVISION DVR
            //URL url = new URL("http://"+username+":"+password+"@"+ipaddress+"/onvifsnapshot/media_service/snapshot?channel=1&subtype=0");
            //URL url = new URL("http://192.168.100.220/onvifsnapshot/media_service/snapshot?channel=1&subtype=1");
            //URL url = new URL("http://admin:user1234@192.168.100.220/cgi-bin/snapshot.cgi?loginuse=admin&loginpas=user1234");

            //**********************
            uc1 = url1.openConnection();
            uc2 = url2.openConnection();
            String userpass = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            //String userpass = "root" + ":" + "Th30r3t1cs";
            String basicAuth = "Basic " + new String(new sun.misc.BASE64Encoder().encode(userpass.getBytes()));
            uc1.setRequestProperty("Authorization", basicAuth);
            uc2.setRequestProperty("Authorization", basicAuth);

            uc1.setConnectTimeout(1000);
            uc2.setConnectTimeout(1000);
            try {
                if (null != uc1) {
                    is1 = (InputStream) uc1.getInputStream();
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }
            try {
                if (null != uc2) {
                    is2 = (InputStream) uc2.getInputStream();
                }
            } catch (Exception ex) {
//                ex.printStackTrace();
            }

            conn = DB.getConnection(true);
            //WITH CAMERA TO DATABASE
            //int status2 = stmt.executeUpdate("INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp) "
            //        + "VALUES ('" + AreaID + "', '" + entranceID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '0', '" + DateTimeIN + "','" + timeStampIN.toString() + "')");
            //String SQL = "INSERT INTO unidb.timeindb (`ID`, `CardCode`, `Vehicle`, `Plate`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`, `Timein`) VALUES "
            //        + "(NULL, ?, 'CAR' , ?, NULL, ?, ?, ?, 'ENTRY', ?)";
            String SQL = "";
            statement = conn.prepareStatement(SQL);
            if (null != is1 && null != is2) {
                SQL = "INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp, PIC, PIC2) VALUES "
                        + "(?, ?, ?, ?, ?, ?, NOW(), UNIX_TIMESTAMP(), ?, ?)";
                statement = conn.prepareStatement(SQL);
                statement.setBinaryStream(7, is1, 128 * 1024); //Last Parameter has to be bigger than actual      
                statement.setBinaryStream(8, is2, 128 * 1024); //Last Parameter has to be bigger than actual 
            }
            if (null == is1 && null != is2) {
                SQL = "INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp, PIC, PIC2) VALUES "
                        + "(?, ?, ?, ?, ?, ?, NOW(), UNIX_TIMESTAMP(), NULL, ?)";
                statement = conn.prepareStatement(SQL);
                statement.setBinaryStream(7, is2, 128 * 1024); //Last Parameter has to be bigger than actual 
            }
            if (null != is1 && null == is2) {
                SQL = "INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp, PIC, PIC2) VALUES "
                        + "(?, ?, ?, ?, ?, ?, NOW(), UNIX_TIMESTAMP(), ?, NULL)";
                statement = conn.prepareStatement(SQL);
                statement.setBinaryStream(7, is1, 128 * 1024); //Last Parameter has to be bigger than actual 
            }
            if (null == is1 && null == is2) {
                SQL = "INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp, PIC, PIC2) VALUES "
                        + "(?, ?, ?, ?, ?, ?, NOW(), UNIX_TIMESTAMP(), NULL, NULL)";
                statement = conn.prepareStatement(SQL);
            }
            statement.setString(1, AreaID);
            statement.setString(2, entranceID);
            statement.setString(3, Card);
            statement.setString(4, Plate);
            statement.setString(5, TRType);
            statement.setBoolean(6, isLost);

            statement.executeUpdate();

            //int status2 = stmt.executeUpdate("INSERT INTO unidb.timeindb (ID, CardCode, Vehicle, Plate, Timein, Operator, PC, PIC, PIC2, Lane) "
            //        + "VALUES (NULL, '" + Card + "', 'CAR', '" + Plate + "', NOW(), NULL, '" + EntryID + "', NULL, NULL, 'LANE')");
            //int status2 = stmt.executeUpdate("INSERT INTO crdplt.main (areaID, entranceID, cardNumber, plateNumber, trtype, isLost, datetimeIN, datetimeINStamp) "
            //        + "VALUES ('" + AreaID + "', '" + entranceID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '0', '" + DateTimeIN + "','" + timeStampIN.toString() + "')");
            return true;
        } catch (FileNotFoundException e) {
            //System.out.println("FileNotFoundException: - " + e);
        } catch (Exception e) {
            System.out.println("Save2DB Exception: - " + e);
        } finally {

            try {
                conn.close();
                statement.close();
                is1.close();
                is2.close();
            } catch (Exception e) {
                System.out.println("Exception Finally: - " + e);
            }
        }
        return false;
    }

    public boolean saveEXParkerTrans2DB(String serverIP, String SentinelID, String TransactionNum, String Entrypoint, String ReceiptNo, String CashierID, String CashierName, String Card, String Plate, String TRType, String DateTimeIN, String DateTimeOUT, String AmountGross, String AmountPaid, long HoursElapsed, long MinutesElapsed, String settlementRef, String settlementName, String settlementAddr, String settlementTIN, String settlementBusStyle, double VAT12, double VATSALE, double vatExemptedSales, String discount, Double tenderFloat, String changeDue) {
//001000000000234,11111111,SERVCE,E01,000001,GELO01,V,0542,04142008,0543,04142008,B,O,C,0
//RECEIPT NO     ,ID      ,Name  ,SW1,CARD  ,PLT   ,T,Time,DATEIN  ,TOut,DATEOUT , , , ,AMOUNT 
        //SW03157842GELO47R1207072478203 <<-.crd .plt

//INSERT INTO `exit_trans` (`ReceiptNumber`, `CashierName`, `EntranceID`, `ExitID`, `CardNumber`, `PlateNumber`, `ParkerType`, `Amount`, `DateTimeIN`, `DateTimeOUT`) 
        //VALUES ('R0000131', 'JENNY', 'EN01', 'EX01', 'DBAE', 'TEST001', 'R', '30', '2017-05-28 06:33:31', CURRENT_TIMESTAMP);
        DataBaseHandler DB = new DataBaseHandler();
        try {

            //DateTimeIN should now be null because Mysql is inserting a default timestamp
            //DateTimeIN = "";
            if (null == DateTimeIN || DateTimeIN.compareToIgnoreCase("") == 0) {  //LOST CARD Scenario
                DateTimeIN = "CURRENT_TIMESTAMP";
            } else {
                DateTimeIN = "'" + DateTimeIN + "'";
            }
            if (changeDue.compareToIgnoreCase("") == 0) {
                changeDue = "0";
            }
            String SQLA = "insert into carpark.exit_trans "
                    + "values(null, 0, null, '" + ReceiptNo + "', '" + CashierID + "', '" + Entrypoint + "', '" + SentinelID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '" + AmountPaid + "', '" + AmountGross + "', '" + discount + "', '" + VAT12 + "', '" + VATSALE + "', '" + vatExemptedSales + "', '" + tenderFloat + "', '" + changeDue + "', " + DateTimeIN + "" + ", '" + DateTimeOUT + "', " + HoursElapsed + ", " + MinutesElapsed + ", '" + settlementRef + "', '" + settlementName + "', '" + settlementAddr + "', '" + settlementTIN + "', '" + settlementBusStyle + "' )";
            //INSERT INTO `incomereport` (`ID`, `TRno`, `Cardcode`, `Plate`, `Operator`, `PC`, `Timein`, `TimeOut`, `BusnessDate`, `Total`, `Vat`, `NonVat`, `VatExemp`, `TYPE`, `Tender`, `Change`, `Regular`, `Overnight`, `Lostcard`, `Payment`, `DiscountType`, `DiscountAmount`, `DiscountReference`, `Cash`, `Credit`, `CreditCardid`, `CreditCardType`, `VoucherAmount`, `GPRef`, `GPDiscount`, `GPoint`, `CompliType`, `Compli`, `CompliRef`, `PrepaidType`, `Prepaid`, `PrepaidRef`) VALUES (NULL, '2-38932', 'ABC23456', 'ABC123', 'cindy', 'POS-2', '2019-07-18 06:29:00', '2019-07-18 12:43:00', '2019-07-18', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
            //String SQL = "INSERT INTO unidb.incomereport (`ID`, `TRno`, `Cardcode`, `Plate`, `Operator`, `PC`, `Timein`, `TimeOut`, `BusnessDate`, `Total`, `Vat`, `NonVat`, `VatExemp`, `TYPE`, `Tender`, `Change`, `Regular`, `Overnight`, `Lostcard`, `Payment`, `DiscountType`, `DiscountAmount`, `DiscountReference`, `Cash`, `Credit`, `CreditCardid`, `CreditCardType`, `VoucherAmount`, `GPRef`, `GPDiscount`, `GPoint`, `CompliType`, `Compli`, `CompliRef`, `PrepaidType`, `Prepaid`, `PrepaidRef`) "
            //       + "VALUES (NULL, '" + ReceiptNo + "', '" + Card + "', '" + Plate + "', '" + CashierID + "', 'POS-2', " + DateTimeIN + ", '" + DateTimeOUT + "', CURRENT_DATE, '" + AmountGross + "', '" + VAT12 + "', '" + VATSALE + "', '" + vatExemptedSales + "', 'REGULAR', '" + tenderFloat + "', '" + changeDue + "', '" + AmountGross + "', '0', '0', 'Regular', '-', " + discount + ", '-', '" + AmountPaid + "', '0', NULL, NULL, '0', NULL, '0', '0', NULL, '0', NULL, NULL, '0', NULL)";

            //        + "values(null, 0, null, '" + ReceiptNo + "', '" + CashierID + "', '" + Entrypoint + "', '" + SentinelID + "', '" + Card + "', '" + Plate + "', '" + TRType + "', '" + Amount + "', " + DateTimeIN + "" + ", CURRENT_TIMESTAMP, " + HoursElapsed + ", " + MinutesElapsed + ", '" + settlementRef + "', '" + settlementName + "', '" + settlementAddr + "', '" + settlementTIN + "', '" + settlementBusStyle + "' )";
            try {
                conn = DB.getConnection(true);
                stmt = conn.createStatement();
                //int status2 = stmt.executeUpdate(SQL);
                int status3 = stmt.executeUpdate(SQLA);
                return true;
            } catch (Exception ex) {
                conn = DB.getConnection(false);
                stmt = conn.createStatement();
                log.info("Print Error in : " + SQLA);
                //int status2 = stmt.executeUpdate(SQL);
                int status3 = stmt.executeUpdate(SQLA);
                return true;
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        } finally {
            try {
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                //log.error(ex.getMessage());
            }
        }

    }

    public boolean updateParkerDB(String cardNumber, String entryID) {
        Connection connection = null;
        PreparedStatement statement = null;
        URLConnection uc1 = null;
        URLConnection uc2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return hostname.equals(CONSTANTS.CAMipaddress1);
            }
        });
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONSTANTS.CAMusername, CONSTANTS.CAMpassword.toCharArray());
            }
        });
        try {
            System.out.println("Trying to access the Cameras...");
            String loginPassword = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            String encoded = new sun.misc.BASE64Encoder().encode(loginPassword.getBytes());

            URL url1 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress1 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras
            URL url2 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress2 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras

            //**********************
            uc1 = url1.openConnection();
            uc2 = url2.openConnection();
            String userpass = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            //String userpass = "root" + ":" + "Th30r3t1cs";
            String basicAuth = "Basic " + new String(new sun.misc.BASE64Encoder().encode(userpass.getBytes()));
            uc1.setRequestProperty("Authorization", basicAuth);
            uc2.setRequestProperty("Authorization", basicAuth);

            is1 = (InputStream) uc1.getInputStream();
            is2 = (InputStream) uc2.getInputStream();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: - " + e);
        } catch (Exception e) {
            System.out.println("Exception: - " + e);
        } finally {

        }

        try {
            System.out.println("Connecting to Database...");
            connection = getConnection(false);

            //String SQL = "UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PIC = ? , PIC2 = ? WHERE CardCode = ?";
            String SQL = "UPDATE crdplt.main SET datetimeIN = NOW(), datetimeINStamp = UNIX_TIMESTAMP(), plateNumber = '', PIC = ? , PIC2 = ? WHERE cardNumber = ?";

            statement = connection.prepareStatement(SQL);
            if (null != is1 && null != is2) {
                SQL = "UPDATE crdplt.main SET datetimeIN = NOW(), plateNumber = '', PIC = ? , PIC2 = ? WHERE cardNumber = ?";
                statement = connection.prepareStatement(SQL);
                statement.setBinaryStream(1, is1, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setBinaryStream(2, is2, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setString(3, cardNumber);
            }
            if (null != is1 && null == is2) {
                SQL = "UPDATE crdplt.main SET datetimeIN = NOW(), plateNumber = '', PIC = ? , PIC2 = NULL WHERE cardNumber = ?";
                statement = connection.prepareStatement(SQL);
                statement.setBinaryStream(1, is1, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setString(2, cardNumber);
            }
            if (null == is1 && null != is2) {
                SQL = "UPDATE crdplt.main SET datetimeIN = NOW(), plateNumber = '', PIC = NULL , PIC2 = ? WHERE cardNumber = ?";
                statement = connection.prepareStatement(SQL);
                statement.setBinaryStream(1, is2, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setString(2, cardNumber);
            }
            if (null == is1 && null == is2) {
                SQL = "UPDATE crdplt.main SET datetimeIN = NOW(), plateNumber = '', PIC = NULL , PIC2 = NULL WHERE cardNumber = ?";
                statement = connection.prepareStatement(SQL);
                statement.setString(1, cardNumber);
            }
            statement.executeUpdate();
            connection.close();
            statement.close();
            if (null != is1) {
                is1.close();
            }
            if (null != is2) {
                is2.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Database Exception Finally: - " + e);
        }
        return false;
    }

    public boolean updateCGHParkerDB(String cardNumber, String entryID) {
        Connection connection = null;
        PreparedStatement statement = null;
        URLConnection uc1 = null;
        URLConnection uc2 = null;
        InputStream is1 = null;
        InputStream is2 = null;
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                return hostname.equals(CONSTANTS.CAMipaddress1);
            }
        });
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(CONSTANTS.CAMusername, CONSTANTS.CAMpassword.toCharArray());
            }
        });
        try {
            System.out.println("Trying to access the Cameras...");
            String loginPassword = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            String encoded = new sun.misc.BASE64Encoder().encode(loginPassword.getBytes());

            URL url1 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress1 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras
            URL url2 = new URL("http://" + CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword + "@" + CONSTANTS.CAMipaddress2 + "/onvif-http/snapshot?Profile_1");//HIKVISION IP Cameras

            //**********************
            uc1 = url1.openConnection();
            uc2 = url2.openConnection();
            String userpass = CONSTANTS.CAMusername + ":" + CONSTANTS.CAMpassword;
            //String userpass = "root" + ":" + "Th30r3t1cs";
            String basicAuth = "Basic " + new String(new sun.misc.BASE64Encoder().encode(userpass.getBytes()));
            uc1.setRequestProperty("Authorization", basicAuth);
            uc2.setRequestProperty("Authorization", basicAuth);

            is1 = (InputStream) uc1.getInputStream();
            is2 = (InputStream) uc2.getInputStream();

        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException: - " + e);
        } catch (Exception e) {
            System.out.println("Exception: - " + e);
        } finally {

        }

        try {
            System.out.println("Connecting to Database...");
            connection = getConnection(false);

            String SQL = "UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PIC = ? , PIC2 = ? WHERE CardCode = ?";
//            String SQL = "UPDATE crdplt.main SET datetimeIN = NOW(), datetimeINStamp = UNIX_TIMESTAMP(), plateNumber = '', PIC = ? , PIC2 = ? WHERE cardNumber = ?";

            statement = connection.prepareStatement(SQL);
            if (null != is1 && null != is2) {
                SQL = "UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PIC = ? , PIC2 = ? WHERE CardCode = ?";
                statement = connection.prepareStatement(SQL);
                statement.setBinaryStream(1, is1, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setBinaryStream(2, is2, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setString(3, cardNumber);
            }
            if (null != is1 && null == is2) {
                SQL = "UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PIC = ? , PIC2 = NULL WHERE CardCode = ?";
                statement = connection.prepareStatement(SQL);
                statement.setBinaryStream(1, is1, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setString(2, cardNumber);
            }
            if (null == is1 && null != is2) {
                SQL = "UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PIC = NULL , PIC2 = ? WHERE CardCode = ?";
                statement = connection.prepareStatement(SQL);
                statement.setBinaryStream(1, is2, 128 * 1024); //Last Parameter has to be bigger than actual 
                statement.setString(2, cardNumber);
            }
            if (null == is1 && null == is2) {
                SQL = "UPDATE unidb.timeindb SET Timein = NOW(), Plate = '', PIC = NULL , PIC2 = NULL WHERE CardCode = ?";
                statement = connection.prepareStatement(SQL);
                statement.setString(1, cardNumber);
            }
            statement.executeUpdate();
            connection.close();
            statement.close();
            if (null != is1) {
                is1.close();
            }
            if (null != is2) {
                is2.close();
            }
            return true;
        } catch (Exception e) {
            System.out.println("Database Exception Finally: - " + e);
        }
        return false;
    }

    public void showCGHEntries(String PC) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(MainServer_URL, "root", "sa");
            st = (Statement) con.createStatement();
            String str = "SELECT * FROM unidb.timeindb WHERE PC = '" + PC + "'";

            ResultSet rs = st.executeQuery(str);

            //INSERT INTO `timeindb` (`ID`, `CardCode`, `Vehicle`, `Plate`, `Timein`, `Operator`, `PC`, `PIC`, `PIC2`, `Lane`) VALUES
//(618563, 'E01D2281', 'CAR', 'AAU7363', '2014-12-17 22:02:00', NULL, 'Entry Zone 2', NULL, NULL, 'ENTRY');
            while (rs.next()) {
                int id = rs.getInt("ID");
                String firstName = rs.getString("CardCode");
                String lastName = rs.getString("Vehicle");
                String plate = rs.getString("Plate");
                String timein = rs.getString("Timein");
                String operator = rs.getString("Operator");
                String pc = rs.getString("PC");
                String lane = rs.getString("Lane");

                System.out.format("%s, %s, %s, %s, %s, %s, %s, %s\n", id, firstName, lastName, plate, timein, operator, pc, lane);
            }

            //st.execute(delstr);
            st.close();
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void FindCard(String cardTest) {
        try {
            //String cardTest = "6D547A01";
            DataBaseHandler DBH = new DataBaseHandler(CONSTANTS.serverIP);
            String entranceID = "Entry Zone 1";
            //DBH.getEntranceCard();
            //boolean test = DBH.testTransactionCGHCard(entranceID, cardTest);
            //System.out.println("Testing results=" + test);
            DBH.getTransactionCGHCard(cardTest);
            //DBH.resetEntryTransactions(entranceID);
            //DBH.showCGHEntries(entranceID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            String cardTest = "1D467401";
            DataBaseHandler DBH = new DataBaseHandler();
            String entranceID = "Entry Zone 1";
            //DBH.getEntranceCard();
            boolean test = DBH.testTransactionCGHCard(entranceID, cardTest);
            System.out.println("Testing results=" + test);
            DBH.getTransactionCGHCard(cardTest);
            //DBH.resetEntryTransactions(entranceID);
            //DBH.showCGHEntries(entranceID);

//            DBH.insertImageFromURLToDB();
            DBH.ShowImageFromDB();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
