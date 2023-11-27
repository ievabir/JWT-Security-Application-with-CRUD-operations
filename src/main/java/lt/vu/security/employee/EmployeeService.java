package lt.vu.security.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee findEmployeeById(Long employeeId){
        return employeeRepository.findById(employeeId).orElse(null);
    }
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee) {


        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(employeeId);

        if (employeeRepository.existsById(employeeId)) {
            Employee existingEmployee = existingEmployeeOptional.get();
            existingEmployee.setTitle(updatedEmployee.getTitle());
            existingEmployee.setPosition(updatedEmployee.getPosition());
            return employeeRepository.save(existingEmployee);
        } else {
            return null;
        }
    }

    public String deleteEmployee(Long employeeId) {
        if (employeeRepository.existsById(employeeId)) {
            employeeRepository.deleteById(employeeId);
            return "employee " +employeeId+ " removed ";
        } else {
            return "No such employee found. No employees were removed ";
        }
    }
}
