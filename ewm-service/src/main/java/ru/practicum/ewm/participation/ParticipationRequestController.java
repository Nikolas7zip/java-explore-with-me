package ru.practicum.ewm.participation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.participation.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ParticipationRequestController {
    private final ParticipationRequestService participationService;

    @Autowired
    public ParticipationRequestController(ParticipationRequestService participationService) {
        this.participationService = participationService;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllParticipationRequestsByRequester(@PathVariable Long userId) {
        return participationService.getAllByRequester(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createParticipationRequest(@PathVariable Long userId,
                                                              @RequestParam(name = "eventId") Long eventId) {
        return participationService.create(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequestByRequester(@PathVariable Long userId,
                                                                         @PathVariable Long requestId) {
        return participationService.cancelByRequester(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllParticipationRequestsByEvent(@PathVariable Long userId,
                                                                            @PathVariable Long eventId) {
        return participationService.getAllByEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmParticipationRequestByInitiator(@PathVariable Long userId,
                                                                          @PathVariable Long eventId,
                                                                          @PathVariable Long reqId) {
        return participationService.confirmByInitiator(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectParticipationRequestByInitiator(@PathVariable Long userId,
                                                                         @PathVariable Long eventId,
                                                                         @PathVariable Long reqId) {
        return participationService.rejectByInitiator(userId, eventId, reqId);
    }
}
