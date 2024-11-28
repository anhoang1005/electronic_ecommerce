package com.example.ecommerce.config.security.aop;

import com.example.ecommerce.config.security.anotation.UserHasResource;
import com.example.ecommerce.entities.Users;
import com.example.ecommerce.entities.enums.ErrorMessage;
import com.example.ecommerce.exceptions.BaseException;
import com.example.ecommerce.service.UsersService;
import com.example.ecommerce.utils.JwtTokenUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AspectUserAccess {
    @PersistenceContext
    private final EntityManager entityManager;
    private final JwtTokenUtils jwtTokenUtils;
    private static final String NOT_FOUND = "NOT_FOUND";
    private final UsersService usersService;

    @Before("@annotation(userHasResource)")
    public void hasUserHasResource(JoinPoint joinPoint, UserHasResource userHasResource){
        String token = jwtTokenUtils.getToken();
        Long userId = jwtTokenUtils.extractUserId(token);

//        if(isAdmin(userId)) return;

        Object[] args = joinPoint.getArgs();
        for(Object arg : args){
            if(arg instanceof Long id){
                Optional<?> entityOptional = findEntityById(userHasResource.entity(),id);

                if(entityOptional.isPresent()){
                    Object entity = entityOptional.get();
                    Long entityUserId = extractUserId(entity);
                    if(entityUserId == null || !entityUserId.equals(userId)){
                        throw new BaseException(ErrorMessage.PERMISSION_DENIED.name());
                    }
                }
            }
        }

    }

//    private boolean isAdmin(Long userId){
//        Users user = userService.getUserById(userId);
//        return user.isAdmin();
//    }

    private Optional<?> findEntityById(Class<?> entityClass, Long id){
        Object entity = entityManager.find(entityClass,id);
        return Optional.ofNullable(entity);
    }

    private Long extractUserId(Object entity){
        try{
            if(entity instanceof  Users){
                return ((Users) entity).getId();
            }
            Users user = (Users) entity.getClass().getMethod("getUsers").invoke(entity);
            return user.getId();
        }catch (Exception e){
            return null;
        }
    }

}
