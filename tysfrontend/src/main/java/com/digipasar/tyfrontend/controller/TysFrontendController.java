/**
 * 
 */
package com.digipasar.tyfrontend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.digipasar.tyfrontend.service.SpaceService;
import com.digipasar.tyfrontend.util.AExcelParser;
import com.digipasar.tyfrontend.util.DBConnector;

/**
 * @author Arun
 *
 */
@Controller
public class TysFrontendController {
	
	@Autowired
	private SpaceService spaceService;
	
	private static String UPLOADED_FOLDER = "D:\\temp\\";
	
	@GetMapping("/greeting")
    public String greeting() {
        return "greeting";
    }
	
	@GetMapping("/index")
    public String index() {
		System.out.println(spaceService.getAllSpace());
        return "index";
    }
	
	@GetMapping("/loadData")
    public String loadData() {
        return "LoadData";
    }
	
	@GetMapping("/techyourhome")
    public String techyourHome() {
        return "techyourhome";
    }
	
	
	@PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   Model model) {

        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {
            byte[] bytes = file.getBytes();
            //Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Path path = Paths.get(file.getOriginalFilename());
            System.out.println(Files.write(path, bytes));
            
            
            //List<Map<String,String>> dataMap = AExcelParser.getExcelData(getClass().getResourceAsStream("TYS Data Template.xlsx"),"Space"); 
            List<Map<String,String>> dataMap = AExcelParser.getExcelData(file.getInputStream(),"Space");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("Space", dataMap, Arrays.asList("SpaceID"));
    		}else {
    			System.out.println("No records to load for Space");
    		}
    		
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"Category");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size()>0) {
    			DBConnector.writeToTable("Category", dataMap,Arrays.asList("CategoryID"));
    		}else {
    			System.out.println("No records to load for Category");
    		}
    		
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"Supplier");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size()>0) {
    			DBConnector.writeToTable("Supplier", dataMap, Arrays.asList("SupplierID"));
    		}else {
    			System.out.println("No records to load for Supplier");
    		}
    		
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"Retailer");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("Retailer", dataMap, Arrays.asList("RetailerID"));
    		}else {
    			System.out.println("No records to load for Retailer");
    		}
    		
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"Contact");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size()>0) {
    			DBConnector.writeToTable("Contact", dataMap, Arrays.asList("ContactID"));
    		}else {
    			System.out.println("No records to load for Contact");
    		}
    		
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"GenericProduct");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("GenericProduct", dataMap, Arrays.asList("GenericProductID"));
    		}else {
    			System.out.println("No records to load for GenericProduct");
    		}
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"Brand");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("Brand", dataMap,Arrays.asList("BrandID"));
    		}else {
    			System.out.println("No records to load for Brand");
    		}
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"Product");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("Product", dataMap, Arrays.asList("ProductID"));
    		}else {
    			System.out.println("No records to load for Product");
    		}
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"ProductSpec");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("ProductSpec", dataMap,Arrays.asList("ProductID","CustomSpecName"));
    		}else {
    			System.out.println("No records to load for ProductSpec");
    		}
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"ProductReview");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size()>0) {
    			DBConnector.writeToTable("ProductReview", dataMap, Arrays.asList("ProductID","CustomReviewAttribute"));
    		}else {
    			System.out.println("No records to load for ProductReview");
    		}
    		
    		dataMap = AExcelParser.getExcelData(file.getInputStream(),"ProductRetailer");
    		System.out.println("Excel Value:"+dataMap);
    		if(dataMap.size() > 0) {
    			DBConnector.writeToTable("ProductRetailer", dataMap, Arrays.asList("ProductID","RetailerID"));
    		}else {
    			System.out.println("No records to load for ProductRetailer");
    		}
    		    		
            model.addAttribute("message",
                    "Succesfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message",
                    "Failed to uploaded '" + file.getOriginalFilename() + "'");
        }

        return "uploadStatus";
    }
}
