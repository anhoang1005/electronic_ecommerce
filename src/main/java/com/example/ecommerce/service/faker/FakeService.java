package com.example.ecommerce.service.faker;

import com.example.ecommerce.entities.Roles;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.repository.RolesRepository;
import com.example.ecommerce.repository.UsersRepository;
import com.example.ecommerce.utils.RandomUtils;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FakeService {

    private final UsersRepository usersRepository;
    private final Faker faker;
    private final PasswordEncoder passwordEncoder;
    private final RandomUtils randomUtils;
    private final RolesRepository rolesRepository;

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



}
