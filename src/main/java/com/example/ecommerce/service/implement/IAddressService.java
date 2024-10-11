package com.example.ecommerce.service.implement;

import com.example.ecommerce.entities.Address;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.exceptions.request.RequestNotFoundException;
import com.example.ecommerce.mapper.address.AddressMapper;
import com.example.ecommerce.models.address.AddressDto;
import com.example.ecommerce.models.address.AddressRequest;
import com.example.ecommerce.payload.ResponseBody;
import com.example.ecommerce.repository.AddressRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.service.AddressService;
import com.example.ecommerce.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class IAddressService implements AddressService {
    private final AuthenticationUtils authenUtils;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UsersRepository usersRepository;

    @Override
    public ResponseBody customerGetAllAddress() {
        try{
            String email = authenUtils.getEmailFromAuthentication();
            List<Address> listAddress = addressRepository.getListAddressByUserEmail(email);
            List<AddressDto> listDtos = new ArrayList<>();
            if(listAddress!=null){
                listDtos = listAddress.stream()
                        .map(addressMapper::entityToAddressDto)
                        .collect(Collectors.toList());
            }
            return new ResponseBody(listDtos, ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Get address òf users failed! Error: {}", e.getMessage());
            throw new RequestNotFoundException("Get address òf users failed!");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerCreateAddress(AddressRequest req) {
        try{
            String email = authenUtils.getEmailFromAuthentication();
            Users users = usersRepository.findUsersEntitiesByEmail(email);
            Address address = new Address();
            if(req.getAddressType() == 2){
                address.setAddressType(Address.AddressType.VAN_PHONG);
            } else{
                address.setAddressType(Address.AddressType.NHA_RIENG);
            }
            address.setFullName(req.getFullName());
            address.setPhoneNumber(req.getPhoneNumber());
            address.setHouseName(req.getHouseName());
            address.setWardName(req.getWardName());
            address.setDistrictName(req.getDistrictName());
            address.setProvinceName(req.getProvinceName());
            address.setWardId(req.getWardId());
            address.setDistrictId(req.getDistrictId());
            address.setProvinceId(req.getProvinceId());
            address.setNote(req.getNote());
            address.setIsDefault(req.getIsDefault());
            address.setUsersEntityInAddress(users);
            if(req.getIsDefault()){
                changeDefaultAddress(users);
            }
            address = addressRepository.save(address);
            return new ResponseBody(addressMapper.entityToAddressDto(address),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Create address òf users failed! Error: {}", e.getMessage());
            throw new RequestNotFoundException("Create address òf users failed!");
        }
    }

    @Override
    @Transactional
    public ResponseBody customerUpdateAddress(AddressRequest req) {
        try{
            String email = authenUtils.getEmailFromAuthentication();
            Users users = usersRepository.findUsersEntitiesByEmail(email);
            Address address = addressRepository.findAddressByIdAndUserEmail(req.getAddressId(), email);
            if(req.getAddressType() == 2){
                address.setAddressType(Address.AddressType.VAN_PHONG);
            } else{
                address.setAddressType(Address.AddressType.NHA_RIENG);
            }
            address.setFullName(req.getFullName());
            address.setPhoneNumber(req.getPhoneNumber());
            address.setHouseName(req.getHouseName());
            address.setWardName(req.getWardName());
            address.setDistrictName(req.getDistrictName());
            address.setProvinceName(req.getProvinceName());
            address.setWardId(req.getWardId());
            address.setDistrictId(req.getDistrictId());
            address.setProvinceId(req.getProvinceId());
            address.setNote(req.getNote());
            address.setIsDefault(req.getIsDefault());
            address.setUsersEntityInAddress(users);
            if(req.getIsDefault()){
                changeDefaultAddress(users);
            }
            address = addressRepository.save(address);
            return new ResponseBody(addressMapper.entityToAddressDto(address),
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Update address òf users failed! Error: {}", e.getMessage());
            throw new RequestNotFoundException("Update address òf users failed!");
        }
    }

    @Override
    public ResponseBody customerDeleteAddress(Long addressId) {
        try{
            String email = authenUtils.getEmailFromAuthentication();
            Address address = addressRepository.findAddressByIdAndUserEmail(addressId, email);
            addressRepository.delete(address);
            return new ResponseBody("",
                    ResponseBody.Status.SUCCESS, ResponseBody.Code.SUCCESS);
        } catch (Exception e){
            log.error("Delete address òf users failed! Error: {}", e.getMessage());
            throw new RequestNotFoundException("Delete address of users failed!");
        }
    }

    public void changeDefaultAddress(Users users){
        for(Address address : users.getListAddressEntity()){
            address.setIsDefault(false);
            addressRepository.save(address);
        }
    }
}
