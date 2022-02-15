package ml.caisff.backendstocksgr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ml.caisff.backendstocksgr.domain.Liberation;

@SuppressWarnings("unsed")
@Repository
public interface LiberationRepository extends JpaRepository<Liberation, Long>{}
