package org.example.corporatebankingsystem.Service;

import org.example.corporatebankingsystem.Dto.CreditRequestCreateDto;
import org.example.corporatebankingsystem.Dto.CreditRequestResponseDto;
import org.example.corporatebankingsystem.Dto.CreditRequestUpdateDto;
import org.example.corporatebankingsystem.Model.CreditRequest;
import org.example.corporatebankingsystem.Model.CreditStatus;
import org.example.corporatebankingsystem.Repo.CreditRequestRepository;
import org.example.corporatebankingsystem.event.CreditRequestEvent;
import org.example.corporatebankingsystem.event.RequestReviewEvent;
import org.example.corporatebankingsystem.kafka.CreditRequestEventProducer;
import org.example.corporatebankingsystem.kafka.RequestReviewEventProducer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CreditRequestService {

    private final CreditRequestRepository repo;
    private final CreditRequestEventProducer eventProducer;
    private final RequestReviewEventProducer reviewEventProducer;

    public CreditRequestService(CreditRequestRepository repo, CreditRequestEventProducer eventProducer, RequestReviewEventProducer reviewEventProducer) {
        this.repo = repo;
        this.eventProducer = eventProducer;
        this.reviewEventProducer = reviewEventProducer;
    }


    public CreditRequestResponseDto create(CreditRequestCreateDto dto, String rmId) {

        // 🔥 SAFELY FETCH RM ID FROM SECURITY CONTEXT
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String resolvedRmId;
        Object principal = auth.getPrincipal();

        if (principal instanceof String) {
            resolvedRmId = (String) principal;
        } else if (principal instanceof UserDetails userDetails) {
            resolvedRmId = userDetails.getUsername();
        } else {
            throw new RuntimeException("Unable to resolve RM identity");
        }

        CreditRequest request = CreditRequest.builder()
                .clientId(dto.getClientId())
                .rmId(resolvedRmId) // ✅ USE RESOLVED RM ID
                .requestedAmount(dto.getRequestedAmount())
                .tenureMonths(dto.getTenureMonths())
                .purpose(dto.getPurpose())
                .status(CreditStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        CreditRequest saved = repo.save(request);

        // 🔥 KAFKA EVENT
        eventProducer.sendEvent(
                new CreditRequestEvent(
                        "CREDIT_REQUEST_CREATED",
                        saved.getId(),
                        saved.getClientId(),
                        saved.getRmId(),
                        saved.getStatus().name(),
                        saved.getRequestedAmount()
                )
        );

        return mapToDto(saved);
    }
    public List<CreditRequestResponseDto> getForRm(String rmId) {
        return repo.findByRmId(rmId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private CreditRequestResponseDto mapToDto(CreditRequest cr) {
        return CreditRequestResponseDto.builder()
                .id(cr.getId())
                .clientId(cr.getClientId())
                .rmId(cr.getRmId())
                .requestedAmount(cr.getRequestedAmount())
                .tenureMonths(cr.getTenureMonths())
                .purpose(cr.getPurpose())
                .status(cr.getStatus())
                .analystComment(cr.getAnalystComment())
                .createdAt(cr.getCreatedAt())
                .build();
    }
    public List<CreditRequestResponseDto> getPendingRequests() {
        return repo.findByStatus(CreditStatus.PENDING)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private String resolveAnalystId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        if (principal instanceof String) return (String) principal;
        if (principal instanceof UserDetails userDetails) return userDetails.getUsername();

        return "UNKNOWN_ANALYST";
    }
    public CreditRequestResponseDto analystReview(
            String requestId,
            String analystComment) {

        CreditRequest cr = repo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        if (cr.getStatus() != CreditStatus.PENDING) {
            throw new RuntimeException("Only PENDING requests can be reviewed");
        }

        cr.setAnalystComment(analystComment);
        CreditRequest saved = repo.save(cr);

        // 🔥 Kafka Event
        reviewEventProducer.send(
                new RequestReviewEvent(
                        "REQUEST_REVIEWED",
                        saved.getId(),
                        resolveAnalystId(),
                        saved.getStatus().name(),
                        analystComment
                )
        );

        return mapToDto(saved);
    }

    public CreditRequestResponseDto analystUpdate(
            String requestId,
            CreditRequestUpdateDto dto) {

        CreditRequest cr = repo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        if (cr.getStatus() != CreditStatus.PENDING) {
            throw new RuntimeException("Only PENDING requests can be updated");
        }

        cr.setStatus(dto.getStatus());
        cr.setAnalystComment(dto.getAnalystComment());

        CreditRequest saved = repo.save(cr);

        // 🔥 Kafka Event
        reviewEventProducer.send(
                new RequestReviewEvent(
                        dto.getStatus() == CreditStatus.APPROVED
                                ? "REQUEST_APPROVED"
                                : "REQUEST_REJECTED",
                        saved.getId(),
                        resolveAnalystId(),
                        saved.getStatus().name(),
                        saved.getAnalystComment()
                )
        );

        return mapToDto(saved);
    }
    public CreditRequestResponseDto updateStatus(
            String requestId,
            CreditRequestUpdateDto dto) {

        CreditRequest cr = repo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        if (cr.getStatus() != CreditStatus.PENDING) {
            throw new RuntimeException("Only PENDING requests can be updated");
        }

        cr.setStatus(dto.getStatus());
        cr.setAnalystComment(dto.getAnalystComment());

        CreditRequest saved = repo.save(cr);

        // 🔥 ADD THIS
        reviewEventProducer.send(
                new RequestReviewEvent(
                        dto.getStatus() == CreditStatus.APPROVED
                                ? "REQUEST_APPROVED"
                                : "REQUEST_REJECTED",
                        saved.getId(),
                        resolveAnalystId(),
                        saved.getStatus().name(),
                        saved.getAnalystComment()
                )
        );

        return mapToDto(saved);
    }
    public CreditRequestResponseDto getById(String id) {
        CreditRequest request = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Credit request not found"));

        return mapToDto(request);
    }
    public List<CreditRequestResponseDto> getAllRequests() {
        return repo.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }
}