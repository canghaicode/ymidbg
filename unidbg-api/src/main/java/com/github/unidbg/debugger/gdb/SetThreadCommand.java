package com.github.unidbg.debugger.gdb;

import com.github.unidbg.Emulator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class SetThreadCommand implements GdbStubCommand {

    private static final Log log = LogFactory.getLog(SetThreadCommand.class);

    @Override
    public boolean processCommand(Emulator<?> emulator, GdbStub stub, String command) {
        char type = command.charAt(1);
        int thread = Integer.parseInt(command.substring(2), 16);
        if (log.isDebugEnabled()) {
            log.debug("Set thread type=" + type + ", thread=" + thread);
        }
        switch (type) {
            case 'c':
            case 'g':
                stub.makePacketAndSend("OK");
                break;
            default:
                stub.makePacketAndSend("E22");
                break;
        }
        return true;
    }

}
