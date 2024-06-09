package io.github.busy_spin.fix_showcase.qfj.utils.store;

import quickfix.*;

import java.io.IOException;
import java.nio.file.Paths;

import static quickfix.FileStoreFactory.SETTING_FILE_STORE_PATH;

public class DefaultBaseDirFileStoreFactory implements MessageStoreFactory {

    private final FileStoreFactory fileStoreFactory;


    public DefaultBaseDirFileStoreFactory(SessionSettings settings) {
        String baseDir = System.getProperty("user.home");
        if (baseDir == null) {
            baseDir = System.getProperty("java.io.tmpdir");
        }

        if (baseDir == null) {
            throw new RuntimeException("No default location for file store.");
        }
        String storePath = Paths.get(baseDir, ".qfj-fix-shell").toString();
        settings.setString(SETTING_FILE_STORE_PATH, storePath);
        fileStoreFactory = new FileStoreFactory(settings);
    }

    @Override
    public MessageStore create(SessionID sessionID) {
        return fileStoreFactory.create(sessionID);
    }

    public int setNextNumIn(SessionID sessionID, int number) {
        FileStore messageStore = (FileStore) fileStoreFactory.create(sessionID);
        try {
            int previousNumber = messageStore.getNextTargetMsgSeqNum();
            messageStore.setNextTargetMsgSeqNum(number);

            return previousNumber;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                messageStore.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int setNextNumOut(SessionID sessionID, int number) {
        FileStore messageStore = (FileStore) fileStoreFactory.create(sessionID);
        try {
            int previousNumber = messageStore.getNextTargetMsgSeqNum();
            messageStore.setNextTargetMsgSeqNum(number);

            return previousNumber;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                messageStore.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
