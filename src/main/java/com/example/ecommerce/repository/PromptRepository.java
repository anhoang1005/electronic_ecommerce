package com.example.ecommerce.repository;

import com.example.ecommerce.entities.Prompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromptRepository extends JpaRepository<Prompt, Long> {
    Optional<Prompt> findPromptByPromptName(String name);
}
