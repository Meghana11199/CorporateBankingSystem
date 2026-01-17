package org.example.corporatebankingsystem.ServiceTest;

import org.example.corporatebankingsystem.Dto.CreditRequestCreateDto;
import org.example.corporatebankingsystem.Dto.CreditRequestUpdateDto;
import org.example.corporatebankingsystem.Model.CreditRequest;
import org.example.corporatebankingsystem.Model.CreditStatus;
import org.example.corporatebankingsystem.Repo.CreditRequestRepository;
import org.example.corporatebankingsystem.Service.CreditRequestService;
import org.example.corporatebankingsystem.kafka.CreditRequestEventProducer;
import org.example.corporatebankingsystem.kafka.RequestReviewEventProducer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRequestServiceTest {

    @Mock
    private CreditRequestRepository repo;

    @Mock
    private CreditRequestEventProducer eventProducer;

    @Mock
    private RequestReviewEventProducer reviewEventProducer;

    @InjectMocks
    private CreditRequestService service;

    // ---------- SECURITY CONTEXT SETUP ----------

    @BeforeEach
    void setupSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        lenient().when(authentication.getPrincipal()).thenReturn("rm1");

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ---------- CREATE ----------

    @Test
    void create_shouldSaveAndReturnDto() {
        CreditRequestCreateDto dto = new CreditRequestCreateDto();
        dto.setClientId("client1");
        dto.setRequestedAmount(100000.0);
        dto.setTenureMonths(12);
        dto.setPurpose("Working Capital");

        CreditRequest saved = mockRequest(CreditStatus.PENDING);

        when(repo.save(any())).thenReturn(saved);
        doNothing().when(eventProducer).sendEvent(any());

        var result = service.create(dto, "rm1");

        assertThat(result.getId()).isEqualTo("id1");
        assertThat(result.getStatus()).isEqualTo(CreditStatus.PENDING);

        verify(eventProducer).sendEvent(any());
    }

    // ---------- GET FOR RM ----------

    @Test
    void getForRm_shouldReturnMappedList() {
        when(repo.findByRmId("rm1"))
                .thenReturn(List.of(mockRequest(CreditStatus.PENDING)));

        var result = service.getForRm("rm1");

        assertThat(result).hasSize(1);
    }

    // ---------- GET PENDING ----------

    @Test
    void getPendingRequests_shouldReturnPendingOnly() {
        when(repo.findByStatus(CreditStatus.PENDING))
                .thenReturn(List.of(mockRequest(CreditStatus.PENDING)));

        var result = service.getPendingRequests();

        assertThat(result).hasSize(1);
    }

    // ---------- ANALYST REVIEW ----------

    @Test
    void analystReview_shouldUpdateComment_whenPending() {
        CreditRequest cr = mockRequest(CreditStatus.PENDING);

        when(repo.findById("id1")).thenReturn(Optional.of(cr));
        when(repo.save(any())).thenReturn(cr);
        doNothing().when(reviewEventProducer).send(any());

        var result = service.analystReview("id1", "Looks good");

        assertThat(result.getAnalystComment()).isEqualTo("Looks good");
        verify(reviewEventProducer).send(any());
    }

    @Test
    void analystReview_shouldThrow_whenNotPending() {
        CreditRequest cr = mockRequest(CreditStatus.APPROVED);
        when(repo.findById("id1")).thenReturn(Optional.of(cr));

        assertThatThrownBy(() ->
                service.analystReview("id1", "test"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void analystReview_shouldThrow_whenNotFound() {
        when(repo.findById("id1")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.analystReview("id1", "test"))
                .isInstanceOf(RuntimeException.class);
    }

    // ---------- ANALYST UPDATE ----------

    @Test
    void analystUpdate_shouldUpdateStatusAndComment() {
        CreditRequest cr = mockRequest(CreditStatus.PENDING);

        CreditRequestUpdateDto dto = new CreditRequestUpdateDto();
        dto.setStatus(CreditStatus.APPROVED);
        dto.setAnalystComment("Approved");

        when(repo.findById("id1")).thenReturn(Optional.of(cr));
        when(repo.save(any())).thenReturn(cr);
        doNothing().when(reviewEventProducer).send(any());

        var result = service.analystUpdate("id1", dto);

        assertThat(result.getStatus()).isEqualTo(CreditStatus.APPROVED);
    }

    // ---------- UPDATE STATUS ----------

    @Test
    void updateStatus_shouldWork_whenPending() {
        CreditRequest cr = mockRequest(CreditStatus.PENDING);

        CreditRequestUpdateDto dto = new CreditRequestUpdateDto();
        dto.setStatus(CreditStatus.APPROVED);
        dto.setAnalystComment("Approved");

        when(repo.findById("id1")).thenReturn(Optional.of(cr));
        when(repo.save(any())).thenReturn(cr);
        doNothing().when(reviewEventProducer).send(any());

        var result = service.updateStatus("id1", dto);

        assertThat(result.getStatus()).isEqualTo(CreditStatus.APPROVED);
    }

    // ---------- GET BY ID ----------

    @Test
    void getById_shouldReturnRequest() {
        when(repo.findById("id1"))
                .thenReturn(Optional.of(mockRequest(CreditStatus.PENDING)));

        var result = service.getById("id1");

        assertThat(result).isNotNull();
    }

    // ---------- GET ALL ----------

    @Test
    void getAllRequests_shouldReturnList() {
        when(repo.findAll())
                .thenReturn(List.of(mockRequest(CreditStatus.PENDING)));

        var result = service.getAllRequests();

        assertThat(result).hasSize(1);
    }

    // ---------- HELPER ----------

    private CreditRequest mockRequest(CreditStatus status) {
        return CreditRequest.builder()
                .id("id1")
                .clientId("client1")
                .rmId("rm1")
                .requestedAmount(1000.0)
                .tenureMonths(12)
                .purpose("Test")
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
