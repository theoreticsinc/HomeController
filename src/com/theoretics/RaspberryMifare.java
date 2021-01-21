/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

/**
 *
 * @author Theoretics Inc
 */
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

public class RaspberryMifare {
    
    public CardChannel channel;
    public CardTerminal terminal;
    private byte _4th = 0x04;
    private byte _5th = 0x05;
    private byte _6th = 0x06;
    private byte _7th = 0x07;
    private HEXConverter hc;
    private byte[] _16Bytes;
    
    private String parkingID;
    private String entID;
    private String cardID;
    private String reservedID;
    private String plateID;
    private String trID;
    private String timeinID;
    private String dateinID;
    private String timestampINID;
    private String formattedDateinID;
    private String timeinSecondsID;
    TerminalFactory factory;
    
    public RaspberryMifare() {
       
        hc = new HEXConverter();
        _16Bytes = new byte[16];
        
        try {
            terminal = null;
            // show the list of available terminals
            factory = null;
            factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
//factory = TerminalFactory.getDefault();
//            terminal = factory.terminals().getTerminal("ACS ACR122 0");
//            card = terminal.connect("T=1");
            String readerName = "";

            for (int i = 0; i < terminals.size(); i++) {

                readerName = terminals.get(i)
                        .toString()
                        .substring(terminals.get(i).toString().length() - 2);
                //System.out.println("readerName" + readerName + terminals);
                if (readerName.equalsIgnoreCase("0")) {
                    terminal = terminals.get(i);
                }
                terminal = terminals.get(i);
            }

        } catch (CardException ex) {
            
            System.out.println("Card Reader not available. Please connect it first");
            
        }

    }
    
    public boolean startWaiting4CardPresent() {
        // Establish a connection with the card
            //System.out.println("Waiting for a card..");
        try {
            if (null != terminal) {
                if (terminal.waitForCardPresent(0)) {
                    NFCUtils nfc = new NFCUtils() {};
                    Card card = terminal.connect("T=1");
                    byte[] baATR = card.getATR().getBytes();
                    //System.out.println("ATR: " + baATR.toString());
                    //System.out.println("Card: " + card);
                    channel = card.getBasicChannel();
                    
                    //startWaiting4CardAbsent();
                }
                
            }
            return true;
        } catch (CardException ex) {
            System.err.println(ex.getMessage());
        }

        return false;
    }
    
    public boolean startWaiting4CardAbsent() {
        // Establish a connection with the card
            System.out.println("Waiting for card removed..");
        try {
            if (null != terminal) {
                if (terminal.waitForCardAbsent(0)) {
                    startWaiting4CardPresent();
                }
            }
            return true;
        } catch (CardException ex) {
            System.err.println(ex.getMessage());
        }

        return false;
    }
    
    public static void disconnect(boolean reset) throws CardException {
//		card.disconnect(true);
    }

    public boolean write16Chars2Block(byte _blockNumber, String _str16chars) {
        ArrayList writeToPage = new ArrayList();
            writeToPage.add((byte) 0xFF);
            writeToPage.add((byte) 0xD6);
            writeToPage.add((byte) 0x00);
            writeToPage.add((byte) _blockNumber); //Block Number
            writeToPage.add((byte) 0x10); //must be 16 bytes
        
        byte[] output2Card = new byte[21];
        if (_str16chars.length() != 16) {
            System.out.println("Object to write is only " + _str16chars.length() + "bytes");
            return false;
        } else {
            for (int i = 0; i < _str16chars.length(); i++) {
                //System.out.println(_str16chars.charAt(i));
                char s = _str16chars.charAt(i);
                //System.out.print(s + " ");
                //System.out.println("HEX:" + hc.charToHex(s));
                //System.out.println("BYTE:" + hexStringToByteArray(hc.charToHex(s))[0]);
                _16Bytes[i] = hexStringToByteArray(hc.charToHex(s))[0];
                //System.out.println(_16Bytes[i]);
                writeToPage.add(_16Bytes[i]);
            }
            int x = 0;
            for (Object object : writeToPage) {
                byte b = (byte) object;
                /*
                System.out.println("byte:" + b);
                System.out.println("(byte) 0xFF" + (b == (byte) 0xFF));
                System.out.println("(byte) 0xD6" + (b == (byte) 0xD6));
                System.out.println("(byte) 0x00" + (b == (byte) 0x00));
                System.out.println("(byte) 0x04" + (b == _blockNumber));
                System.out.println("(byte) 0x10" + (b == (byte) 0x10));
                */
                output2Card[x] = b;
                x++;
            }
            /*
            (byte) 0xFF,
            (byte) 0xD6,
            (byte) 0x00,
            (byte) 0x06, //Block Number
            (byte) 0x10, //must be 16 bytes
            (byte) 0x00,
            (byte) 0x01,
            (byte) 0x02,
            (byte) 0x03,
            (byte) 0x04,
            (byte) 0x05,
            (byte) 0x06,
            (byte) 0x07,
            (byte) 0x08,
            (byte) 0x09,
            (byte) 0x0A,
            (byte) 0x0B,
            (byte) 0x0C,
            (byte) 0x0D,
            (byte) 0x0E,
            (byte) 0x0F
            */
            
            //for (int i = 0; i < output2Card.length; i++) {
            //    System.out.println("WRITE TO Block: " + output2Card[i]); 
            //}
         /* **********************************************
         * BE CAREFUL WRITING AND Authenticate
         *
         ***********************************************
         */
        System.out.println("WRITE TO Block: " + send(output2Card, channel));           
        /**
         * **********************************************
         * END OF WRITE **********************************************
         */
            return true;
        }
    }

    public boolean eraseBlock(byte _blockNumber) {
        this.authenticateBlock(_blockNumber);
        byte[] writeToPage = new byte[32];
        writeToPage = new byte[]{
            (byte) 0xFF,
            (byte) 0xD6,
            (byte) 0x00,
            (byte) _blockNumber, //Block Number
            (byte) 0x10, //must be 16 bytes
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x00
        };
        
         /* **********************************************
         * BE CAREFUL WRITING AND Authenticate
         *
         ***********************************************
         */
        System.out.println("ERASE Block: " + send(writeToPage, channel));           
        /**
         * **********************************************
         * END OF WRITE **********************************************
         */
        
        this.authenticateBlock(_blockNumber);
            return true;
        
    }
    
    public void initializeVIPcard() {
           authenticateBlock(_4th);
           authenticateBlock(_5th);
           authenticateBlock(_6th);
           
           write16Chars2Block(_4th, "-VIP------------");
           write16Chars2Block(_5th, "-VIP------------");
           write16Chars2Block(_6th, "-VIP------------");
           
           authenticateBlock(_4th);
           authenticateBlock(_5th);
           authenticateBlock(_6th);
           
           System.out.println(read16BytesbyBlockNum(_4th));
           System.out.println(read16BytesbyBlockNum(_5th));
           System.out.println(read16BytesbyBlockNum(_6th));
    }
    
    public String readBlock(byte _blockNumber) {
        authenticateBlock(_blockNumber);
        return read16BytesbyBlockNum(_blockNumber);
    }
    
    
    
    
    private void readTest() {
        startWaiting4CardPresent();
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           System.out.println("UAC: " + readUID());
           
           System.out.println(read16BytesbyBlockNum(_4th));
           System.out.println(read16BytesbyBlockNum(_5th));
           System.out.println(read16BytesbyBlockNum(_6th));
           System.out.println(read16BytesbyBlockNum(_7th));
    }
    
    public void createNewVIPCard() {
        startWaiting4CardPresent();
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           System.out.println(readUID());
           
           System.out.println(write16Chars2Block(_4th, "-VIP------------"));
           System.out.println(write16Chars2Block(_5th, "-VIP------------"));
           System.out.println(write16Chars2Block(_6th, "-VIP------------"));
           
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           
           System.out.println(read16BytesbyBlockNum(_4th));
           System.out.println(read16BytesbyBlockNum(_5th));
           System.out.println(read16BytesbyBlockNum(_6th));
           System.out.println(read16BytesbyBlockNum(_7th));
    }
    
    public void test() {
        char s = 'V';
        System.out.println(s);
        System.out.println("HEX:" + hc.charToHex(s));
        System.out.println((byte) 0x56 == hexStringToByteArray(hc.charToHex(s))[0]);
        System.out.println("STRING:" + fromHexString("56"));
        System.out.println("BYTE:" + hexStringToByteArray(hc.charToHex(s))[0]);

        // Start with something simple, read UID, kinda like Hello World!
        byte[] baReadUID = new byte[5];

        baReadUID = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00,
            (byte) 0x00, (byte) 0x00};

        System.out.println("UID: " + send(baReadUID, channel));
        // If successfull, the output will end with 9000

        // Authenticate
        byte[] baAuth = new byte[7];

        baAuth = new byte[]{(byte) 0xFF, (byte) 0x88, (byte) 0x00,
            (byte) 0x09, (byte) 0x61, (byte) 0x00};

        byte[] authenticationByte = new byte[10];

        authenticationByte = new byte[]{
            (byte) 0xFF,
            (byte) 0x86,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x05,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x04,//Block Number
            (byte) 0x60,
            (byte) 0x00};
        System.out.println("AUTHENTICATE 4: " + send(authenticationByte, channel));
        // If successfull, will output 9000

        // OK, now, the real work
        // Get Serial Number
        // Load key
        byte[] baLoadKey = new byte[12];

        baLoadKey = new byte[]{(byte) 0xFF, (byte) 0x82, (byte) 0x20,
            (byte) 0x1A, (byte) 0x06, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF};

        baLoadKey = new byte[]{(byte) 0xFF, (byte) 0x82, (byte) 0x00,
            (byte) 0x00, (byte) 0x06, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
            (byte) 0xFF, (byte) 0xFF};

        System.out.println("LOAD KEY: " + send(baLoadKey, channel));
        // If successfull, will output 9000

        // Read Serial
        byte[] baRead = new byte[6];

        baRead = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x01, (byte) 0x10};

        byte[] readFromPage = new byte[10];
        readFromPage = new byte[]{(byte) 0xFF, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x05, (byte) 0xD4, (byte) 0x40,
            (byte) 0x00, (byte) 0x30, (byte) 0x01};

        System.out.println("READ: " + send(baRead, channel));
        // If successfull, the output will end with 9000

        byte[] baBlock4 = new byte[6];

        baBlock4 = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x04, (byte) 0x10};

        System.out.println("READ 16 Bytes From Block4: " + send(baBlock4, channel));

        baBlock4 = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x04, (byte) 0x10};

        System.out.println("READ 4 Bytes From Block4: " + send(baBlock4, channel));

        byte[] baBlock5 = new byte[6];

        baBlock5 = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x05, (byte) 0x10};

        System.out.println("READ 4 Bytes From Block5: " + send(baBlock5, channel));

        baBlock5 = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x06, (byte) 0x10};

        System.out.println("READ 4 Bytes From Block6: " + send(baBlock5, channel));

        baBlock5 = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x07, (byte) 0x10};

        System.out.println("READ 4 Bytes From Block7: " + send(baBlock5, channel));

        //WRITING TO RFID
        //
        byte[] writeToPage = new byte[32];
        writeToPage = new byte[]{
            (byte) 0xFF,
            (byte) 0xD6,
            (byte) 0x00,
            (byte) 0x06, //Block Number
            (byte) 0x10, //must be 16 bytes
            (byte) 0x00,
            (byte) 0x01,
            (byte) 0x02,
            (byte) 0x03,
            (byte) 0x04,
            (byte) 0x05,
            (byte) 0x06,
            (byte) 0x07,
            (byte) 0x08,
            (byte) 0x09,
            (byte) 0x0A,
            (byte) 0x0B,
            (byte) 0x0C,
            (byte) 0x0D,
            (byte) 0x0E,
            (byte) 0x0F
        };

        /**
         * **********************************************
         * BE CAREFUL WRITING AND Authenticate
         *
         ***********************************************
         */
        //System.out.println("WRITE TO Block4: " + send(writeToPage, channel));           
        /**
         * **********************************************
         * END OF WRITE **********************************************
         */
        authenticationByte = new byte[]{
            (byte) 0xFF,
            (byte) 0x86,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x05,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x05,//Block Number
            (byte) 0x60,
            (byte) 0x00};

        System.out.println("AUTHENTICATE Block5: " + send(authenticationByte, channel));

        baBlock4 = new byte[]{(byte) 0xFF, (byte) 0xB0, (byte) 0x00,
            (byte) 0x04, (byte) 0x10};

        System.out.println("READ 16 Bytes From Block4: " + send(baBlock4, channel));
    }

    /**
     * **********************************************
     * BE CAREFUL WRITING AND Authenticate
     *
     ***********************************************
     */
    public String writeToBlock(Byte blockNumber) {
        /**
         * **********************************************
         * BE CAREFUL WRITING AND Authenticate
         *
         ***********************************************
         */
        if (blockNumber < (byte) 0x04 || blockNumber > (byte) 0x07) {
            return ("not valid");
        }
        byte[] writeToPage = new byte[258];
        writeToPage = new byte[]{
            (byte) 0xFF,
            (byte) 0xD6,
            (byte) 0x00,
            (byte) blockNumber, //Block Number
            (byte) 0x10, //Must be all 16 bytes
            (byte) 0x00,
            (byte) 0x01,
            (byte) 0x02,
            (byte) 0x03,
            (byte) 0x04,
            (byte) 0x05,
            (byte) 0x06,
            (byte) 0x07,
            (byte) 0x08,
            (byte) 0x09,
            (byte) 0x0A,
            (byte) 0x0B,
            (byte) 0x0C,
            (byte) 0x0D,
            (byte) 0x0E,
            (byte) 0x0F
        };

        /**
         * **********************************************
         * BE CAREFUL WRITING AND Authenticate
         *
         ***********************************************
         */
        String result = null;
        result = send(writeToPage, channel);
        if (result.endsWith("9000")) {
            return "write success";
        } else {
            return "write failed";
        }
    }

    public String read16BytesbyBlockNum(byte blockNumber) {
        byte[] baBlock = new byte[6];
        baBlock = new byte[]{
            (byte) 0xFF,
            (byte) 0xB0,
            (byte) 0x00,
            blockNumber,
            (byte) 0x10};
        String result = send(baBlock, channel);
        if (result.endsWith("9000")) {
            return result.substring(0, result.length() - 4);
        } else {
            return "failed";
        }
    }
    
    public String readDecoded16BytesbyBlockNum(byte blockNumber) {
        byte[] baBlock = new byte[6];
        String decodedStr;
        baBlock = new byte[]{
            (byte) 0xFF,
            (byte) 0xB0,
            (byte) 0x00,
            blockNumber,
            (byte) 0x10};
        String result = send(baBlock, channel);
        if (result.endsWith("9000")) {
            String raw = result.substring(0, result.length() - 4);
            System.out.println("RAW: " + raw);
            int x = 0;
            decodedStr = "";
            while (x < 32) {
                decodedStr += hc.hexToStr(raw.substring(x, x + 2));
                x = x + 2;
            }
            System.out.println(blockNumber+ "decodedStr: " + decodedStr);
        } else {
            return "failed";
        }
        return decodedStr;
    }

    public String read4BytesbyBlockNum(byte blockNumber) {
        byte[] baBlock = new byte[6];
        baBlock = new byte[]{
            (byte) 0xFF,
            (byte) 0xB0,
            (byte) 0x00,
            blockNumber,
            (byte) 0x04};

        String result = send(baBlock, channel);
        if (result.endsWith("9000")) {
            return result.substring(0, result.length() - 4);
        } else {
            return "failed";
        }
    }

    public String readBlockbyCommand(byte[] APDU) {
        return send(APDU, channel);
    }

    public String readUID() {
        byte[] baReadUID = new byte[5];

        baReadUID = new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00,
            (byte) 0x00, (byte) 0x00};
        String result = send(baReadUID, channel);
        if (result.endsWith("9000")) {
            return result.substring(0, result.length() - 4);
        } else {
            return "failed";
        }
    }

    public String authenticateBlock(byte blockNumber) {
        byte[] authenticationByte = new byte[10];
        authenticationByte = new byte[]{
            (byte) 0xFF,
            (byte) 0x86,
            (byte) 0x00,
            (byte) 0x00,
            (byte) 0x05,
            (byte) 0x00,
            (byte) 0x00,
            blockNumber,//Block Number
            (byte) 0x60,
            (byte) 0x00};
        System.out.print("Authenticating: " + blockNumber + "\n");
        return send(authenticationByte, channel);
    }

    private String send(byte[] cmd, CardChannel channel) {

        String res = "";

        byte[] baResp = new byte[258];
        ByteBuffer bufCmd = ByteBuffer.wrap(cmd);
        ByteBuffer bufResp = ByteBuffer.wrap(baResp);

        // output = The length of the received response APDU
        int output = 0;

        try {

            output = channel.transmit(bufCmd, bufResp);

        } catch (CardException ex) {
            System.err.println(ex.getMessage());
        }

        for (int i = 0; i < output; i++) {
            res += String.format("%02X", baResp[i]);
            // The result is formatted as a hexadecimal integer
        }

        return res;
    }

    

    public static String toHexString(byte[] ba) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < ba.length; i++) {
            str.append(String.format("%x", ba[i]));
        }
        return str.toString();
    }

    public static String fromHexString(String hex) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            str.append((char) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        return str.toString();
    }

    public byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    boolean retrieveCRDPLTFromCard(boolean exit) {
        String data;
        readTest();
        if (exit == true) {
            data = readDecoded16BytesbyBlockNum((byte)0x05);
        } else {
            data = readDecoded16BytesbyBlockNum((byte)0x04);
        }
        if (data != "") {
            parkingID = data.substring(0,2);
            entID = data.substring(2,4);
            reservedID = data.substring(4,5);
            cardID = this.readUID();
            trID = data.substring(5,6);
            timestampINID = data.substring(6,16);
            /*
                dateinID = data.substring(5,11);
                trID = data.substring(11,12);
                timeinID = data.substring(12,16);
            formattedDateinID = "20" + dateinID.substring(4, 6) + "-" + dateinID.substring(2, 4) + "-" + dateinID.substring(0, 2) + " " +
                    timeinID.substring(0, 2) + ":" + timeinID.substring(2, 4) + ":00.0";
            */
            System.out.println("parkingID:"+parkingID);
            System.out.println("entID:"+entID);
            System.out.println("reservedID:"+reservedID);
            System.out.println("trID:"+trID);
            System.out.println("timestampINID:"+timestampINID);
            return true;
        }
        
        return false;
       
    }

    public String getParkingID() {
        return parkingID;
    }
    public String getEntID() {
        return entID;
    }
    public String getCardID() {
        return cardID;
    }
    public String getPlateID() {
        return plateID;
    }
    public String getTrID() {
        return trID;
    }
    public String getTimeinID() {
        return timeinID;
    }
    public String getDateinID() {
        return dateinID;
    }
    public String getReservedID() {
        return reservedID;
    }
    public String getTimestampINID() {
        return timestampINID;
    }
    public String getTimeinSecondsID() {
        DateConversionHandler dch = new DateConversionHandler();
        long seconds = 0;
            try {
                seconds = dch.convertDate2Sec(formattedDateinID.toString());
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
            }
            timeinSecondsID = String.valueOf(seconds);
        return timeinSecondsID;
    }
        
    public String getFormattedDateinID() {
        return formattedDateinID;
    }
    
    public void writeManualEntrance(String timeStamp) {
        startWaiting4CardPresent();
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           
           System.out.println(write16Chars2Block(_4th, "P1E1#R" + timeStamp));    
           System.out.println(write16Chars2Block(_5th, "----------------"));
           System.out.println(write16Chars2Block(_6th, "----------------"));
                
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           
    }
    
    private void write2CardTest() {
        startWaiting4CardPresent();
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           System.out.println(readUID());
           
           //System.out.println(write16Chars2Block(_4th, "P1E1#150617R2235"));    //OVERNIGHT
           //System.out.println(write16Chars2Block(_4th, "P1E1#R1498312668"));    //06/24/2017 2:00PM
           System.out.println(write16Chars2Block(_4th, "THEORETICS---INC"));    
           System.out.println(write16Chars2Block(_5th, "----------------"));
           System.out.println(write16Chars2Block(_6th, "----------------"));
          
           //eraseBlock(_4th);
           //eraseBlock(_5th);
           //eraseBlock(_6th);
                
           System.out.println(authenticateBlock(_4th));
           System.out.println(authenticateBlock(_5th));
           System.out.println(authenticateBlock(_6th));
           System.out.println(authenticateBlock(_7th));
           
           System.out.println(read16BytesbyBlockNum(_4th));
           System.out.println(read16BytesbyBlockNum(_5th));
           System.out.println(read16BytesbyBlockNum(_6th));
           System.out.println(read16BytesbyBlockNum(_7th));
    }
    

    public static void main(String[] args) {
        try {
            RaspberryMifare mi = new RaspberryMifare();
            /*
            TESTING ONLY
            */
            if (null!= mi && mi.terminal.isCardPresent()) {
                //mi.write2CardTest();
                mi.readTest();
                mi.readDecoded16BytesbyBlockNum((byte) 0x04);
                mi.readDecoded16BytesbyBlockNum((byte) 0x05);
                mi.readDecoded16BytesbyBlockNum((byte) 0x06);
                //mi.retrieveCRDPLTFromCard(false);
            }
        } catch (CardException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
