package com.example.ecommerce.service.faker;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Shop;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.ShopRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.utils.RandomUtils;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FakeService {

    private final UsersRepository usersRepository;
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;
    private final RandomUtils randomUtils;
    private final RolesRepository rolesRepository;
    private final ShopRepository shopRepository;


    //user - fake
    @Transactional
    public void generateAndInsertFakeUserData(){

        Set<String> uniqueEmails = new HashSet<>();
        for(int i = 0 ; i < 100 ; i++){
            Optional<Roles> roleOptional = rolesRepository.findByRoleName("KHACHHANG");
            List<Roles> roleEntityList = new ArrayList<>();
            roleOptional.ifPresent(roleEntityList::add);

            Users users = new Users();
            users.setFirstName(faker.name().firstName());
            users.setLastName(faker.name().lastName());
            users.setDob(generateFakeLocalDate());
            users.setGender(randomGender());
            users.setPhoneNumber(faker.phoneNumber().subscriberNumber(10));
            users.setPasswordHash(passwordEncoder.encode("1234"));
            users.setActive(true);
            users.setImageUrl("user.jpg");
            users.setVerifyCode(randomUtils.generateRandomString(9));
            users.setListRolesOfUsers(roleEntityList);
            users.setEnable(true);
            users.setUsername(faker.name().username());
            String email;
            do {
                email = faker.internet().emailAddress();
            } while (uniqueEmails.contains(email));

            uniqueEmails.add(email);
            users.setEmail(email);
            usersRepository.save(users);
        }
    }

    public LocalDate generateFakeLocalDate(){
        Date fakeDate = faker.date().birthday();
        return fakeDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public Users.Gender randomGender(){
        Users.Gender[] genders = Users.Gender.values();
        int randomIndex = new Random().nextInt(genders.length);
        return genders[randomIndex];
    }


    //shop - fake
    @Transactional
    public void generateAndInsertFakeShopData(){
        for(int i = 0 ;i<1;i++){
            Shop shop = new Shop();
            shop.setShopCode(randomUtils.generateRandomString(15));
            shop.setBusinessType(Shop.BusinessType.CA_NHAN);
            shop.setShopName(faker.name().name());
            shop.setShopLogo(faker.animal().name());
            shop.setAddress(faker.address().fullAddress());
            shop.setShopHotLine(faker.phoneNumber().phoneNumber());
            shop.setIdentityCode(randomUtils.generateRandomString(9));
            shop.setFullName(faker.name().fullName());
            shop.setShopEmail(faker.internet().emailAddress());
            shop.setWardId(randomUtils.generateRandomString(4));
            shop.setProvinceId(randomUtils.generateRandomString(4));
            shop.setProvinceId(randomUtils.generateRandomString(4));

            shop.setUsersInShop(usersRepository.findUsersById(Long.valueOf(1)));
            shopRepository.save(shop);
            log.info(shop.toString());
        }
    }

}
