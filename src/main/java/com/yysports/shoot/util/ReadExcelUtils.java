package com.yysports.shoot.util;

import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.services.ShootMgeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

public class ReadExcelUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadExcelUtils.class);
    private static final String ERROR_MSG = "Workbook对象为空！";
    private Workbook wb;
    private Sheet sheet;
    private Row row;

    public ReadExcelUtils(String filepath) {
        if (filepath == null) {
            return;
        }
        String ext = filepath.substring(filepath.lastIndexOf('.'));
        InputStream is = null;
        try {
            is = new FileInputStream(filepath);
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    LOGGER.error("关闭FileInputStream异常", e);
                }
            }
        }
    }

    public ReadExcelUtils(MultipartFile myfile, String fileName) {
        if (myfile.isEmpty()) {
            return;
        }
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        InputStream is = null;
        try {
            is = myfile.getInputStream();
            if (".xls".equals(ext)) {
                wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = null;
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("FileNotFoundException", e);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    LOGGER.error("关闭FileInputStream异常", e);
                }
            }
        }
    }

    /**
     * 读取Excel表格表头的内容
     *
     * @return String 表头内容的数组
     * @author zengwendong
     */
    public String[] readExcelTitle() throws Exception {
        if (wb == null) {
            throw new Exception(ERROR_MSG);
        }
        sheet = wb.getSheetAt(0);
        row = sheet.getRow(0);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        LOGGER.info("colNum:" + colNum);
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            // title[i] = getStringCellValue(row.getCell((short) i));
            title[i] = row.getCell(i).getStringCellValue();
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     *
     * @param
     * @return Map 包含单元格数据内容的Map对象
     * @author zengwendong
     */
    public Map<Integer, Map<Integer, Object>> readExcelContent() throws Exception {
        if (wb == null) {
            throw new Exception(ERROR_MSG);
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                if (row.getCell(j) == null || "".equals(row.getCell(j))) {
                    break;
                }
                row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                Object obj = getCellFormatValue(row.getCell(j));
                if (obj == null || "".equals(obj)) {
                    break;
                }
                cellValue.put(j, obj);
                j++;
            }
            if (!cellValue.isEmpty()) {
                content.put(i, cellValue);
            }
        }
        return content;
    }

    /**
     * 根据Cell类型设置数据
     *
     * @param cell
     * @return
     * @author zengwendong
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                    DecimalFormat df = new DecimalFormat("#.##");
                    cellvalue = String.valueOf(df.format(cell.getNumericCellValue()));
                    break;
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2013-7-10
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {
                        // 如果是纯数字
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }


    /**
     * @param contentMap
     * @Description: 去除空行
     */
    public void removeEmptyLine(Map<Integer, Map<Integer, Object>> contentMap) {
        List<Integer> emptyLineKey = new ArrayList<>();
        for (int i = 1; i <= contentMap.size(); i++) {
            boolean falg = true;// 是否为空行标识,默认为是
            Map<Integer, Object> myrow = (Map<Integer, Object>) contentMap.get(i);
            for (Map.Entry<Integer, Object> entry : myrow.entrySet()) {
                int key = entry.getKey();
                if (null != myrow.get(key) && StringUtils.isNotBlank(myrow.get(key).toString())) {
                    falg = false;
                    break;
                }
            }
            if (falg) {
                emptyLineKey.add(i);
            }
        }
        for (Integer rowNum : emptyLineKey) {
            contentMap.remove(rowNum);
        }
    }

    /**
     * 读取Excel数据内容
     *
     * @param
     * @return Map 包含单元格数据内容的Map对象(保留空列)
     * @author lijie
     */
    public Map<Integer, Map<Integer, Object>> readExcelContentAll() throws Exception {
        if (wb == null) {
            throw new Exception(ERROR_MSG);
        }
        Map<Integer, Map<Integer, Object>> content = new HashMap<Integer, Map<Integer, Object>>();

        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                if (row.getCell(j) != null) {
                    row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    Object obj = getCellFormatValue(row.getCell(j));
                    cellValue.put(j, obj);
                } else {
                    cellValue.put(j, "");
                }

                j++;
            }
            if (!cellValue.isEmpty()) {
                content.put(i, cellValue);
            }
        }
        return content;
    }

    public List<Map<Integer, Object>> readExcelContentList() throws Exception {
        List<Map<Integer, Object>> list = new ArrayList<>();
        if (wb == null) {
            throw new Exception(ERROR_MSG);
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            Map<Integer, Object> cellValue = new HashMap<Integer, Object>();
            while (j < colNum) {
                if (row.getCell(j) == null || "".equals(row.getCell(j))) {
                    break;
                }
                row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                Object obj = getCellFormatValue(row.getCell(j));
                if (obj == null || "".equals(obj)) {
                    break;
                }
                cellValue.put(j, obj);
                j++;
            }
            if (!cellValue.isEmpty()) {
                list.add(cellValue);
            }
        }
        return list;
    }


    public List<ShootBean> readExcelCqList(ShootMgeService shootMgeService) throws Exception {
        if (wb == null) {
            throw new Exception(ERROR_MSG);
        }
        sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        List<ShootBean> list = new ArrayList<>(colNum);
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            ShootBean shootBean = new ShootBean();
            //Object obj0 = row.getCell(0);//用户id
            Object obj0 = this.getCellFormatValue(row.getCell(0));
            if(checkIsBlank(obj0)) {
                LOGGER.error("导入数据异常，存在用户id错误，行号={}，uid={}",i,obj0);
                continue;
            }
            long uid = Integer.valueOf(String.valueOf(obj0));
            shootBean.setUid(uid);
            Object obj1 = this.getCellFormatValue(row.getCell(1));//鞋子款号
            if(checkIsBlank(obj1)) {
                LOGGER.error("导入数据异常，存在用户鞋编码错误，行号={}, 编码={}",i, obj1);
                continue;
            }
            shootBean.setItemModel(String.valueOf(obj1));

            Object obj2 = this.getCellFormatValue(row.getCell(2));//鞋子尺码
            if(!checkIsBlank(obj2)) {
                shootBean.setShoesSize(String.valueOf(obj2));
            }

            Object obj3 = this.getCellFormatValue(row.getCell(3));//店铺名称
            if(checkIsBlank(obj3)) {
                LOGGER.error("导入数据异常，店铺名称异常，行号={}, 店铺名称={}",i, obj3);
                continue;
            }
            shootBean.setShopName(String.valueOf(obj3));

            String uuid = UUID.randomUUID().toString().replace("-","");
            shootBean.setSerialNo(uuid);
            /*int count = shootMgeService.updateUserById(shootBean);
            if(count == 0) {
                LOGGER.error("更新用户失败!,uid={}", uid);
                continue;
            }*/
            list.add(shootBean);
        }
        return list;
    }

    public List<ShootBean> readExcelList(ShootMgeService shootMgeService) throws Exception {
        if (wb == null) {
            throw new Exception(ERROR_MSG);
        }
        sheet = wb.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();
        row = sheet.getRow(0);
        List<ShootBean> list = new ArrayList<>();
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            Object obj0 = this.getCellFormatValue(row.getCell(0));
            Object obj1 = this.getCellFormatValue(row.getCell(1));
            if(checkIsBlank(obj0) || checkIsBlank(obj1)) {
                LOGGER.error("导入数据异常，存在用户id错误，行号={}，uid={}",i,obj0);
                continue;
            }
            ShootBean shootBean1 = new ShootBean();
            long minUid = Integer.valueOf(String.valueOf(obj0));
            long maxUid = Integer.valueOf(String.valueOf(obj1));
            shootBean1.setMinUid(minUid);
            shootBean1.setMaxUid(maxUid);
            List<ShootBean> shootBeanList = shootMgeService.getShootBeanList(shootBean1);

            Object obj2 = this.getCellFormatValue(row.getCell(2));//鞋子款号
            if(checkIsBlank(obj2)) {
                LOGGER.error("导入数据异常，存在用户鞋编码错误，行号={}, 编码={}",i, obj2);
                continue;
            }
            Object obj3 = this.getCellFormatValue(row.getCell(3));//鞋子尺码
            Object obj4 = this.getCellFormatValue(row.getCell(4));//店铺名称
            if(checkIsBlank(obj4)) {
                LOGGER.error("导入数据异常，店铺名称异常，行号={}, 店铺名称={}",i, obj4);
                continue;
            }

            for(int a = 0; a < shootBeanList.size(); a++) {
                ShootBean shootBean = new ShootBean();
                ShootBean bean = shootBeanList.get(a);
                long id = bean.getId();
                shootBean.setId(id);
                shootBean.setUid(id);
                shootBean.setItemModel(String.valueOf(obj2));
                if(!checkIsBlank(obj3)) {
                    shootBean.setShoesSize(String.valueOf(obj3));
                }
                shootBean.setShopName(String.valueOf(obj4));
                String uuid = UUID.randomUUID().toString().replace("-","");
                shootBean.setSerialNo(uuid);
                list.add(shootBean);
            }

        }
        return list;
    }



    private boolean checkIsBlank(Object obj) {
        String str = obj == null || "".equals(obj) || "null".equals(obj) || "NULL".equals(obj) ? "" : String.valueOf(obj);
        return str.length() == 0;
    }

}
