package com.fengxiang.service.impl;

import com.fengxiang.entity.OrderDetail;
import com.fengxiang.entity.Orders;
import com.fengxiang.mapper.OrderDetailMapper;
import com.fengxiang.mapper.OrderMapper;
import com.fengxiang.mapper.UserMapper;
import com.fengxiang.service.ReportService;
import com.fengxiang.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

}
