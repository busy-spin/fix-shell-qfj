package io.github.busy_spin.fix_showcase.qfj.utils.store;

import quickfix.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static quickfix.FileStoreFactory.SETTING_FILE_STORE_PATH;

public class DefaultBaseDirFileStoreFactory implements MessageStoreFactory {

    private final FileStoreFactory fileStoreFactory;

    private final SessionSettings settings;

    private final Map<SessionID, MessageStore> messageStoreMap = new HashMap<>();

    public DefaultBaseDirFileStoreFactory(SessionSettings settings) {
        String baseDir = System.getProperty("user.home");
        if (baseDir == null) {
            baseDir = System.getProperty("java.io.tmpdir");
        }

        if (baseDir == null) {
            throw new RuntimeException("No default location for file store.");
        }
        this.settings = settings;
        settings.setString(SETTING_FILE_STORE_PATH, baseDir);
        fileStoreFactory = new FileStoreFactory(settings);
    }

    @Override
    public MessageStore create(SessionID sessionID) {
        MessageStore messageStore = fileStoreFactory.create(sessionID);
        messageStoreMap.put(sessionID, messageStore);
        return messageStore;
    }

    public void setNextNumIn(SessionID sessionID, int number) {
        MessageStore messageStore = messageStoreMap.get(sessionID);
        if (messageStore != null) {
            try {
                messageStore.setNextTargetMsgSeqNum(number);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setNextNumOut(SessionID sessionID, int number) {
        MessageStore messageStore = messageStoreMap.get(sessionID);
        if (messageStore != null) {
            try {
                messageStore.setNextSenderMsgSeqNum(number);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
