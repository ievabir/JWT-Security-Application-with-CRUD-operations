package lt.vu.security.controller;
import lt.vu.security.employee.Employee;
import lt.vu.security.employee.EmployeeService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/crud")

public class EmployeeController {


    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @PostMapping("/addemployee")
    public Employee addEmployee(@RequestBody Employee employee) {
        return service.saveEmployee(employee);
    }

    @GetMapping("/employees")
    public List<Employee> findByReportsToEmail(Authentication authentication) {
        String authenticatedUserEmail = authentication.getName();
        return service.findByReportsToEmail(authenticatedUserEmail);
    }

    @PutMapping("/update/{id}")
    public Employee updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUserEmail = authentication.getName();

        return service.updateEmployee(id, employee, authenticatedUserEmail);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails) {
        String authenticatedUserEmail = userDetails.getUsername();
        return service.deleteEmployee(id, authenticatedUserEmail);
    }
}

