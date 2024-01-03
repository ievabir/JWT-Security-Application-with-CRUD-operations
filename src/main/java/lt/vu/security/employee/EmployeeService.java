package lt.vu.security.employee;

import jakarta.persistence.EntityNotFoundException;
import lt.vu.security.user.User;
import lt.vu.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }
    private final UserRepository userRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, UserRepository userRepository) {
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
    }

    public Employee saveEmployee(Employee newEmployee) {
        String authenticatedUserEmail = getAuthenticatedUserEmail();

        if (authenticatedUserEmail == null) {
            throw new AccessDeniedException("You are not authenticated.");
        }

        User authenticatedUser = userRepository.findByEmail(authenticatedUserEmail).orElse(null);

        if (authenticatedUser == null) {
            throw new AccessDeniedException("Authenticated user not found.");
        }

        newEmployee.setReportsTo(authenticatedUser);

        return employeeRepository.save(newEmployee);
    }

    public List<Employee> findByReportsToEmail(String authenticatedUserEmail) {
        return employeeRepository.findByReportsToEmail(authenticatedUserEmail);
    }

    public Employee updateEmployee(Integer employeeId, Employee updatedEmployee, String authenticatedUserEmail) {
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(employeeId);

        if (existingEmployeeOptional.isPresent()) {
            Employee existingEmployee = existingEmployeeOptional.get();

            if (existingEmployee.getReportsTo().getEmail().equals(authenticatedUserEmail)) {
                existingEmployee.setTitle(updatedEmployee.getTitle());
                existingEmployee.setPosition(updatedEmployee.getPosition());
                return employeeRepository.save(existingEmployee);
            } else {
                throw new AccessDeniedException("You don't have permission to update this employee");
            }
        } else {
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
    }

    public String deleteEmployee(Integer employeeId, String authenticatedUserEmail) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);

        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();

            if (employee.getReportsTo() != null && employee.getReportsTo().getEmail().equals(authenticatedUserEmail)) {
                employeeRepository.deleteById(employeeId);
                return "Employee with ID " + employeeId + " removed.";
            } else {
                return "You are not authorized to delete this employee.";
            }
        } else {
            return "No such employee found. No employees were removed.";
        }
    }

}
