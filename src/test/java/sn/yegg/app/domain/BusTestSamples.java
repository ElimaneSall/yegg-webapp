package sn.yegg.app.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Bus getBusSample1() {
        return new Bus()
            .id(1L)
            .numeroVehicule("numeroVehicule1")
            .plaque("plaque1")
            .modele("modele1")
            .constructeur("constructeur1")
            .capacite(1)
            .capaciteDebout(1)
            .anneeFabrication(1)
            .autonomieKm(1)
            .gpsDeviceId("gpsDeviceId1")
            .gpsStatus("gpsStatus1")
            .gpsBatteryLevel(1)
            .currentCap(1)
            .prochainEntretienKm(1);
    }

    public static Bus getBusSample2() {
        return new Bus()
            .id(2L)
            .numeroVehicule("numeroVehicule2")
            .plaque("plaque2")
            .modele("modele2")
            .constructeur("constructeur2")
            .capacite(2)
            .capaciteDebout(2)
            .anneeFabrication(2)
            .autonomieKm(2)
            .gpsDeviceId("gpsDeviceId2")
            .gpsStatus("gpsStatus2")
            .gpsBatteryLevel(2)
            .currentCap(2)
            .prochainEntretienKm(2);
    }

    public static Bus getBusRandomSampleGenerator() {
        return new Bus()
            .id(longCount.incrementAndGet())
            .numeroVehicule(UUID.randomUUID().toString())
            .plaque(UUID.randomUUID().toString())
            .modele(UUID.randomUUID().toString())
            .constructeur(UUID.randomUUID().toString())
            .capacite(intCount.incrementAndGet())
            .capaciteDebout(intCount.incrementAndGet())
            .anneeFabrication(intCount.incrementAndGet())
            .autonomieKm(intCount.incrementAndGet())
            .gpsDeviceId(UUID.randomUUID().toString())
            .gpsStatus(UUID.randomUUID().toString())
            .gpsBatteryLevel(intCount.incrementAndGet())
            .currentCap(intCount.incrementAndGet())
            .prochainEntretienKm(intCount.incrementAndGet());
    }
}
