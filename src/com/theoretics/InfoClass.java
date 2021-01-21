/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.theoretics;

import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;
import java.io.IOException;
import java.util.logging.Level;

/**
 *
 * @author Theoretics
 */
public class InfoClass {
    
    public void showInfo() {
        // display a few of the available system information properties
        System.out.println("----------------------------------------------------");
        System.out.println("PLATFORM INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Platform Name     :  " + PlatformManager.getPlatform().getLabel());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Platform ID       :  " + PlatformManager.getPlatform().getId());
        } catch (Exception ex) {
        }
        System.out.println("----------------------------------------------------");
        System.out.println("HARDWARE INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Serial Number     :  " + SystemInfo.getSerial());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Temperature   :  " + SystemInfo.getCpuTemperature());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Core Voltage  :  " + SystemInfo.getCpuVoltage());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CPU Model Name    :  " + SystemInfo.getModelName());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Processor         :  " + SystemInfo.getProcessor());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Hardware          :  " + SystemInfo.getHardware());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Board Type        :  " + SystemInfo.getBoardType().name());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("MEMORY INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
        } catch (Exception ex) {
        }
        try {
            System.out.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
        } catch (Exception ex) {
        }
        try {
            System.out.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
        } catch (Exception ex) {
        }
        try {
            System.out.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("OPERATING SYSTEM INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("OS Name           :  " + SystemInfo.getOsName());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Version        :  " + SystemInfo.getOsVersion());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());
        } catch (Exception ex) {
        }
        try {
            System.out.println("OS Firmware Date  :  " + SystemInfo.getOsFirmwareDate());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("JAVA ENVIRONMENT INFO");
        System.out.println("----------------------------------------------------");
        System.out.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
        System.out.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
        System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
        System.out.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
        System.out.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

        System.out.println("----------------------------------------------------");
        System.out.println("NETWORK INFO");
        System.out.println("----------------------------------------------------");

        try {
            // display some of the network information
            System.out.println("Hostname          :  " + NetworkInfo.getHostname());
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (String ipAddress : NetworkInfo.getIPAddresses()) {
                System.out.println("IP Addresses      :  " + ipAddress);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (String fqdn : NetworkInfo.getFQDNs()) {
                System.out.println("FQDN              :  " + fqdn);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            for (String nameserver : NetworkInfo.getNameservers()) {
                System.out.println("Nameserver        :  " + nameserver);
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(MainStart.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("----------------------------------------------------");
        System.out.println("CODEC INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
        } catch (Exception ex) {
        }
        try {
            System.out.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
        } catch (Exception ex) {
        }
        try {
            System.out.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());
        } catch (Exception ex) {
        }

        System.out.println("----------------------------------------------------");
        System.out.println("CLOCK INFO");
        System.out.println("----------------------------------------------------");
        try {
            System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
        } catch (Exception ex) {
        }
        try {
            System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
        } catch (Exception ex) {
        }
        try {
            System.out.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
        } catch (Exception ex) {
        }
        try {
            System.out.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
        } catch (Exception ex) {
        }
        try {
            System.out.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
        } catch (Exception ex) {
        }
        try {
            System.out.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
        } catch (Exception ex) {
        }
        try {
            System.out.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
        } catch (Exception ex) {
        }
        try {
            System.out.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
        } catch (Exception ex) {
        }
        try {
            System.out.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
        } catch (Exception ex) {
        }
        try {
            System.out.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
        } catch (Exception ex) {
        }
        try {
            System.out.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
        } catch (Exception ex) {
        }
        try {
            System.out.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());
        } catch (Exception ex) {
        }

        System.out.println();
        System.out.println();
        System.out.println("Exiting SystemInfoExample");
    }

}
