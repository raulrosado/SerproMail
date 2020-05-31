package com.serpro.email;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.FromTerm;
import javax.mail.search.SearchTerm;
import java.util.logging.Logger;
import javax.mail.internet.MimeMessage;

public class MailService {

    private Session session;
    private Store store;
    private Folder folder;
    private String _messageId;

    // hardcoding protocol and the folder
    // it can be parameterized and enhanced as required
    private String protocol = "imap";
    private String file = "INBOX";
    UIDFolder UID_Folder = null; // cast folder to UIDFolder interface

    public MailService() {

    }

    public boolean isLoggedIn() {
        return store.isConnected();
    }

    /**
     * to login to the mail host server
     */
    public void login(String host, String username, String password)
            throws Exception {
        URLName url = new URLName(protocol, host, 143, file, username, password);

        if (session == null) {
            Properties props = null;
            try {
                props = System.getProperties();
            } catch (SecurityException sex) {
                props = new Properties();
            }
            session = Session.getInstance(props, null);
        }

        store = session.getStore(url);
        store.connect();

        folder = store.getFolder(url);
        UID_Folder = (UIDFolder)folder; // cast folder to UIDFolder interface
        folder.open(Folder.READ_ONLY);
    }

    public Properties returProps(){
        Properties props = null;
        try {
            props = System.getProperties();
        } catch (SecurityException sex) {
            props = new Properties();
        }
        return props;
    }

    /**
     * to logout from the mail host server
     */
    public void logout() throws MessagingException {
        folder.close(false);
        store.close();
        store = null;
        session = null;
    }

    public int getMessageCount() {
        int messageCount = 0;
        try {
            messageCount = folder.getMessageCount();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
        return messageCount;
    }

    public Message[] getMessages() throws MessagingException {
        return folder.getMessages();
    }

    public Message[] getFolder(String emailSistem) throws MessagingException {
            FlagTerm unseenFlagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
            FromTerm fromTerm = new FromTerm(new InternetAddress(emailSistem));
            SearchTerm searchTerm = new AndTerm(unseenFlagTerm, fromTerm);
            Message[] result = folder.search(searchTerm);
            return result;
    }

    public Session getSession() {
        return session;
    }

    public UIDFolder getUID_Folder(){
        return UID_Folder;
    }

}