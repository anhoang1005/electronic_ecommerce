package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributeRepository extends JpaRepository<Attribute,Long> {
//    @Query("SELECT e FROM MyEntity e WHERE e.value COLLATE utf8mb4_unicode_ci = :value")
//    List<MyEntity> findByValueIgnoreAccent(@Param("value") String value);
//    @Query("SELECT att FROM Attribute att where att.attributeName COLLATE utf8mb4_unicode_ci = :name")

    @Query(value = "SELECT * from attributes  a where a.attribute_name COLLATE utf8mb4_unicode_ci = :name",nativeQuery = true)
    Attribute findAttributeByAttributeName(String name);
}
