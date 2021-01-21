/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 *
 * @author theor
 */
public abstract class NFCUtils {
    public static Card connect() throws Exception {
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();
        
        CardTerminal terminal = terminals.get(0);
        
        terminal.waitForCardPresent(0);
        
        Card card = terminal.connect("*");
        
        byte[] baATR = card.getATR().getBytes();
        //log.info("ATR: " + baATR.toString());
        
        return card;
    }
}
