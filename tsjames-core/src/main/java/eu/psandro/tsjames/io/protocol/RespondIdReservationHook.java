package eu.psandro.tsjames.io.protocol;

@FunctionalInterface
public interface RespondIdReservationHook {

    int reservate(ResponseCall responseCall);

}
