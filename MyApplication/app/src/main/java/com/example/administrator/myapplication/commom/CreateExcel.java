package com.example.administrator.myapplication.commom;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CreateExcel {
    // 准备设置excel工作表的标题
    private WritableSheet sheet;
    /**创建Excel工作薄*/
    private WritableWorkbook wwb;
    private String[] title = { "日期", "内容"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    public CreateExcel() {
        excelCreate();
    }

    public void excelCreate() {
        try {
            /**输出的excel文件的路径*/
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/family_bill";
            File file = new File(filePath, "bill.xls");
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            wwb = Workbook.createWorkbook(file);
            /**添加第一个工作表并设置第一个Sheet的名字*/

            sheet = wwb.createSheet("便签列表", 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveDataToExcel(List<String[]> content) throws Exception {
        Label label;
        for (int i = 0; i < title.length; i++) {
            /**Label(x,y,z)其中x代表单元格的第x+1列，第y+1行, 单元格的内容是y
             * 在Label对象的子对象中指明单元格的位置和内容
             * */
            label = new Label(i, 0, title[i]);
            /**将定义好的单元格添加到工作表中*/
            sheet.addCell(label);
        }
        /*
         * 把数据填充到单元格中
         * 需要使用jxl.write.Number
         * 路径必须使用其完整路径，否则会出现错误
         */

        for (int j=0;j<content.size();j++){
            String[] map = content.get(j);
            for (int i = 0; i < title.length; i++) {
                Label labeli = new Label(i, j+1, map[i]);
                sheet.addCell(labeli);
            }
        }

        // 写入数据
        wwb.write();
        // 关闭文件
        wwb.close();
    }
}
