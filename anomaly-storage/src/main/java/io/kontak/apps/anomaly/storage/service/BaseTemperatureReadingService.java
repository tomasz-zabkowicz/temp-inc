package io.kontak.apps.anomaly.storage.service;

import io.kontak.apps.anomaly.storage.model.Room;
import io.kontak.apps.anomaly.storage.model.Thermometer;
import io.kontak.apps.anomaly.storage.repository.RoomRepository;
import io.kontak.apps.anomaly.storage.repository.ThermometerRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BaseTemperatureReadingService {

    protected final RoomRepository roomRepository;

    protected final ThermometerRepository thermometerRepository;

    public BaseTemperatureReadingService(RoomRepository roomRepository, ThermometerRepository thermometerRepository)
    {
        this.roomRepository = roomRepository;
        this.thermometerRepository = thermometerRepository;
    }

    protected Room findOrCreateRoom(UUID roomId)
    {
        Room room = roomRepository.findById(roomId).orElse(null);
        if (null == room) {
            room = roomRepository.save(new Room(roomId, null));
        }
        return room;
    }

    protected Thermometer findOrCreateThermometer(UUID thermometerId, Room room)
    {
        Thermometer thermometer = thermometerRepository.findById(thermometerId).orElse(null);
        if (null == thermometer) {
            thermometer = thermometerRepository.save(new Thermometer(thermometerId, null, room));
        }
        return thermometer;
    }
}
