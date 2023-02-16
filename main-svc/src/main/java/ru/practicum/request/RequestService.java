package ru.practicum.request;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.Request;

import java.util.Collection;

public interface RequestService {
    Collection<Request> getRequestByUser(Long userId);

    Request getRequestByIdAndUser(Long requestId, Long userId);

    Request postRequestByUser(Long userId, Long eventId);

    Request cancelRequestByUser(Long userId, Long requestId);

    Collection<Request> getRequestEventByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult patchRequestEventByUser(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

}
