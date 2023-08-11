package io.kontak.apps.anomaly.storage.repository;

import io.kontak.apps.anomaly.storage.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {

}
