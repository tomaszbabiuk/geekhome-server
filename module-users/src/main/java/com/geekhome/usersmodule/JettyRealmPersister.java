package com.geekhome.usersmodule;

import com.geekhome.common.configuration.DescriptiveName;
import com.geekhome.common.logging.LoggingService;
import com.geekhome.common.logging.ILogger;
import com.geekhome.httpserver.modules.CollectorCollection;
import com.geekhome.httpserver.modules.ObjectNotFoundException;
import org.eclipse.jetty.util.security.Password;

import java.io.*;
import java.util.concurrent.locks.ReentrantLock;

public class JettyRealmPersister {
    protected ILogger _logger = LoggingService.getLogger();
    private ReentrantLock _lock = new ReentrantLock();
    private String _fileName;

    protected String getFileName() {
        return _fileName;
    }

    protected String createFileName() throws IOException {
        String currentDir = new File(".").getCanonicalPath();
        return String.format("%s%sgeekhomerealm.properties",currentDir,File.separator);
    }

    public JettyRealmPersister() throws IOException {
        _fileName = createFileName();
    }

    public void save(CollectorCollection<User> users) throws IOException {
        File file = new File(getFileName());

        _lock.tryLock();
        BufferedWriter writer = null;
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            writer = new BufferedWriter(fw);

            saving(fw, users);

            writer.flush();

        } catch (Exception ex) {
            _logger.error("Exception while saving users %s configuration", ex);
        } finally {
            _lock.unlock();

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    protected void saving(FileWriter writer, CollectorCollection<User> users) throws Exception {
        addDefaultUserIfNeeded(users);

        for (User user : users.values()) {
            String userLine = String.format("%s: %s,user\n", user.getName(), Password.obfuscate(user.getPassword()));
            writer.write(userLine);
        }
    }

    private void addDefaultUserIfNeeded(CollectorCollection<User> users) {
        if (users.size() == 0) {
            User defaultUser = new User(new DescriptiveName("user", ""),"user");
            users.add(defaultUser);
        }
    }

    public CollectorCollection<User> load() throws IOException, ObjectNotFoundException {
        CollectorCollection<User> result = new CollectorCollection<>();

        File f = new File(getFileName());
        if (f.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;
            while ((line = br.readLine()) != null) {
                String[] userPassword = line.split(": ");
                String userName = userPassword[0];
                String password = userPassword[1].replace(",user", "");
                if (password.startsWith("OBF:")) {
                    password = Password.deobfuscate(password);
                }
                User user = new User(new DescriptiveName(userName, userName), password);
                result.add(user);
            }
            br.close();
        }

        return result;
    }
}