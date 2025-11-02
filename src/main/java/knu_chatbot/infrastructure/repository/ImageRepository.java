package knu_chatbot.infrastructure.repository;

import knu_chatbot.infrastructure.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
