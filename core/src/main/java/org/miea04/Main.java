package org.miea04;

import org.miea04.core.MCSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    public static void main(String[] args) {
        MCSync mcSync = new MCSync();
//        mcSync.start(
//                "WORK_PATH@D:/program/Java/McMod/MCSyncProject/neoforge-1.21.1/测试用服务器/test-server03-neoforge-1.21.1" +
//                        "|" +
//                        "SERVICE_MODE@SERVER" +
//                        "|" +
//                        "NODE_TYPE@complete" +
//                        "|" +
//                        "DELEGATE_HOST@127.0.0.1:2814"
//        );

        mcSync.start(
                "WORK_PATH@D:\\program\\Java\\McMod\\MCSyncProject\\neoforge-1.21.1\\测试用服务器\\test-server03-neoforge-1.21.1" +
                        "|" +
                        "SERVICE_MODE@SERVER" +
                        "|" +
                        "NODE_TYPE@delegate" +
                        "|" +
                        "DELEGATE_HOST@hkt.mieai.top:19241"
        );
    }
}