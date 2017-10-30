import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import utils.ExportExcel;
import utils.ExportExcelUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ExportExcelUtilsTest {

    @Test
    public void testUtils(){
        try {
            OutputStream out = new FileOutputStream("D:\\test.xls");
            List<List<String>> data = new ArrayList<List<String>>();
            List<List<String>> data1 = new ArrayList<List<String>>();
            for (int i = 1; i < 5; i++) {
                List rowData = new ArrayList();
                rowData.add(String.valueOf(i));
                rowData.add("东霖柏鸿");
                data.add(rowData);
            }
            for (int i = 1; i < 5; i++) {
                List rowData = new ArrayList();
                rowData.add(String.valueOf(i));
                rowData.add("hello");
                data1.add(rowData);
            }
            String[] headers = { "ID", "用户名" };
            ExportExcelUtils eeu = new ExportExcelUtils();
            HSSFWorkbook workbook = new HSSFWorkbook();
            eeu.exportExcel(workbook, 0, "上海", headers, data, out);
            eeu.exportExcel(workbook, 1, "深圳", headers, data1, out);
            eeu.exportExcel(workbook, 2, "广州", headers, data, out);
            //原理就是将所有的数据一起写入，然后再关闭输入流。
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void  exportExcelTest() throws IOException {
        String sheetName = "测试Excel格式";
        String sheetTitle = "测试Excel格式";
        List<String> columnNames = new LinkedList<String>();
        columnNames.add("日期-String");
        columnNames.add("日期-Date");
        columnNames.add("时间戳-Long");
        columnNames.add("客户编码");
        columnNames.add("整数");
        columnNames.add("带小数的正数");

        ExportExcel exportExcel = new ExportExcel();

        exportExcel.writeExcelTitle("E:\\temp", "a", sheetName, columnNames, sheetTitle);

        for (int j = 0; j < 2; j++) {
            List<List<Object>> objects = new LinkedList<List<Object>>();
            for (int i = 0; i < 1000; i++) {
                List<Object> dataA = new LinkedList<Object>();
                dataA.add("2016-09-05 17:27:25");
                dataA.add(new Date(1451036631012L));
                dataA.add(1451036631012L);
                dataA.add("000628");
                dataA.add(i);
                dataA.add(1.323 + i);
                objects.add(dataA);
            }
            try {
                exportExcel.writeExcelData("E:\\temp", "a", sheetName, objects);
            } catch (Exception e) {
                e.printStackTrace();
            }
            objects.clear();
        }

        exportExcel.dispose();

    }
}
