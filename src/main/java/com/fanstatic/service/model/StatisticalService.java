package com.fanstatic.service.model;

import com.fanstatic.dto.ResponseDTO;
import com.fanstatic.dto.ResponseDataDTO;
import com.fanstatic.dto.ResponseListDataDTO;
import com.fanstatic.dto.model.comboProduct.ComboProductDTO;
import com.fanstatic.dto.model.product.ProductDTO;
import com.fanstatic.dto.model.product.ProductVarientDTO;
import com.fanstatic.dto.model.statistical.DashBoardDataDTO;
import com.fanstatic.dto.model.statistical.DataDTO;
import com.fanstatic.dto.model.statistical.DataSellComboProductDTO;
import com.fanstatic.dto.model.statistical.DataSellProductDTO;
import com.fanstatic.dto.model.statistical.DataSellProductVariantDTO;
import com.fanstatic.dto.model.statistical.StatisticalRevenueDTO;
import com.fanstatic.model.ComboProduct;
import com.fanstatic.model.Product;
import com.fanstatic.model.ProductVarient;
import com.fanstatic.repository.BillRepository;
import com.fanstatic.repository.OrderItemRepository;
import com.fanstatic.repository.OrderRepository;
import com.fanstatic.util.CommonUtils;
import com.fanstatic.util.ResponseUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticalService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BillRepository billRepository;
    private final ModelMapper modelMapper;

    // thống kê data dashboard thống kê ngày , tuần , top sản phẩm
    public ResponseDTO analysisDashBoardOverViewData() {

        Date today = new Date();
        Date startDate = startDate(today);
        Date startDateOfWeek = getStartOfWeek(today);
        Date startDateOfMonth = getStartOfMonth(today);
        List<String> state = new ArrayList<>();
        state.add("COMPLETE");
        state.add("ITEM_COMPLETE");
         state.add("PAID");

        // Số lượng sản phẩm bán được trong hôm nay
        Integer soldProductsToday = orderItemRepository.countSoldProductsByDateRangeAndState(startDate, today,
                state);

        // Số lượng sản phẩm bán được trong tuần này
        Integer soldProductsThisWeek = orderItemRepository.countSoldProductsByDateRangeAndState(startDateOfWeek,
                today,
                state);

        // Số lượng sản phẩm bán được trong tháng này
        Integer soldProductsThisMonth = orderItemRepository.countSoldProductsByDateRangeAndState(
                startDateOfMonth,
                today, state);

        // Doanh thu trong hôm nay với các trạng thái A
        BigInteger revenueToday = billRepository.calculateRevenueByDateRangeAndStates(startDate, today,
                state);

        // Doanh thu trong tuần này với các trạng thái A
        BigInteger revenueThisWeek = billRepository.calculateRevenueByDateRangeAndStates(startDateOfWeek,
                today,
                state);

        // Doanh thu trong tháng này với các trạng thái A
        BigInteger revenueThisMonth = billRepository.calculateRevenueByDateRangeAndStates(startDateOfMonth,
                today,
                state);

        // Số đơn hàng trong hôm nay với các trạng thái A
        Long ordersToday = orderRepository.countOrdersByDateRangeAndStates(startDate, today, state);

        // Số đơn hàng trong tuần này với các trạng thái A
        Long ordersThisWeek = orderRepository.countOrdersByDateRangeAndStates(startDateOfWeek, today, state);

        // Số đơn hàng trong tháng này với các trạng thái A
        Long ordersThisMonth = orderRepository.countOrdersByDateRangeAndStates(startDateOfMonth, today, state);

        Date startOfYear = getStartOfYear(today);
        List<Object[]> listProductYear = orderItemRepository.findTop10BestSellingProductsByRangeAndStates(startOfYear,
                today, state);
        List<Object[]> listComboProductYear = orderItemRepository.findTop10BestSellingComboProductsByRangeAndStates(startOfYear,
                today, state);
        List<Object[]> listProductVariantYear = orderItemRepository.findTop10BestSellingProductVariantByRangeAndStates(startOfYear,
                today, state);
        
                if(listProductYear.size() > 6) {
                      listProductYear.subList(0, 5);
                }
                 if(listComboProductYear.size() > 6) {
                      listComboProductYear.subList(0, 5);
                }
                 if(listProductVariantYear.size() > 6) {
                      listProductVariantYear.subList(0, 5);
                }

        List<DataSellProductDTO> listDataSellProductDTOs = new ArrayList<>();
        List<DataSellComboProductDTO> listDataSellComboProductDTOs = new ArrayList<>();
        List<DataSellProductVariantDTO> listDataSellProductVariantDTOs = new ArrayList<>();

        for (int i = 0; i < listProductYear.size(); i++) {
            Object[] result = listProductYear.get(i);
            Product product = (Product) result[0];
            long quantity;
            if(result[1] != null) {
                quantity = (Long) result[1];
            }else {
                quantity = 0;
            }
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            DataSellProductDTO dataSellProductDTO = new DataSellProductDTO(productDTO, quantity);

            listDataSellProductDTOs.add(dataSellProductDTO);
        }
         for (int i = 0; i < listComboProductYear.size(); i++) {
            Object[] result = listComboProductYear.get(i);
            ComboProduct comboProduct = (ComboProduct) result[0];
             long quantity;
            if(result[1] != null) {
                quantity = (Long) result[1];
            }else {
                quantity = 0;
            }
            ComboProductDTO comboProductDTO = modelMapper.map(comboProduct, ComboProductDTO.class);
            DataSellComboProductDTO dataSellComboProductDTO = new DataSellComboProductDTO(comboProductDTO, quantity);

            listDataSellComboProductDTOs.add(dataSellComboProductDTO);
        }
         for (int i = 0; i < listProductVariantYear.size(); i++) {
            Object[] result = listProductVariantYear.get(i);
            ProductVarient productVariant = (ProductVarient) result[0];
             long quantity;
            if(result[1] != null) {
                quantity = (Long) result[1];
            }else {
                quantity = 0;
            }
            ProductVarientDTO productVarientDTO = modelMapper.map(productVariant, ProductVarientDTO.class);
            DataSellProductVariantDTO dataSellProductVariantDTO = new DataSellProductVariantDTO(productVarientDTO, quantity);

            listDataSellProductVariantDTOs.add(dataSellProductVariantDTO);
        }

        if (soldProductsToday == null) {
            soldProductsToday = Integer.valueOf(0);
        }
        if (soldProductsThisWeek == null) {
            soldProductsThisWeek = Integer.valueOf(0);
        }
        if (soldProductsThisMonth == null) {
            soldProductsThisMonth = Integer.valueOf(0);
        }

        if (revenueToday == null) {
            revenueToday = BigInteger.valueOf(0);
        }
        if (revenueThisWeek == null) {
            revenueThisWeek = BigInteger.valueOf(0);
        }
        if (revenueThisMonth == null) {
            revenueThisMonth = BigInteger.valueOf(0);
        }

        if (ordersToday == null) {
            ordersToday = Long.valueOf(0);
        }
        if (ordersThisWeek == null) {
            ordersThisWeek = Long.valueOf(0);
        }
        if (ordersThisMonth == null) {
            ordersThisMonth = Long.valueOf(0);
        }

        DashBoardDataDTO dashBoardOverviewDTO = new DashBoardDataDTO();
        dashBoardOverviewDTO.setListOrders(new DataDTO(ordersToday, ordersThisWeek, ordersThisMonth));
        dashBoardOverviewDTO
                .setListRevenue((new DataDTO(CommonUtils.convertToCurrencyString(revenueToday, " VNĐ"),
                        CommonUtils.convertToCurrencyString(revenueThisWeek, " VNĐ"),
                        CommonUtils.convertToCurrencyString(revenueThisMonth, " VNĐ"))));
        dashBoardOverviewDTO
                .setListSoldProducts(new DataDTO(soldProductsToday, soldProductsThisWeek,
                        soldProductsThisMonth));
        dashBoardOverviewDTO.setListTopProduct(listDataSellProductDTOs);
        dashBoardOverviewDTO.setListTopComboProduct(listDataSellComboProductDTOs);
        dashBoardOverviewDTO.setListTopProductVariant(listDataSellProductVariantDTOs);

        return ResponseUtils.success(200, "Data DashBoard", dashBoardOverviewDTO);

    }

    

    
    // tính doanh thu ngày cụ thể được truyền vào
     public ResponseDTO calculateRevenueByDate(Date statDate , Date endDate) {
          List<String> state = new ArrayList<>();
        state.add("COMPLETE");
        state.add("ITEM_COMPLETE");
        // Doanh thu trong hôm nay với các trạng thái A
        BigInteger revenue = orderRepository.calculateRevenueByDateRangeAndStates(statDate, endDate,
                state);



       return ResponseUtils.success(200, "Data Thống Kê Doanh thu Tháng", null);
    }


    // tính doanh thu mỗi tháng trong năm
    public ResponseDTO calculateRevenueByMonths(Integer year) {
     // Lấy đối tượng Calendar được khởi tạo với thời gian hiện tại của hệ thống
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        if (year != null) {
            currentYear = year;
        }

        List<String> state = new ArrayList<>();
        state.add("COMPLETE");
         state.add("PAID");
        DataDTO dataMonths = new DataDTO();
       
        for (int month = 0; month < 12; month++) {
            calendar.set(currentYear, month, 1);
            Date startDate = calendar.getTime();
            calendar.set(currentYear, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            BigInteger revenue = billRepository.calculateRevenueByDateRangeAndStates(startDate, endDate,
                    state);
            revenue = revenue == null ? BigInteger.valueOf(0) : revenue;

            dataMonths.setDataMonth(month + 1, revenue);

        }
        List<DataDTO> listData = new ArrayList<>();
        listData.add(dataMonths);

        StatisticalRevenueDTO statisticalRevenueDTO = new StatisticalRevenueDTO();
        statisticalRevenueDTO.setListdataRevenueByMonths(Arrays.asList(dataMonths.getDataMonths()));
        List<Integer> listYear = getYearRange();
        statisticalRevenueDTO.setListYear(listYear);
        return ResponseUtils.success(200, "Data Thống Kê Doanh thu Tháng", statisticalRevenueDTO);
    }


    // count số lượng khách hàng trong mỗi tháng của năm
    public ResponseDTO countCustomersByMonths(Integer year) {

        Calendar calendar = Calendar.getInstance();

        int currentYear = calendar.get(Calendar.YEAR);
        if (year != null) {
            currentYear = year;
        }

        List<String> state = new ArrayList<>();
        state.add("COMPLETE");
        DataDTO dataMonths = new DataDTO();
        // DataDTO dataMonthsStr = new DataDTO();
        for (int month = 0; month < 12; month++) {
            calendar.set(currentYear, month, 1);
            Date startDate = calendar.getTime();
            calendar.set(currentYear, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            BigInteger revenue = orderRepository.calculateRevenueByDateRangeAndStates(startDate, endDate,
                    state);
            revenue = revenue == null ? BigInteger.valueOf(0) : revenue;

            dataMonths.setDataMonth(month + 1, revenue);
            // dataMonthsStr.setDataMonth(month + 1,
            // CommonUtils.convertToCurrencyString(revenue, " VNĐ"));

        }
        List<DataDTO> listData = new ArrayList<>();
        listData.add(dataMonths);

        StatisticalRevenueDTO statisticalRevenueDTO = new StatisticalRevenueDTO();
        statisticalRevenueDTO.setListdataRevenueByMonths(Arrays.asList(dataMonths.getDataMonths()));
        List<Integer> listYear = getYearRange();
        statisticalRevenueDTO.setListYear(listYear);
        return ResponseUtils.success(200, "Data Thống Kê Doanh thu Tháng", statisticalRevenueDTO);
    }

    // cout số lượng oder mỗi tháng trong năm
    public ResponseDTO countOrdersByMonths(Integer year) {
        Calendar calendar = Calendar.getInstance();
        // int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        if (year != null) {
            currentYear = year;
        }

        List<String> state = new ArrayList<>();
        state.add("COMPLETE");
        DataDTO dataMonths = new DataDTO();
        for (int month = 0; month < 12; month++) {
            calendar.set(currentYear, month, 1);
            Date startDate = calendar.getTime();
            calendar.set(currentYear, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            Long orders = orderRepository.countOrdersByDateRangeAndStates(startDate, endDate,
                    state);

            orders = orders == null ? Long.valueOf(0) : orders;

            dataMonths.setDataMonth(month + 1, orders);

        }
        List<DataDTO> listData = new ArrayList<>();
        listData.add(dataMonths);

        StatisticalRevenueDTO statisticalRevenueDTO = new StatisticalRevenueDTO();
        statisticalRevenueDTO.setListdataRevenueByMonths(Arrays.asList(dataMonths.getDataMonths()));
        List<Integer> listYear = getYearRange();
        statisticalRevenueDTO.setListYear(listYear);
        return ResponseUtils.success(200, "Data Thống Kê Oder Tháng", statisticalRevenueDTO);
    }

    public ResponseDTO findTop10BestSellingProducts(Integer year) {
        Date today = new Date();
        // Date startDate = startDate(today);
        Date startOfYear = getStartOfYear(today);

        if (year != null) {
            startOfYear = getStartOfYear(year);
            today = getEndOfYear(year);
        }

        List<String> state = new ArrayList<>();
        state.add("ITEM_COMPLETE");
        List<Object[]> topProducts = orderItemRepository
                .findTop10BestSellingProductsByRangeAndStates(startOfYear, today, state);
        List<ResponseDataDTO> listDataSellProductDTOs = new ArrayList<>();
       List<Object[]> topProductSup = new ArrayList<>();
         if (topProducts.size() >= 5) {
            topProductSup =topProducts.subList(0, 5);
         }
        for (int i = 0; i < topProductSup.size(); i++) {
            Object[] result = topProductSup.get(i);

            Product product = (Product) result[0];
              long quantity;
            if (result[1] != null) {
                  quantity = (Long) result[1];
            }else {
                quantity = 0;
            }
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
            DataSellProductDTO dataSellProductDTO = new DataSellProductDTO(productDTO, quantity);

            listDataSellProductDTOs.add(dataSellProductDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(listDataSellProductDTOs);

        return ResponseUtils.success(200, "Danh sách top 10 sản phẩm bán chạy", reponseListDataDTO);
    }
    public ResponseDTO findTop10BestSellingComboProducts(Integer year) {
        Date today = new Date();
        // Date startDate = startDate(today);
        Date startOfYear = getStartOfYear(today);

        if (year != null) {
            startOfYear = getStartOfYear(year);
            today = getEndOfYear(year);
        }

        List<String> state = new ArrayList<>();
        state.add("ITEM_COMPLETE");
        List<Object[]> topComboProducts = orderItemRepository
                .findTop10BestSellingComboProductsByRangeAndStates(startOfYear, today, state);
        List<ResponseDataDTO> listDataSellProductDTOs = new ArrayList<>();
        if (topComboProducts.size() > 5) {

        }
        for (int i = 0; i < topComboProducts.size(); i++) {
            Object[] result = topComboProducts.get(i);

            ComboProduct comboProduct = (ComboProduct) result[0];
             long quantity;
            if (result[1] != null) {
                  quantity = (Long) result[1];
            }else {
                quantity = 0;
            }
           
            ComboProductDTO comboProductDTO = modelMapper.map(comboProduct, ComboProductDTO.class);
            DataSellComboProductDTO dataSellComboProductDTO = new DataSellComboProductDTO(comboProductDTO, quantity);

            listDataSellProductDTOs.add(dataSellComboProductDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(listDataSellProductDTOs);

        return ResponseUtils.success(200, "Danh sách top 10 combo bán chạy", reponseListDataDTO);
    }

     public ResponseDTO findTop10BestSellingProductVariant(Integer year) {
        Date today = new Date();
        // Date startDate = startDate(today);
        Date startOfYear = getStartOfYear(today);

        if (year != null) {
            startOfYear = getStartOfYear(year);
            today = getEndOfYear(year);
        }
        List<String> state = new ArrayList<>();
        state.add("ITEM_COMPLETE");
        List<Object[]> topProductVariants = orderItemRepository
                .findTop10BestSellingProductVariantByRangeAndStates(startOfYear, today, state);
        List<ResponseDataDTO> listDataSellProductDTOs = new ArrayList<>();
    

        for (int i = 0; i < topProductVariants.size(); i++) {
            Object[] result = topProductVariants.get(i);

            ProductVarient productVariant = (ProductVarient) result[0];
              long quantity;
            if (result[1] != null) {
                  quantity = (Long) result[1];
            }else {
                quantity = 0;
            }
            ProductVarientDTO productVarientDTO = modelMapper.map(productVariant, ProductVarientDTO.class);
            DataSellProductVariantDTO dataSellProductVariantDTO = new DataSellProductVariantDTO(productVarientDTO, quantity);

            listDataSellProductDTOs.add(dataSellProductVariantDTO);
        }
        ResponseListDataDTO reponseListDataDTO = new ResponseListDataDTO();
        reponseListDataDTO.setDatas(listDataSellProductDTOs);

        return ResponseUtils.success(200, "Danh sách top 10 sản phẩm size bán chạy", reponseListDataDTO);
    }

    // truyền vào date trả về ngày đầu tuần của date truyền vào
    private Date getStartOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(truncateDate(date));
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        return calendar.getTime();
    }

    //trả về ngày đàu tiên của tháng
    private Date getStartOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(truncateDate(date));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    // set date giờ phút giây, mili giây về 0
    private Date truncateDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    // trả về tháng đầu tiên ngày đầu tiên của năm
    private Date getStartOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(truncateDate(date));
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private static Date getStartOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private static Date getEndOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public Date startDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(truncateDate(date));
        return calendar.getTime();
    }

    // năm hiện tại
    private static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    // khởi tạo danh sách từ năm -> năm hiện tại
    public List<Integer> getYearRange() {
        int startYear = 2018;
        int endYear = getCurrentYear();
        List<Integer> yearRange = new ArrayList<>();
        for (int year = startYear; year <= endYear; year++) {
            yearRange.add(year);

        }
        // đảo array giảm dần
        Collections.reverse(yearRange);
        return yearRange;
    }
}
