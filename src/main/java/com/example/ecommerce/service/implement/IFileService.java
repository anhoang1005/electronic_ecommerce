package com.example.ecommerce.service.implement;

import com.cloudinary.Cloudinary;
import com.example.ecommerce.exceptions.external.FileFailedUploadException;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class IFileService implements FileService {

    private final Cloudinary cloudinary;
    public IFileService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }


    //upload file len cloud
    @Override
    public ResponseBody uploadToCloudinary(MultipartFile file)  {
        try{
            //Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            var data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            //System.out.println(data.get("url"));
            return new ResponseBody(data.get("url"), ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (IOException io){
            log.info(io.getMessage());
            throw new FileFailedUploadException("Failed upload file");
        }
    }
}
