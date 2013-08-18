package com.zpadtech.pcsynchronizer;

import java.io.File;
import java.io.IOException;
//import java.util.concurrent.ExecutorService;

import android.os.AsyncTask;
import android.util.Log;
import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class FtpSync implements Runnable {

	private FTPClient mFTPClient;
	public String getmFTPHost() {
		return mFTPHost;
	}


	public void setmFTPHost(String mFTPHost) {
		this.mFTPHost = mFTPHost;
	}


	public int getmFTPPort() {
		return mFTPPort;
	}


	public void setmFTPPort(int mFTPPort) {
		this.mFTPPort = mFTPPort;
	}


	public String getmFTPUser() {
		return mFTPUser;
	}


	public void setmFTPUser(String mFTPUser) {
		this.mFTPUser = mFTPUser;
	}


	public String getmFTPPassword() {
		return mFTPPassword;
	}


	public void setmFTPPassword(String mFTPPassword) {
		this.mFTPPassword = mFTPPassword;
	}


	public String getTargetStoragePath() {
		return targetStoragePath;
	}


	public void setTargetStoragePath(String targetStoragePath) {
		this.targetStoragePath = targetStoragePath;
	}


	private String mFTPHost = "10.0.2.2";
	private int mFTPPort = 21;
	private String mFTPUser = "778daf97-b5fe-4804-a94b-5474628ba169";
	private String mFTPPassword = "cba31853-5059-4742-804d-0b3b2c2e96e3";
	private String targetStoragePath = "/sdcard/pcsync/";

    
    @Override
    public void run() {
    	// TODO Auto-generated method stub
        try {
        	mFTPClient = new FTPClient();
            String[] welcome = mFTPClient.connect(mFTPHost, mFTPPort);  
            if (welcome != null) {  
                for (String value : welcome) {  
                    Log.v("FtpSync", "connect " + value);  
                }  
            }  
            mFTPClient.login(mFTPUser, mFTPPassword);  
            FTPFile fileList[] = mFTPClient.list();
            File targetStoragePathFile = new File(targetStoragePath);
            if(!targetStoragePathFile.exists()){
            	Log.v("FtpSync", "Create dirs");
            	if(!targetStoragePathFile.mkdir()){
            		Log.v("FtpSync", "mkdir returns false");
            	}
            	if(!targetStoragePathFile.exists()){
            		Log.v("FtpSync", "Still not exist");
            	}
            }
            for(FTPFile ftpFile : fileList){
            	String ftpFilename = ftpFile.getName();
            	File localFile = new File(targetStoragePath + ftpFilename);
            	if(ftpFile.getType() == FTPFile.TYPE_DIRECTORY){
            		localFile.mkdirs();
            		Log.v("FtpSync", "Creating dir: "+targetStoragePath + ftpFilename);
            		continue;
            	}
            	
            	if(localFile.exists() && (localFile.length() == ftpFile.getSize())){
            		Log.v("FtpSync", "Object already downloaded");
            	}else{
            		Log.v("FtpSync", "Before download");
            		mFTPClient.download(ftpFilename, localFile);
            		Log.v("FtpSync", "After download");
            	}
            }
        }catch (IllegalStateException illegalEx) {  
            illegalEx.printStackTrace();  
        }catch (IOException ex) {  
            ex.printStackTrace();  
        }catch (FTPIllegalReplyException e) {  
            e.printStackTrace();  
        }catch (FTPException e) {  
            e.printStackTrace();
        }  
        catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FTPListParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
        Log.v("FtpSync", "Exiting");

	}

}
