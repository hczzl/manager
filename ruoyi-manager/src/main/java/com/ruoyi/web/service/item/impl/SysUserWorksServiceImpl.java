package com.ruoyi.web.service.item.impl;



import com.ruoyi.web.domain.SysUserWorks;
import com.ruoyi.web.mapper.item.SysUserWorksMapper;
import com.ruoyi.web.service.item.SysUserWorksService;
import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhongzhilong
 * @date 2020-07-30
 * @description 用户工作情况统计实现类
 */
@Service
public class SysUserWorksServiceImpl implements SysUserWorksService {
    @Autowired
    private SysUserWorksMapper sysUserWorksMapper;

    @Override
    public int insertUserWorks(SysUserWorks sysUserWorks) {
        return sysUserWorksMapper.insertUserWorks(sysUserWorks);
    }

    @Override
    public int updateUserWorks(SysUserWorks sysUserWorks) {
        return sysUserWorksMapper.updateUserWorks(sysUserWorks);
    }

    @Override
    public List<SysUserWorks> selectUserWorks(SysUserWorks sysUserWorks) {
        return sysUserWorksMapper.selectUserWorks(sysUserWorks);
    }

    @Override
    public Integer selectWorkDays(Long startTime, Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String date = sdf.format(new Date(startTime));
        String[] arr = date.split("-");
        String year = arr[0];
        String month = arr[1];
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("month", month);
        map.put("year", year);
        Integer period = sysUserWorksMapper.selectWorkDays(map);
        return period;
    }

    @Override
    public void buildPieChart(Sheet sheet, List<String> fldNameArr, List<Integer> dataList, int col, int col2, int row, int row2, int firstRow) {
        // 饼状图的实现
        String sheetName = sheet.getSheetName();
        // 创建一个画布
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        // 画一个图区域
        // 前四个默认0，从第1行到第7行,从第4列到第5列的区域
        // ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 4, 0, 6, 8);
        ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, col, row, col2, row2);
        // 创建一个chart对象
        Chart chart = drawing.createChart(anchor);
        CTChart ctChart = ((XSSFChart) chart).getCTChart();
        CTPlotArea ctPlotArea = ctChart.getPlotArea();
        CTBoolean ctBoolean = null;
        CTPie3DChart ctPie3Dchart = null;
        CTPieChart ctPieChart = null;
        // ctPieChart = ctPlotArea.addNewPieChart();
        ctPie3Dchart = ctPlotArea.addNewPie3DChart();
        // ctBoolean = ctPieChart.addNewVaryColors();
        ctBoolean = ctPie3Dchart.addNewVaryColors();
        // 创建序列,并且设置选中区域
        for (int i = 0; i < fldNameArr.size() - 1; i++) {
            CTPieSer ctPieSer = null;
            // ctPieSer = ctPieChart.addNewSer();
            ctPieSer = ctPie3Dchart.addNewSer();
            CTSerTx ctSerTx = ctPieSer.addNewTx();
            // 图例区
            CTStrRef ctStrRef = ctSerTx.addNewStrRef();
            // 选定区域第0行,第1,2,3列标题作为图例 //1 2 3
            String legendDataRange = new CellRangeAddress(firstRow, firstRow, i + 1, i + 1).formatAsString(sheetName, true);
            ctStrRef.setF(legendDataRange);
            ctPieSer.addNewIdx().setVal(i);
            // 横坐标区
            CTAxDataSource cttAxDataSource = ctPieSer.addNewCat();
            ctStrRef = cttAxDataSource.addNewStrRef();
            // 选第0列,第1-6行作为横坐标区域
            String axisDataRange = new CellRangeAddress(firstRow, firstRow + dataList.size() - 1, 0, 0).formatAsString(sheetName, true);
            ctStrRef.setF(axisDataRange);
            // 数据区域
            CTNumDataSource ctNumDataSource = ctPieSer.addNewVal();
            CTNumRef ctNumRef = ctNumDataSource.addNewNumRef();
            // 选第1-6行,第1-3列作为数据区域 //1 2 3
            String numDataRange = new CellRangeAddress(firstRow, firstRow + dataList.size() - 1, i + 1, i + 1).formatAsString(sheetName, true);
            ctNumRef.setF(numDataRange);
            // 显示边框线
            ctPieSer.addNewSpPr().addNewLn().addNewSolidFill().addNewSrgbClr().setVal(new byte[]{0, 0, 0});
            // 设置标签格式
            ctBoolean.setVal(true);
            // 设置标签格式
            // CTDLbls newDLbls = ctPieSer.addNewDLbls();
            // newDLbls.setShowLegendKey(ctBoolean);
            // newDLbls.setShowPercent(ctBoolean);
        }
        // legend图注
        CTLegend ctLegend = ctChart.addNewLegend();
        ctLegend.addNewLegendPos().setVal(STLegendPos.B);
        // 显示图注但不与图表重叠
        ctLegend.addNewOverlay().setVal(false);
    }
}
