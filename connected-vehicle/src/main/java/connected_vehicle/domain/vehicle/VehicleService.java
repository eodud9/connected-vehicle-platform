package connected_vehicle.domain.vehicle;

import connected_vehicle.domain.ownership.OwnershipRepository;
import connected_vehicle.domain.ownership.OwnershipService;
import connected_vehicle.domain.user.UserRole;
import connected_vehicle.domain.vehicle.dto.VehicleCreateRequest;
import connected_vehicle.domain.vehicle.dto.VehicleCreateResponse;
import connected_vehicle.domain.vehicle.dto.VehicleResponse;
import connected_vehicle.exception.DuplicateLicensePlateException;
import connected_vehicle.exception.DuplicateVinException;
import connected_vehicle.exception.VehicleAuthenticationException;
import connected_vehicle.exception.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final OwnershipRepository ownershipRepository;
    private final OwnershipService ownershipService;

    private final SecureRandom secureRandom = new SecureRandom();

    private String generateApiKey(){
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String hashApiKey(String apiKey)  {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] input = apiKey.getBytes(StandardCharsets.UTF_8);
            byte[] hashedBytes = digest.digest(input);

            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e){
            throw new IllegalStateException("SHA-256 알고리즘을 사용할 수 없습니다.", e);
        }

    }

    public VehicleCreateResponse createVehicle(VehicleCreateRequest request) {
        if (vehicleRepository.existsByVin(request.vin())){
            throw new DuplicateVinException("이미 등록된 VIN이 존재합니다.");
        }
        if (vehicleRepository.existsByLicensePlate(request.licensePlate())){
            throw new DuplicateLicensePlateException("이미 등록된 LICENSE PLATE가 존재합니다.");
        }

        String apiKey = generateApiKey();

        String apiKeyHash = hashApiKey(apiKey);

        Vehicle vehicle = new Vehicle(request.vin(), request.licensePlate(), request.modelName(), request.modelYear(), apiKeyHash);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);

        return new VehicleCreateResponse(savedVehicle.getId(),
                savedVehicle.getVin(),
                savedVehicle.getLicensePlate(),
                savedVehicle.getModelName(),
                savedVehicle.getModelYear(),
                savedVehicle.getRegisteredAt(),
                apiKey
                );
    }

    public List<VehicleResponse> getVehicles(Long userId, UserRole role){

        if(role == UserRole.ADMIN){
            List<VehicleResponse> responses = vehicleRepository.findAll().stream().map(vehicle -> new VehicleResponse(
                    vehicle.getId(),
                    vehicle.getVin(),
                    vehicle.getLicensePlate(),
                    vehicle.getModelName(),
                    vehicle.getModelYear(),
                    vehicle.getRegisteredAt())).toList();
            return responses;
        }

        List<Vehicle> vehicles = ownershipRepository.findCurrentVehiclesByUserId(userId);

        List<VehicleResponse> responses = vehicles.stream().map(vehicle -> new VehicleResponse(
                vehicle.getId(),
                vehicle.getVin(),
                vehicle.getLicensePlate(),
                vehicle.getModelName(),
                vehicle.getModelYear(),
                vehicle.getRegisteredAt()
        )).toList();

        return responses;

    }

    public VehicleResponse getVehicle(Long userId, UserRole role, Long vehicleId){

        if(role != UserRole.ADMIN){
            ownershipService.validateOwnership(userId, vehicleId);
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId).orElseThrow(
                () -> new VehicleNotFoundException("조회하신 차량이 존재하지 않습니다.")
        );

        return new VehicleResponse(vehicle.getId(),
                vehicle.getVin(),
                vehicle.getLicensePlate(),
                vehicle.getModelName(),
                vehicle.getModelYear(),
                vehicle.getRegisteredAt());

    }

    public Vehicle authenticateVehicle(Long vehicleId, String apiKey) {

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException("해당 차량을 찾을 수 없습니다."));

        boolean matches = MessageDigest.isEqual(
                apiKey.getBytes(),
                vehicle.getApiKeyHash().getBytes()
        );

        if(!matches){
            throw new VehicleAuthenticationException("차량 인증에 실패했습니다.");
        }

        return vehicle;

    }
}
