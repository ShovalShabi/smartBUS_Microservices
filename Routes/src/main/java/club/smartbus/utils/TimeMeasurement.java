package club.smartbus.utils;

import club.smartbus.dto.transit.TransitDetails;

import java.util.List;

public class TimeMeasurement {
    public static String getInitialDepartureTime(List<TransitDetails> detailsList) {
        return detailsList.get(0).getLocalizedValues().getDepartureTime().getTime().getText();
    }

    public static String getFinalArrivalTime(List<TransitDetails> detailsList) {
        return detailsList.get(detailsList.size() - 1).getLocalizedValues().getArrivalTime().getTime().getText();
    }
}
