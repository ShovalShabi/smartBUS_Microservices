package club.smartbus.controller;

import club.smartbus.dto.FeedbackDTO;
import club.smartbus.service.FeedbackService;
import club.smartbus.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class FeedbackController {

    @Autowired
    private final FeedbackService feedbackService;

    @PostMapping(path = "/{company}", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Mono<FeedbackDTO> createFeedback(
            @PathVariable String company,
            @RequestBody FeedbackDTO feedbackDTO) {

        return feedbackService.createFeedbackToCompany(feedbackDTO, company);
    }

    @GetMapping(
            path = {"/{company}"},
            produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public Flux<FeedbackDTO> getCompanyFeedbacks(
            @PathVariable String company,
            @RequestParam(name = "size", required = false, defaultValue = Constants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "page", required = false, defaultValue = Constants.DEFAULT_PAGE) int page,
            @RequestParam(name = "minRating", required = false, defaultValue = "0") Double minRating,
            @RequestParam(name = "fromDate", required = false) //should receive a data as yyyy-MM-ddTHH:MM:SS
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime fromDate,
            @RequestParam(name = "tillDate", required = false) //should receive a data as yyyy-MM-ddTHH:MM:SS
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime tillDate) {

        return feedbackService.getCompanyFeedbacksFromRatingOnwardsByDates(company,
                minRating,
                fromDate,
                tillDate,
                size,
                page);
    }
}
