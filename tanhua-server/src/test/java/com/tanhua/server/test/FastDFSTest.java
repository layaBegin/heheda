package com.tanhua.server.test;


import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FastDFSTest {

    @Autowired
    private FastFileStorageClient fSClient;
    @Autowired
    private FdfsWebServer webServer;

    @Test
    public void testUpload() throws IOException {
        File file = new File("E:\\hehe.jpg");
        StorePath storePath = fSClient.uploadFile(FileUtils.openInputStream(file), file.length(), "jpg", null);
        String fullPath = storePath.getFullPath();
        System.out.println("==fullPath:"+ fullPath);
        String s = webServer.getWebServerUrl() + fullPath;
        System.out.println("==s:"+s);
    }

}
