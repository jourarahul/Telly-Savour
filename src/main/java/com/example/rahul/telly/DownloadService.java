package com.example.rahul.telly;


        import android.app.Service;
        import android.content.Intent;
        import android.os.Environment;
        import android.os.IBinder;
        import android.util.Log;
        import android.widget.Toast;

        import java.io.BufferedReader;
        import java.io.DataInputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.InputStreamReader;
        import java.net.HttpURLConnection;
        import java.net.URL;

public class DownloadService extends Service {
    String path;
    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent in, int flag,int serviceId){
        super.onStartCommand(in,flag,serviceId);
        path =  in.getStringExtra("path");
        Log.d("MSG",path+"");
        new Thread(new DownloadJob1()).start();
        //  Toast.makeText(this, "Downloading Complete..", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }
    class DownloadJob1 implements Runnable
    {
        public void run()
        {
            try
            {
                //String pathoffileonserver="/myuploads/new.mp4";

                final String urlpath=Global.global+"/FileDownloader?filepath=/"+path;
                URL url = new URL(urlpath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int resCode = connection.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK)
                {
                    Log.d("MSG","Content Type "+connection.getContentType());

                    if(connection.getContentType().contains("text/plain"))   //Logic for Text response
                    {
                        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        StringBuffer sb = new StringBuffer();

                        while (true)
                        {
                            String s = br.readLine();

                            if (s == null)
                            {
                                break;
                            }
                            sb.append(s);
                        }


                        //This time its simple String Data, But it can be JSON
                        final String ans=sb.toString();
                        Log.d("MSG","ANSWER FROM SERVER "+ans);
                    }
                    else                                             //Logic for non-text response ie file is coming
                    {
                        long filesize = connection.getContentLength();
                        String fileheadername = connection.getHeaderField("Content-Disposition");
                        Log.d("MSG", "C-D " + fileheadername);

                        String extractedstring=fileheadername.substring(fileheadername.indexOf("filename="));
                        int pos1=extractedstring.indexOf('\"');
                        int pos2=extractedstring.lastIndexOf('\"');

                        String incomingfilename = extractedstring.substring(pos1+1,pos2);

                        Log.d("MSG", "Incoming File Name "+incomingfilename);

                        long count = 0;
                        int r,per;
                        byte b[]=new byte[10000];
                        // Log.d("VODMSG", "declaration ok ");

                        File f=new File(Environment.getExternalStorageDirectory()+File.separator+incomingfilename);

                        if(!f.exists())
                        {
//                            boolean flag=f.mkdir();
                            f.createNewFile();
                            Log.d("MSG","File created name= "+Environment.getExternalStorageDirectory()+File.separator+incomingfilename+"  f.getpath "+f.getPath());
                        }

                        DataInputStream dis = new DataInputStream(connection.getInputStream());
                        FileOutputStream fos=new FileOutputStream(f.getPath());
                        // Log.d("VODMSG", "File name object ok ");

                        while(true)
                        {

                            r=dis.read(b,0,10000);
                            fos.write(b,0,r);

                            count=count+r;
                            Log.d("MSG","File downloaded= "+count);
                            per=(int)((count*100)/filesize);
                            Log.d("MYBR","per indownload service"+per);
                            Intent it=new Intent("my.download.broadcast");
                            it.putExtra("value",per);
                            sendBroadcast(it);

                            // pbar.setProgress(per);


                            if(count==filesize)
                            {
                                break;
                            }
                        }

                        // Log.d("VODMSG", "end ok ");
                        fos.close();
                        dis.close();
                        Log.d("VODMSG", "......Download Complete........ ");


                    }

                }
                else if(resCode==HttpURLConnection.HTTP_NOT_FOUND)
                {
                    Log.d("VODMSG","404 NOT FOUND");
                    Log.d("VODMSG","urlpath "+urlpath);
                    Toast.makeText(getApplicationContext(),"404 NOT FOUND\nCHECK Web Path\n"+urlpath,Toast.LENGTH_SHORT).show();

                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }
}

