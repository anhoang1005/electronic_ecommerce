package com.example.ecommerce.repository;

import com.example.ecommerce.entities.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeValueRepository extends JpaRepository<AttributeValue,Long> {
//
@Query(value = "SELECT * FROM attribute_value a WHERE a.attribute_id = :id AND a.value COLLATE utf8mb4_unicode_ci = :value", nativeQuery = true)
AttributeValue findAttributeValueByValueAndAttributeId(@Param("id") Long id,
                                                       @Param("value") String value);

}
