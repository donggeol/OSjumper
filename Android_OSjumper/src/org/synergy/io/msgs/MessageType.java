
package org.synergy.io.msgs;

/**
 * For more information on the Synergy message format, see 
 * http://synergy-foss.org/code/filedetails.php?repname=synergy&path=%2Ftrunk%2Fsrc%2Flib%2Fsynergy%2FProtocolTypes.cpp
 */
public enum MessageType {
    HELLO ("Synergy", "[Init] Hello"),           // Not a standard message
    HELLOBACK ("Synergy", "[Init] Hello Back"),  // Not a standard message
    CNOOP ("CNOP", "[Command] NoOp"),
    CCLOSE ("CBYE", "[Command] Close"),
    CENTER ("CINN", "[Command] Enter"),
    CLEAVE ("COUT", "[Command] Leave"),
    CCLIPBOARD ("CCLP", "[Command] Clipboard"),
    CSCREENSAVER ("CSEC", "[Command] Screensaver"),
    CRESETOPTIONS ("CROP", "[Command] Reset Options"),
    CINFOACK ("CIAK", "[Command] Info Ack"),
    CKEEPALIVE ("CALV", "[Command] Keep Alive"),
    DKEYDOWN ("DKDN", "[Data] Key Down"),
    DKEYREPEAT ("DKRP", "[Data] Key Repeat"),
    DKEYUP ("DKUP", "[Data] Key Up"),
    DMOUSEDOWN ("DMDN", "[Data] Mouse Down"),
    DMOUSEUP ("DMUP", "[Data] Mouse Up"),
    DMOUSEMOVE ("DMMV", "[Data] Mouse Move"),
    DMOUSERELMOVE ("DMRM", "[Data] Mouse Relative Move"),
    DMOUSEWHEEL ("DMWM", "[Data] Mouse Wheel"),
    DCLIPBOARD ("DCLP", "[Data] Clipboard"),
    DFILETRANSFER ("DFTR", "[Data] File Transfer"),
    DDRAGINFO ("DDRG", "[Data] Drag Info"),
    DINFO ("DINF", "[Data] Info"),
    DSETOPTIONS ("DSOP", "[Data] Set Options"),
    QINFO ("QINF", "[Query] Info"),
    EINCOMPATIBLE ("EICV", "[Error] Incompatible"),
    EBUSY ("EBSY", "[Error] Busy"),
    EUNKNOWN ("EUNK", "[Error] Unknown"),
    EBAD ("EBAD", "[Error] Bad");

    private MessageType (String value, String commonName) {
        this.value = value;
        this.commonName = commonName;
    }

    public static MessageType fromString (String messageValue) {
        for (MessageType t : MessageType.values ()) {
            if (messageValue.equalsIgnoreCase (t.value)) {
                return t;
            }
        }
        throw new IllegalArgumentException ("No MessageType with value " + messageValue);
    }

    public String getValue () {
        return value;
    }

    public String toString () {
        return commonName;
    }

    private String value;
    private String commonName;
}
