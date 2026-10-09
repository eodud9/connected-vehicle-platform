package connected_vehicle.domain.ownership;

import connected_vehicle.domain.ownership.dto.OwnershipCreateRequest;
import connected_vehicle.domain.ownership.dto.OwnershipCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ownerships")
@RequiredArgsConstructor
public class OwnershipController {

    private final OwnershipService ownershipService;

    @PostMapping
    public ResponseEntity<OwnershipCreateResponse> createOwnership(@Valid @RequestBody OwnershipCreateRequest request){
        OwnershipCreateResponse response = ownershipService.createOwnership(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
