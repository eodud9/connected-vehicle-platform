package connected_vehicle.domain.ownership;

import connected_vehicle.domain.ownership.dto.OwnershipCreateRequest;
import connected_vehicle.domain.ownership.dto.OwnershipCreateResponse;
import connected_vehicle.domain.user.User;
import connected_vehicle.domain.user.UserRepository;
import connected_vehicle.domain.vehicle.Vehicle;
import connected_vehicle.domain.vehicle.VehicleRepository;
import connected_vehicle.exception.ActiveOwnershipAlreadyExistsException;
import connected_vehicle.exception.UserNotFoundException;
import connected_vehicle.exception.VehicleAccessDeniedException;
import connected_vehicle.exception.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OwnershipService {

    private final OwnershipRepository ownershipRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public void validateOwnership(Long userId, Long vehicleId){


            boolean exists = ownershipRepository.existsByUser_IdAndVehicle_IdAndEndedAtIsNull(userId, vehicleId);

            if(!exists){
                throw new VehicleAccessDeniedException("해당 차량에 접근할 수 있는 권한이 없습니다.");
            }
        }

    public OwnershipCreateResponse createOwnership(OwnershipCreateRequest request){
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        Vehicle vehicle = vehicleRepository.findById(request.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException("해당 차량을 찾을 수 없습니다."));

        if(ownershipRepository.existsByVehicle_IdAndEndedAtIsNull(vehicle.getId())){
            throw new ActiveOwnershipAlreadyExistsException("해당 차량은 현재 소유자가 존재합니다.");
        };

        Ownership ownership = new Ownership(user, vehicle);

        Ownership savedOwnership = ownershipRepository.save(ownership);

        return new OwnershipCreateResponse(savedOwnership.getId(), savedOwnership.getUser().getId(), savedOwnership.getVehicle().getId());
    }
}
