package com.earthgee.mymap.util;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by earthgee on 16/3/25.
 * 搜索历史的管理
 */
public class HistoryManager {

    private Context context;

    public HistoryManager(Context context){
        this.context=context;
    }

    public List<String> getHistory(){
        File parentFile=context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File historyFile=new File(parentFile,"history");
        if(!historyFile.exists()){
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileInputStream inputStream=new FileInputStream(historyFile);
            int length=inputStream.available();
            byte[] bytes=new byte[length];
            int result=inputStream.read(bytes);
            inputStream.close();
            if(result==-1){
                return null;
            }else{
                String res=new String(bytes);
                Gson gson=new Gson();
                List<String> historys=gson.fromJson(res,new TypeToken<List<String>>(){}.getType());
                return historys;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   catch (JsonSyntaxException e){
            System.out.print("here");
        }   catch (Exception e){
            System.out.print("here");
        }

        return null;
    }

    public void saveHistory(List<String> historys){
        if(historys==null){
            return;
        }
        Gson gson=new Gson();
        String result=gson.toJson(historys);
        File file=context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File historyFile=new File(file,"history");
        if(!historyFile.exists()){
            try {
                historyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileOutputStream outputStream=new FileOutputStream(historyFile);
            outputStream.write(result.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
