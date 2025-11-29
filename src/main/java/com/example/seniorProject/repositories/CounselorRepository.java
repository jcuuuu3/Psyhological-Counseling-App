package com.example.seniorProject.repositories;

import com.example.seniorProject.models.Counselor;
import com.example.seniorProject.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CounselorRepository extends JpaRepository<Counselor, Integer> {

    @Query(value = "SELECT s"
            + " from Session s"
            + " where s.startTime > ?3 "
            + " and s.startTime < ?4 "
            + " and  (s.counselor.id = ?1 or s.roomNumber = ?2)")
    List<Session> clashingSessions(int counselorId, String roomId, LocalDateTime lower, LocalDateTime upper);

    Optional<Counselor> findByUsername(String e);
}
