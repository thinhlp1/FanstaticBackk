package com.fanstatic.util;



import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ParamUtils {

    @Autowired
    HttpServletRequest request;


    /**
     * Đọc chuỗi giá trị của tham số
     *
     * @param name         tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public String getString(String name, String defaultValue) {
        String value = request.getParameter(name);
        return value != null ? value : defaultValue;
    }

    /**
     * Đọc số nguyên giá trị của tham số
     *
     * @param name         tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public int getInt(String name, int defaultValue) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ nếu giá trị không phải số nguyên
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Đọc số thực giá trị của tham số
     *
     * @param name         tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public double getDouble(String name, double defaultValue) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                // Xử lý ngoại lệ nếu giá trị không phải số thực
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Đọc giá trị boolean của tham số
     *
     * @param name         tên tham số
     * @param defaultValue giá trị mặc định
     * @return giá trị tham số hoặc giá trị mặc định nếu không tồn tại
     */
    public boolean getBoolean(String name, boolean defaultValue) {
        String value = request.getParameter(name);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    /**
     * Đọc giá trị thời gian của tham số
     *
     * @param name    tên tham số
     * @param pattern là định dạng thời gian
     * @return giá trị tham số hoặc null nếu không tồn tại
     * @throws RuntimeException lỗi sai định dạng
     */
    public Date getDate(String name, String pattern) {
        String value = request.getParameter(name);
        if (value != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            try {
                return dateFormat.parse(value);
            } catch (ParseException e) {
                // Xử lý ngoại lệ nếu giá trị không đúng định dạng
                throw new RuntimeException("Invalid date format");
            }
        }
        return null;
    }

    /**
     * Lưu file upload vào thư mục
     *
     * @param file chứa file upload từ client
     * @param path đường dẫn tính từ webroot
     * @return đối tượng chứa file đã lưu hoặc null nếu không có file upload
     * @throws RuntimeException lỗi lưu file
     */
	public String save(MultipartFile file, String path) {
		File dir = new File(request.getServletContext().getRealPath(path));
		if(!dir.exists()) {
			dir.mkdir();
		}
		try {
			File saveFile = new File(dir, file.getOriginalFilename());
			file.transferTo(saveFile);
			return saveFile.getName();
		} catch (Exception e) {
            e.printStackTrace();
			throw new RuntimeException();
		}
	}

    public String saveSpringBootUpdated(MultipartFile file, String path) {

        // Lấy đường dẫn tới thư mục hiện tại của ứng dụng
        String currentWorkingDir = System.getProperty("user.dir");

        // Tạo đối tượng File từ đường dẫn tương đối
        File dir = new File(currentWorkingDir, path);
        if(!dir.exists()) {
            dir.mkdir();
        }
        try {
            File saveFile = new File(dir, file.getOriginalFilename());
            file.transferTo(saveFile);
            return saveFile.getName();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
