
package org.synergy.io.msgs;

import java.io.IOException;

public class HelloBackMessage extends Message {
    private static final MessageType MESSAGE_TYPE = MessageType.HELLOBACK;

    // Protocol version and screen name
    private int majorVersion;
    private int minorVersion;
    private String name;

    public HelloBackMessage (int majorVersion, int minorVersion, String name) {
        super (MESSAGE_TYPE);

        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.name = name;
    }

    @Override
    protected final void writeData () throws IOException {
        dataStream.writeShort (majorVersion);
        dataStream.writeShort (minorVersion);
        dataStream.writeString (name);
    }
}
