package id.urbanwash.wozapp;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

import id.urbanwash.wozapp.model.CustomerBean;
import id.urbanwash.wozapp.model.EmployeeBean;

public class Installation {

    private static String sID = null;
    private static final String INSTALLATION = "INSTALLATION";
    private static final String PROFILE_CUSTOMER = "PROFILE_CUSTOMER";
    private static final String PROFILE_EMPLOYEE = "PROFILE_EMPLOYEE";

    public synchronized static String getInstallationId(Context context) {

        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists())
                    writeInstallationFile(installation);
                sID = readInstallationFile(installation);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sID;
    }

    private static String readInstallationFile(File installation) throws IOException {

        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        f.readFully(bytes);
        f.close();
        return new String(bytes);
    }

    private static void writeInstallationFile(File installation) throws IOException {

        FileOutputStream out = new FileOutputStream(installation);
        String id = UUID.randomUUID().toString();
        out.write(id.getBytes());
        out.close();
    }

    public static CustomerBean getCustomerProfile(Context context) {

        CustomerBean customerBean = null;
        ObjectInputStream objectinputstream = null;

        try {

            File file = new File(context.getFilesDir(), PROFILE_CUSTOMER);
            FileInputStream streamIn = new FileInputStream(file);
            objectinputstream = new ObjectInputStream(streamIn);
            customerBean = (CustomerBean) objectinputstream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectinputstream);
        }

        return customerBean;
    }

    public static CustomerBean saveCustomerProfile(Context context, CustomerBean customerBean) {

        ObjectOutputStream objectOutputStream = null;

        try {

            File installation = new File(context.getFilesDir(), PROFILE_CUSTOMER);
            FileOutputStream streamOut = new FileOutputStream(installation);
            objectOutputStream = new ObjectOutputStream(streamOut);
            objectOutputStream.writeObject(customerBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectOutputStream);
        }

        return customerBean;
    }

    public static EmployeeBean getEmployeeProfile(Context context) {

        EmployeeBean employeeBean = null;
        ObjectInputStream objectinputstream = null;

        try {

            File file = new File(context.getFilesDir(), PROFILE_EMPLOYEE);
            FileInputStream streamIn = new FileInputStream(file);
            objectinputstream = new ObjectInputStream(streamIn);
            employeeBean = (EmployeeBean) objectinputstream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectinputstream);
        }

        return employeeBean;
    }

    public static EmployeeBean saveEmployeeProfile(Context context, EmployeeBean employeeBean) {

        ObjectOutputStream objectOutputStream = null;

        try {

            File installation = new File(context.getFilesDir(), PROFILE_EMPLOYEE);
            FileOutputStream streamOut = new FileOutputStream(installation);
            objectOutputStream = new ObjectOutputStream(streamOut);
            objectOutputStream.writeObject(employeeBean);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(objectOutputStream);
        }

        return employeeBean;
    }

    private static void close(InputStream inputStream) {

        try {
            if (inputStream != null) {
                inputStream.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void close(OutputStream outputStream) {

        try {
            if (outputStream != null) {
                outputStream.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
